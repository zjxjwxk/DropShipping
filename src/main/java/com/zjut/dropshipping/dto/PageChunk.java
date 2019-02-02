package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class PageChunk<T> {

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int numberOfElements;
}
