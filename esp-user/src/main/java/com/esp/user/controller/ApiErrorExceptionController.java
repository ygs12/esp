package com.esp.user.controller;

import com.esp.user.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: microservice-house
 * @Auther: GERRY
 * @Date: 2018/11/8 20:47
 * @Description:
 */
@Controller
public class ApiErrorExceptionController implements ErrorController {

    public static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * web错误的处理方法
     * @return
     */
    @RequestMapping(value =ERROR_PATH, produces = "text/html")
    public String errorHandler(HttpServletResponse response) {
        int status = response.getStatus();

        switch (status) {
            case 403:
                return "403";
            case 404:
                return "404";
            case 500:
                return "500";
        }

        return "index";
    }

    /**
     * 非web错误信息处理方法
     * @param request
     * @return
     */
    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public ApiResponse errorHandler(HttpServletRequest request) {
        ServletWebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributesMap = this.errorAttributes.getErrorAttributes(webRequest, false);
        int statusCode = getStatus(request);

        return ApiResponse.ofMessage(statusCode, String.valueOf(errorAttributesMap.getOrDefault("message", "error")));
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }

        return 500;
    }

}
