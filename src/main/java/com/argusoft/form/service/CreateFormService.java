package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.entity.UserInfo;
import com.argusoft.form.repository.CreateFormRepository;

@Service
public class CreateFormService {

    @Autowired
    private CreateFormRepository createFormRepository;

    public Page<CreateForm> findAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        return this.createFormRepository.findAll(p);
    }

    public Optional<CreateForm> findById(Long id) {
        return createFormRepository.findById(id);
    }

    public List<CreateForm> findFormsByAdmin(UserInfo adminUser) {
        return createFormRepository.findByAdmin(adminUser);
    }

    public void save(CreateForm createForm) {
        createFormRepository.insertCreateForm(createForm.getTitle(), createForm.getAdmin().getId(),
                createForm.getCreatedAt(),
                createForm.getQuestions());
    }

    public void deleteById(Long id) {
        createFormRepository.deleteById(id);
    }

    public void updateForm(CreateForm createForm) {
        createFormRepository.updateCreateForm(createForm.getId(), createForm.getTitle(), createForm.getAdmin().getId(),
                createForm.getCreatedAt(),
                createForm.getQuestions());
    }
}
