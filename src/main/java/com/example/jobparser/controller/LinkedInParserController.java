package com.example.jobparser.controller;

import com.example.jobparser.exception.CustomRuntimeException;
import com.example.jobparser.service.LinkedinJobsParseService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/parser")
public class LinkedInParserController {

    private final LinkedinJobsParseService linkedinJobsParseService;

    @GetMapping
    @ExceptionHandler(CustomRuntimeException.class)
    public Integer parseJobs() {
       return linkedinJobsParseService.parseJobs();
    }

    @GetMapping("/scroll")
    @ExceptionHandler(CustomRuntimeException.class)
    public void parseJobsWithScroll(@RequestParam Integer start) {
        linkedinJobsParseService.parseScrollJob(start);
    }
}
