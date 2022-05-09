package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.GiveawayRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Giveaway;
import com.example.giveawaycalendar.entities.Log;
import com.example.giveawaycalendar.repositories.GiveawayRepository;
import com.example.giveawaycalendar.repositories.LogRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GiveawayService implements GiveawayServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(GiveawayService.class);

    @Autowired
    GiveawayRepository repository;
    @Autowired
    LogRepository logRepository;

    private final Date oldDate;
    private final Date futureDate;

    public GiveawayService() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.oldDate = new Date(format.parse("2000-01-01").getTime());
        this.futureDate = new Date(format.parse("2100-01-01").getTime());
    }

    @Override
    public Page<Giveaway> getAllWithPaging(ParamsRequest params) {
        String[] parts = params.getOrderBy().split("-");
        Pageable paging = PageRequest.of(
                params.getPage(), params.getSize(),
                parts[1].equals("desc") ? Sort.by(parts[0]).descending() : Sort.by(parts[0]).ascending()
        );

        Page<Giveaway> pagination;

        switch (params.getFilter()) {
            case "exported":
                pagination = repository.findAllByExported(true, paging);
                break;
            case "processing":
                pagination = repository.findAllByStatus(0, paging);
                break;
            case "ended":
                pagination = repository.findAllByStatus(1, paging);
                break;
            default:
                pagination = repository.findAll(paging);
                break;
        }

        return pagination;
    }

    @Override
    public Boolean create(GiveawayRequest request) {
        try {
            Giveaway giveaway = new Giveaway(request);
            repository.save(giveaway);
            String strData = "";
            HashMap<String, Object> data = new HashMap<>();
            data.put("root", request.getLink());
            data.put("ref", request.getRef());
            data.put("type", request.getType());
            data.put("network", request.getNetwork());
            data.put("ended", request.getEndedAt());
            strData = new Gson().toJson(data);

            Log log = new Log(strData, "api-create-giveaway", giveaway.getCreatedAt());
            logRepository.save(log);

            return true;
        } catch (Exception e) {
            log.info("GiveawayService(create):" + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Giveaway giveaway) {
        try {
            repository.delete(giveaway);
            return true;
        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
            return false;
        }
    }

    @Override
    public int countByExported(Boolean exported, Date endDate) {

        try {
            return endDate == null ?
                    repository.countAllByExportedAndEndedAtBetween(exported, oldDate, futureDate) :
                    repository.countAllByExportedAndEndedAtBetween(exported, oldDate, endDate);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public List<String> export(GiveawayRequest request) {
        List<String> links = new ArrayList<>();
        try {
            List<Giveaway> giveaways;
            if (request.getEndDate() == null) {
                giveaways = repository.findAllByExportedAndEndedAtBetweenOrderByEndedAtAsc(false, oldDate, futureDate);
            } else {
                giveaways = repository.findAllByExportedAndEndedAtBetweenOrderByEndedAtAsc(
                        false, oldDate, new Date(request.getEndDate().getTime())
                );
            }
            giveaways.forEach(giveaway -> {
                giveaway.setExported(true);
                links.add(giveaway.getRef());
            });
//            repository.saveAll(giveaways);

            String strData = new Gson().toJson(links);
            Log log = new Log(strData, "api-export-giveaway", new Date(new Timestamp(System.currentTimeMillis()).getTime()));
            logRepository.save(log);

            return links;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateStatusGiveaways(int status, Date date) {
        List<Integer> ids = new ArrayList<>();
        try {
            List<Giveaway> giveaways = repository.findAllByStatusAndEndedAtLessThan(0, date);
            if (giveaways.size() > 0) {
                giveaways.forEach(giveaway -> {
                    giveaway.setStatus(1);
                    repository.save(giveaway);
                    ids.add(giveaway.getId());
                });

                String strData = new Gson().toJson(ids);
                Log log = new Log(strData, "schedule-check-status", date);
                logRepository.save(log);
            }

        } catch (Exception e) {
            log.error("GiveawayService(updateStatusGiveaways): " + e.getMessage());
        }
    }
}
