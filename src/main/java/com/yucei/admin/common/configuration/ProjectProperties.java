package com.yucei.admin.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;


/**
 * 项目配置
 *
 * @author
 * @date 2018/5/26
 */
@Data
@Repository
@ConfigurationProperties("project")
public class ProjectProperties {

    /**
     * 工程名
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 工程描述
     */
    private String description;

    /**
     * 项目组织标识
     */
    private String groupId;

    /**
     * 项目标识
     */
    private String artifactId;

    /**
     * 项目根目录
     */
    private String basedir;

    /**
     * 核心项目包
     */
    private String corePackage;

    /**
     * 业务项目包
     */
    private String servicePackage;

    /**
     * 当前环境值
     */
    private String[] env;

    /**
     * 项目作者
     */
    private ProjectAuthorProperties author;

    /**
     * 注入的spring环境上下文
     */
    private final Environment environment;

}
