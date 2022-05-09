package com.example.giveawaycalendar.schedule;

import com.example.giveawaycalendar.services.ContentServiceImpl;
import com.example.giveawaycalendar.services.GiveawayService;
import com.example.giveawaycalendar.services.LogServiceImpl;
import com.example.giveawaycalendar.services.PreviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class ScheduleTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduleTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    GiveawayService giveawayService;

    @Autowired
    PreviewServiceImpl previewService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    ContentServiceImpl contentService;

    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    public void checkStatusGiveaways(){
        log.info("checkStatusGiveaways-start-{}", dateFormat.format(new Date()));
        giveawayService.updateStatusGiveaways(0, new java.sql.Date(new Timestamp(System.currentTimeMillis()).getTime()));
        log.info("checkStatusGiveaways-end-{}", dateFormat.format(new Date()));
    }

    @Scheduled(fixedRate = 15 * 24 * 60 * 60 * 1000)
    public void removeOldData() {
        log.info("removeOldData-preview-start-{}", dateFormat.format(new Date()));
        LocalDateTime endDate = LocalDateTime.now().minusDays(15);
        LocalDateTime startDate = endDate.minusYears(3);
        previewService.clearOldData(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        log.info("removeOldData-preview-end-{}", dateFormat.format(new Date()));

        log.info("removeOldData-content-start-{}", dateFormat.format(new Date()));
        contentService.clearOldData(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        log.info("removeOldData-content-end-{}", dateFormat.format(new Date()));

        log.info("removeOldData-log-start-{}", dateFormat.format(new Date()));
        LocalDate logEndDate = LocalDate.now().minusMonths(1);
        LocalDate logStartDate = logEndDate.minusYears(3);
        logService.clearOldData(java.sql.Date.valueOf(logStartDate), java.sql.Date.valueOf(logEndDate));
        log.info("removeOldData-log-end-{}", dateFormat.format(new Date()));
    }
}
