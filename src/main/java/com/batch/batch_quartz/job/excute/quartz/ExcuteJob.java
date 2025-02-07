package com.batch.batch_quartz.job.excute.quartz;

import java.time.Instant;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "EXCUTE_JOB")
public class ExcuteJob implements org.quartz.Job{

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobLocator;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Job excuteJob = jobLocator.getJob("TASKLET-JOB");

            JobParameters parameter = new JobParametersBuilder()
                .addDate("date", Date.from(Instant.now()))
                .toJobParameters();

            jobLauncher.run(excuteJob, parameter);
        } catch (Exception e) {
            log.error("ERROR :: ", e);
            throw new JobExecutionException(e);
        }
    }

   

}
