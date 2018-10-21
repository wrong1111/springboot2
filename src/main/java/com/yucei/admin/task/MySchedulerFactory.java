package com.yucei.admin.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
@Slf4j
public class MySchedulerFactory {
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    // 任务配置读取服务
    @Autowired
    private AsyncQuartzConfService asyncQuartzConfService;

    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = getScheduler();
        startJob(scheduler);
    }

    // 获取scheduler
    private Scheduler getScheduler(){
        return schedulerFactoryBean.getScheduler();
    }
    // 项目启动 开启任务
    private void startJob(Scheduler scheduler)  {
        try {
            List<QuartzConfig> jobList = asyncQuartzConfService.getJobList();
            for (QuartzConfig config : jobList) {
                // 1-暂停的任务 0-正常运行任务
                if (1l==config.getStatus()){
                    continue;
                }
                @SuppressWarnings("unchecked")
                Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(config.getQuartzClass());
                JobDetail jobDetail = JobBuilder.newJob(clazz)
                        .withIdentity(config.getName(), config.getGroup()).build();
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(config.getCron());
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(config.getName(), config.getGroup())
                        .withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 任务暂停
    public void pauseJob(long id) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        QuartzConfig QuartzConfig = asyncQuartzConfService.findById(id);
        JobKey jobKey = JobKey.jobKey(QuartzConfig.getName(), QuartzConfig.getGroup());
        scheduler.deleteJob(jobKey);
    }

    // 任务恢复
    public void resumeJob(long id) throws SchedulerException, ClassNotFoundException {
        Scheduler scheduler = getScheduler();
        QuartzConfig QuartzConfig = asyncQuartzConfService.findById(id);
        JobKey jobKey = JobKey.jobKey(QuartzConfig.getName(), QuartzConfig.getGroup());
        Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(QuartzConfig.getQuartzClass());
        JobDetail jobDetail1 = scheduler.getJobDetail(jobKey);
        if (jobDetail1==null){
            JobDetail jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity(QuartzConfig.getName(), QuartzConfig.getGroup()).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(QuartzConfig.getCron());
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(QuartzConfig.getName(), QuartzConfig.getGroup())
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        }else {
            scheduler.resumeJob(jobKey);
        }
    }

}