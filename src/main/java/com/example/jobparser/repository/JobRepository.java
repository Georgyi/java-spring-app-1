package com.example.jobparser.repository;

import com.example.jobparser.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByJobId(Long jobId);

    @Query("SELECT j from Job j ORDER BY j.date DESC LIMIT 3")
    List<Job> findTop3();
}
