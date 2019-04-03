package de.PhilosophenProblem.OldStyle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosoph extends Thread {

	// Variablen zum ausf�hren
	private Semaphore linkeGabel, rechteGabel;
	private static Random rand = new Random();
	
	// Variablen f�r menschliches Verst�ndnis
	private static int philosophen = 0;
	private int id, nudelnVerzehrt = 0;
	
	// Construktor
	public Philosoph(Semaphore linkeGabel, Semaphore rechteGabel) {
		this.linkeGabel = linkeGabel;
		this.rechteGabel = rechteGabel;
		this.id = ++philosophen;
	}
	
	// Einfache formatierte Textausgabe um den aktuellen Status
	// des Philosophen auszugeben, inkl. Zeitverzoegerung.
	private void log(String message, double multiplier) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.printf("[%s] [gegessen x%s] Philosoph #%s - %s\n", format.format(new Date(System.currentTimeMillis())), nudelnVerzehrt, id, message);
		
		try {
			Thread.sleep((long) (multiplier * (rand.nextInt(2000)+1000)));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Philosophen "Gehirn"
	@Override
	public void run() {
		
		// Informationsausgabe
		log("Wurde erweckt", 2.0d);
		
		// Endlosschleife, Abbruchbedingung k�nnte gegessene Portionen > x sein.
		while(true) {
			
			log("Denkt nach...", 1.5d);
			log("Versucht zu essen", 0d);
			
			// Versuche linke Gabel aufzunehmen, wenn nicht m�glich, warte solange
			// bis der linke Sitznachbar sie zur�cklegt. 
			try {
				
				linkeGabel.acquire();
				
				// Linke Gabel ist nun im eigenen Besitz -> Versuche rechte Gabel
				// Aufzunehmen
				// Wenn nicht m�glich -> Information ausgeben
				//					  -> Sprung zu finally
				//					  -> Nach kurzer Nachdenkepause neuer Versuch
				if(!rechteGabel.tryAcquire()) {
					log("Konnte nicht essen...", 0.5d);
					continue;
				}
				
				// Genie�t Nudeln, Anzahl erh�hen
				log("Genie�t seine Nudeln", 2.0d);
				nudelnVerzehrt ++;
				
				// Zun�chst rechte Gabel zur�ck geben
				rechteGabel.release();
				
			} 
			catch (InterruptedException e) {
				// .acquire() kann Fehler schmei�en -->
				log("Fehler bei der Gabelaufnahme", 0.5d);
			} 
			finally {
				// Wird immer & muss immer ausgef�hrt (werden)
				linkeGabel.release();
			}
			
		}
		
	}
	
}
