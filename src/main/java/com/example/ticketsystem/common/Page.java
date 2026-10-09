package com.example.ticketsystem.common;

import java.util.List;

public class Page<T> {
    private final List<T> items;
    private final int page;
    private final int size;
    private final long totalItems;

    public Page(List<T> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
    }

    public List<T> getItems() { return items; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalItems() { return totalItems; }
}
