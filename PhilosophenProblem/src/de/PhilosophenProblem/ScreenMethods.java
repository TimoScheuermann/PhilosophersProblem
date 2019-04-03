package de.PhilosophenProblem;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 * 
 * Diese Klasse beinhaltet im wesentlichen, alle Objekte, die
 * für die Anzeige benötigt werden, für ein einheitliches Design
 * des UI's in Methoden zusammengefasst.
 * 
 */
public class ScreenMethods {
	
	// Standard Font & Farbe
	public static Font font = new Font("", Font.PLAIN, 20);
	static Color color = new Color(44, 62, 80);
	
	// Label für große Überschriften
	public static JLabel getLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setForeground(color);
		label.setFont(font.deriveFont(Font.BOLD, 30.0f));
		label.setBounds(x, y, width, height);
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		return label;
	}
	
	// Label für kleine Überschriften
	public static JLabel getInfoLabel(String text, int x, int y, int width, int height) {
		JLabel label = getLabel(text, x, y, width, height);
		label.setFont(font.deriveFont(Font.PLAIN, 20.0f));
		label.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		return label;
	}
	
	// Label für HTML Anzeigen und variabler Schriftgröße
	public static JLabel getLabel(String text, int x, int y, int width, int height, int size) {
		JLabel label = new JLabel(text, SwingConstants.LEFT);
		label.setForeground(color);
		label.setFont(font.deriveFont(Font.PLAIN, size));
		label.setBounds(x, y, width, height);
		label.setBackground(Color.BLUE);
		return label;
	}
	
	// Custom Label für die Anzeige in der Mitte des "Tisches"
	public static JLabel getAmountLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setForeground(color);
		label.setFont(font.deriveFont(Font.BOLD, 30.0f));
		label.setBounds(x, y, width, height);
		label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, color));
		label.setFont(font.deriveFont(Font.PLAIN, 24));
		return label;
	}
	
	// Wird ausschließlich für die Informationen der Philosophen genutzt
	public static JTextArea getTextArea(String text, int x, int y, int width, int height, int size) {
		JTextArea area = new JTextArea();
		area.setBounds(x, y, width, height);
		area.setEditable(false);
		area.setText(text);
		area.setBackground(null);
		area.setFont(font.deriveFont(Font.PLAIN, size));
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		return area;
	}
	
	// Für die Eingabe der Anzahl der Philosophen
	public static JTextField getTextField(int x, int y, int width, int height) {
		JTextField field = new JTextField(1);
		field.setHorizontalAlignment(0);
		field.setFont(font.deriveFont(Font.PLAIN, 20.0f));
		field.setForeground(color);
		field.setOpaque(false);
		field.setBounds(x, y, width, height);
		field.setFont(font);
		field.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)));
		field.addKeyListener(new KeyListener() {
			@Override 
			public void keyTyped(KeyEvent e) {
				
				/*
				 * Überprüfung, dass auch wirklich nur zahlen eingegeben werden < 1000
				 */
				
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
	
	// Button für Start und Varianten Wahl der Problemlösung
	public static JButton getButton(String text, int x, int y, int width, int height, Color color) {
		JButton button = new JButton(text);
		button.setFont(font.deriveFont(Font.BOLD));
		button.setForeground(Color.WHITE);
		button.setBackground(color);
		button.setBorder(BorderFactory.createLineBorder(color, 0, true));
		button.setIgnoreRepaint(true);
		
		button.addMouseListener(new MouseAdapter() {
			
			Color c = button.getBackground();
			
			// "Animation" damit User sieht, ob ausgewählt oder nicht
			@Override public void mouseExited(MouseEvent e) {
				button.setBackground(c);
				button.setBorder(BorderFactory.createLineBorder(c, 0, true));
			}
			
			@Override public void mouseEntered(MouseEvent e) {
				button.setBorder(BorderFactory.createLineBorder(c.brighter().brighter(), 3, true));
				button.setBackground(c.brighter());
			}
			
		});
		
		button.setBounds(x, y, width, height);
		button.setFocusable(false);
		return button;
	}
	
	// Spezielle Buttons für den URL Aufruf
	public static JButton getURLButton(String text, String url, int x, int y, int width, int height, int size) {
		JButton button = new JButton(text);
		button.setBounds(x, y, width, height);
		button.setFont(font.deriveFont(Font.PLAIN, size));
		button.setFocusable(false);
		button.setForeground(color);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 50)));
		
		button.addMouseListener(new MouseListener() {
			
			String text_orig = text;
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				// URL öffnen, wenn Gerät supported über Desktop.class
				if(Desktop.isDesktopSupported()){
		            Desktop desktop = Desktop.getDesktop();
		            try { desktop.browse(new URI(url)); }
		            catch (IOException | URISyntaxException ex) {
		                ex.printStackTrace();
		            }
		        }
				// Ansonsten, Konsolen-Befehl
				else {
		            Runtime runtime = Runtime.getRuntime();
		            try { runtime.exec("xdg-open " + url); }
		            catch (IOException ex) {
		                ex.printStackTrace();
		            }
		        }
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				button.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 50)));
				button.setText(text_orig);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color));
				button.setText("\u25B6 " + text + " \u25C0");
			}
			
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			
		});
		
		return button;
	}

}
