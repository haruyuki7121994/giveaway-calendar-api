package com.example.giveawaycalendar.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "created_at", nullable = true)
    private Date createdAt;

    @Basic
    @Column(name = "data", nullable = false, length = -1)
    private String data;

    public Log(String strData, String name, Date createdAt) {
        this.data = strData;
        this.name = name;
        this.createdAt = createdAt;
    }
}
