package com.batch.batch_quartz.jpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COM_JOB_CYCLE_BAS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ComJobCycleBas {

    @Id
    @Column(name = "JOB_NM", nullable = false)
    private String jobNm;

    @Column(name = "CYCLE", nullable = false)
    private String cycle;

    @Column(name = "CREATE_DT")
    @CreatedDate
    private LocalDateTime createDt;

    @Column(name = "UPDATE_DT")
    @LastModifiedDate
    private LocalDateTime updateDt;

    @Column(name = "UPDATE_UESR")
    private String updateUser;

}
