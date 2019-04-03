package de.PhilosophenProblem.Frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.PhilosophenProblem.PhilosophenProblem;
import de.PhilosophenProblem.ScreenMethods;

public class MainScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Variablen, die später spezialisiert werden // vom Nutzer geändert werden können
	private int style = 0; // Art der Problemlösung (Funktional/Oldschool)
	private JButton button_next, button_prev, button_start;
	private JLabel currentStyle;
	private JTextField anzahlField;
	private JLabel background = null;
	
	// Konstruktor des Hauptfensters
	public MainScreen() {
		
		// Title setzen
		super("Gruppe 2: Philosophen Problem - Welcome");
		
		// Grundeinstellungen
		setSize(500, 700);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		style = 0;
		
		// Objekte in Container hinzufügen und später zum Frame
		Container con = new Container();
		
		con.add(currentStyle = ScreenMethods.getInfoLabel("Functional", getWidth()/2-130, 100, 260, 50));
		con.add(button_next = ScreenMethods.getButton("→", getWidth()-100, 100, 55, 50));
		con.add(button_prev = ScreenMethods.getButton("←", 45, 100, 55, 50));
		
		// Aktionen zum wechseln der Herangehensweise
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
		
		// Start-Button
		con.add(button_start = ScreenMethods.getButton("Starte die Simulation", 50, 280, getWidth()-100, 50));
		button_start.setBackground(new Color(231, 76, 60));
		button_start.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				
				// Anzahl-Eingabe Auslesen und erfassen
				String text = anzahlField.getText();
				if(text.length() == 0) text = "5";
				int amount = Integer.parseInt(text);
				
				// Es müssen mindestens 2 Philosophen sein
				if(amount < 2) amount = 2;
				
				// Frame in der Mainklasse setzen für Philosophenausgaben und gleichzeitig öffnen
				PhilosophenProblem.runningScreen = new RunningScreen(style == 0, amount, background);
				
				// Aktuelles Fenster schließen
				setVisible(false);
				dispose();
				
			}
		});
		
		con.add(ScreenMethods.getLabel("Philosophen Problem", 40, 15, getWidth()-80, 40));
		con.add(ScreenMethods.getInfoLabel("Anzahl der Philosophen", 45, 180, 320, 50));
		con.add(anzahlField = ScreenMethods.getTextField(400, 180, getWidth()-445, 50));
		con.add(ScreenMethods.getLabel("Projektarbeit Gruppe 2", 20, 360, getWidth()-50, 40));
		con.add(ScreenMethods.getLabel("<html>✶ Timo Scheuermann, Nina Wolff, Andrei Berbescu & Julian Homburg ✶</html>",
				50, 360, getWidth()-100, 250, 15));
		
		setContentPane(con);
		
		// Fenster aktualisieren
		repaint();
		
	}
	
	// Methode um den Style zu ändern
	private void changeCurrentStyle(int i) {
		style = (style+2) % 2;
		if(style == 0) currentStyle.setText("Funktional");
		else if(style == 1) currentStyle.setText("Oldschool");
		
		// Falls aus irgend einem Grund die Zahl nicht richtig gezählt wird
		else currentStyle.setText("ERROR" + i);
	}
	
	// Methode wird bei repaint(); ausgeführt
	@Override
	public void paint(Graphics g) {
		
		// Wenn Hintergrund noch nicht geladen -> Laden
		if(background == null) {
			try {
				// URL öffnen, hinter der sich der Background verbirgt.
				URL url = new URL("https://i1.wp.com/angularscript.com/wp-content/uploads/2018/06/Progressively-Loading-Images-With-Blur-Effect-min.png?ssl=1");
				// Bild aus URL laden
				BufferedImage img = ImageIO.read(url);
				// Bild an Frame anpassen
				Image imgscaled = img.getScaledInstance(getWidth()*2, getHeight()*2, Image.SCALE_SMOOTH);
				// Neues Label als "Träger" für Bild initialisieren
				background = new JLabel();
				background.setBounds(0, 0, getWidth(), getHeight());
				// Bild auf JLabel setzen & zum frame hinzufügen
				background.setIcon(new ImageIcon(imgscaled));
				add(background);
			} 
			catch (Exception e) {
				// Fehler wird eigentlich nur geworfen, wenn das Bild nicht geladen 
				// werden konnte, oder die URL nicht mehr existiert.
				e.printStackTrace();
				System.out.println("Vermutlich keine Internet Verbindung ^.^");
			}
		}
		
		// Zum laden der Objekte im Frame
		super.paint(g);
	}
	
}
