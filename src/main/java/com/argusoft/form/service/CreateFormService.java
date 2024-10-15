package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.repository.CreateFormRepository;

@Service
public class CreateFormService {

    @Autowired
    private CreateFormRepository createFormRepository;

    public List<CreateForm> findAll() {
        return createFormRepository.findAll();
    }

    public Optional<CreateForm> findById(Long id) {
        return createFormRepository.findById(id);
    }

    public void save(CreateForm createForm) {
        createFormRepository.insertCreateForm(createForm.getAdminId(), createForm.getCreatedAt(),
                createForm.getQuestions());
    }

    public void deleteById(Long id) {
        createFormRepository.deleteById(id);
    }
}
