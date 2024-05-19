package com.example.jobparser.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class JobDto extends TimestampsFields {
    // Id's
    private Integer jobId;
    private String trackingId;
    private String referenceId;

    // Urls
    private String jobUrl;
    private String companyUrl;

    // Meta
    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private String date;
    private String timeMessage;
    private LocalDateTime jobCreatedAt;
}
