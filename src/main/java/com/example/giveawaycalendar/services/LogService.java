package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Log;
import com.example.giveawaycalendar.repositories.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class LogService implements LogServiceImpl{
    @Autowired
    LogRepository repository;

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    @Override
    public Page<Log> getAllWithPaging(ParamsRequest params) {
        String[] parts = params.getOrderBy().split("-");
        Pageable paging = PageRequest.of(
                params.getPage(), params.getSize(),
                parts[1].equals("desc") ? Sort.by(parts[0]).descending() : Sort.by(parts[0]).ascending()
        );

        return repository.findAllByName(params.getFilter(), paging);
    }

    @Override
    public void clearOldData(Date startDate, Date endDate) {
        try {
            List<Log> logs = repository.findAllByCreatedAtBetween(startDate, endDate);
            repository.deleteAll(logs);
        } catch (Exception e) {
            log.error(String.format("LogService(clearOldData): %s", e.getMessage()));
        }
    }
}
