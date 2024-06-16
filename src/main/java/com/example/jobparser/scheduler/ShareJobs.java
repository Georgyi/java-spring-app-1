package com.example.jobparser.scheduler;

import com.example.jobparser.database.entity.Job;
import com.example.jobparser.database.service.LinkedinJobsParseService;
import com.example.jobparser.telegram.LinkedInBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShareJobs {
    private final LinkedinJobsParseService linkedinJobsParseService;
    private final LinkedInBot linkedInBot;

    @Async("taskExecutor")
//    @Scheduled(fixedDelay = 300_000)
    public void parserJobsTask() {
        log.info("------------------------------ ShareJobs - every 5 minutes - start ------------------------------");

        List<Job> jobs = linkedinJobsParseService.findTopJobs();
        LocalDateTime currentTime = LocalDateTime.now();

        for (var job : jobs) {
            var duration = Duration.between(job.getJobCreatedAt(), currentTime);
            var text = getJobMessage(job, duration);

            System.out.println("----------- job: " + job);

            linkedInBot.sendMessageToPrivateChat(text);
        }

        log.info("------------------------------ ShareJobs - every 5 minutes - finish ------------------------------");
    }

    private String getJobMessage(Job job, Duration duration) {
        var days = duration.toDays();
        var hours = duration.toHours() % 24;
        var minutes = duration.toMinutes() % 60;

        var durationText = "";

        if (days > 0) {
            durationText += days + " дней ";
        }

        if (hours > 0) {
            durationText += hours + " часов ";
        }

        if (minutes > 0) {
            durationText += minutes + " минут ";
        }

        return """
            Время публикации: *%s* назад
             
            Компания: *%s*
            Роль: *%s*
            
            [Ссылка](%s)
            """.formatted(durationText.trim(), job.getCompanyName(), job.getJobTitle(), job.getJobUrl());
    }
}
