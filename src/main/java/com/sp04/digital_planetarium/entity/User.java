package com.sp04.digital_planetarium.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Embedded
    private Figure figure;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.figure = new Figure(true);
    }

    public User(Long uid, String username, String password, Figure figure) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.figure = figure;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}

