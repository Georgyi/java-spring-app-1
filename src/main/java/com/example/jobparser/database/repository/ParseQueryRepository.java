package com.example.jobparser.database.repository;

import com.example.jobparser.database.entity.ParseQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParseQueryRepository extends JpaRepository<ParseQuery, Long> {
    @Query("SELECT pq FROM ParseQuery pq ORDER BY pq.id ASC")
    Optional<ParseQuery> findFirst();
}
