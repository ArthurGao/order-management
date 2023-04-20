package com.a2xaccounting.models.page;

import lombok.Data;

import java.util.List;

@Data
public class PagedResult<T> {

  private PageInfo pageInfo;
  private List<T> content;

  public void setPagedResult(List<T> content, int pageNumber, int pageSize,
      int totalElements,
      int totalPages) {
    this.content = content;
    this.pageInfo = PageInfo.builder().pageNumber(pageNumber).pageSize(pageSize)
        .totalElements(totalElements).totalPages(totalPages).build();
  }
}
