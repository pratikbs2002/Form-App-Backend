package com.argusoft.form.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.FormDTO;
import com.argusoft.form.entity.QuestionDTO;
import com.argusoft.form.service.CreateFormService;
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

    @GetMapping("/all")
    public ResponseEntity<List<FormDTO>> getAllForms() {

        List<FormDTO> formDTOs = new ArrayList<>();
        List<CreateForm> allForms = createFormService.findAll();
        for (CreateForm createForm : allForms) {
            FormDTO formDTO = new FormDTO();

            try {
                List<QuestionDTO> questions = objectMapper.readValue(createForm.getQuestions(),
                        new TypeReference<List<QuestionDTO>>() {
                        });
                formDTO.setId(createForm.getId());
                formDTO.setAdminId(createForm.getAdminId());
                formDTO.setCreatedAt(createForm.getCreatedAt());
                formDTO.setQuestions(questions);
                formDTOs.add(formDTO);

            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(formDTOs);

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
                formDTO.setAdminId(createForm.get().getAdminId());
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
        // System.out.println("+++++++++++++++++++++++"+createForm.get("questions"));
        if (createForm.containsKey("adminId")) {
            savedForm.setAdminId(Long.parseLong(createForm.get("adminId").toString()));
        }
        if (createForm.containsKey("id")) {
            savedForm.setId(Long.parseLong(createForm.get("id").toString()));
        }

        try {
            String questionsJson = objectMapper.writeValueAsString(createForm.get("questions"));
            savedForm.setQuestions(questionsJson);
            createFormService.save(savedForm);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid questions format");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Saved");
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
                savedForm.setAdminId(Long.parseLong(createForm.get("adminId").toString()));
            }
            if (createForm.containsKey("id")) {
                savedForm.setId(Long.parseLong(createForm.get("id").toString()));
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
