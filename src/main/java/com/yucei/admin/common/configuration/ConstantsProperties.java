package com.yucei.admin.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/9/20
 */

@Configuration
@Data
@ConfigurationProperties(prefix = "com.yucei")
public class ConstantsProperties {


    Integer loginUserMaxSecond;

    String tokenHashString;

    String tokenPreString;

    String validContentString;

    String companySign;

    String salt;

    String forgetContentPassword;
    //对接api.xyungame.com需要的partnerid,以及串
    String zsurl;
    String elurl;
    String signstring;
    String partnerid;

    //是否开放评论默认0 不允许
    String iscoment;

    String filepath;

    String imageDomain;

}
