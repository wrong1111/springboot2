package com.yucei.admin.common.exception;


import com.yucei.admin.common.base.ExceptionEnum;
import com.yucei.admin.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @项目名称：yucei-admin
 * @包名：com.yucei.manage.web.error
 * @类描述：普通请求统一使用error页面处理，异步请求，返回统一的Result(status,message,data)对象
 * @创建人：yucei
 * @创建时间：2018-06-06 17:00
 * @version：V1.0
 */
@Controller
@RequestMapping("error")
public class GlobalErrorController extends AbstractErrorController {

    private static final Logger logger = LoggerFactory
            .getLogger(GlobalErrorController.class);

    private static final String ERROR_PATH = "error";


    //构造
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping("text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView(ERROR_PATH);
        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        ExceptionEnum ee = getMessage(request);
        Result<String> result = new Result<String>(
                String.valueOf(ee.getType()), model.get("error") + "[" + (model.get("path") == null ? "" : model.get("path")) + "]", model.get("message").toString());
        view.addObject("result", result);
        return view;
    }

    @RequestMapping
    @ResponseBody
    //设置响应状态码为：200，结合前端约定的规范处理。也可不设置状态码，前端ajax调用使用error函数进行控制处理
    @ResponseStatus(value = HttpStatus.OK)
    public Result<String> error(HttpServletRequest request, HttpServletResponse response) {
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】text/html=普通请求：request=" + request);
        /** model对象包含了异常信息 */
        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】统一异常处理：model=" + model);
        // 1 获取错误状态码（也可以根据异常对象返回对应的错误信息）
        ExceptionEnum ee = getMessage(request);
        // 2 返回错误提示
        Result<String> result = new Result<String>(
                String.valueOf(ee.getType()), model.get("error") + "[" + (model.get("path") == null ? "" : model.get("path")) + "]", model.get("message").toString());
        // 3 将错误信息返回
        logger.info("统一异常处理【" + getClass().getName()
                + ".error】统一异常处理!错误信息result：" + result);
        return result;
    }

    /**
     * @param request
     * @return
     * @描述：根据error状态码，返回不同的错误提示信息
     * @创建人：yucei
     * @创建时间：2018年5月31日 下午2:52:50
     */
    @SuppressWarnings("static-access")
    private ExceptionEnum getMessage(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        if (httpStatus.is4xxClientError()) {
            // 4开头的错误状态码
            if (400 == httpStatus.BAD_REQUEST.value()) {
                return ExceptionEnum.BAD_REQUEST;
            } else if (403 == httpStatus.FORBIDDEN.value()) {
                return ExceptionEnum.BAD_REQUEST;
            } else if (404 == httpStatus.NOT_FOUND.value()) {
                return ExceptionEnum.NOT_FOUND;
            }
        } else if (httpStatus.is5xxServerError()) {
            // 5开头的错误状态码
            if (500 == httpStatus.INTERNAL_SERVER_ERROR.value()) {
                return ExceptionEnum.SERVER_EPT;
            }
        }
        // 统一返回：未知错误
        return ExceptionEnum.UNKNOW_ERROR;
    }

    private boolean isIncludeStackTrace(HttpServletRequest request,
                                        MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    /**
     * 此处也可以通过注入ServerProperties获取ErrorProperties
     *
     * @return
     */
    protected ErrorProperties getErrorProperties() {
        return new ErrorProperties();
    }
}
