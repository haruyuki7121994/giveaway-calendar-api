package com.example.giveawaycalendar.controllers;

import com.example.giveawaycalendar.dto.ContentRequest;
import com.example.giveawaycalendar.dto.GiveawayRequest;
import com.example.giveawaycalendar.dto.ParamsRequest;
import com.example.giveawaycalendar.entities.Content;
import com.example.giveawaycalendar.entities.Giveaway;
import com.example.giveawaycalendar.repositories.ContentRepository;
import com.example.giveawaycalendar.services.ContentService;
import com.example.giveawaycalendar.services.ContentServiceImpl;
import com.example.giveawaycalendar.services.PaginationServiceImpl;
import com.example.giveawaycalendar.services.ResponseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @Autowired
    ResponseServiceImpl responseService;

    @Autowired
    ContentServiceImpl service;

    @Autowired
    PaginationServiceImpl paginationService;

    @Autowired
    ContentRepository repository;

    @GetMapping("/")
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id-desc") String orderBy,
            @RequestParam(defaultValue = "") String filter
    ) {
        ParamsRequest paramsRequest = new ParamsRequest(page, size, orderBy, filter);
        Page<Content> contents = service.getAllWithPaging(paramsRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("contents", contents.getContent());

        Map<String, Object> metadata = paginationService.getMetadata(contents);
        return responseService.successWithPaging(metadata, response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ContentRequest contentRequest) {
        return service.create(contentRequest) ?
                responseService.success("Create successful!", null) :
                responseService.badRequest("Create failed!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ContentRequest contentRequest) {
        Content content = repository.findById(id).orElse(null);
        if (content == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        contentRequest.setCreatedAt(content.getCreatedAt());
        contentRequest.setId(id);
        return service.create(contentRequest) ?
                responseService.success("Update successful!", null) :
                responseService.badRequest("Update failed!");
    }
}
