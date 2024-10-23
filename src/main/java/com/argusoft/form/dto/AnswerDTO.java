package com.argusoft.form.dto;

public class AnswerDTO {
  private String answer_id;
  private String answer;

  public AnswerDTO() {
  }

  public AnswerDTO(String answer_id, String answer) {
    this.answer_id = answer_id;
    this.answer = answer;
  }

  public String getAnswer_id() {
    return answer_id;
  }

  public void setAnswer_id(String answer_id) {
    this.answer_id = answer_id;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  @Override
  public String toString() {
    return "AnswerDTO [answer_id=" + answer_id + ", answer=" + answer + "]";
  }

  
}
