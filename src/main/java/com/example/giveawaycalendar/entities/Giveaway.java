package com.example.giveawaycalendar.entities;

import com.example.giveawaycalendar.dto.GiveawayRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "giveaways")
public class Giveaway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "ref", nullable = false, length = -1)
    private String ref;

    @Basic
    @Column(name = "link", nullable = false, length = -1)
    private String link;

    @Basic
    @Column(name = "network", nullable = true, length = 50)
    private String network;

    @Basic
    @Column(name = "type", nullable = true)
    private String type;

    @Basic
    @Column(name = "status", nullable = true)
    private Integer status;

    @Basic
    @Column(name = "created_at", nullable = true)
    private Date createdAt;

    @Basic
    @Column(name = "ended_at", nullable = false)
    private Date endedAt;

    @Basic
    @Column(name = "exported", nullable = true)
    private Boolean exported;

    @Basic
    @Column(name = "data_json", nullable = true)
    private String dataJson;


    public Giveaway(GiveawayRequest request) {
        setId(request.getId());
        setNetwork(request.getNetwork());
        setExported(request.getExported() != null && request.getExported());
        setCreatedAt(request.getCreatedAt() == null ? new Date(new java.util.Date().getTime()) : request.getCreatedAt());
        setEndedAt(request.getEndedAt());
        setLink(request.getLink());
        setRef(request.getRef());
        setType(request.getType());
        setStatus(request.getStatus() == null ? 0 : request.getStatus());
        setDataJson(request.getDataJson());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giveaway giveaway = (Giveaway) o;
        return id == giveaway.id  && Objects.equals(network, giveaway.network) && Objects.equals(type, giveaway.type) && Objects.equals(status, giveaway.status) && Objects.equals(createdAt, giveaway.createdAt) && Objects.equals(endedAt, giveaway.endedAt) && Objects.equals(exported, giveaway.exported) && Objects.equals(ref, giveaway.ref) && Objects.equals(link, giveaway.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, network, type, status, createdAt, endedAt, exported, ref, link);
    }
}
