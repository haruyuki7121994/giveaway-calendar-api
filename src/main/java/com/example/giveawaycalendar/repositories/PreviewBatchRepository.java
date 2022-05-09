package com.example.giveawaycalendar.repositories;

import com.example.giveawaycalendar.entities.PreviewBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PreviewBatchRepository extends JpaRepository<PreviewBatch, Integer> {
    List<PreviewBatch> findAllByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);
}
