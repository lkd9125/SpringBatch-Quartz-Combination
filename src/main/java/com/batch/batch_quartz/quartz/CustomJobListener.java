package com.batch.batch_quartz.quartz;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.batch.batch_quartz.jpa.entity.ComJobCycleBas;
import com.batch.batch_quartz.jpa.repository.ComJobCycleBasRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomJobListener implements JobListener{

    private final ComJobCycleBasRepository comJobCycleBasRepository;

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler.getListenerManager()
                .addJobListener(this);
        } catch (SchedulerException e) {
            log.error("Failed to register job listener", e);
        }
    }

    @Override
    public String getName() {
        return "CustomJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("START JOB :: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.warn("JOB NOT WORKING :: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("JOB FINISH :: {}", context.getJobDetail().getKey());

        String jobKeyName = context.getJobDetail().getKey().getName();

        ComJobCycleBas entity =  comJobCycleBasRepository.findById(jobKeyName)
            .orElse(null);
        
        if(entity == null) {
            return;
        }

        JobKey jobKey = JobKey.jobKey(entity.getJobNm());

        JobDetail jobDetail = null;
        
        try {
            jobDetail = scheduler.getJobDetail(jobKey);
        } catch (Exception e) {
            log.error("등록된 잡이 존재 하지 않습니다 :: JOB KEY IS `{}`", jobKey);
            return;
        }


        // _TRIGGER
        TriggerKey triggerKey = TriggerKey.triggerKey(entity.getJobNm() + "_TRIGGER");
        Trigger updateTrigger = TriggerBuilder
            .newTrigger()
            .withIdentity(triggerKey)
            .withSchedule(CronScheduleBuilder.cronSchedule(entity.getCycle()))
            .forJob(jobKey)
            .build();

        try {
            scheduler.rescheduleJob(triggerKey, updateTrigger);
        } catch (Exception e) {
            log.error("ERROR :: {}", e);
        }
    }

}
