package com.yucei.admin.task;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AsyncQuartzConfService {

    private static Map<Long, QuartzConfig> quartzConfigMap = new ConcurrentHashMap<Long, QuartzConfig>();

    private static List<QuartzConfig> quartzConfigList = new ArrayList<>();

    public List<QuartzConfig> getJobList() {
        QuartzConfig config = new QuartzConfig();
        config.setCron("0 0 1 * * ?");
        config.setGroup("group");
        config.setName("buildIssueJob");
        config.setQuartzClass("com.yucei.admin.task.job.BuildIssueJob");
        config.setStatus(0);// 1 代表暂停 0 代表正常
        config.setId(1);
        quartzConfigMap.put(Long.valueOf(config.getId()),config);
        quartzConfigList.add(config);
        return quartzConfigList;
    }

    public QuartzConfig findById(Long id) {
        return quartzConfigMap.get(id);
    }
}
