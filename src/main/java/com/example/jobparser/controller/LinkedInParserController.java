package com.example.jobparser.controller;

import com.example.jobparser.database.service.LinkedInSeleniumService;
import com.example.jobparser.exception.CustomRuntimeException;
import com.example.jobparser.database.service.LinkedinJobsParseService;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.time.Duration;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/parser")
public class LinkedInParserController {

    private final LinkedinJobsParseService linkedinJobsParseService;
    private final LinkedInSeleniumService linkedInSeleniumService;

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<String> handleCustomRuntimeException(CustomRuntimeException ex) {
        return ResponseEntity.status(500).body("Произошла ошибка: " + ex.getMessage());
    }

    @GetMapping
    public Integer parseJobs() {
        return linkedinJobsParseService.parseJobs();
    }

    @GetMapping("/scroll")
    public void parseJobsWithScroll(@RequestParam Integer start) {
        linkedinJobsParseService.parseScrollJob(start);
    }

    @GetMapping("/selenium")
    public ResponseEntity<String> testSelenium() {
        linkedInSeleniumService.sendMessage(
                "Hello Fedya!",
                "2-ZDY3YTBmNmQtMDlhYS00OTUyLWEzNTEtZGQ5NGZkNWQwMjc4XzAxMg==/"
        );
        return ResponseEntity.status(200).body("Тестирование закончено!");
    }
}
