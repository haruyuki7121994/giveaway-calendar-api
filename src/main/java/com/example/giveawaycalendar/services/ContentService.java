package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ContentRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Content;
import com.example.giveawaycalendar.entities.Giveaway;
import com.example.giveawaycalendar.entities.Log;
import com.example.giveawaycalendar.repositories.ContentRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ContentService implements ContentServiceImpl{
    private static final Logger log = LoggerFactory.getLogger(GiveawayService.class);
    @Autowired
    ContentRepository repository;

    @Override
    public Page<Content> getAllWithPaging(ParamsRequest params) {
        String[] parts = params.getOrderBy().split("-");
        Pageable paging = PageRequest.of(
                params.getPage(), params.getSize(),
                parts[1].equals("desc") ? Sort.by(parts[0]).descending() : Sort.by(parts[0]).ascending()
        );

        return repository.findAll(paging);
    }

    @Override
    public Boolean create(ContentRequest request) {
        try {
            Integer id = request.getId();
            String type = id == null ? "create" : "update";
            Content content = new Content(request, type);
            repository.save(content);
            return true;
        } catch (Exception e) {
            log.error("ContentService(create): " + e.getMessage());
            return false;
        }
    }

    @Override
    public void clearOldData(Timestamp startDate, Timestamp endDate) {
        try {
            List<Content> contents = repository.findAllByCreatedAtBetween(startDate, endDate);
            repository.deleteAll(contents);
        } catch (Exception e) {
            log.error(String.format("ContentService(clearOldData): %s", e.getMessage()));
        }
    }
}
