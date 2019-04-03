package de.PhilosophenProblem.Frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import de.PhilosophenProblem.PhilosophFunctional;
import de.PhilosophenProblem.PhilosophOldschool;
import de.PhilosophenProblem.ScreenMethods;

public class RunningScreen extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private boolean isFunctional; // Genereller Wahrheitswert zur Unterscheidung der Anzeige
	private int amount; // Anzahl der Philosophen und Gabeln
	
	// Werden hier noch nicht definiert, da nicht klar ist,
	// wie viele, bzw welche Variante gew�hlt wurde
	private PhilosophFunctional[] func_Phils; 
	private PhilosophOldschool[] old_Phils;
	private Semaphore[] sem;
	
	// Wird sp�ter f�r die Ausgabe der Aktionen der
	// Philosophen genutzt
	private JTextArea info;
	private JLabel background = null;
	
	// Konstanten zur Festlegung der Positionen der Objekte im Frame
	private final int 
		centerX = 250, 
		dotSize = 30, 
		dotScale = 150, 
		center = centerX - dotSize/2;

	// Konstruktor mit �bergabe der Variante, der Anzahl und des Hintergrunds,
	// damit dieser nicht erneut heruntergeladen werden muss.
	public RunningScreen(boolean isFunctional, int amount, JLabel background) {
		// Titel setzen
		super("Gruppe 2: PhilosophenProblem - Running " + (isFunctional ? "functional" : "the old way"));
		
		// �bergebene Variablen setzen.
		this.isFunctional = isFunctional;
		this.amount = amount;
		this.background = background;
		
		// Grundlegende Einstellungen des Fensters
		setSize(500, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// Falls beim ersten laden des Hintergrunds ein Fehler
		// aufgetreten ist, wird kein Hintergrund gesetzt
		if(background != null) add(background);
		
		Container con = new Container();
		
		con.add(ScreenMethods.getAmountLabel("<html><b>" + amount + "</b> Philosophen</style>", getWidth()/2-125, center-60, 240, 30));
		con.add(ScreenMethods.getLabel("<html><font color=#000000*>&#9899;</font> denkt nach</html>", 20, 350, 150, 25, 15));
		con.add(ScreenMethods.getLabel("<html><font color=#27ae60*>&#9899;</font> isst Suppe</html>", getWidth()-135, 350, 110, 25, 15));
		con.add(ScreenMethods.getLabel("", 12, 380, getWidth()-40, 1));
		
		// Erstellen der "Konsole"
		// Zun�chste Textfeld erstellen und sagen,
		// dass immer ans Ende gescrollt werden soll
		info = ScreenMethods.getTextArea("", 0, 0, getWidth(), getHeight() * 10, 12);
		DefaultCaret caret = (DefaultCaret) info.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// TextFeld zu einer Scrollbaren anzeige hinzuf�gen,
		// bei dieser Grundeinstellungen festlegen
		// und zum Container hinzuf�gen.
		JScrollPane scrollPane = new JScrollPane(info);
		scrollPane.setBounds(12, 390, getWidth()-40, getHeight() - 435);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		con.add(scrollPane);
		
		setContentPane(con);

		// Semaphoren deklarieren
		sem = new Semaphore[amount];
		// Je nachdem welche Variante gew�hlt wurde
		// Die dazu ben�tigten Philosophen Arrays
		// deklarieren.
		if(isFunctional) initFunc(amount);
		else initOld(amount);
		
		// Neuen Thread starten, der die Tischanzeige
		// updatet, um schneller und leichter zu sehen
		// welcher Philosoph gerade isst und welcher
		// gerad denkt.
		new Thread(() -> {
			
			// Kurze Info f�r den Anwender rausschicken
			info.append("### Gedanken aller Philosophen ###");
			
			while(true) {
				try {
					// Zwischendurch mal kurz warten mit dem Updaten
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Fenster updaten
				repaint();
			}
		}).start();
		
	}
	
	// Wenn Funktionell gew�hlt wurde,
	// wird diese Methode ausgef�hrt
	private void initFunc(int amount) {
		
		// Semaphoren initlialisieren
		IntStream.range(0, amount)
		.forEach(x -> {
			sem[x] = new Semaphore(1, true);
		});
		
		// Philosophen initialisieren und "aufwecken"
		func_Phils = new PhilosophFunctional[amount];
		IntStream.range(0, amount)
		.forEach(x -> {
			// Philosoph erstellen, ...
			PhilosophFunctional phil = new PhilosophFunctional()
				.id(x)
				.linkeGabel(sem[x])
				.rechteGabel(sem[(x+1) % amount]);
			// hinzuf�gen ...
			func_Phils[x] = phil;
			// und starten.
			phil.aufwecken();
		});
	}
	
	private void initOld(int amount) {
		// Semaphoren initialisieren
		for(int i = 0; i < amount; i ++) {
			sem[i] = new Semaphore(1, true);
		}
			
		// Philosophen initialisieren und starten
		old_Phils = new PhilosophOldschool[amount];
		for(int i = 0; i < amount; i ++) {
			// Jeweils linke und rechte Gabel zuweisen (vereinfacht geschrieben)
			PhilosophOldschool phil = new PhilosophOldschool(sem[i], sem[(i+1) % amount]);
			// Philosoph hinzuf�gen ...
			old_Phils[i] = phil;
			// und Thread starten
			phil.start();
		}
	}
	
	// Methode um unabh�ngig der gew�hlten
	// Variante herauszufinden, ob ein
	// Philosoph gerade isst, oder nicht.
	private boolean isEating(int id) {
		if(isFunctional) return func_Phils[id].isEating();
		else return old_Phils[id].isEating();
	}

	// Methode um unabh�ngig der gew�hlten
	// Variante herauszufinden, wie viele
	// Suppen ein Philosoph gegessen hat.
	private int getAmountOfSoups(int id) {
		if(isFunctional) return func_Phils[id].getAmountOfSoups();
		else return old_Phils[id].getAmountOfSoups();
	}
	
	// Nachrichten zur "Konsole" hinzuf�gen
	public void addLog(String message) {
		info.append("\n" + message);
	}
	
	
	// Methode wird bei repaint(); ausgef�hrt
	@Override
	public void paint(Graphics g) {
		
		// Falls Hintergrund verf�gbar, diesen setzen
		if(background != null) add(background);
		
		super.paint(g);

		// Wenn sich noch keine Philosophen im Array befinden -> STOP
		if(isFunctional && func_Phils == null) return;
		if(!isFunctional && old_Phils == null) return;
		
		// Philosophen im Kreis anordnen
		for(int i = 0; i < amount; i ++) {
			
			// Mit polaren Koordinaten, sehr bequem und unkompliziert.
			double angle = 2*Math.PI/amount*i + Math.PI/7;
			double x = center + dotScale * Math.cos(angle);
			double y = center + dotScale * Math.sin(angle);
			
			// Wenn Philosoph ist gr�ner Punkt, ansonsten schwarz
			if(isEating(i)) g.setColor(new Color(39, 174, 96));
			else g.setColor(Color.BLACK);
			// Punkt zeichnen
			g.fillOval((int) x, (int) y - 30, dotSize, dotSize);
			
			// In den Kreis die Anzahl der gegessenen Suppen
			// als zus�tzliche Information dar�ber schreiben.
			g.setFont(ScreenMethods.font.deriveFont(Font.PLAIN, 12));
			g.setColor(Color.WHITE);
			g.drawString("x" + getAmountOfSoups(i), (int) x + 5, (int) y - dotSize/3);
			
		}
		
	}

}
