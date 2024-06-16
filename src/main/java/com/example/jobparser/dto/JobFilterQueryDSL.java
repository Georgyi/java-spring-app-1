package com.example.jobparser.dto;

import java.time.LocalDateTime;

public record JobFilterQueryDSL(String jobTitle, String companyName, String jobLocation, LocalDateTime jobCreatedAt) {
}
