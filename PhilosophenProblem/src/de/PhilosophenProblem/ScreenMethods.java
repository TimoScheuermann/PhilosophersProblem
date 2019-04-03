package de.PhilosophenProblem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ScreenMethods {
	
	static Font font = new Font("SF Pro Text", Font.PLAIN, 20);
	static Color color = new Color(44, 62, 80);
	
	public static JLabel getLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setForeground(color);
		label.setFont(font.deriveFont(Font.BOLD, 30.0f));
		label.setBounds(x, y, width, height);
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		return label;
	}
	
	public static JLabel getInfoLabel(String text, int x, int y, int width, int height) {
		JLabel label = getLabel(text, x, y, width, height);
		label.setFont(font.deriveFont(Font.PLAIN, 20.0f));
		label.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		return label;
	}
	
	public static JLabel getLabel(String text, int x, int y, int width, int height, int size) {
		JLabel label = new JLabel(text, SwingConstants.LEFT);
		label.setForeground(color);
		label.setFont(font.deriveFont(Font.PLAIN, size));
		label.setBounds(x, y, width, height);
		label.setBackground(Color.BLUE);
		return label;
	}
	
	public static JTextField getTextField(int x, int y, int width, int height) {
		JTextField field = new JTextField(1);
		field.setHorizontalAlignment(0);
		field.setFont(font.deriveFont(Font.PLAIN, 20.0f));
		field.setForeground(Color.BLACK);
		field.setOpaque(false);
		field.setBounds(x, y, width, height);
		field.setFont(font);
		field.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		field.addKeyListener(new KeyListener() {
			@Override 
			public void keyTyped(KeyEvent e) { 
				if(((JTextField) e.getSource()).getText().length() == 3) {
					e.consume();
					return;
				}
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}
		});
		return field;
	}
	
	public static JButton getButton(String text, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setFont(font.deriveFont(Font.BOLD));
		button.setForeground(Color.WHITE);
		button.setBackground(Color.DARK_GRAY);
		button.setIgnoreRepaint(true);
		
		button.addMouseListener(new MouseAdapter() {
			
			@Override public void mouseExited(MouseEvent e) {
				button.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0), 0, true));
			}
			
			@Override public void mouseEntered(MouseEvent e) {
				button.setBorder(BorderFactory.createLineBorder(color, 2, true));
			}
			
		});
		
		button.setBounds(x, y, width, height);
		button.setFocusable(false);
		return button;
	}

}
