package com.example.giveawaycalendar.repositories;

import com.example.giveawaycalendar.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> findAllByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);
}
