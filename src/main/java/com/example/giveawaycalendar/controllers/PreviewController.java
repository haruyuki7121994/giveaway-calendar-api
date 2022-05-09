package com.example.giveawaycalendar.controllers;

import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.dto.PreviewRequest;
import com.example.giveawaycalendar.entities.PreviewBatch;
import com.example.giveawaycalendar.services.LogServiceImpl;
import com.example.giveawaycalendar.services.PaginationServiceImpl;
import com.example.giveawaycalendar.services.PreviewServiceImpl;
import com.example.giveawaycalendar.services.ResponseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/previews")
public class PreviewController {
    @Autowired
    PreviewServiceImpl service;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    ResponseServiceImpl responseService;

    @Autowired
    PaginationServiceImpl paginationService;

    @GetMapping("/")
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id-desc") String orderBy,
            @RequestParam(defaultValue = "") String filter
    ) {
        ParamsRequest paramsRequest = new ParamsRequest(page, size, orderBy, filter);
        Page<PreviewBatch> batches = service.getAllWithPaging(paramsRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("batches", batches.getContent());

        Map<String, Object> metadata = paginationService.getMetadata(batches);
        return responseService.successWithPaging(metadata, response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PreviewRequest previewRequest) {
        return service.create(previewRequest) ?
                responseService.success("Create successful!", null) :
                responseService.badRequest("Create failed!");
    }
}
