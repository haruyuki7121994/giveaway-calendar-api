package com.example.giveawaycalendar.repositories;

import com.example.giveawaycalendar.entities.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    Page<Log> findAllByName(String name, Pageable pageable);
    List<Log> findAllByCreatedAtBetween(Date createdAt, Date createdAt2);
}
