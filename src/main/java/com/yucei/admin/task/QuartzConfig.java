package com.yucei.admin.task;

import lombok.Data;

@Data
public class QuartzConfig {

    private Integer status;
    private String quartzClass;
    private String name;
    private String group;
    private String cron;
    private Integer id;
}
