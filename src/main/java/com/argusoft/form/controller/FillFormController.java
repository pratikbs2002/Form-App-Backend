package com.argusoft.form.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.dto.AnswerDTO;
import com.argusoft.form.dto.FillFormDTO;
import com.argusoft.form.dto.FillFormResponseDTO;
import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.FillForm;
import com.argusoft.form.entity.Location;
import com.argusoft.form.entity.LocationPoint;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.service.CreateFormService;
import com.argusoft.form.service.FillFormService;
import com.argusoft.form.service.LocationService;
import com.argusoft.form.service.UserInfoService;
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

  @Autowired
  private UserInfoService userInfoService;

  @Autowired
  private LocationService locationService;

  @PostMapping("/add")
  public ResponseEntity<String> addSubmittedForm(@RequestBody Map<String, Object> fillForm) {
    FillForm submittedForm = new FillForm();
    if (fillForm.containsKey("formId")) {
      Long formId = Long.parseLong(fillForm.get("formId").toString());
      Optional<CreateForm> formOptional = createFormService.findById(formId);
      if (formOptional.isPresent()) {
        submittedForm.setForm(formOptional.get());
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Form not found");
      }
    }

    if (fillForm.containsKey("userId")) {
      Long userId = Long.parseLong(fillForm.get("userId").toString());
      Optional<UserInfo> userOptional = userInfoService.getUserById(userId);
      if (userOptional.isPresent()) {
        submittedForm.setUserInfo(userOptional.get());
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("User not found");
      }
    }

    if (fillForm.containsKey("locationId")) {
      Long locationId = Long.parseLong(fillForm.get("locationId").toString());

      Optional<Location> location = locationService.findLocationById(locationId);
      if (location.isPresent()) {
        submittedForm.setLocation(location.get());
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Location not found");
      }
    }

    if (fillForm.containsKey("isSubmitted")) {
      submittedForm.setIsSubmitted(true);
    } else {
      submittedForm.setIsSubmitted(false);
    }

    LocationPoint locationPoint = new LocationPoint(20.202020, 20.202020);
    submittedForm.setLocationPoint(locationPoint);

    try {
      String answersJson = objectMapper.writeValueAsString(fillForm.get("answers"));
      submittedForm.setAnswers(answersJson);
      fillFormService.save(submittedForm);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " +
          e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.CREATED).body("Submitted");
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getSubmittedForm(@PathVariable Long id) {
    FillForm fillForm = fillFormService.getFillFormById(id);
    FillFormDTO fillFormDTO = new FillFormDTO();
    System.out.println(fillForm);
    if (fillForm != null) {
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setTitle(fillForm.getTitle());
      fillFormDTO.setFormId(fillForm.getForm().getId());
      fillFormDTO.setUserId(fillForm.getUserInfo().getId());
      fillFormDTO.setLocationId(fillForm.getLocation().getId());
      fillFormDTO.setLocation(fillForm.getLocationPoint());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      try {
        if (fillForm.getAnswers() != null) {
          List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
              new TypeReference<List<AnswerDTO>>() {
              });
          fillFormDTO.setAnswers(answersList);
        } else {
          fillFormDTO.setAnswers(null);
        }

      } catch (Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
      }
    }
    System.out.println(fillFormDTO);
    return ResponseEntity.ok(fillFormDTO);
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllSubmittedForm(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

    Page<FillForm> pageFillForm;

    if ((sortBy).equalsIgnoreCase("title")) {
      pageFillForm = fillFormService.getAllFillForm(pageNumber, pageSize, "id", sortDir);

      List<FillForm> sortedContent = pageFillForm.getContent().stream()
          .sorted((f1, f2) -> {
            Optional<CreateForm> createForm1 = createFormService.findById(f1.getForm().getId());
            Optional<CreateForm> createForm2 = createFormService.findById(f2.getForm().getId());
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
      pageFillForm = fillFormService.getAllFillForm(pageNumber, pageSize, sortBy, sortDir);
    }

    FillFormResponseDTO fillFormResponseDTO = new FillFormResponseDTO();
    fillFormResponseDTO.setPageNumber(pageFillForm.getNumber());
    fillFormResponseDTO.setPageSize(pageFillForm.getSize());
    fillFormResponseDTO.setTotalElements(pageFillForm.getTotalElements());
    fillFormResponseDTO.setTotalPages(pageFillForm.getTotalPages());
    fillFormResponseDTO.setLastPage(pageFillForm.isLast());

    for (FillForm fillForm : pageFillForm.getContent()) {

      FillFormDTO fillFormDTO = new FillFormDTO();
      String title = createFormService.findById(fillForm.getForm().getId()).get().getTitle();

      fillFormDTO.setTitle(title);
      fillFormDTO.setFormId(fillForm.getForm().getId());
      fillFormDTO.setUserId(fillForm.getUserInfo().getId());
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setLocationId(fillForm.getLocation().getId());
      fillFormDTO.setLocation(fillForm.getLocationPoint());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      fillFormDTO.setSubmitted(fillForm.getIsSubmitted());
      System.out.println(fillFormDTO);
      try {
        List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
            new TypeReference<List<AnswerDTO>>() {
            });
        fillFormDTO.setAnswers(answersList);
        fillFormResponseDTO.addContent(fillFormDTO);
      } catch (JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
      }
    }
    return ResponseEntity.ok(fillFormResponseDTO);
  }

  @DeleteMapping("/remove/{id}")
  public ResponseEntity<Void> deleteSubmittedForm(@PathVariable Long id) {
    if (fillFormService.getFillFormById(id) != null) {
      fillFormService.deleteById(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PostMapping("/assign/{locationId}/{formId}")
  public ResponseEntity<String> assignFormToUser(@PathVariable Long locationId, @PathVariable Long formId) {

    FillForm submittedForm = new FillForm();
    if (formId != null) {
      Optional<CreateForm> formOptional = createFormService.findById(formId);
      if (formOptional.isPresent()) {
        submittedForm.setForm(formOptional.get());
        submittedForm.setTitle(formOptional.get().getTitle());
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Form not found");
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Form Id can not be null");
    }

    LocationPoint locationPoint = new LocationPoint(20.202020, 20.202020);

    if (locationId != null) {
      Optional<Location> location = locationService.findLocationById(locationId);
      if (location.isPresent()) {
        submittedForm.setLocation(location.get());
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Location not found");
      }
      List<UserInfo> users = userInfoService.getUsersByLocationAndChildLocations(locationId);
      for (UserInfo userInfo : users) {
        FillForm fillForm = new FillForm();
        fillForm.setTitle(submittedForm.getTitle());
        fillForm.setForm(submittedForm.getForm());
        fillForm.setAnswers(null);
        fillForm.setLocation(submittedForm.getLocation());
        fillForm.setLocationPoint(locationPoint);
        fillForm.setUserInfo(userInfo);
        System.out.println(fillForm);
        try {
          fillFormService.save(fillForm);
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " +
              e.getMessage());
        }
      }
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Location Id can not be null");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body("Submitted");
  }

  @GetMapping("/all/{userId}")
  public ResponseEntity<?> getAllFillFormByReportingUserId(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
      @PathVariable Long userId) {
    Page<FillForm> pageFillForm;

    if ((sortBy).equalsIgnoreCase("title")) {
      pageFillForm = fillFormService.getAllFillFormByReportingUserId(pageNumber, pageSize, "id", sortDir, userId);

      List<FillForm> sortedContent = pageFillForm.getContent().stream()
          .sorted((f1, f2) -> {
            Optional<CreateForm> createForm1 = createFormService.findById(f1.getForm().getId());
            Optional<CreateForm> createForm2 = createFormService.findById(f2.getForm().getId());
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
      pageFillForm = fillFormService.getAllFillFormByReportingUserId(pageNumber, pageSize, sortBy, sortDir, userId);
    }

    FillFormResponseDTO fillFormResponseDTO = new FillFormResponseDTO();
    fillFormResponseDTO.setPageNumber(pageFillForm.getNumber());
    fillFormResponseDTO.setPageSize(pageFillForm.getSize());
    fillFormResponseDTO.setTotalElements(pageFillForm.getTotalElements());
    fillFormResponseDTO.setTotalPages(pageFillForm.getTotalPages());
    fillFormResponseDTO.setLastPage(pageFillForm.isLast());

    for (FillForm fillForm : pageFillForm.getContent()) {

      FillFormDTO fillFormDTO = new FillFormDTO();
      String title = createFormService.findById(fillForm.getForm().getId()).get().getTitle();

      fillFormDTO.setTitle(title);
      fillFormDTO.setFormId(fillForm.getForm().getId());
      fillFormDTO.setUserId(fillForm.getUserInfo().getId());
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setLocationId(fillForm.getLocation().getId());
      fillFormDTO.setLocation(fillForm.getLocationPoint());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      fillFormDTO.setSubmitted(fillForm.getIsSubmitted());
      System.out.println(fillFormDTO);

      try {
        if (fillForm.getAnswers() != null) {
          List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
              new TypeReference<List<AnswerDTO>>() {
              });
          fillFormDTO.setAnswers(answersList);
        }
        fillFormResponseDTO.addContent(fillFormDTO);
      } catch (JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

      }
    }
    return ResponseEntity.ok(fillFormResponseDTO);
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateSubmittedForm(@RequestBody Map<String, Object> fillForm) {
    if (!fillForm.containsKey("fillFormId")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("fillForm are required for updating the form.");
    }
    if (!fillForm.containsKey("userId")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("UserId are required for updating the form.");
    }

    Long userId = Long.parseLong(fillForm.get("userId").toString());
    Long fillFormId = Long.parseLong(fillForm.get("fillFormId").toString());

    Optional<FillForm> existingFormOptional = fillFormService.getFillFormByFormIdAndUserId(fillFormId, userId);
    if (existingFormOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Form not found.");
    }

    String answersJson;
    LocalDateTime createdAt = LocalDateTime.now();

    try {
      if (fillForm.containsKey("answers")) {
        answersJson = objectMapper.writeValueAsString(fillForm.get("answers"));
      } else {
        answersJson = existingFormOptional.get().getAnswers();
      }
      fillFormService.updateFillForm(existingFormOptional.get().getId(), answersJson, createdAt, true);
      return ResponseEntity.status(HttpStatus.OK).body("Form updated successfully.");
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing JSON: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
  }

  @PutMapping("/update/save")
  public ResponseEntity<String> updateSaveSubmittedForm(@RequestBody Map<String, Object> fillForm) {
    System.out.println("********************************");
    if (!fillForm.containsKey("fillFormId")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("fillForm are required for updating the form.");
    }
    if (!fillForm.containsKey("userId")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("UserId are required for updating the form.");
    }

    Long userId = Long.parseLong(fillForm.get("userId").toString());
    Long fillFormId = Long.parseLong(fillForm.get("fillFormId").toString());

    Optional<FillForm> existingFormOptional = fillFormService.getFillFormByFormIdAndUserId(fillFormId, userId);
    if (existingFormOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submitted form not found.");
    }

    String answersJson;
    LocalDateTime createdAt = LocalDateTime.now();

    try {
      if (fillForm.containsKey("answers")) {
        answersJson = objectMapper.writeValueAsString(fillForm.get("answers"));
      } else {
        answersJson = existingFormOptional.get().getAnswers();
      }
      System.out.println("=============");
      fillFormService.updateFillForm(existingFormOptional.get().getId(), answersJson, createdAt, false);
      return ResponseEntity.status(HttpStatus.OK).body("Form updated successfully.");
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing JSON: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
  }

  // Filled form for admin
  @GetMapping("/all/admin/{userId}")
  public ResponseEntity<?> getAllFillFormByAdminUserId(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
      @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
      @PathVariable Long userId) {

    Page<FillForm> pageFillForm;

    if ((sortBy).equalsIgnoreCase("title")) {
      pageFillForm = fillFormService.getAllFillFormByAdminUserId(pageNumber, pageSize, "id", sortDir, userId);

      List<FillForm> sortedContent = pageFillForm.getContent().stream()
          .sorted((f1, f2) -> {
            Optional<CreateForm> createForm1 = createFormService.findById(f1.getForm().getId());
            Optional<CreateForm> createForm2 = createFormService.findById(f2.getForm().getId());
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
      pageFillForm = fillFormService.getAllFillFormByAdminUserId(pageNumber, pageSize, sortBy, sortDir, userId);
    }

    FillFormResponseDTO fillFormResponseDTO = new FillFormResponseDTO();
    fillFormResponseDTO.setPageNumber(pageFillForm.getNumber());
    fillFormResponseDTO.setPageSize(pageFillForm.getSize());
    fillFormResponseDTO.setTotalElements(pageFillForm.getTotalElements());
    fillFormResponseDTO.setTotalPages(pageFillForm.getTotalPages());
    fillFormResponseDTO.setLastPage(pageFillForm.isLast());

    for (FillForm fillForm : pageFillForm.getContent()) {

      FillFormDTO fillFormDTO = new FillFormDTO();
      String title = createFormService.findById(fillForm.getForm().getId()).get().getTitle();

      fillFormDTO.setTitle(title);
      fillFormDTO.setFormId(fillForm.getForm().getId());
      fillFormDTO.setUserId(fillForm.getUserInfo().getId());
      fillFormDTO.setId(fillForm.getId());
      fillFormDTO.setLocationId(fillForm.getLocation().getId());
      fillFormDTO.setLocation(fillForm.getLocationPoint());
      fillFormDTO.setCreatedAt(fillForm.getCreatedAt());
      fillFormDTO.setSubmitted(fillForm.getIsSubmitted());
      System.out.println(fillFormDTO);

      try {
        if (fillForm.getAnswers() != null) {
          List<AnswerDTO> answersList = objectMapper.readValue(fillForm.getAnswers(),
              new TypeReference<List<AnswerDTO>>() {
              });
          fillFormDTO.setAnswers(answersList);
        }
        fillFormResponseDTO.addContent(fillFormDTO);
      } catch (JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
      }
    }
    return ResponseEntity.ok(fillFormResponseDTO);
  }

}