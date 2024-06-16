package com.example.jobparser.database.repository;

import com.example.jobparser.database.entity.Job;
import com.example.jobparser.database.querydsl.QPredicates;
import com.example.jobparser.dto.JobFilter;
import com.example.jobparser.dto.JobFilterQueryDSL;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.jobparser.database.entity.QJob.job;

@Repository
@RequiredArgsConstructor
public class JobFilterRepositoryImpl implements JobFilterRepository {

    private final EntityManager entityManager;

    @Override
    public List<Job> findAllJobsByFilter(JobFilterQueryDSL filter) {
        var predicates = QPredicates.builder()
            .add(filter.jobTitle(), job.jobTitle::containsIgnoreCase)
            .add(filter.companyName(), job.companyName::containsIgnoreCase)
            .build();

        return new JPAQuery<Job>(entityManager)
            .select(job)
            .from(job)
            .where(predicates)
            .fetch();
    }
}
