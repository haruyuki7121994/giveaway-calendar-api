package com.example.giveawaycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ContentRequest {
    private Integer id;
    private String content;
    private Timestamp createdAt;
}
