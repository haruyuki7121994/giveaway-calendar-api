package com.example.giveawaycalendar.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaginationService implements PaginationServiceImpl {
    @Override
    public Map<String, Object> getMetadata(Page<?> objectPage) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("currentPage", objectPage.getNumber());
        metadata.put("totalItems", objectPage.getTotalElements());
        metadata.put("totalPages", objectPage.getTotalPages());
        metadata.put("size", objectPage.getSize());
        return metadata;
    }
}
