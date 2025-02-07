package com.batch.batch_quartz.job.excute.spring;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.batch_quartz.jpa.repository.ComJobCycleBasRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class SpringExcuteSampleJob {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final ComJobCycleBasRepository comJobCycleBasRepository;

    private static final String TASKLET_JOB_NAME = "TASKLET-JOB";

    @Bean(name = TASKLET_JOB_NAME)
    public Job excuteSimpleJob(){
        return jobBuilderFactory.get(TASKLET_JOB_NAME)
            .start(validateStep())
                .on("*").to(taskletStep())
            .end()
            .build();
    }

    @Bean("TASKLET-VALIDTAE-STEP")
    public Step validateStep(){
        return stepBuilderFactory.get("EXCUETE-VALIDATE-STEP")
            .tasklet((stepContribution, chunkContext) -> {
                
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean("TASKLET-STEP")
    public Step taskletStep(){
        return stepBuilderFactory.get("SIMPLE-STEP")
            .tasklet((stepContribution, chunkContext) -> {
                for(int i = 0; i < 5; i++){
                    Thread.sleep(1000);
                    log.info("{} :: Hello~~", i);
                }
                return RepeatStatus.FINISHED;
            })
            .build();
    }
}
