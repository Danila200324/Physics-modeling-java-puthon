package com.danven.gameOfLife.event;

public interface EventHandler<T> {
    void handleEvent(T event);
}