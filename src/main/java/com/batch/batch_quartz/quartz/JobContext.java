package com.batch.batch_quartz.quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobContext {

    private final ApplicationContext applicationContext;

    private final static Map<String, Job> jobMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        jobMap.putAll(applicationContext.getBeansOfType(Job.class));
    }

    public static Job getJob(String jobName){
        return jobMap.get(jobName);
    }

    public static Map<String, Job> getJobAllMap(){
        return new HashMap<>(jobMap);
    }

}
