package com.example.giveawaycalendar.entities;

import com.example.giveawaycalendar.dto.ContentRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    private String content;

    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = true)
    private Timestamp createdAt;

    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = true)
    private Timestamp updatedAt;

    public Content(ContentRequest request, String type) {
        if (request.getId() != null) setId(request.getId());

        setContent(request.getContent());

        if (type.equals("create")) setCreatedAt(new Timestamp(System.currentTimeMillis()));
        else {
            setCreatedAt(request.getCreatedAt());
            setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        }
    }
}
