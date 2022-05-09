package com.example.giveawaycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParamsRequest {
    private Integer page;
    private Integer size;
    private String orderBy;
    private String filter;
}
