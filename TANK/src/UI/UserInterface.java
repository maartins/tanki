package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GameStates.GameStateManager;
import GameStates.GameStates;
import GameStates.IGameStateObserver;
import Main.MainPanel;
import Main.Settings;

public class UserInterface implements IGameStateObserver {

	private JButton startButton = new JButton("Play");
	private JButton multiButton = new JButton("Top 10");
	private JLabel healthLable1 = new JLabel("Tank HP: ");
	private JLabel healthLable2 = new JLabel("Bird HP: ");
	private JLabel scoreLable = new JLabel("Score: ");
	private JLabel totalScoreLable = new JLabel();
	private JTextField nameTextField = new JTextField("Tank name");
	private JLabel titleLable = new JLabel();
	private JPanel scorePanel = new JPanel();

	private JPanel parent;
	private GameStateManager gsm;

	public UserInterface(JPanel panel) {
		parent = panel;
		guiSetUp();
	}

	public UserInterface(JPanel panel, GameStateManager gsm) {
		parent = panel;
		this.gsm = gsm;
		gsm.addObserver(this);
		guiSetUp();
	}

	public void guiSetUp() {
		startButton.setBounds(140, 200, 200, 60);
		startButton.setFont(new Font("Arial", Font.BOLD, 16));
		startButton.setBackground(new Color(182, 149, 67));
		startButton.setForeground(Color.WHITE);
		startButton.setBorderPainted(false);
		startButton.setFocusPainted(false);
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!nameTextField.getText().isEmpty() || nameTextField.getText().equals(" ")) {
					MainPanel.tank.setName(nameTextField.getText());
					nameTextField.setText("Tank name");
					gsm.setState(GameStates.MainGame);
				} else {
					nameTextField.setText("Tank name");
				}
			}
		});
		parent.add(startButton);

		nameTextField.setBounds(140, 290, 200, 30);
		nameTextField.setFont(new Font("Arial", Font.BOLD, 16));
		nameTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		nameTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.doClick();
			}
		});
		parent.add(nameTextField);

		scorePanel.setBounds(140, 200, 200, 170);
		scorePanel.setLayout(null);
		parent.add(scorePanel);

		multiButton.setBounds(140, 380, 200, 25);
		multiButton.setFont(new Font("Arial", Font.BOLD, 16));
		multiButton.setBackground(new Color(182, 149, 67));
		multiButton.setForeground(Color.WHITE);
		multiButton.setBorderPainted(false);
		multiButton.setFocusPainted(false);
		multiButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (gsm.getState() == GameStates.MainMenu) {
					if (multiButton.getText() == "Top 10") {
						startButton.setVisible(false);
						nameTextField.setVisible(false);
						multiButton.setText("Back");
						// ArrayList<String> topTen = database.getTopScore();

						int count = 0;

						// for (String s : topTen) {
						// JLabel item = new JLabel(s);
						// item.setBounds(0, count * 15, 200, 30);
						// scorePanel.add(item);
						// count++;
						// }

						scorePanel.setVisible(true);
					} else {
						scorePanel.removeAll();
						scorePanel.setVisible(false);
						startButton.setVisible(true);
						nameTextField.setVisible(true);
						multiButton.setText("Top 10");
					}
				} else if (gsm.getState() == GameStates.LevelFinished) {
					gsm.setState(GameStates.MainGame);
					multiButton.setText("Top 10");
				} else {
					gsm.setState(GameStates.MainMenu);
					multiButton.setText("Top 10");
				}
			}
		});
		parent.add(multiButton);

		healthLable1.setBounds(5, 485, 200, 30);
		healthLable1.setFont(new Font("Arial", Font.BOLD, 16));
		healthLable1.setForeground(new Color(255, 255, 255));
		parent.add(healthLable1);

		healthLable2.setBounds(10, 510, 200, 30);
		healthLable2.setFont(new Font("Arial", Font.BOLD, 16));
		healthLable2.setForeground(new Color(255, 255, 255));
		parent.add(healthLable2);

		scoreLable.setBounds(340, 510, 200, 30);
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		scoreLable.setForeground(new Color(255, 255, 255));
		parent.add(scoreLable);

		totalScoreLable.setBounds(140, 200, 200, 30);
		totalScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		totalScoreLable.setForeground(new Color(255, 255, 255));
		parent.add(totalScoreLable);

		titleLable.setBounds(0, 0, Settings.width.value(), Settings.height.value());
		titleLable.setIcon(new ImageIcon("Images//tank_title.png"));
		parent.add(titleLable);
	}

	@Override
	public void doAction(GameStates state) {
		switch (state) {
		case MainMenu:
			titleLable.setVisible(true);
			startButton.setVisible(true);
			nameTextField.setVisible(true);
			scoreLable.setVisible(false);
			healthLable1.setVisible(false);
			healthLable2.setVisible(false);
			totalScoreLable.setVisible(false);
			scorePanel.setVisible(false);
			break;
		case MainGame:
			titleLable.setVisible(false);
			startButton.setVisible(false);
			nameTextField.setVisible(false);
			scoreLable.setVisible(true);
			healthLable1.setVisible(true);
			healthLable2.setVisible(true);
			totalScoreLable.setVisible(false);
			scorePanel.setVisible(false);
			multiButton.setVisible(false);
			break;
		case LevelFinished:
			titleLable.setVisible(true);
			scoreLable.setVisible(false);
			healthLable1.setVisible(false);
			healthLable2.setVisible(false);
			totalScoreLable.setVisible(true);
			multiButton.setVisible(true);
			multiButton.setText("Continue");
			totalScoreLable.setText("Score " + String.format("%08d", MainPanel.tank.getScore()));
			break;
		case EndScreen:
			titleLable.setVisible(true);
			scoreLable.setVisible(false);
			healthLable1.setVisible(false);
			healthLable2.setVisible(false);
			totalScoreLable.setVisible(true);
			multiButton.setVisible(true);
			multiButton.setText("End game");
			totalScoreLable.setText("Score " + String.format("%08d", MainPanel.tank.getScore()));
			break;
		default:
			break;
		}
	}
}
