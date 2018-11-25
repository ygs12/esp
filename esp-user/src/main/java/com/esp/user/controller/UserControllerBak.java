/**
 *
 */
package com.esp.user.controller;

import com.esp.user.domain.User;
import com.esp.user.model.QiniuPutRet;
import com.esp.user.service.AgencyService;
import com.esp.user.service.QiNiuService;
import com.esp.user.service.UserService;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author eric
 */
@Controller
public class UserControllerBak {


    @Autowired
    private UserService accountService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private Gson gson;


//----------------------------注册流程-------------------------------------------
    @RequestMapping(value = "accounts/register", method = {RequestMethod.POST, RequestMethod.GET})
    public String accountsSubmitToQiniu(User account, RedirectAttributes model, ModelMap modelMap, HttpServletRequest request) {
        if (account == null || account.getName() == null) {
            modelMap.put("agencyList", agencyService.getAllAgency());
            return "/accounts/register";
        }
        boolean exist = accountService.isExist(account.getEmail());

        if (!exist) {
            String baseUrl = StringUtils.substringBeforeLast(request.getRequestURL().toString(), "/accounts/register");
            // 定义激活的连接地址
            String activeUrl = baseUrl + "/accounts/verify";
            // 获取到用头像上传成功后的路径
            MultipartFile avatarFile = account.getAvatarFile();
            String photoUrl = "";
            try {
                Response response = qiNiuService.uploadFile(avatarFile.getInputStream());
                QiniuPutRet qiniuPutRet = gson.fromJson(response.bodyString(), QiniuPutRet.class);
                photoUrl = qiniuPutRet.getKey();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 设置到user对象中
            account.setAvatar(photoUrl);
            account.setEnableUrl(activeUrl);
            accountService.addAccount(account);
            modelMap.put("email", account.getEmail());

            return "/accounts/registerSubmit";
        } else {
            model.addFlashAttribute("errorMsg", "邮件已被注册");
            return "redirect:/accounts/register";
        }
    }

    @RequestMapping("accounts/verify")
    public String verify(String key, RedirectAttributes model) {
        boolean result = accountService.enableAccount(key);
        if (result) {
            model.addFlashAttribute("successMsg", "激活成功");
            return "redirect:/index";
        } else {
            model.addAttribute("errorMsg", "激活失败,请确认链接是否过期");
            return "/accounts/signin";
        }
    }


    //----------------------------登录操作流程-------------------------------------------

    @RequestMapping(value = "/accounts/signin", method = {RequestMethod.POST, RequestMethod.GET})
    public String loginSubmit(User user, RedirectAttributes model, HttpSession session) {
        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())) {
            return "/accounts/signin";
        }
        User loginUser = accountService.login(user.getEmail(), user.getPassword());
        if (loginUser == null) {
            model.addFlashAttribute("errorMsg", "输入的账号和密码错误");
            return "redirect:/accounts/signin";
        } else {
            session.setAttribute("email", loginUser.getEmail());
            session.setAttribute("type", loginUser.getType());
            return "redirect:/index";
        }
    }

    /**
     * @return
     */
    @RequestMapping("accounts/logout")
    public String logout(String token) {
        accountService.logout(token);
        return "redirect:/index";
    }


    @PostMapping("accounts/remember")
    public String remember(String email, RedirectAttributes model, ModelMap modelMap, HttpServletRequest request) {
        if (StringUtils.isBlank(email)) {
            model.addFlashAttribute("errorMsg", "邮箱不能为空");
            return "redirect:/accounts/signin";
        }
        String baseUrl = StringUtils.substringBeforeLast(request.getRequestURL().toString(), "/accounts/remember");
        // 定义激活的连接地址
        String activeUrl = baseUrl + "/accounts/reset";
        accountService.resetNotify(email, activeUrl);
        modelMap.put("email", email);
        return "/accounts/remember";
    }


    @RequestMapping("accounts/reset")
    public String reset(String key, RedirectAttributes model, ModelMap modelMap) {
        String email = accountService.getResetKeyEmail(key);

        if (StringUtils.isBlank(email)) {
            model.addFlashAttribute("errorMsg", "重置链接已过期");
            return "redirect:/accounts/signin";
        }

        modelMap.put("email", email);
        modelMap.put("success_key", key);

        return "/accounts/reset";
    }

    @RequestMapping(value = "accounts/resetSubmit", method = {RequestMethod.POST, RequestMethod.GET})
    public String resetSubmit(User user, RedirectAttributes model) {
        User updatedUser = accountService.reset(user.getKey(), user.getPassword());
        model.addFlashAttribute("successMsg", "密码重置成功");
        return "redirect:/index";
    }


    //----------------------------个人信息修改--------------------------------------
    @RequestMapping(value = "accounts/profile", method = {RequestMethod.POST, RequestMethod.GET})
    public String profile(String email, Model model) {
        model.addAttribute("user", accountService.getUserByEmail(email));
        return "/accounts/profile";
    }


    @RequestMapping(value = "accounts/profileSubmit", method = {RequestMethod.POST, RequestMethod.GET})
    public String profileSubmit(HttpServletRequest req, User updateUser, RedirectAttributes model) {
        User user = accountService.updateUser(updateUser);
        model.addFlashAttribute("successMsg", "个人信息更新成功");
        return "redirect:/accounts/profile?email=" + user.getEmail();
    }

    /**
     * 修改密码操作
     *
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @RequestMapping("accounts/changePassword")
    public String changePassword(String email, String password, String newPassword,
                                 String confirmPassword, RedirectAttributes model) {
        User user = accountService.login(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            model.addFlashAttribute("errorMsg", "密码错误");
            return "redirect:/accounts/profile?email=" + email;
        }
        User updateUser = new User();
        updateUser.setPassword(newPassword);
        updateUser.setEmail(email);
        accountService.updateUser(updateUser);
        model.addFlashAttribute("successMsg", "密码更新成功");
        return "redirect:/accounts/signin";
    }
}
