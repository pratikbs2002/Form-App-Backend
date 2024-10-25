package com.argusoft.form.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.dto.AnswerDTO;
import com.argusoft.form.dto.FillFormDTO;
import com.argusoft.form.dto.FillFormResponseDTO;
import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.FillForm;
import com.argusoft.form.entity.LocationPoint;
import com.argusoft.form.service.CreateFormService;
import com.argusoft.form.service.FillFormService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/fillform")
public class FillFormController {

  @Autowired
  private FillFormService fillFormService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CreateFormService createFormService;

  @PostMapping("/add")
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
    LocationPoint locationPoint = new LocationPoint(20.202020, 20.202020);

    submittedForm.setLocation(locationPoint);

    try {
      String answersJson = objectMapper.writeValueAsString(fillForm.get("answers"));
      submittedForm.setAnswers(answersJson);
      System.out.println(submittedForm);
      fillFormService.save(submittedForm);
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.CREATED).body("Submitted");
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getSubmittedForm(@PathVariable Long id) {
    FillForm fillForm = fillFormService.findById(id).orElse(null);

    FillFormDTO fillFormDTO = new FillFormDTO();

    if (fillForm != null) {
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setFormId(fillForm.getFormId());
      fillFormDTO.setUserId(fillForm.getUserId());
      fillFormDTO.setLocationId(fillForm.getLocationId());
      fillFormDTO.setLocation(fillForm.getLocation());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      try {
        List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
            new TypeReference<List<AnswerDTO>>() {
            });
        System.out.println(answersList);
        for (AnswerDTO answerDTO : answersList) {
          System.out.println(answerDTO.getAnswer() instanceof String);
        }

        fillFormDTO.setAnswers(answersList);
      } catch (Exception e) {
        System.out.println(e.getMessage());

      }
    }

    return ResponseEntity.ok(fillFormDTO);
  }

  @GetMapping("/all")
  public ResponseEntity<FillFormResponseDTO> getAllSubmittedForm(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

    Page<FillForm> pageFillForm;

    if ((sortBy).equalsIgnoreCase("title")) {
      pageFillForm = fillFormService.findAll(pageNumber, pageSize, "id", sortDir);

      List<FillForm> sortedContent = pageFillForm.getContent().stream()
          .sorted((f1, f2) -> {
            Optional<CreateForm> createForm1 = createFormService.findById(f1.getFormId());
            Optional<CreateForm> createForm2 = createFormService.findById(f2.getFormId());
            String title1 = createForm1.isPresent() ? createForm1.get().getTitle() : "undefined";
            String title2 = createForm2.isPresent() ? createForm2.get().getTitle() : "undefined";
            return "asc".equalsIgnoreCase(sortDir)
                ? title1.compareTo(title2)
                : title2.compareTo(title1);
          })
          .collect(Collectors.toList());

      pageFillForm = new PageImpl<>(sortedContent, PageRequest.of(pageNumber, pageSize),
          pageFillForm.getTotalElements());
    } else {
      pageFillForm = fillFormService.findAll(pageNumber, pageSize, sortBy, sortDir);
    }

    FillFormResponseDTO fillFormResponseDTO = new FillFormResponseDTO();
    fillFormResponseDTO.setPageNumber(pageFillForm.getNumber());
    fillFormResponseDTO.setPageSize(pageFillForm.getSize());
    fillFormResponseDTO.setTotalElements(pageFillForm.getTotalElements());
    fillFormResponseDTO.setTotalPages(pageFillForm.getTotalPages());
    fillFormResponseDTO.setLastPage(pageFillForm.isLast());

    for (FillForm fillForm : pageFillForm.getContent()) {
      FillFormDTO fillFormDTO = new FillFormDTO();
      String title = createFormService.findById(fillForm.getFormId()).get().getTitle();

      fillFormDTO.setTitle(title);
      fillFormDTO.setFormId(fillForm.getFormId());
      fillFormDTO.setUserId(fillForm.getUserId());
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setLocationId(fillForm.getLocationId());
      fillFormDTO.setLocation(fillForm.getLocation());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      try {
        List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
            new TypeReference<List<AnswerDTO>>() {
            });
        System.out.println(answersList);
        for (AnswerDTO answerDTO : answersList) {
          System.out.println(answerDTO.getAnswer() instanceof String);
        }

        fillFormDTO.setAnswers(answersList);
        fillFormResponseDTO.addContent(fillFormDTO);
      } catch (JsonProcessingException e) {
        System.out.println(e.getMessage());
      }
    }
    return ResponseEntity.ok(fillFormResponseDTO);
  }

  @DeleteMapping("/remove/{id}")
  public ResponseEntity<Void> deleteSubmittedForm(@PathVariable Long id) {
    if (fillFormService.findById(id).isPresent()) {
      System.out.println("----------------------");
      fillFormService.deleteById(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
