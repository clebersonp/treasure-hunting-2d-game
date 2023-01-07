package main;

import javax.swing.JFrame;

public class Main {

	public static JFrame window;

	public static void main(String[] args) {

		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(Boolean.FALSE);
		window.setTitle("2D Adventure");

		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		// CONFIG
		gamePanel.getConfig().load();
		if (gamePanel.isFullScreenOn()) {
			window.setUndecorated(Boolean.TRUE);
		}

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(Boolean.TRUE);

		gamePanel.setupGame();
		gamePanel.startGameThread();
	}

}
