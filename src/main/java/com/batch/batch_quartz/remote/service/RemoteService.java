package com.batch.batch_quartz.remote.service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.batch_quartz.jpa.entity.ComJobCycleBas;
import com.batch.batch_quartz.jpa.repository.ComJobCycleBasRepository;
import com.batch.batch_quartz.jpa.repository.ComJobCycleJdbcRepository;
import com.batch.batch_quartz.quartz.JobContext;
import com.batch.batch_quartz.remote.model.ChangeCronFormulaRQ;
import com.batch.batch_quartz.remote.model.CreateJobRQ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RemoteService {

    private final ComJobCycleBasRepository comJobCycleBasRepository;

    private final ComJobCycleJdbcRepository comJobCycleJdbcRepository;

    private final ApplicationContext applicationContext;

    private final Scheduler scheduler;

    private final JobLauncher jobLauncher;

    private final JobRegistry jobLocator;

    public void jobExcute(String jobName){
        Thread async = new Thread (() -> {
            log.warn("{} JOB START!!!", jobName);

            try {
                Job simpleJob = jobLocator.getJob(jobName);
                JobParametersBuilder jobParam = new JobParametersBuilder().addDate("TODAY_DATE", Date.from(Instant.now()));
    
                jobLauncher.run(simpleJob, jobParam.toJobParameters());
            } catch (Exception e) {
                log.error("", e);
            }

            log.warn("{} JOB END!!!", jobName);
        });

        async.start();

    }

    @Transactional
    public void changeCronFormula(ChangeCronFormulaRQ rq) {
        
        ComJobCycleBas entity = comJobCycleBasRepository.findById(rq.getJobName())
            .orElseThrow(() -> new RuntimeException());

        entity.setCycle(rq.getCronFormula());
        
        comJobCycleBasRepository.save(entity);
        

        // try {
        //     // scheduler.pauseJob(jobKey);
        //     // scheduler.pauseTrigger(triggerKey);

        //     scheduler.deleteJob(jobKey);

        //     org.quartz.Job job = JobContext.getJob(rq.getJobName());

        //     JobDetail jobDetail = JobBuilder
        //         .newJob(job.getClass())
        //         .withIdentity(jobKey)
        //         .build();

        //     Trigger trigger = TriggerBuilder
        //         .newTrigger()
        //         .withIdentity(triggerKey)
        //         .withSchedule(CronScheduleBuilder.cronSchedule(rq.getCronFormula()))
        //         .build();

        //     scheduler.scheduleJob(jobDetail, trigger);
        // } catch (Exception e) {
        //     log.error("ERROR :: {}", e);
        // } 

    }

    @Transactional
    public void createJob(CreateJobRQ rq) {
        ComJobCycleBas entity = ComJobCycleBas.builder()
            .jobNm(rq.getJobName())
            .cycle(rq.getCronFormula())
            .build();

        comJobCycleBasRepository.save(entity);
    }
}
