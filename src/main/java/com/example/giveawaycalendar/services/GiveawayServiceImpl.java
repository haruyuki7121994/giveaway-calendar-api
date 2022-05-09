package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.GiveawayRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Giveaway;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;

public interface GiveawayServiceImpl {
    Page<Giveaway> getAllWithPaging(ParamsRequest params);
    Boolean create(GiveawayRequest request);
    Boolean delete(Giveaway giveaway);
    int countByExported(Boolean exported, Date endDate);
    List<String> export(GiveawayRequest request);
    void updateStatusGiveaways(int status, Date date);
}
