package com.example.giveawaycalendar.controllers;

import com.example.giveawaycalendar.dto.GiveawayRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Giveaway;
import com.example.giveawaycalendar.entities.Log;
import com.example.giveawaycalendar.entities.PreviewLink;
import com.example.giveawaycalendar.repositories.GiveawayRepository;
import com.example.giveawaycalendar.repositories.PreviewLinkRepository;
import com.example.giveawaycalendar.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/giveaways")
public class GiveawayController {
    @Autowired
    GiveawayServiceImpl giveawayService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    PreviewLinkRepository linkRepository;

    @Autowired
    PreviewServiceImpl previewService;

    @Autowired
    ResponseServiceImpl responseService;

    @Autowired
    PaginationServiceImpl paginationService;

    @Autowired
    GiveawayRepository repository;

    @GetMapping("/")
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id-desc") String orderBy,
            @RequestParam(defaultValue = "") String filter
    ) {
        ParamsRequest paramsRequest = new ParamsRequest(page, size, orderBy, filter);
        Page<Giveaway> giveaways = giveawayService.getAllWithPaging(paramsRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("giveaways", giveaways.getContent());

        Map<String, Object> metadata = paginationService.getMetadata(giveaways);
        return responseService.successWithPaging(metadata, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable("id") int id) {
        return repository.findById(id)
                .map(result -> responseService.success("Find successful!", result))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GiveawayRequest giveawayRequest) {
        if (giveawayRequest.getPreviewId() != null) {
            PreviewLink previewLink = linkRepository.getById(giveawayRequest.getPreviewId());
            previewService.updatePreviewLink(previewLink);
        }
        return giveawayService.create(giveawayRequest) ?
                responseService.success("Create successful!", null) :
                responseService.badRequest("Create failed!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiveawayRequest giveawayRequest) {
        giveawayRequest.setId(id);
        return giveawayService.create(giveawayRequest) ?
                responseService.success("Update successful!", null) :
                responseService.badRequest("Update failed!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        Optional<Giveaway> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return responseService.notFound();
        }
        return giveawayService.delete(optional.get()) ?
                responseService.success("Delete successful!", null) :
                responseService.badRequest("Delete failed!");
    }

    @GetMapping("/export/count")
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "") Date endDate
    ) {
        return responseService.success("Count successful!", giveawayService.countByExported(false, endDate));
    }

    @GetMapping("/export")
    public ResponseEntity<?> getExport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id-desc") String orderBy,
            @RequestParam(defaultValue = "api-export-giveaway") String filter
    ) {
        ParamsRequest paramsRequest = new ParamsRequest(page, size, orderBy, filter);
        Page<Log> logs = logService.getAllWithPaging(paramsRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("logs", logs.getContent());

        Map<String, Object> metadata = paginationService.getMetadata(logs);
        return responseService.successWithPaging(metadata, response);
    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestBody GiveawayRequest giveawayRequest) {
        return responseService.success("Export successful!", giveawayService.export(giveawayRequest));
    }

    @PostMapping("/ref/all")
    public ResponseEntity<?> findByRefs(@RequestBody GiveawayRequest giveawayRequest) {
        return responseService.success("Get successful!", repository.findAllByRefIn(giveawayRequest.getRefs()));
    }
}
