package com.argusoft.form.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.FillForm;
import com.argusoft.form.entity.LocationPointConverter;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.repository.FillFormRepository;

import jakarta.persistence.EntityManager;

@Service
public class FillFormService {

  @Autowired
  private FillFormRepository fillFormRepository;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserInfoService userInfoService;

  @Autowired
  private CreateFormService createFormService;

  public void save(FillForm fillForm) {
    LocationPointConverter locationPointConverter = new LocationPointConverter();
    String points = locationPointConverter.convertToDatabaseColumn(fillForm.getLocationPoint());
    fillFormRepository.insertFillForm(fillForm.getForm().getId(), fillForm.getUserInfo().getId(), fillForm.getAnswers(),
        fillForm.getCreatedAt(), points, fillForm.getLocation().getId(), fillForm.getIsSubmitted());
  }

  public void deleteById(Long id) {
    FillForm entity = fillFormRepository.findById(id).orElse(null);
    if (entity != null) {
      entityManager.detach(entity);
      fillFormRepository.deleteFillFormById(id);
    }
  }

  public Optional<FillForm> findById(Long id) {
    return fillFormRepository.findById(id);
  }

  public List<FillForm> findByFormId(Long formId) {
    return fillFormRepository.getFillFormByFormId(formId);
  }

  public Optional<FillForm> getFillFormByFormIdAndUserId(Long formId, Long userId) {
    return fillFormRepository.findFillFormByFormIdAndUserId(formId, userId);
  }

  public Page<FillForm> findAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
    return fillFormRepository.getAllFillForm(p);
  }

  public Page<FillForm> getAllFillFormByReportingUserId(Integer pageNumber, Integer pageSize, String sortBy,
      String sortDir, Long userId) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable p = PageRequest.of(pageNumber, pageSize, sort);

    return fillFormRepository.findAllFillFormByReportingUserId(p, userId);
  }

  public Page<FillForm> getAllFillFormByAdminUserId(Integer pageNumber, Integer pageSize, String sortBy,
      String sortDir, Long userId) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Optional<UserInfo> adminUserInfo = userInfoService.getUserById(userId);
    if (adminUserInfo.isEmpty()) {
      throw new IllegalArgumentException("Admin user not found with userId: " + userId);
    }
    List<CreateForm> createForms = createFormService.findFormsByAdmin(adminUserInfo.get());
    List<Long> formIds = createForms.stream().map(CreateForm::getId).toList();
    return fillFormRepository.findAllFillFormByFormIdsForAdmin(pageable, formIds);

  }

  public void updateFillForm(Long fillFormId, String answers, LocalDateTime createdAt, boolean isSubmitted) {
    // try {
      fillFormRepository.updateFillForm(fillFormId, answers, createdAt, isSubmitted);
    // } catch (Exception e) {
    //   throw new RuntimeException("Failed to update fill form with ID: " + fillFormId, e);
    // }
  }
}
