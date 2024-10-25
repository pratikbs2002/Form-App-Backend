package com.argusoft.form.dto;

import java.util.ArrayList;
import java.util.List;

public class FillFormResponseDTO {

  private List<FillFormDTO> content = new ArrayList<>();
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean lastPage;

  public FillFormResponseDTO() {
  }

  public FillFormResponseDTO(List<FillFormDTO> content, int pageNumber, int pageSize, long totalElements,
      int totalPages, boolean lastPage) {
    this.content = content;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.lastPage = lastPage;
  }

  public List<FillFormDTO> getContent() {
    return content;
  }

  public void setContent(List<FillFormDTO> content) {
    this.content = content;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public boolean isLastPage() {
    return lastPage;
  }

  public void setLastPage(boolean lastPage) {
    this.lastPage = lastPage;
  }

  public void addContent(FillFormDTO fillFormDTO){
    content.add(fillFormDTO);
  }

  @Override
  public String toString() {
    return "FillFormResponseDTO [content=" + content + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize
        + ", totalElements=" + totalElements + ", totalPages=" + totalPages + ", lastPage=" + lastPage + "]";
  }

}
