package com.wl1217.mybox.config;

import rxhttp.wrapper.annotation.DefaultDomain;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.mybox.config
 * @ClassName: Url
 * @Description: 接口配置
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-03 16:31
 * @UpdateUser: wlDayDayUp
 * @UpdateDate: 2019-09-03 16:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Url {

    /**
     * 默认接口域名
     * DefaultDomain注解，需要ReBuild project，自动生成RxHttp类
     */
    @DefaultDomain()
    public static String baseUrl = "http://192.168.0.105:3000";

    /*--------------------------------------接口定义----------------------------------------------------*/

    public static String listNotice = "/mobile/notice/listNotice";

    public static String login = "/users/login";

    public static String getCs = "/test/getCs"; /* GET 测试*/

    public static String oneUploadFile = "/test/uploadfile";    /* 单个文件上传 测试*/

    public static String moreUploadFile = "/test/uploadfiles";  /* 多个文件上传 测试*/

    public static String updateVserion = "/update/version.txt"; /* 检查更新*/

    public static String downApk = "/update/test.apk"; /* 检查更新*/
}
