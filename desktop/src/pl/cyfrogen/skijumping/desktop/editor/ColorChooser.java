package pl.cyfrogen.skijumping.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooser extends JDialog implements ChangeListener {
    private final JColorChooser colorChooser;
    private final SwingColorChanged onColorChanged;

    public ColorChooser(JFrame frame, Color color, SwingColorChanged onColorChanged) {
        super(frame);
        this.onColorChanged = onColorChanged;
        JPanel panel = new JPanel(new BorderLayout());

        colorChooser = new JColorChooser(color);
        colorChooser.getSelectionModel().addChangeListener(this);
        add(colorChooser, BorderLayout.PAGE_END);
        frame.add(panel);
        pack();
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Color newColor = colorChooser.getColor();
        onColorChanged.colorChanged(newColor);
    }
}
