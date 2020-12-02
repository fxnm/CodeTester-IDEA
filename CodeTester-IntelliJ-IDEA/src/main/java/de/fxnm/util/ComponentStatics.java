package de.fxnm.util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

public final class ComponentStatics {

    private ComponentStatics() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    public static JPanel addComponent(final int x, final int y, final JPanel panel) {
        return addComponent(new JPanel(), x, y, 1, 1, 1, 1, panel);
    }

    public static JPanel addComponent(final Component component, final int x, final int y, final JPanel panel) {
        return addComponent(component, x, y, 1, 1, 1, 1, panel);
    }

    public static JPanel addComponent(final Component component, final int x, final int y, final int width, final int height, final JPanel panel) {
        return addComponent(component, x, y, width, height, 1, 1, panel);
    }

    public static JPanel addComponent(final Component component, final int x, final int y, final int width,
                                      final int height, final int weightX, final int weightY, final JPanel panel) {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(component, constraints);

        return panel;
    }
}

