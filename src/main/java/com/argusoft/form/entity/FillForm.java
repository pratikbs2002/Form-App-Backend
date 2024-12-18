package com.argusoft.form.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FillForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "jsonb")
    private String answers;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String title;

    @Column(columnDefinition = "loc", name = "location")
    @Convert(converter = LocationPointConverter.class)
    private LocationPoint locationPoint;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private CreateForm form;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "is_submitted")
    private boolean isSubmitted;

    public FillForm() {
    }

    public FillForm(Long id, String answers, LocalDateTime createdAt, LocationPoint locationPoint, CreateForm form,
            UserInfo user, Location location, boolean isSubmitted, String title) {
        this.id = id;
        this.answers = answers;
        this.createdAt = createdAt;
        this.locationPoint = locationPoint;
        this.form = form;
        this.user = user;
        this.location = location;
        this.isSubmitted = isSubmitted;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocationPoint getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(LocationPoint locationPoint) {
        this.locationPoint = locationPoint;
    }

    public CreateForm getForm() {
        return form;
    }

    public void setForm(CreateForm form) {
        this.form = form;
    }

    public UserInfo getUserInfo() {
        return user;
    }

    public void setUserInfo(UserInfo user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FillForm [id=" + id + ", answers=" + answers + ", createdAt=" + createdAt + ", title=" + title
                + ", locationPoint=" + locationPoint + ", form=" + form + ", user=" + user + ", location=" + location
                + ", isSubmitted=" + isSubmitted + "]";
    }

}
