package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.dto.PreviewRequest;
import com.example.giveawaycalendar.entities.PreviewBatch;
import com.example.giveawaycalendar.entities.PreviewLink;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

public interface PreviewServiceImpl {
    Page<PreviewBatch> getAllWithPaging(ParamsRequest params);
    Boolean create(PreviewRequest request);
    void updatePreviewLink(PreviewLink previewLink);
    void clearOldData(Timestamp startDate, Timestamp endDate);
}
