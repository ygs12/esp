package com.esp.user.controller;

import com.esp.user.common.ApiResponse;
import com.esp.user.domain.User;
import com.esp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
  
  @Autowired
  private UserService userService;
  //-------------------查询---------------------
  
  @RequestMapping("getById")
  public ApiResponse getUserById(Long id){
    User user = userService.getUserById(id);
    return ApiResponse.ofSuccess(user);
  }
  
  @RequestMapping("getList")
  public ApiResponse getUserList(@RequestBody User user){
    List<User> users = userService.getUserByCondition(user);
    return ApiResponse.ofSuccess(users);
  }
  
  
  //----------------------注册----------------------------------
  @RequestMapping("add")
  public ApiResponse add(@RequestBody User user){
    userService.addAccount(user);
    return ApiResponse.ofSuccess();
  }
  
  /**
   * 主要激活key的验证
   */
  @RequestMapping("enable")
  public ApiResponse enable(String key){
    userService.enableAccount(key);
    return ApiResponse.ofSuccess();
  }
  
  //------------------------登录/鉴权--------------------------
  
  @RequestMapping("login")
  public ApiResponse auth(@RequestBody User user){
    User finalUser = userService.login(user.getEmail(), user.getPassword());
    return ApiResponse.ofSuccess(finalUser);
  }
  
  
  @RequestMapping("auth")
  public ApiResponse getUser(String token){
    User finalUser = userService.getLoginUserByToken(token);
    return ApiResponse.ofSuccess(finalUser);
  }
  
  @RequestMapping("logout")
  public ApiResponse logout(String token){
    userService.logout(token);
    return  ApiResponse.ofSuccess();
  }
  
  @RequestMapping("update")
  public ApiResponse update(@RequestBody User user){
    User updateUser = userService.updateUser(user);
    return ApiResponse.ofSuccess(updateUser);
  }

  // 重置密码
  @RequestMapping("reset")
  public ApiResponse reset(String key ,String password){

    User updateUser = userService.reset(key,password);
    return ApiResponse.ofSuccess(updateUser);
  }
  
  @RequestMapping("getKeyEmail")
  public ApiResponse getKeyEmail(String key){
    String email = userService.getResetKeyEmail(key);
    return ApiResponse.ofSuccess(email);
  }
  
  @RequestMapping("resetNotify")
  public ApiResponse resetNotify(String email,String url){
    userService.resetNotify(email,url);
    return ApiResponse.ofSuccess();
  }
}
