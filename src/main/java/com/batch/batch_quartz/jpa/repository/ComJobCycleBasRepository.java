package com.batch.batch_quartz.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.batch_quartz.jpa.entity.ComJobCycleBas;

@Repository
public interface ComJobCycleBasRepository extends JpaRepository<ComJobCycleBas, String>{

    List<ComJobCycleBas> findByJobNmIn(List<String> jobNms);
}
