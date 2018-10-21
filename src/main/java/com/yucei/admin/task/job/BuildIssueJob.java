package com.yucei.admin.task.job;

import com.yucei.admin.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Configuration
@Component
@EnableScheduling
@Slf4j
@DisallowConcurrentExecution
public class BuildIssueJob implements Job, Serializable {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("====== 定时任务实现类（BuildIssueJob）ScheduleTask ====> 开启!" + DateUtil.getNow());
        Date now = new Date();
        try {
            //TODO 写具体的业务实现
        } catch (Exception e) {
            log.error("==== 定时任务实现类（BuildIssueJob）ScheduleTask ====>异常!", e);
        } finally {
            log.info("==== 定时任务实现类（BuildIssueJob）ScheduleTask ====> 结束!" + DateUtil.getNow());
        }
    }
}
