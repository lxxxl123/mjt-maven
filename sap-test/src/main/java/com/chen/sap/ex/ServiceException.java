package com.chen.sap.ex;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author Chill
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;



    public ServiceException(String message) {
        super(StrUtil.format(message));
    }

    public ServiceException(String message,Object... objects) {
        this(StrUtil.format(message, objects));
    }




    /**
     * 提高性能
     *
     * @return Throwable
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }

}

