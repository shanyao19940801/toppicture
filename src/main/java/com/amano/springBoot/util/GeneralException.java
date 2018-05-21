package com.amano.springBoot.util;

public class GeneralException extends RuntimeException{

    private int errCode;
    private String errorMsg;

    public GeneralException(int errCode) {
        super(String.valueOf(errCode));
        this.errCode = errCode;
        this.errorMsg = "";
    }

    public GeneralException(int errCode, String errMsg) {
        super(errCode + ": " + errMsg);
        this.errCode = errCode;
        this.errorMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errorMsg;
    }
}
