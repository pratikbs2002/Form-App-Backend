package com.argusoft.form.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FillForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_id", nullable = false)
    private Long formId;

    private Long userId;

    @Column(columnDefinition = "jsonb")
    private String answers;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // @Embedded
    // private LocationType location;

    @Column(columnDefinition = "point")
    @Convert(converter = LocationPointConverter.class)
    private String location;

    @Column(name = "loaction_id", nullable = false)
    private Integer locationId;

    public FillForm() {
    }

    public FillForm(Long id, Long formId, Long userId, String answers, LocalDateTime createdAt, String location,
            Integer locationId) {
        this.id = id;
        this.formId = formId;
        this.userId = userId;
        this.answers = answers;
        this.createdAt = createdAt;
        this.location = location;
        this.locationId = locationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "FillForm [id=" + id + ", formId=" + formId + ", userId=" + userId + ", answers=" + answers
                + ", createdAt=" + createdAt + ", location=" + location + ", locationId=" + locationId + "]";
    }

}
