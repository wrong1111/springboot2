package com.yucei.admin.common.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid注入的配置参数
 *
 * @author
 * @date 2018/5/18
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidProperties {

    private String url;

    private String driverClassName;

    private String username;

    private String password;

    private String publicKey;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    private Long maxWait;

    private Long timeBetweenEvictionRunsMillis;

    private Long minEvictableIdleTimeMillis;

    private Boolean poolPreparedStatements;

    private Integer maxPoolPreparedStatementPerConnectionSize;

    private String validationQuery;

    private String filters;

    private Boolean testWhileIdle;

    private Boolean testOnBorrow;

    private Boolean testOnReturn;


}
