package com.example.giveawaycalendar.repositories;

import com.example.giveawaycalendar.entities.PreviewLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface PreviewLinkRepository extends JpaRepository<PreviewLink, Integer> {
    Set<PreviewLink> findAllByLinkIn(Collection<String> link);
}
