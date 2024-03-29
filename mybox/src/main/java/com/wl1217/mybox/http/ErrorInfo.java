package com.wl1217.mybox.http;

import android.text.TextUtils;

import com.wl1217.mybox.MyApp;

import rxhttp.wrapper.exception.HttpStatusCodeException;
import rxhttp.wrapper.exception.ParseException;


public class ErrorInfo {

    private int errorCode;  //仅指服务器返回的错误码
    private String errorMsg; //错误文案，网络错误、请求失败错误、服务器返回的错误等文案
    private Throwable throwable; //异常信息

    public ErrorInfo(Throwable throwable) {
        this.throwable = throwable;
        String errorMsg = ExceptionHelper.handleNetworkException(throwable); //网络异常
        if (throwable instanceof HttpStatusCodeException) { //请求失败异常
            String code = throwable.getLocalizedMessage();
            if ("416".equals(code)) {
                errorMsg = "请求范围不符合要求";
            }
        } else if (throwable instanceof ParseException) { // ParseException异常表明请求成功，但是数据不正确
            String errorCode = throwable.getLocalizedMessage();
            if ("-1".equals(errorCode)) {
                errorMsg = "数据解析失败,请稍后再试";
            } else {
                this.errorCode = Integer.valueOf(errorCode);
                errorMsg = throwable.getMessage();
                if (TextUtils.isEmpty(errorMsg)) errorMsg = errorCode;//errorMsg为空，显示errorCode
            }
        }
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean show() {
        Tip.show(TextUtils.isEmpty(errorMsg) ? throwable.getMessage() : errorMsg);
        return true;
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    public boolean show(String standbyMsg) {
        Tip.show(TextUtils.isEmpty(errorMsg) ? standbyMsg : errorMsg);
        return true;
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    public boolean show(int standbyMsg) {
        Tip.show(TextUtils.isEmpty(errorMsg) ? MyApp.getInstance().getString(standbyMsg) : errorMsg);
        return true;
    }
}
