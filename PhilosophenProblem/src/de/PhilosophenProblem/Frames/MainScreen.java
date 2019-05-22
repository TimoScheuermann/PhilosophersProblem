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
	
	// Variablen, die später spezialisiert werden // vom Nutzer geändert werden kÃ¶nnen
	private int style = 0; // Art der ProblemlÃ¶sung (Funktional/Oldschool)
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
		setResizable(false);
		ScreenMethods.setIconImage(this);
		style = 0;
		
		// Objekte in Container hinzufügen und später zum Frame
		Container con = new Container();
		
		con.add(currentStyle = ScreenMethods.getInfoLabel("Functional", getWidth()/2-130, 100, 260, 50));
		con.add(button_next = ScreenMethods.getButton("\u2192", getWidth()-100, 100, 55, 50, new Color(52, 73, 94)));
		con.add(button_prev = ScreenMethods.getButton("\u2190", 45, 100, 55, 50, new Color(52, 73, 94)));
		
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
		con.add(button_start = ScreenMethods.getButton("Starte die Simulation", 50, 280, getWidth()-100, 50, new Color(231, 76, 60)));
		button_start.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				
				// Anzahl-Eingabe Auslesen und erfassen
				String text = anzahlField.getText();
				if(text.length() == 0) text = "5";
				int amount = Integer.parseInt(text);
				
				// Es müssen mindestens 2 Philosophen sein
				if(amount < 2) amount = 2;
				
				// Frame in der Mainklasse setzen für Philosophenausgaben und gleichzeitig Ã¶ffnen
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
		con.add(ScreenMethods.getLabel("<html><u>Gruppenteilnehmer:</u> Timo Scheuermann, Nina Wolff, Andrei Berbescu & Julian Homburg | (WWI18SEC)<br><br>"
				+ "<i>\"Es sitzen fünf Philosophen an einem runden Tisch, und jeder hat einen Teller mit Spaghetti vor sich. Zum Essen von Spaghetti bentigt jeder Philosoph zwei Gabeln. Allerdings waren im Restaurant nur fünf Gabeln vorhanden, die nun zwischen den Tellern liegen. Die Philosophen knnen also nicht gleichzeitig speisen.\"</i>"
				+ "<br>~ codeplanet.eu/tutorials/java/69-speisende-philosophen"
				+ "</html>",
				25, 390, getWidth()-50, 250, 15));
		
		con.add(ScreenMethods.getURLButton("GitHub-Repo", "https://github.com/TimoScheuermann/PhilosophersProblem/", 50, getHeight() - 60, 130, 20, 12));
		con.add(ScreenMethods.getURLButton("Dokumentation", "https://timoscheuermann.github.io/PhilosophersProblem/", getWidth()-190, getHeight() - 60, 130, 20, 12));
		
		setContentPane(con);
		
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
			String u = "https://timoscheuermann.github.io/PhilosophersProblem/img/background-java.png";
			try {
				// URL Ã¶ffnen, hinter der sich der Background verbirgt.
				URL url = new URL(u);
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
				System.err.println("Hintergrundbild konnte nicht geladen werden, da keine Verbindung zum Internet besteht oder die URL nicht mehr existiert:");
				System.err.println(u);
			}
		}
		
		// Zum laden der Objekte im Frame
		super.paint(g);
	}
	
}
