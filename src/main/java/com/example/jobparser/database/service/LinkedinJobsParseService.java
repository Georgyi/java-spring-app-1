package com.example.jobparser.database.service;

import com.example.jobparser.database.entity.Job;
import com.example.jobparser.exception.CustomRuntimeException;
import com.example.jobparser.database.repository.JobRepository;
import com.example.jobparser.telegram.LinkedInBot;
import com.example.jobparser.utils.TextUtils;
import com.example.jobparser.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LinkedinJobsParseService {
    @Value("${linkedin.parsing.jobs.url}")
    private String jobParseUrl;

    @Value("${linkedin.parsing.jobs.scroll-url}")
    private String jobParseScrollUrl;

    @Value("${linkedin.parsing.jobs.list.class-name}")
    private String jobListClassName;

    @Value("${linkedin.parsing.jobs.card.base-card.class-name}")
    private String cardBaseClassName;

    @Value("${linkedin.parsing.jobs.card.base-card.data-attributes.entity-urn}")
    private String dataEntityUrnName;

    @Value("${linkedin.parsing.jobs.card.base-card.data-attributes.tracking-id}")
    private String dataTrackingIdName;

    @Value("${linkedin.parsing.jobs.card.base-card.data-attributes.reference-id}")
    private String dataReferenceIdName;

    @Value("${linkedin.parsing.jobs.full-link-class-name}")
    private String detailJobUrlClassName;

    @Value("${linkedin.parsing.jobs.meta.job-title-class-name}")
    private String jobTitleClassName;

    @Value("${linkedin.parsing.jobs.meta.job-subtitle-class-name}")
    private String jobSubTitleClassName;


    @Value("${linkedin.parsing.jobs.meta.meta-data-class-name}")
    private String jobMetaClassName;

    @Value("${linkedin.parsing.jobs.meta.job-time-datetime-att-name}")
    private String jobTimeAttName;

    @Value("${linkedin.parsing.jobs.meta.jobs-count-class-name}")
    private String jobsCountClassName;

    @Value("${linkedin.parsing.jobs.card.base-card.search-entity-media.class-name}")
    private String searchEntityMediaClassName;

    private final LinkedInBot linkedInBot;
    private final JobRepository jobRepository;

    private String getJobId(Element element) {
        var dataEntityAttributeListValues = element.attribute(dataEntityUrnName).getValue().split(":");
        return dataEntityAttributeListValues.length > 0 ? dataEntityAttributeListValues[dataEntityAttributeListValues.length - 1] : "";
    }

    private String getTrackingId(Element element) {
        return element.attribute(dataTrackingIdName).getValue();
    }

    private String getReferenceId(Element element) {
        return element.attribute(dataReferenceIdName).getValue();
    }

    private String getJobUrl(Element element) {
        var detailedLinkElement = element.getElementsByClass(detailJobUrlClassName).get(0);
        return detailedLinkElement.attribute("href").getValue();
    }

    private String getJobTitle(Element element) {
        var jobTitleElement = element.getElementsByClass(jobTitleClassName).get(0);
        return jobTitleElement.ownText().trim();
    }

    private Element getSubtitleElement(Element element) {
        var jobSubTitleElement = element.getElementsByClass(jobSubTitleClassName).get(0);
        return jobSubTitleElement.getElementsByTag("a").get(0);
    }

    private String getCompanyUrl(Element element) {
        return element.attribute("href").getValue();
    }

    private String getCompanyImageUrl(Element element) {
        System.out.println("------ getCompanyImageUrl 1");
        var wrapper = element.getElementsByClass(searchEntityMediaClassName).get(0);
        System.out.println("------ getCompanyImageUrl 2: " + wrapper);
        var image = wrapper.getElementsByTag("img").get(0);
        System.out.println("------ getCompanyImageUrl 3: " + image);
        var url = image.attribute("data-delayed-url");
        System.out.println("------ getCompanyImageUrl 4: " + url);
        return url.getValue();
    }

    private String getCompanyName(Element element) {
        return element.ownText().trim();
    }

    private Element getMetaElement(Element element) {
        return element.getElementsByClass(jobMetaClassName).get(0);
    }

    private String getJobLocation(Element element) {
        var jobLocationElement = element.getElementsByTag("span").get(0);
        return jobLocationElement.ownText().trim();
    }

    private Element getTimeElement(Element element) {
        return element.getElementsByTag("time").get(0);
    }

    private String getJobDate(Element element) {
        return element.attribute(jobTimeAttName).getValue();
    }

    private String getTimeMessage(Element element) {
        return element.ownText().trim();
    }

    private Integer getJobsCountMessage(Element element) {
        var dirtyCount = element.getElementsByClass(jobsCountClassName).get(0).ownText();
        return Objects.requireNonNull(TextUtils.extractFirstNumber(dirtyCount)).intValue();
    }

    private Job parseJob(Element element) {
        var baseCard = element.getElementsByClass(cardBaseClassName).get(0);

        var jobId = getJobId(baseCard);
        var trackingId = getTrackingId(baseCard);
        var referenceId = getReferenceId(baseCard);
        var jobUrl = getJobUrl(element);
        var jobTitle = getJobTitle(element);
        var subtitleElement = getSubtitleElement(element);
        var companyImageUrl = getCompanyImageUrl(baseCard);
        var companyUrl = getCompanyUrl(subtitleElement);
        var companyName = getCompanyName(subtitleElement);
        var jobMetaElement = getMetaElement(element);
        var timeElement = getTimeElement(jobMetaElement);
        var jobLocation = getJobLocation(jobMetaElement);
        var date = getJobDate(timeElement);
        var timeMessage = getTimeMessage(timeElement);

        var jobCreatedAtLocalDateTime = TimeUtils.getDateSubtractTimeMessage(timeMessage);
        var jobCreatedAtInstance = TimeUtils.toInstant(jobCreatedAtLocalDateTime);

        System.out.println("companyImageUrl: " + companyImageUrl);

        return Job.builder()
                .jobId(Long.valueOf(jobId))
                .trackingId(trackingId)
                .referenceId(referenceId)
                .jobUrl(jobUrl)
                .companyUrl(companyUrl)
                .jobTitle(jobTitle)
                .companyName(companyName)
                .jobLocation(jobLocation)
                .date(date)
                .companyImageUrl(companyImageUrl)
                .timeMessage(timeMessage)
                .jobCreatedAt(jobCreatedAtInstance)
                .build();
    }

    private String getJobParseUrl() {
        URI uri = UriComponentsBuilder.fromHttpUrl(jobParseUrl)
                .queryParam("f_T", "9,3172,25170,25201,100,8632")
                .queryParam("geoId", "101318387")
                .queryParam("keywords", "frontend developer")
                .queryParam("location", "Florida, United States")
                .queryParam("origin", "JOB_SEARCH_PAGE_JOB_FILTER")
                .queryParam("sortBy", "R")
                .build()
                .encode()
                .toUri();

        return uri.toString();
    }

    public void saveParsedJobs(List<Job> jobs) {
        try {
            List<Job> filteredParsedJobs = jobs.stream()
                    .filter(job -> !jobRepository.existsByJobId(job.getJobId()))
                    .toList();

            System.out.println("                                    SAVE JOBS!         PARSED Count: " + jobs.size());
            System.out.println("                                    SAVE JOBS!         FILTERED PARSED Count: " + filteredParsedJobs.size());

            jobRepository.saveAll(filteredParsedJobs);
        } catch (RuntimeException e) {
            throw new CustomRuntimeException("Something was wrong... Class: LinkedinJobsParseService / Method: parseJobs");
        }
    }

    public Integer parseJobs() {
        try {
            var url = getJobParseUrl();
            var doc = Jsoup.connect(url).get();

            var jobsCount = getJobsCountMessage(doc);

            var jobList = doc.getElementsByClass(jobListClassName).get(0);
            var jobs = jobList.getElementsByTag("li");

            List<Job> parsedJobs = new ArrayList<>();

            for (var job : jobs) {
                parsedJobs.add(parseJob(job));
            }

            System.out.println("                                    PARSED FINISH! Count: " + parsedJobs.size());

            saveParsedJobs(parsedJobs);

            return jobsCount;
        } catch (RuntimeException | IOException e) {
            throw new CustomRuntimeException("Something was wrong... Class: LinkedinJobsParseService / Method: parseJobs");
        }
    }

    private String getJobParseScrollUrl(Integer start) {
        URI uri = UriComponentsBuilder.fromHttpUrl(jobParseScrollUrl)
                .queryParam("start", start)
                .queryParam("f_T", "9,3172,25170,25201,100,8632")
                .queryParam("geoId", "101318387")
                .queryParam("keywords", "frontend developer")
                .queryParam("location", "Florida, United States")
                .queryParam("origin", "JOB_SEARCH_PAGE_JOB_FILTER")
                .queryParam("sortBy", "R")
                .build()
                .encode()
                .toUri();

        return uri.toString();
    }

    public Integer parseScrollJob(Integer start) {
        try {
            var url = getJobParseScrollUrl(start);
            var doc = Jsoup.connect(url).get();

            var jobs = doc.getElementsByTag("li");

            List<Job> parsedJobs = new ArrayList<>();

            for (var job : jobs) {
                parsedJobs.add(parseJob(job));
            }

            System.out.println("                                    SCROLL PARSED FINISH! Count: " + parsedJobs.size());

            saveParsedJobs(parsedJobs);

            return jobs.size();
        } catch (RuntimeException | IOException e) {
            throw new CustomRuntimeException("Something was wrong... Class: LinkedinJobsParseService / Method: parseJobs");
        }
    }

    public List<Job> findTopJobs() {
        try {
            return jobRepository.findTop3();
        } catch (RuntimeException e) {
            throw new CustomRuntimeException("Something was wrong... Class: LinkedinJobsParseService / Method: parseJobs");
        }
    }
}
