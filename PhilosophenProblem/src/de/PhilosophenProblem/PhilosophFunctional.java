package de.PhilosophenProblem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

public class PhilosophFunctional {

	// Variablen zum ausführen
	private Semaphore linkeGabel, rechteGabel;
	private static Random rand = new Random();
	private boolean isEating;
	
	// Variablen für menschliches Verständnis
	private int id;
	private int nudelnVerzehrt = 0;
	
	// Setze ID
	public PhilosophFunctional id(int i) {
		this.id = i;
		return this;
	};

	// Setze linke Gabel
	public PhilosophFunctional linkeGabel(Semaphore s) {
		this.linkeGabel = s;
		return this;
	}
	
	// Setze rechte Gabel
	public PhilosophFunctional rechteGabel(Semaphore s) {
		this.rechteGabel = s;
		return this;
	}
	
	// Gibt zurück ob der Philosoph gerade isst
	public boolean isEating() {
		return isEating;
	}

	// Gibt zurück wie viele Suppen der Philosoph gegessen hat
	public int getAmountOfSoups() {
		return nudelnVerzehrt;
	}
	
	// Einfache formatierte Textausgabe, um den aktuellen Status
	// des Philosophen auszugeben, inkl. Zeitverzögerung.	
	BiConsumer<String, Double> log = (m, d) -> {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		PhilosophenProblem.runningScreen.addLog(
			String.format("[%s] [gegessen x%s] Philosoph #%s - %s", 
			format.format(new Date(System.currentTimeMillis())), nudelnVerzehrt, id, m)
		);
		
		try {
			Thread.sleep((long) (d * (rand.nextInt(2000)+1000)));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	};
	
	public void aufwecken() {
		
		new Thread(() -> {
			
				// Informationsausgabe
				log.accept("Wurde erweckt", 2.0d);
					
				// Endlosschleife, Abbruchbedingung könnte gegessene Portionen > x sein.
				while(true) {
						
					log.accept("Denkt nach...", 1.5d);
					log.accept("Versucht zu essen", 0d);
						
					// Versuche linke Gabel aufzunehmen, wenn nicht möglich, warte solange
					// bis der linke Sitznachbar sie zurücklegt. 
					try {
							
						linkeGabel.acquire();
							
						// Linke Gabel ist nun im eigenen Besitz -> Versuche rechte Gabel
						// Aufzunehmen
						// Wenn nicht möglich 
						// -> Information ausgeben
						// -> Sprung zu finally
						// -> Nach kurzer Nachdenkepause neuer Versuch
						if(!rechteGabel.tryAcquire()) {
							log.accept("Konnte nicht essen...", 0.5d);
							continue;
						}
					
						// Er isst, genießt Nudeln, Anzahl erhöhen
						isEating = true;
						log.accept("Genießt seine Nudeln", 2.0d);
						nudelnVerzehrt ++;
						
						// Zunächst rechte Gabel zurück geben
						rechteGabel.release();
							
					} 
					catch (InterruptedException e) {
						// .acquire() kann Fehler schmeißen -->
						log.accept("Fehler bei der Gabelaufnahme", 0.5d);
					} 
					finally {
						// Wird immer & muss immer ausgeführt (werden)
						// "Legt Gabel zurück" und isst nicht mehr
						linkeGabel.release();
						isEating = false;
					}
			}
				
		}).start();
	}
	
}
