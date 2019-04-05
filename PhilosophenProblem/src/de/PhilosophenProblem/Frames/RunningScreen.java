package de.PhilosophenProblem.Frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;

import de.PhilosophenProblem.PhilosophFunctional;
import de.PhilosophenProblem.PhilosophOldschool;
import de.PhilosophenProblem.ScreenMethods;

public class RunningScreen extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private boolean isFunctional; // Genereller Wahrheitswert zur Unterscheidung der Anzeige
	private int amount; // Anzahl der Philosophen und Gabeln
	
	// Werden hier noch nicht definiert, da nicht klar ist,
	// wie viele, bzw welche Variante gewählt wurde
	private PhilosophFunctional[] func_Phils; 
	private PhilosophOldschool[] old_Phils;
	private Semaphore[] sem;
	
	// Wird später für die Ausgabe der Aktionen der
	// Philosophen genutzt
	private JTextArea info;
	private JLabel background = null;
	
	// Konstanten zur Festlegung der Positionen der Objekte im Frame
	private final int 
		centerX = 250,
		dotSize = 30,
		dotScale = 150,
		center = centerX - dotSize/2;

	// Konstruktor mit Übergabe der Variante, der Anzahl und des Hintergrunds,
	// damit dieser nicht erneut heruntergeladen werden muss.
	public RunningScreen(boolean isFunctional, int amount, JLabel background) {
		// Titel setzen
		super("Gruppe 2: PhilosophenProblem - Running " + (isFunctional ? "functional" : "the old way"));
		
		// Übergebene Variablen setzen.
		this.isFunctional = isFunctional;
		this.amount = amount;
		this.background = background;
		
		// Grundlegende Einstellungen des Fensters
		setSize(500, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		// Falls beim ersten laden des Hintergrunds ein Fehler
		// aufgetreten ist, wird kein Hintergrund gesetzt
		if(background != null) add(background);
		
		Container con = new Container();
		
		con.add(ScreenMethods.getAmountLabel("<html><b>" + amount + "</b> Philosophen</style>", getWidth()/2-125, center-60, 240, 30));
		con.add(ScreenMethods.getLabel("<html><font color=#000000*>&#9899;</font> denkt nach</html>", 20, 350, 150, 25, 15));
		con.add(ScreenMethods.getLabel("<html><font color=#27ae60*>&#9899;</font> isst Nudeln</html>", getWidth()-140, 350, 110, 25, 15));
		con.add(ScreenMethods.getLabel("", 12, 380, getWidth()-40, 1));
		
		// Erstellen der "Konsole"
		// Zunächste Textfeld erstellen und sagen,
		// dass immer ans Ende gescrollt werden soll
		info = ScreenMethods.getTextArea("", 0, 0, getWidth(), getHeight() * 10, 12);
		DefaultCaret caret = (DefaultCaret) info.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// TextFeld zu einer Scrollbaren anzeige hinzufügen,
		// bei dieser Grundeinstellungen festlegen
		// und zum Container hinzufügen.
		JScrollPane scrollPane = new JScrollPane(info);
		scrollPane.setBorder(null);
		scrollPane.setBounds(12, 390, getWidth()-40, getHeight() - 435);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// Custom Scrollbar implementieren, die zum restlichen Design passt
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Color color = null;
				JScrollBar sb = (JScrollBar) c;
				
				// Farben für unterschiedliche Stadien setzen
				if (!sb.isEnabled() || r.width > r.height) return; 
				else if (isDragging | isThumbRollover()) color = new Color(231, 76, 60).darker();
				else color = new Color(231, 76, 60);
				
				// Bar zeichnen
				g2.setPaint(color);
				g2.fillRoundRect(r.x+4, r.y+1, r.width-8, r.height, (r.width-8), (r.width-8));
			}
			// Button überschreiben, damit keine angezeigt werden
			@Override protected JButton createDecreaseButton(int orientation) { JButton button = new JButton(); button.setSize(1, 1); button.setVisible(false); return button; }
			@Override protected JButton createIncreaseButton(int orientation) { return createDecreaseButton(orientation); }
		});
		con.add(scrollPane);
		
		setContentPane(con);

		// Semaphoren deklarieren
		sem = new Semaphore[amount];
		// Je nachdem welche Variante gewählt wurde
		// Die dazu benötigten Philosophen Arrays
		// deklarieren.
		if(isFunctional) initFunc(amount);
		else initOld(amount);
		
		// Neuen Thread starten, der die Tischanzeige
		// updatet, um schneller und leichter zu sehen
		// welcher Philosoph gerade isst und welcher
		// gerad denkt.
		new Thread(() -> {
			
			// Kurze Info für den Anwender rausschicken
			info.append(" ### Gedanken aller Philosophen ###");
			
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
	
	// Wenn Funktionell gewählt wurde,
	// wird diese Methode ausgeführt
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
			// hinzufügen ...
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
			// Philosoph hinzufügen ...
			old_Phils[i] = phil;
			// und Thread starten
			phil.start();
		}
	}
	
	// Methode um unabhängig der gewählten
	// Variante herauszufinden, ob ein
	// Philosoph gerade isst, oder nicht.
	private boolean isEating(int id) {
		if(isFunctional) return func_Phils[id].isEating();
		else return old_Phils[id].isEating();
	}

	// Methode um unabhängig der gewählten
	// Variante herauszufinden, wie viele
	// Suppen ein Philosoph gegessen hat.
	private int getAmountOfSoups(int id) {
		if(isFunctional) return func_Phils[id].getAmountOfSoups();
		else return old_Phils[id].getAmountOfSoups();
	}
	
	// Nachrichten zur "Konsole" hinzufügen
	public void addLog(String message) {
		info.append("\n " + message);
	}
	
	
	// Methode wird bei repaint(); ausgeführt
	@Override
	public void paint(Graphics g) {
		
		// Falls Hintergrund verfügbar, diesen setzen
		if(background != null) add(background);
		
		super.paint(g);
		
		// Zeichnungseinstellung Für langsameres, dafür schärferes zeichnen einstellen
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(hints);
		
		// Wenn sich noch keine Philosophen im Array befinden -> STOP
		if(isFunctional && func_Phils == null) return;
		if(!isFunctional && old_Phils == null) return;
		
		// Philosophen im Kreis anordnen
		for(int i = 0; i < amount; i ++) {
			
			// Mit polaren Koordinaten, sehr bequem und unkompliziert.
			double angle = ((2*Math.PI)/amount)*i + 0.3;
			double x = center + dotScale * Math.cos(angle);
			double y = center + dotScale * Math.sin(angle);
			
			// Wenn Philosoph ist grüner Punkt, ansonsten schwarz
			if(isEating(i)) g.setColor(new Color(39, 174, 96));
			else g2.setColor(Color.BLACK);
			// Punkt zeichnen
			g2.fillOval((int) x, (int) y - 30, dotSize, dotSize);
			
			// In den Kreis die Anzahl der gegessenen Suppen
			// als zusätzliche Information darüber schreiben.
			g2.setFont(ScreenMethods.font.deriveFont(Font.PLAIN, 12));
			g2.setColor(Color.WHITE);
			
			int amount = getAmountOfSoups(i);
			g2.drawString("x" + amount, (int) x + (amount < 10 ? 7 : 4), (int) y - dotSize/3);
			
		}
		
	}

}
