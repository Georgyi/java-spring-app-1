package com.example.jobparser.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jobs")
public class Job extends Auditable {
    @Id
    @Column(name = "job_id", nullable = false, unique = true)
    private Long jobId;

    @Column(name = "tracking_id", nullable = false)
    private String trackingId;
    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Column(name = "job_url", nullable = false)
    private String jobUrl;
    @Column(name = "company_url", nullable = false)
    private String companyUrl;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;
    @Column(name = "company_name", nullable = false)
    private String companyName;
    @Column(name = "job_location", nullable = false)
    private String jobLocation;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "time_message", nullable = false)
    private String timeMessage;
    @Column(name = "job_created_at", nullable = false)
    private LocalDateTime jobCreatedAt;
}
