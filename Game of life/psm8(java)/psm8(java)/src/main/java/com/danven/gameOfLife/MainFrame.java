package com.danven.gameOfLife;

import com.danven.gameOfLife.enums.Axis;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final Simulation simulation = new Simulation(150, 150);
    private final JPanel rightPanel = new JPanel();
    private final Timer timer;

    public MainFrame() {
        setTitle("Conway's Game of Life");
        setBounds(50, 50, 800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        timer = new Timer(100, ev -> {
            simulation.update();
        });

        JPanel root = new JPanel();
        root.setLayout(new LayoutLinear(Axis.AXIS_X));

        CGOLRenderingComponent viewport = new CGOLRenderingComponent(simulation);
        root.add(viewport, LayoutLinear.ElementSpec.createGrow(100));

        rightPanel.setLayout(new LayoutLinear(Axis.AXIS_Y));

        JTextField ruleTextField = new JTextField("B3/S23");
        rightPanel.add(ruleTextField);

        JButton applyRuleButton = new JButton("Apply Rule");

        applyRuleButton.addActionListener(ev -> {
            if (!simulation.applyRule(ruleTextField.getText())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid rule string",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        rightPanel.add(applyRuleButton);

        JSlider timerDelaySlider = addSlider("Update delay", "ms", 10, 1000, 100);
        timerDelaySlider.addChangeListener(ev -> {
            timer.setInitialDelay(timerDelaySlider.getValue());
            timer.setDelay(timerDelaySlider.getValue());
        });

        JButton pauseResumeButton = new JButton("Resume");
        pauseResumeButton.addActionListener(ev -> {
            if (timer.isRunning()) {
                timer.stop();
                pauseResumeButton.setText("Resume");
            } else {
                timer.start();
                pauseResumeButton.setText("Pause");
            }
        });
        rightPanel.add(pauseResumeButton);

        JButton singleStepButton = new JButton("Step");
        singleStepButton.addActionListener(ev -> {
            simulation.update();
        });
        rightPanel.add(singleStepButton);


        JSlider fieldWidthSlider = addSlider("Field width", "", 3, 120, 100);
        fieldWidthSlider.addChangeListener(ev -> {
            simulation.reset(fieldWidthSlider.getValue(), simulation.getHeight());
        });

        JSlider fieldHeightSlider = addSlider("Field height", "", 3, 120, 100);
        fieldHeightSlider.addChangeListener(ev -> {
            simulation.reset(simulation.getWidth(), fieldHeightSlider.getValue());
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(ev -> {
            simulation.reset();
        });
        rightPanel.add(resetButton);

        JTextArea helpInfo = new JTextArea("Left click to draw, Right click to erase");
        helpInfo.setEditable(false);
        helpInfo.setFocusable(false);
        rightPanel.add(helpInfo);

        root.add(rightPanel, LayoutLinear.ElementSpec.createMin(300));

        setContentPane(root);
    }

    private JSlider addSlider(String title, String unitSuffix, int min, int max, int initial) {
        JLabel label = new JLabel(title + " (" + initial + unitSuffix + "):");
        rightPanel.add(label);

        JSlider slider = new JSlider(min, max, initial);
        slider.addChangeListener(ev -> {
            label.setText(title + " (" + slider.getValue() + unitSuffix + "):");
        });
        rightPanel.add(slider);

        return slider;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}