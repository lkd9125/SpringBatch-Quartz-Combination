package com.batch.batch_quartz.remote.model;

import lombok.Data;

@Data
public class CreateJobRQ {

    private String jobName;

    private String cronFormula;
}
