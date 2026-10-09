package com.example.ticketsystem.ticket;

import java.util.List;

public class TicketPageResponse {
    private final List<TicketResponse> items;
    private final int page;
    private final int size;
    private final long totalItems;

    public TicketPageResponse(List<TicketResponse> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
    }

    public List<TicketResponse> getItems() { return items; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalItems() { return totalItems; }
}
