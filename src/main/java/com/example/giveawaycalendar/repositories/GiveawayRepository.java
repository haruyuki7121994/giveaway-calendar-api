package com.example.giveawaycalendar.repositories;

import com.example.giveawaycalendar.entities.Giveaway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

public interface GiveawayRepository extends JpaRepository<Giveaway, Integer> {
    Page<Giveaway> findAllByExported(Boolean exported, Pageable pageable);
    Page<Giveaway> findAllByStatus(Integer status, Pageable pageable);
    int countAllByExportedAndEndedAtBetween(Boolean exported, Date endedAt, Date endedAt2);
    List<Giveaway> findAllByExportedAndEndedAtBetweenOrderByEndedAtAsc(Boolean exported, Date endedAt, Date endedAt2);
    List<Giveaway> findAllByStatusAndEndedAtLessThan(Integer status, Date endedAt);
    List<Giveaway> findAllByRefIn(Collection<String> refs);
}
