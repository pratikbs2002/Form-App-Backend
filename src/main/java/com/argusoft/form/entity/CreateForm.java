package com.argusoft.form.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CreateForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "jsonb")
    private String questions;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private UserInfo admin;

    public CreateForm() {
    }

    public CreateForm(Long id, String title, String questions, LocalDateTime createdAt, UserInfo admin) {
        this.id = id;
        this.title = title;
        this.questions = questions;
        this.createdAt = createdAt;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestions() {
        return questions;}


    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserInfo getAdmin() {
        return admin;
    }

    public void setAdmin(UserInfo admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "CreateForm [id=" + id + ", title=" + title + ", questions=" + questions + ", createdAt=" + createdAt
                + ", admin=" + admin + "]";
    }

}
