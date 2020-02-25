package pl.cyfrogen.skijumping.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.desktop.launcher.Resolution;
import pl.cyfrogen.skijumping.desktop.launcher.Resolutions;
import pl.cyfrogen.skijumping.platform.LeaderboardListener;
import pl.cyfrogen.skijumping.platform.PlatformAPI;
import pl.cyfrogen.skijumping.platform.SignInListener;

public class DesktopLauncher {

	public static void main (String[] arg) {
		final List<Resolution> resolutions = Resolutions.resolutions;

		final JFrame frame = new JFrame();
		frame.setTitle("PSJ Launcher");
		frame.setVisible(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JPanel resChooserPanel = new JPanel();
		final JComboBox<Resolution> resChooser = new JComboBox<Resolution>(resolutions.toArray(new Resolution[0]));

		resChooserPanel.add(resChooser);

		JPanel fullscreenPanel = new JPanel();
		final JCheckBox fullscreen = new JCheckBox();
		fullscreen.setText("Fullscreen");
		fullscreenPanel.add(fullscreen);

		JPanel panel2 = new JPanel();
		JButton start = new JButton();
		start.setText("start");
		panel2.add(start);

		mainPanel.add(resChooserPanel);
		mainPanel.add(fullscreenPanel);
		mainPanel.add(panel2);

		frame.add(mainPanel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(400, frame.getHeight());
		frame.setLocationRelativeTo(null);
		frame.revalidate();

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
				config.width = resolutions.get(resChooser.getSelectedIndex()).width;
				config.height = resolutions.get(resChooser.getSelectedIndex()).height;
				config.resizable = false;
				config.title = "Pure Ski Jumping";
				config.fullscreen = fullscreen.isSelected();

				new LwjglApplication(new Main(new PlatformAPI() {
					@Override
					public boolean isSignedIn() {
						return true;
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
				}), config);
			}

		});

	}
}
