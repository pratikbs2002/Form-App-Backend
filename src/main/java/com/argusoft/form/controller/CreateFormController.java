package com.argusoft.form.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
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

import com.argusoft.form.dto.FormDTO;
import com.argusoft.form.dto.FormResponseDTO;
import com.argusoft.form.dto.QuestionDTO;
import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.service.CreateFormService;
import com.argusoft.form.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/forms")
public class CreateFormController {

    @Autowired
    private CreateFormService createFormService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/all")
    public ResponseEntity<FormResponseDTO> getAllForms(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        Page<CreateForm> pageForm = createFormService.findAll(pageNumber, pageSize, sortBy, sortDir);

        FormResponseDTO formResponseDTO = new FormResponseDTO();
        formResponseDTO.setPageNumber(pageForm.getNumber());
        formResponseDTO.setPageSize(pageForm.getSize());
        formResponseDTO.setTotalElements(pageForm.getTotalElements());
        formResponseDTO.setTotalPages(pageForm.getTotalPages());
        formResponseDTO.setLastPage(pageForm.isLast());

        for (CreateForm createForm : pageForm.getContent()) {
            FormDTO formDTO = new FormDTO();

            try {
                List<QuestionDTO> questions = objectMapper.readValue(createForm.getQuestions(),
                        new TypeReference<List<QuestionDTO>>() {
                        });
                formDTO.setId(createForm.getId());
                formDTO.setTitle(createForm.getTitle());
                formDTO.setAdminId(createForm.getAdmin().getId());
                formDTO.setCreatedAt(createForm.getCreatedAt());
                formDTO.setQuestions(questions);
                formResponseDTO.addContent(formDTO);

            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(formResponseDTO);

        // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<FormDTO> getFormById(@PathVariable Long id) {
        Optional<CreateForm> createForm = createFormService.findById(id);
        FormDTO formDTO = new FormDTO();

        if (createForm.isPresent()) {
            try {
                List<QuestionDTO> questions = objectMapper.readValue(createForm.get().getQuestions(),
                        new TypeReference<List<QuestionDTO>>() {
                        });
                formDTO.setId(id);
                formDTO.setTitle(createForm.get().getTitle());
                formDTO.setAdminId(createForm.get().getAdmin().getId());
                formDTO.setCreatedAt(createForm.get().getCreatedAt());
                formDTO.setQuestions(questions);

            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok(formDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> createdddForm(@RequestBody Map<String, Object> createForm) {
        System.out.println(createForm);

        CreateForm savedForm = new CreateForm();

        if (createForm.containsKey("adminId")) {
            Long adminId = Long.parseLong(createForm.get("adminId").toString());
            UserInfo admin = userInfoService.getUserById(adminId).get();
            if (admin != null) {
                savedForm.setAdmin(admin);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Admin with provided ID not found");
            }
        }

        if (createForm.containsKey("title")) {
            savedForm.setTitle(createForm.get("title").toString());
        }

        try {
            if (createForm.containsKey("questions")) {
                String questionsJson = objectMapper.writeValueAsString(createForm.get("questions"));
                savedForm.setQuestions(questionsJson);
            }
            createFormService.save(savedForm);
            return ResponseEntity.status(HttpStatus.CREATED).body("Form created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid questions format");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        if (createFormService.findById(id).isPresent()) {
            createFormService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<String> editForm(@RequestBody Map<String, Object> createForm, @PathVariable Long id) {
        Optional<CreateForm> form = createFormService.findById(id);
        if (form.isPresent()) {
            CreateForm savedForm = new CreateForm();
            if (createForm.containsKey("adminId")) {
                Long adminId = Long.parseLong(createForm.get("adminId").toString());
                UserInfo admin = userInfoService.getUserById(adminId).get();
                if (admin != null) {
                    savedForm.setAdmin(admin);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Admin with provided ID not found");
                }
            }
            if (createForm.containsKey("id")) {
                savedForm.setId(Long.parseLong(createForm.get("id").toString()));
            }
            if (createForm.containsKey("title")) {
                savedForm.setTitle(createForm.get("title").toString());
            }

            try {
                String questionsJson = objectMapper.writeValueAsString(createForm.get("questions"));
                savedForm.setQuestions(questionsJson);
                System.out.println(createForm);
                createFormService.updateForm(savedForm);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid questions format");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Saved");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Form Not Found!");
    }
}
