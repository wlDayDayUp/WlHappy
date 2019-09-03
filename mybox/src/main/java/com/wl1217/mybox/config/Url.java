package com.wl1217.mybox.config;

import rxhttp.wrapper.annotation.DefaultDomain;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.mybox.config
 * @ClassName: UrlConfig
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-03 16:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-03 16:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Url {
    @DefaultDomain() //设置为默认域名
    public static String baseUrl = "http://1l865v9452.iask.in:8889/yzjsj";

    public static String listNotice = "/mobile/notice/listNotice";

}
