package com.argusoft.form.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.FillForm;
import com.argusoft.form.entity.LocationPointConverter;
import com.argusoft.form.repository.FillFormRepository;

import jakarta.persistence.EntityManager;

@Service
public class FillFormService {

  @Autowired
  private FillFormRepository fillFormRepository;

  @Autowired
  private EntityManager entityManager;

  public void save(FillForm fillForm) {
    System.out.println("inside-------------------");
    System.out.println(fillForm);
    // LocationPoint location = new LocationPoint();

    LocationPointConverter locationPointConverter = new LocationPointConverter();
    String points = locationPointConverter.convertToDatabaseColumn(fillForm.getLocation());

    fillFormRepository.insertFillForm(fillForm.getFormId(), fillForm.getUserId(), fillForm.getAnswers(),
        fillForm.getCreatedAt(), points, fillForm.getLocationId());
  }

  public void deleteById(Long id) {
    FillForm entity = fillFormRepository.findById(id).orElse(null);
    if (entity != null) {
      entityManager.detach(entity);
      fillFormRepository.deleteFillFormById(id);
    }
    // fillFormRepository.deleteById(id);
  }

  // public void updateForm(FillForm fillForm) {
  // fillFormRepository.updateFillForm(fillForm.getFormId(), fillForm.getUserId(),
  // fillForm.getAnswers(),
  // fillForm.getCreatedAt(), fillForm.getLocation(), fillForm.getLocationId());
  // }

  public Optional<FillForm> findById(Long id) {
    return fillFormRepository.findById(id);
  }

  public Page<FillForm> findAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable p = PageRequest.of(pageNumber, pageSize, sort);


    return fillFormRepository.getAllFillForm(p);
  }
}
