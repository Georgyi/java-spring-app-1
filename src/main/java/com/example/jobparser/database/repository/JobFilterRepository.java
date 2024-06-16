package com.example.jobparser.database.repository;

import java.util.List;

import com.example.jobparser.database.entity.Job;
import com.example.jobparser.dto.JobFilterQueryDSL;

public interface JobFilterRepository {
    List<Job> findAllJobsByFilter(JobFilterQueryDSL filter);
}
