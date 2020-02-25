package pl.cyfrogen.skijumping.desktop.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.editor.Editor;
import pl.cyfrogen.skijumping.editor.OnCheckboxChanged;
import pl.cyfrogen.skijumping.editor.OnColorChanged;
import pl.cyfrogen.skijumping.editor.OnSliderChanged;
import pl.cyfrogen.skijumping.platform.LeaderboardListener;
import pl.cyfrogen.skijumping.platform.PlatformAPI;
import pl.cyfrogen.skijumping.platform.SignInListener;

public class EditorLauncher extends JFrame implements Editor {

    private final JPanel mainPanel;

    public EditorLauncher() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout(new BorderLayout());

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1000;
        config.height = 500;
        config.vSyncEnabled = false;

        Main main = new Main(new PlatformAPI() {
            @Override
            public boolean isSignedIn() {
                return false;
            }

            @Override
            public void signInAsync(SignInListener listener) {

            }

            @Override
            public void submitScore(String scoreboard, long score) {

            }

            @Override
            public void showLeaderboard(String scoreboard, LeaderboardListener listener) {

            }

            @Override
            public void sendLoggingEvent(String id, Map<String, String> values) {

            }

            @Override
            public void logCrash(Exception e) {

            }

            @Override
            public void logCrashMessage(String s) {

            }

            @Override
            public void logCrashString(String key, String value) {

            }
        });
        main.attachEditor(this);

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(main, config);
        container.add(canvas.getCanvas(), BorderLayout.CENTER);

        pack();
        setVisible(true);
        setSize(config.width, config.height);

        JDialog dialog = new JDialog(this, "dialog Box");

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));




        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;

        JScrollPane scrollPane = new JScrollPane(mainPanel, v, h);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setSize(300, 600);
        dialog.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditorLauncher();
            }
        });
    }

    @Override
    public void addSlider(String name, float minValue, float maxValue, float currentValue, final OnSliderChanged onSliderChanged) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        jPanel.add(new JLabel(name), BorderLayout.NORTH);
        JSlider slider = new JSlider((int) (minValue * 1000), (int) (maxValue * 1000), (int) (currentValue * 1000));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                onSliderChanged.sliderChanged(((JSlider)e.getSource()).getValue() / (float) 1000);
            }
        });

        jPanel.add(slider, BorderLayout.CENTER);

        addToMainPanel(jPanel);
    }

    private void addToMainPanel(JPanel jpanel) {
        mainPanel.add(jpanel);

        JPanel panel = new JPanel(new BorderLayout());
        JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(jSeparator);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void addColorChanged(String name, String initColor, final OnColorChanged onColorChanged) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        final Color[] color = {new Color(
                Integer.valueOf(initColor.substring(0, 2), 16),
                Integer.valueOf(initColor.substring(2, 4), 16),
                Integer.valueOf(initColor.substring(4, 6), 16),
                Integer.valueOf(initColor.substring(6, 8), 16))};


        final JPanel colorPanel = new JPanel();
        colorPanel.setBackground(color[0]);
        colorPanel.setSize(300, 300);
        jPanel.add(colorPanel, BorderLayout.CENTER);

        jPanel.add(new JLabel(name), BorderLayout.NORTH);
        JButton jButton = new JButton("CHANGE");
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ColorChooser(EditorLauncher.this, color[0], new SwingColorChanged() {

                    @Override
                    public void colorChanged(Color color_) {
                        color[0] = color_;
                        colorPanel.setBackground(color_);
                        String hex = String.format("%02x%02x%02x%02x", color_.getRed(), color_.getGreen(), color_.getBlue(), color_.getAlpha());
                        onColorChanged.colorChanged(hex);

                    }
                }).setVisible(true);
            }
        });
        jPanel.add(jButton, BorderLayout.LINE_START);


        addToMainPanel(jPanel);
    }

    @Override
    public void addCheckbox(String name, boolean state, final OnCheckboxChanged onCheckboxChanged) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        jPanel.add(new JLabel(name), BorderLayout.NORTH);
        final JCheckBox checkbox1 = new JCheckBox(name);
        checkbox1.setSelected(state);
        checkbox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                onCheckboxChanged.checkboxChanged(checkbox1.isSelected());
            }
        });
        jPanel.add(checkbox1, BorderLayout.CENTER);

        addToMainPanel(jPanel);
    }

}
