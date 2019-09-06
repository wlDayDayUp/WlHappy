package com.wl1217.wlhappy.bean;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.wlhappy.bean
 * @ClassName: GetCsBean
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-05 15:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-05 15:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GetCsBean {

    private boolean success;
    private String msg;
    private InfoBean info;

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



    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * phone : 13333333333
         */

        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
