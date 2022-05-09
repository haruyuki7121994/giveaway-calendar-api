package com.example.giveawaycalendar.services;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface PaginationServiceImpl {
    Map<String, Object> getMetadata(Page<?> objectPage);
}
