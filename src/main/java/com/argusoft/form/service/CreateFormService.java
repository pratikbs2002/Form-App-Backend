package com.argusoft.form.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.CreateForm;
import com.argusoft.form.repository.CreateFormRepository;

@Service
public class CreateFormService {

    @Autowired
    private CreateFormRepository createFormRepository;

    public Page<CreateForm> findAll(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        // int pageSize = 5;
        // int pageNumber = 0;
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        return this.createFormRepository.findAll(p);

        // Page<CreateForm> pageForm =
        // return this.createFormRepository.findAll(p);
        // FormResponseDTO formResponse = new FormResponseDTO();
        // formResponse.setContent(pageForm.getContent());
        // formResponse.setPageNumber(pageForm.getNumber());
        // formResponse.setPageSize(pageForm.getSize());
        // formResponse.setTotalElements(pageForm.getTotalElements());
        // formResponse.setTotalPages(pageForm.getTotalPages());
        // formResponse.setLastPage(pageForm.isLast());
        // return formResponse;
        // return createFormRepository.findAll();
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

    public void updateForm(CreateForm createForm) {
        createFormRepository.updateCreateForm(createForm.getId(), createForm.getAdminId(), createForm.getCreatedAt(),
                createForm.getQuestions());
    }
}
