package com.wl1217.mybox.config;

/**
 * @ProjectName: WlHappy
 * @Package: com.wl1217.mybox.config
 * @ClassName: GlobConfig
 * @Description: 类作用描述
 * @Author: wlDayDayUp
 * @CreateDate: 2019-09-06 14:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-09-06 14:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GlobConfig {


    public static final String APP_NAME = "test.apk"; /* 用于下载更新，保存的apk名称*/

    // //与manifest中定义的provider中的authorities="com.shawpoo.app.fileprovider"保持一致
    public static final String UriForFile = "com.wl1217.wlhappy.fileprovider"; /* 适配Android 7 UriForFile*/


}
