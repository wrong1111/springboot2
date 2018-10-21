package com.yucei.admin.manage.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class IssueVo implements Serializable {
    Integer lotteryid;
    String issueno;
    String startDate;
    String endDate;
}
