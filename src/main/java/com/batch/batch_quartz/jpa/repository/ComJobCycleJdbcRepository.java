package com.batch.batch_quartz.jpa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ComJobCycleJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void updateMetaQuartzCronFormula(String jobName, String cronFormula, String triggerName){

        try {
            jdbcTemplate.update("UPDATE QRTZ_CRON_TRIGGERS SET CRON_EXPRESSION = ? WHERE TRIGGER_NAME = ?", 
                cronFormula,
                triggerName
            );
        } catch (Exception e) {
            log.error("ERROR :: ", e);
        }
    }

}
