package com.batch.batch_quartz.quartz;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.batch.batch_quartz.jpa.entity.ComJobCycleBas;
import com.batch.batch_quartz.jpa.repository.ComJobCycleBasRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 최초 실행
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JobCycleConfig {

    private final ComJobCycleBasRepository comJobCycleBasRepository;

    private final Scheduler scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void quartzCycleRegister (){
        Map<String, Job> jobs = JobContext.getJobAllMap();

        List<String> jobNames = jobs.entrySet().stream()
            .map(entry -> {
                return entry.getKey();
            })
            .collect(Collectors.toList());

        Map<String, String> comJobCycleEntityMap = comJobCycleBasRepository.findByJobNmIn(jobNames).stream()
            .collect(
                Collectors.toMap(
                    ComJobCycleBas::getJobNm, 
                    ComJobCycleBas::getCycle
                )
            );
        
        try {
            for(Entry<String, Job> entry : jobs.entrySet()){

                String jobName = entry.getKey();
                String cronFormula = comJobCycleEntityMap.get(jobName);

                if(cronFormula == null || cronFormula.isEmpty()){
                    continue;
                };

                Class<? extends Job> job = entry.getValue().getClass();
                JobKey jobKey = new JobKey(jobName);

                JobDetail jobDetail = JobBuilder
                    .newJob(job)
                    .withIdentity(jobKey)
                    .build();

                TriggerKey triggerKey = new TriggerKey(jobName + "_TRIGGER");

                Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(comJobCycleEntityMap.get(jobName)))
                    .build();

                if (scheduler.checkExists(jobDetail.getKey())) {
                    scheduler.deleteJob(jobDetail.getKey());
                }
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            log.error("ERROR :: ", e);
        }
        
    }
}
