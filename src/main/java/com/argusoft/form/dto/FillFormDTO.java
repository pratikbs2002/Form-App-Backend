package com.argusoft.form.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.argusoft.form.entity.LocationPoint;

public class FillFormDTO {
  private Long id;
  private String title;
  private Long formId;
  private Long userId;
  private List<AnswerDTO> answers = new ArrayList<>();
  private LocalDateTime createdAt;
  private LocationPoint location;
  private Integer locationId;

  public FillFormDTO() {
  }

  public FillFormDTO(Long id, String title, Long formId, Long userId, List<AnswerDTO> answers, LocalDateTime createdAt,
      LocationPoint location, Integer locationId) {
    this.id = id;
    this.title = title;
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

  public List<AnswerDTO> getAnswers() {
    return answers;
  }

  public void setAnswers(List<AnswerDTO> answers) {
    this.answers = answers;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocationPoint getLocation() {
    return location;
  }

  public void setLocation(LocationPoint location) {
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
    return "FillFormDTO [id=" + id + ", formId=" + formId + ", userId=" + userId + ", answers=" + answers
        + ", createdAt=" + createdAt + ", location=" + location + ", locationId=" + locationId + "]";
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
