package com.argusoft.form.dto;

public class AnswerDTO {
  private String answer_id;
  private Object answer;

  public AnswerDTO() {
  }

  public AnswerDTO(String answer_id, Object answer) {
    this.answer_id = answer_id;
    this.answer = answer;
  }

  public String getAnswer_id() {
    return answer_id; 
  }

  public void setAnswer_id(String answer_id) {
    this.answer_id = answer_id;
  }

  public Object getAnswer() {
    return answer;
  }

  public void setAnswer(Object answer) {
    this.answer = answer;
  }

  @Override
  public String toString() {
    return "AnswerDTO [answer_id=" + answer_id + ", answer=" + answer + "]";
  }

  
}
