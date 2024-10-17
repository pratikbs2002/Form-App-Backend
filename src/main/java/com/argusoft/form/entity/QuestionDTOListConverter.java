package com.argusoft.form.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QuestionDTOListConverter implements AttributeConverter<List<QuestionDTO>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<QuestionDTO> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            String jsonString = objectMapper.writeValueAsString(attribute);
            System.out.println("Converting to JSON: " + jsonString); // Debug log
            return jsonString; // Return JSON string
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of QuestionDTO to JSON string.", e);
        }
    }

    @Override
    public List<QuestionDTO> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<QuestionDTO>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON string to list of QuestionDTO.", e);
        }
    }
}
