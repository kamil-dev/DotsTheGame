package main.java.view;

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    public ComboBoxRenderer() {
        setOpaque(true);
    }

    boolean isPicked = false;

    @Override
    public void setBackground(Color bg) {
        if(!isPicked) return;
        super.setBackground(bg);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        isPicked = true;
        setBackground((Color)value);
        isPicked = false;

        return this;
    }
}
