package com.danven.gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CGOLRenderingComponent extends JComponent {
    private final Simulation simulation;

    private final int CELL_SIZE = 6;

    public CGOLRenderingComponent(Simulation simulation) {
        this.simulation = simulation;

        simulation.eventSink_update.addHandler(ev -> repaint());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                if (x >= 0 && x < simulation.getWidth() && y >= 0 && y < simulation.getHeight()) {
                    simulation.setCell(x, y, e.getButton() == MouseEvent.BUTTON1);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                if (x >= 0 && x < simulation.getWidth() && y >= 0 && y < simulation.getHeight()) {
                    boolean isButtonDown = (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK;
                    simulation.setCell(x, y, isButtonDown);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < simulation.getWidth(); x++) {
            for (int y = 0; y < simulation.getHeight(); y++) {
                g.setColor(simulation.queryCell(x, y) ? Color.WHITE : Color.BLACK);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        g.setColor(new Color(0x303030));

        for (int x = 0; x <= simulation.getWidth(); x++) {
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, simulation.getHeight() * CELL_SIZE);
        }

        for (int y = 0; y <= simulation.getHeight(); y++) {
            g.drawLine(0, y * CELL_SIZE, simulation.getWidth() * CELL_SIZE, y * CELL_SIZE);
        }
    }
}
