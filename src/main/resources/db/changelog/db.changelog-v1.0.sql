--liquibase formatted sql

--changeset Georgii:1
CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    telegram_id BIGINT UNIQUE,
    user_name   varchar(64)                         NOT NULL,
    first_name  varchar(64)                         NOT NULL,
    last_name   varchar(64)                         NOT NULL,
    email       varchar(64),
    phone       varchar(64),
    created_at  TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    deleted_at  TIMESTAMP
);

--changeset Georgii:2
CREATE TABLE IF NOT EXISTS jobs
(
    job_id         BIGINT PRIMARY KEY                  NOT NULL UNIQUE,
    tracking_id    VARCHAR(128)                        NOT NULL,
    reference_id   VARCHAR(128)                        NOT NULL,
    job_title      VARCHAR(255)                        NOT NULL,
    company_name   VARCHAR(255)                        NOT NULL,
    job_location   VARCHAR(255)                        NOT NULL,
    time_message   VARCHAR(32)                         NOT NULL,
    date           VARCHAR(32)                         NOT NULL,
    is_checked     BOOLEAN                             NOT NULL DEFAULT false,
    job_created_at TIMESTAMP                           NOT NULL,
    job_url        TEXT                                NOT NULL,
    company_url    TEXT                                NOT NULL,
    created_at     TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    deleted_at     TIMESTAMP
);

--changeset Georgii:3
CREATE TABLE IF NOT EXISTS parse_query
(
    id         BIGSERIAL PRIMARY KEY,
    f_T        VARCHAR(128),
    geo_id     VARCHAR(128),
    keywords   VARCHAR(128),
    location   VARCHAR(128),
    origin     VARCHAR(128),
    sort_by    VARCHAR(128),
    created_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP
);

--changeset Georgii:4
INSERT INTO parse_query (f_T, geo_id, keywords, location, origin, sort_by)
VALUES ('9,3172,25170,25201,100,8632',
        '101318387',
        'frontend developer',
        'Florida, United States',
        'JOB_SEARCH_PAGE_JOB_FILTER',
        'R');