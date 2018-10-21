package com.yucei.admin.common.exception;

import com.yucei.admin.common.base.ExceptionEnum;
import lombok.*;

/**
 * 业务异常
 *
 * @author
 * @date 2018/3/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    /**
     * 异常代码
     */
    private int errorCode = ExceptionEnum.SERVER_EPT.getType();
    /**
     * 异常信息
     */
    private String errorMessage;

    public BusinessException(String errorMessage) {
        this.errorCode = ExceptionEnum.SERVER_EPT.getType();
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = ExceptionEnum.SERVER_EPT.getType();
    }

    public BusinessException(int errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
    }

}
