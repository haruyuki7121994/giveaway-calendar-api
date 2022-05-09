package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ContentRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Content;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

public interface ContentServiceImpl {
    Page<Content> getAllWithPaging(ParamsRequest params);
    Boolean create(ContentRequest request);
    void clearOldData(Timestamp startDate, Timestamp endDate);
}
