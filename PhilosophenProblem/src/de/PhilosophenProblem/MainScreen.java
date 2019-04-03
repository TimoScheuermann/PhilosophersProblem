package de.PhilosophenProblem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private int style = 0;
	private JButton button_next, button_prev, button_start;
	private JLabel currentStyle;
	private JTextField anzahlField;
	
	public MainScreen() {
		super("Philosophen Problem");
		
		setSize(500, 700);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		style = 0;
		
		add(currentStyle = ScreenMethods.getInfoLabel("Functional", getWidth()/2-130, 100, 260, 50));
		add(button_next = ScreenMethods.getButton("→", getWidth()-100, 100, 55, 50));
		add(button_prev = ScreenMethods.getButton("←", 45, 100, 55, 50));
		
		button_next.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				changeCurrentStyle(++style);
			}
		});
		button_prev.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				changeCurrentStyle(--style);
			}
		});
		
		add(button_start = ScreenMethods.getButton("Starte die Simulation", 50, 280, getWidth()-100, 50));
		button_start.setBackground(new Color(231, 76, 60));
		button_start.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				String text = anzahlField.getText();
				if(text.length() == 0) text = "5";
				new RunningScreen(style == 1, Integer.parseInt(text));
				setVisible(false);
				dispose();
			}
		});
		
		add(ScreenMethods.getLabel("Philosophen Problem", 40, 15, getWidth()-80, 40));
		add(ScreenMethods.getInfoLabel("Anzahl der Philosophen", 45, 180, 320, 50));
		add(anzahlField = ScreenMethods.getTextField(400, 180, getWidth()-445, 50));
		add(ScreenMethods.getLabel("Projektarbeit Gruppe 2", 20, 360, getWidth()-50, 40));
		add(ScreenMethods.getLabel("<html>✶ Timo Scheuermann<br>✶ Nina Wolff<br>✶ Andrei Berbescu<br>✶ Julian Homburg</html>", 50, 400, getWidth()-100, 250, 15));
		add(ScreenMethods.getLabel("", 0, 0, 0, 0));
		
		repaint();
	}
	
	private void changeCurrentStyle(int i) {
		style = (style+2) % 2;
		if(style == 0) currentStyle.setText("Funktional");
		else if(style == 1) currentStyle.setText("Oldschool");
		else currentStyle.setText("#" + i);
	}
	
}
