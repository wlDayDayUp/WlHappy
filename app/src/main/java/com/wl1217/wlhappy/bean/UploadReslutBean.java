package com.wl1217.wlhappy.bean;

import java.io.IOException;

import okhttp3.Response;
import rxhttp.wrapper.parse.Parser;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.wlhappy.bean
 * @ClassName: UploadReslutBean
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-06 10:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-06 10:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UploadReslutBean {

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
