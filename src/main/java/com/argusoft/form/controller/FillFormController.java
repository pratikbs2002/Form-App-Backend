package com.argusoft.form.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.FillForm;
import com.argusoft.form.entity.LocationType;
import com.argusoft.form.service.FillFormService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/fillform")
public class FillFormController {

  @Autowired
  private FillFormService fillFormService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/submit")
  public ResponseEntity<String> submittedForm(@RequestBody Map<String, Object> fillForm) {
    System.out.println(fillForm);

    FillForm submittedForm = new FillForm();
    // submittedForm.setFormId();
    if (fillForm.containsKey("formId")) {
      submittedForm.setFormId(Long.valueOf(fillForm.get("formId").toString()));
    }
    if (fillForm.containsKey("userId")) {
      submittedForm.setUserId(Long.valueOf(fillForm.get("userId").toString()));
    }
    if (fillForm.containsKey("locationId")) {
      submittedForm.setLocationId(Integer.parseInt(fillForm.get("locationId").toString()));
    }
    LocationType locationType = new LocationType(20.202020, 20.202020);
    submittedForm.setLocation(locationType);

    try {
      String answersJson = objectMapper.writeValueAsString(fillForm.get("answers"));
      submittedForm.setAnswers(answersJson);
      System.out.println(submittedForm);
      fillFormService.save(submittedForm);
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid answers format");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body("Submitted");
  }

}
