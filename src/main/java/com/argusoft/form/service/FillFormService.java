package com.argusoft.form.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.FillForm;
import com.argusoft.form.repository.FillFormRepository;

@Service
public class FillFormService {

  @Autowired
  private FillFormRepository fillFormRepository;

  public void save(FillForm fillForm) {
    System.out.println("inside-------------------");
    System.out.println(fillForm);
    fillFormRepository.insertFillForm(fillForm.getFormId(), fillForm.getUserId(), fillForm.getAnswers(),
        fillForm.getCreatedAt(), fillForm.getLocation().getLat(),fillForm.getLocation().getLng(), fillForm.getLocationId());
  }

  public void deleteById(Long id) {
    fillFormRepository.deleteById(id);
  }

  public void updateForm(FillForm fillForm) {
    fillFormRepository.updateFillForm(fillForm.getFormId(), fillForm.getUserId(), fillForm.getAnswers(),
        fillForm.getCreatedAt(), fillForm.getLocation(), fillForm.getLocationId());
  }

  public Optional<FillForm> findById(Long id){
    return fillFormRepository.findById(id);
  }
}
