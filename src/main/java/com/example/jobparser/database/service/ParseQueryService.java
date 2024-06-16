package com.example.jobparser.database.service;

import com.example.jobparser.database.entity.ParseQuery;
import com.example.jobparser.database.repository.ParseQueryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ParseQueryService {
    private final ParseQueryRepository parseQueryRepository;

    private ParseQuery updateQueryParam(ParseQuery parseQuery, String param, String value) {
        if (param.equals("f_T")) {
            parseQuery.setFT(value);
        }

        if (param.equals("geo_id")) {
            parseQuery.setGeoId(value);
        }

        if (param.equals("keywords")) {
            parseQuery.setKeywords(value);
        }

        if (param.equals("location")) {
            parseQuery.setLocation(value);
        }

        if (param.equals("origin")) {
            parseQuery.setOrigin(value);
        }

        if (param.equals("sort_by")) {
            parseQuery.setSortBy(value);
        }

        return parseQuery;
    }

    public void clearQueryParam(String param) {
        var parseQueryOptional = parseQueryRepository.findFirst();

        if (parseQueryOptional.isPresent()) {
            var parseQuery = parseQueryOptional.get();
            var updatedParseQuery = updateQueryParam(parseQuery, param, "");
            parseQueryRepository.save(updatedParseQuery);
        }
    }



    // TODO::Исправить функционал
    public void addValueToParam(String param, String value) {
        var parseQueryOptional = parseQueryRepository.findFirst();

        if (parseQueryOptional.isPresent()) {
            var parseQuery = parseQueryOptional.get();
            var updatedParseQuery = updateQueryParam(parseQuery, param, value);
            parseQueryRepository.save(updatedParseQuery);
        }
    }

    // TODO::Исправить функционал
    public void removeValueFromParam(String param, String value) {
        var parseQueryOptional = parseQueryRepository.findFirst();

        if (parseQueryOptional.isPresent()) {
            var parseQuery = parseQueryOptional.get();
            var updatedParseQuery = updateQueryParam(parseQuery, param, value);
            parseQueryRepository.save(updatedParseQuery);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ParseQuery> getParseQuery() {
        return parseQueryRepository.findFirst();
    }
}
