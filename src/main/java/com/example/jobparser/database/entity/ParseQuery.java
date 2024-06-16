package com.example.jobparser.database.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parse_query")
public class ParseQuery extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_T")
    private String fT;
    @Column(name = "geo_id")
    private String geoId;
    @Column(name = "keywords")
    private String keywords;
    @Column(name = "location")
    private String location;
    @Column(name = "origin")
    private String origin;
    @Column(name = "sort_by")
    private String sortBy;
}
