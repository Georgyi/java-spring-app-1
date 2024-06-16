package com.example.jobparser.database.service;

import com.example.jobparser.database.specification.CustomSpecification;
import com.example.jobparser.database.entity.Job;
import com.example.jobparser.database.repository.JobRepository;
import com.example.jobparser.database.specification.MethodsEnum;
import com.example.jobparser.dto.JobFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Page<Job> getJobs(Pageable pageable, JobFilter filter) {
        System.out.println("1");

        Specification<Job> specification = Specification
                .where(CustomSpecification.<Job>hasString("jobTitle", filter.getJobTitle(), MethodsEnum.LIKE))
                .and(CustomSpecification.<Job>hasString("companyName", filter.getCompanyName(), MethodsEnum.LIKE))
                .and(CustomSpecification.<Job>hasString("jobLocation", filter.getLocation(), MethodsEnum.LIKE))
                .and(CustomSpecification.<Job>hasTime("jobCreatedAt", filter.getGt(), MethodsEnum.GT))
                .and(CustomSpecification.<Job>hasTime("jobCreatedAt", filter.getLt(), MethodsEnum.LT))
                .and(CustomSpecification.<Job>hasLong("jobId", filter.getJobId()));

        System.out.println("2");

        return jobRepository.findAll(specification, pageable);
    }

    public Page<Job> getJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
}
