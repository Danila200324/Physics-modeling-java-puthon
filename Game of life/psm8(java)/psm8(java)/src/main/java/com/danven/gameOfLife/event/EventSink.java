package com.danven.gameOfLife.event;

import java.util.ArrayList;
import java.util.List;

public class EventSink<T> {
    private final List<EventHandler<T>> eventHandlers = new ArrayList<>();

    public void addHandler(EventHandler<T> handler) {
        eventHandlers.add(handler);
    }

    public void emit(final T event) {
        eventHandlers.forEach(handler -> handler.handleEvent(event));
    }
}
