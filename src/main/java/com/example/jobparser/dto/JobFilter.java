package com.example.jobparser.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class JobFilter {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private Instant gt;
    private Instant lt;
}
