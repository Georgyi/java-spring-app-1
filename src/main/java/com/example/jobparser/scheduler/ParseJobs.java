package com.example.jobparser.scheduler;

import com.example.jobparser.exception.CustomRuntimeException;
import com.example.jobparser.service.LinkedinJobsParseService;
import com.example.jobparser.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParseJobs {

    private final LinkedinJobsParseService linkedinJobsParseService;
    private static final Integer MAX_JOBS_COUNT = 1_000;
    private static final Integer ITERATION_DELAY = 5_000;
    private static final Integer MAX_ITERATIONS = NumberUtils.clamp(300_000 / ITERATION_DELAY, 0, 100);

    @Async("taskExecutor")
    @Scheduled(fixedDelay = 300_000)
    public void parserJobsTask() {
        log.info("------------------------------ parserJobsTask - every 5 minutes - start ------------------------------");

        try {
            var allJobsCount = linkedinJobsParseService.parseJobs();
            var cutJobsCount = NumberUtils.clamp(allJobsCount, 0, MAX_JOBS_COUNT);

            System.out.println("jobsCount: " + cutJobsCount);

            var start = 0;
            var iteration = 0;

            while (start < cutJobsCount && iteration <= MAX_ITERATIONS) {
                var parsedJobsCount = linkedinJobsParseService.parseScrollJob(start);

                start = NumberUtils.clamp(start + parsedJobsCount, 0, cutJobsCount);
                iteration++;

                Thread.sleep(ITERATION_DELAY);
            }
        } catch (RuntimeException e) {
            System.out.println("Some error!");
        } catch (InterruptedException e) {
            throw new CustomRuntimeException("Something was wrong... Class: ParseJobs / Method: parserJobsTask");
        }

        log.info("------------------------------ parserJobsTask - every 5 minutes - finish ------------------------------");
    }

}
