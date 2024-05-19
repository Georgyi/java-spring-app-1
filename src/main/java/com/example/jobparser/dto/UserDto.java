package com.example.jobparser.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserDto extends TimestampsFields {
    private Long id;
    private Long telegramId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
