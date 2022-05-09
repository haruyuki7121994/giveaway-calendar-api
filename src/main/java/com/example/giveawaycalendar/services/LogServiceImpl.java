package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Log;
import org.springframework.data.domain.Page;

import java.sql.Date;

public interface LogServiceImpl {
    Page<Log> getAllWithPaging(ParamsRequest params);
    void clearOldData(Date startDate, Date endDate);
}
