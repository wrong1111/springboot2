package com.yucei.admin.common.exception;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/9/21
 */
public class ServiceException extends Throwable {


    public ServiceException(String errorMessage) {
        super(errorMessage);
    }

    public ServiceException(String errorMessage, Throwable e) {
        super(errorMessage, e);

    }
}
