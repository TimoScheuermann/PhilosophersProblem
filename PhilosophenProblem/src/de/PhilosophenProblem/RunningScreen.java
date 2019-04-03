package de.PhilosophenProblem;

import javax.swing.JFrame;

public class RunningScreen extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public RunningScreen(boolean isFunctional, int amount) {
		super("PhilosophenProblem - Running " + isFunctional);
		setSize(500, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

}
