package com.example.jobparser.controller;

import com.example.jobparser.entity.ParseQuery;
import com.example.jobparser.exception.CustomRuntimeException;
import com.example.jobparser.service.ParseQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/parse-query")
@AllArgsConstructor
public class ParseQueryController {

    private final ParseQueryService parseQueryService;

    @GetMapping
    @ExceptionHandler(CustomRuntimeException.class)
    public ParseQuery getParseQuery() {
        return parseQueryService
                .getParseQuery()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/test")
    @ExceptionHandler(CustomRuntimeException.class)
    public void test() {
        // geo_id / 101318387
        parseQueryService.clearQueryParam("geo_id");
    }
}
