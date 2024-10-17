package com.argusoft.form.dto;

import java.util.List;

public class QuestionDTO {
    private String question_id;
    private String question_text;
    private String answerType;
    private List<String> options;
    private boolean required;

    public QuestionDTO() {
    }

    public QuestionDTO(String question_id, String question_text, String answerType, List<String> options,
            boolean required) {
        this.question_id = question_id;
        this.question_text = question_text;
        this.answerType = answerType;
        this.options = options;
        this.required = required;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "QuestionDTO [question_id=" + question_id + ", question_text=" + question_text + ", answerType="
                + answerType + ", options=" + options + ", required=" + required + "]";
    }

}
