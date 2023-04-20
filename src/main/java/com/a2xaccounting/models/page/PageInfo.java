package com.a2xaccounting.models.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PageInfo {

  private Integer pageNumber;
  private Integer pageSize;
  private Integer totalElements;
  private Integer totalPages;
  private SortInfo sortInfo;

  public enum Direction {
    ASC,
    DESC;
  }

  @Data
  @AllArgsConstructor
  public static class SortInfo {

    private List<String> sortBys;
    private Direction sortDirection;
  }
}
