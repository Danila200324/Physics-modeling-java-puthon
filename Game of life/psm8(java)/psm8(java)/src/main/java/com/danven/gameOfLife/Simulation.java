package com.danven.gameOfLife;

import com.danven.gameOfLife.event.EventSink;

public class Simulation {
    private int width, height;
    private boolean[][] data;
    private boolean[][] newGeneration;
    private int ruleMask = (1 << 3) | (((1 << 2) | (1 << 3)) << 10);

    public static class UpdateEvent {

    }

    public final EventSink<UpdateEvent> eventSink_update = new EventSink<>();

    public Simulation(int width, int height) {
        reset(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean queryCell(int x, int y) {
        return data[x][y];
    }

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new boolean[width][height];
        this.newGeneration = new boolean[width][height];

        eventSink_update.emit(new UpdateEvent());
    }

    public void reset() {
        reset(getWidth(), getHeight());
    }

    private int countNeighbors(int x, int y) {
        int total = 0;
        for (int yy = -1; yy <= 1; yy++) {
            for (int xx = -1; xx <= 1; xx++) {
                if (xx == 0 && yy == 0) continue;
                if (queryCell(
                        Math.floorMod(x + xx, width),
                        Math.floorMod(y + yy, height)
                )) {
                    total++;
                }
            }
        }
        return total;
    }

    private boolean findNewState(int x, int y) {
        boolean cellState = queryCell(x, y);
        int neighborCount = countNeighbors(x, y);

        if (cellState) {
            return ((ruleMask >> neighborCount >> 10) & 1) == 1;
        } else {
            return ((ruleMask >> neighborCount) & 1) == 1;
        }
    }

    public void setCell(int x, int y, boolean state) {
        data[x][y] = state;

        eventSink_update.emit(new UpdateEvent());
    }

    public void applyRule(int ruleMask) {
        this.ruleMask = ruleMask;
    }

    public boolean applyRule(String ruleString) {
        String[] spl = ruleString.split("/", 2);

        int mask = 0;

        if (spl[0].isEmpty() || spl[1].isEmpty()) return false;
        if (spl[0].charAt(0) != 'B') return false;
        if (spl[1].charAt(0) != 'S') return false;

        for (int i = 1; i < spl[0].length(); i++) {
            char c = spl[0].charAt(i);
            if (c >= '0' && c <= '9') {
                mask |= 1 << (c - '0');
            } else {
                return false;
            }
        }

        for (int i = 1; i < spl[1].length(); i++) {
            char c = spl[1].charAt(i);
            if (c >= '0' && c <= '9') {
                mask |= (1 << (c - '0')) << 10;
            } else {
                return false;
            }
        }

        applyRule(mask);

        return true;
    }

    public void update() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                newGeneration[x][y] = findNewState(x, y);
            }
        }

        // swap buffers
        boolean[][] tmp = data;
        data = newGeneration;
        newGeneration = tmp;

        eventSink_update.emit(new UpdateEvent());
    }
}
