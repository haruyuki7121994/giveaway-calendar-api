package com.example.giveawaycalendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GiveawayRequest {
    private Integer previewId;
    private int id;
    private String ref;
    private String link;
    private String network;
    private String type;
    private Integer status;
    private Date createdAt;
    private Date endedAt;
    private Boolean exported;
    private Date endDate;
    private String dataJson;
    private List<String> refs;
}
