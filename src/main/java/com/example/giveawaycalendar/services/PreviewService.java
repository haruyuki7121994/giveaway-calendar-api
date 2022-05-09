package com.example.giveawaycalendar.services;

import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.dto.PreviewLinkDto;
import com.example.giveawaycalendar.dto.PreviewRequest;
import com.example.giveawaycalendar.entities.PreviewBatch;
import com.example.giveawaycalendar.entities.PreviewLink;
import com.example.giveawaycalendar.repositories.PreviewBatchRepository;
import com.example.giveawaycalendar.repositories.PreviewLinkRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PreviewService implements PreviewServiceImpl{
    @Autowired
    PreviewLinkRepository linkRepository;

    @Autowired
    PreviewBatchRepository batchRepository;

    private final Type previewLinksType = new TypeToken<ArrayList<PreviewLinkDto>>() {}.getType();
    private static final Logger log = LoggerFactory.getLogger(GiveawayService.class);

    @Override
    public Page<PreviewBatch> getAllWithPaging(ParamsRequest params) {
        String[] parts = params.getOrderBy().split("-");
        Pageable paging = PageRequest.of(
                params.getPage(), params.getSize(),
                parts[1].equals("desc") ? Sort.by(parts[0]).descending() : Sort.by(parts[0]).ascending()
        );

        return batchRepository.findAll(paging);
    }

    @Override
    public Boolean create(PreviewRequest request) {

        try {
            List<PreviewLinkDto> data = new Gson().fromJson(request.getPreviewJson(), previewLinksType);
            List<PreviewLinkDto> validPreviewLinks = getValidPreviewLink(data);

            if (validPreviewLinks.size() == 0 ) return false;
            PreviewBatch newBatch = createNewBatch();

            for (PreviewLinkDto previewLinkDto : validPreviewLinks) {
                createPreviewLink(previewLinkDto, newBatch);
            }

            return true;
        } catch (Exception e) {
            log.error(String.format("PreviewService(create): %s", e.getMessage()));
            return false;
        }
    }

    @Override
    public void updatePreviewLink(PreviewLink previewLink) {
        try {
            previewLink.setInserted(true);
            linkRepository.save(previewLink);
        } catch (Exception e) {
            log.error(String.format("PreviewService(updatePreviewLink): %s", e.getMessage()));
        }
    }

    @Override
    public void clearOldData(Timestamp startDate, Timestamp endDate) {
        try {
            List<PreviewBatch> previewBatches = batchRepository.findAllByCreatedAtBetween(startDate, endDate);
            batchRepository.deleteAll(previewBatches);
        } catch (Exception e) {
            log.error(String.format("PreviewService(clearOldData): %s", e.getMessage()));
        }
    }

    private List<PreviewLinkDto> getValidPreviewLink(List<PreviewLinkDto> uncheckPreviewLinks) {
        Set<String> links = uncheckPreviewLinks.stream()
                .map(PreviewLinkDto::getLink)
                .collect(Collectors.toSet());

        Set<PreviewLink> existPreviewLinks = linkRepository.findAllByLinkIn(links);

        return  uncheckPreviewLinks.stream()
                .filter(previewLinkDto -> {
                    PreviewLink existLink = existPreviewLinks.stream()
                            .filter(previewLink -> Objects.equals(previewLink.getLink(), previewLinkDto.getLink()))
                            .findFirst()
                            .orElse(null);
                    return existLink == null;
                })
                .collect(Collectors.toList());
    }

    private PreviewBatch createNewBatch() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PreviewBatch batch = new PreviewBatch();
        batch.setCreatedAt(timestamp);
        batch.setName("BATCH-" + timestamp.getTime());
        batchRepository.save(batch);
        return batch;
    }

    private void createPreviewLink(PreviewLinkDto dto, PreviewBatch batch) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PreviewLink link = new PreviewLink();
        link.setLink(dto.getLink());
        link.setEndedAt(new Timestamp(simpleDateFormat.parse(dto.getEnded_at()).getTime()));
        link.setBatch(batch);
        linkRepository.save(link);
    }
}
