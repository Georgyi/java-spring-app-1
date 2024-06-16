package com.example.jobparser.controller;

import com.example.jobparser.database.entity.Job;
import com.example.jobparser.database.service.JobService;
import com.example.jobparser.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class JobController {

    private final JobService jobService;

    @GetMapping
    public Response<PaginatedResponse<Job>> getJobs(
            @PageableDefault(sort = {"jobCreatedAt"}, direction = Sort.Direction.DESC, size = 25) Pageable pageable,
            @ModelAttribute JobParams jobParams
    ) {
        Page<Job> page;
        JobFilter filter = jobParams.getFilter();

        if (filter != null) {
            page = jobService.getJobs(pageable, filter);
        } else {
            page = jobService.getJobs(pageable);
        }

        PaginatedResponse<Job> paginationResponse = PaginatedResponse.transform(page);
        return Response.generate(paginationResponse, "OK", 200);
    }
}
