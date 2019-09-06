package com.wl1217.wlhappy.bean;

import java.util.List;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.wlhappy
 * @ClassName: NotictionBean
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-03 17:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-03 17:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NotictionBean {

    /**
     * success : true
     * msg : 查询信息列表成功
     * infolist : [{"header":"关于下载管理事项的通知","id":"28e0818545ef438b0145efc5b2360000","rn":"1","createtime":"2014-05-12"}]
     * pagecount : 1
     */

    private boolean success;
    private String msg;
    private String pagecount;
    private List<InfolistBean> infolist;

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

    public String getPagecount() {
        return pagecount;
    }

    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    public List<InfolistBean> getInfolist() {
        return infolist;
    }

    public void setInfolist(List<InfolistBean> infolist) {
        this.infolist = infolist;
    }

    public static class InfolistBean {
        @Override
        public String toString() {
            return "InfolistBean{" +
                    "header='" + header + '\'' +
                    ", id='" + id + '\'' +
                    ", rn='" + rn + '\'' +
                    ", createtime='" + createtime + '\'' +
                    '}';
        }

        /**
         * header : 关于下载管理事项的通知
         * id : 28e0818545ef438b0145efc5b2360000
         * rn : 1
         * createtime : 2014-05-12
         */

        private String header;
        private String id;
        private String rn;
        private String createtime;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRn() {
            return rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }

    @Override
    public String toString() {
        return "NotictionBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", pagecount='" + pagecount + '\'' +
                ", infolist=" + infolist +
                '}';
    }
}
