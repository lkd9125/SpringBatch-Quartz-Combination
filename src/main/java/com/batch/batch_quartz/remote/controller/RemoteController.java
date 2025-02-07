package com.batch.batch_quartz.remote.controller;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.http.HttpServletResponse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batch.batch_quartz.remote.model.ChangeCronFormulaRQ;
import com.batch.batch_quartz.remote.model.CreateJobRQ;
import com.batch.batch_quartz.remote.service.RemoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/remote")
public class RemoteController {

    private final RemoteService remoteService;

    @GetMapping("/excute")
    public ResponseEntity<Void> excuteJob(String jobName){

        remoteService.jobExcute(jobName);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-cron")
    public ResponseEntity<?> changeCronFormula(@RequestBody ChangeCronFormulaRQ rq){

        remoteService.changeCronFormula(rq);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-job")
    public ResponseEntity<Void> createJob(@RequestBody CreateJobRQ rq){

        remoteService.createJob(rq);

        return ResponseEntity.ok().build();
    }
    
}
