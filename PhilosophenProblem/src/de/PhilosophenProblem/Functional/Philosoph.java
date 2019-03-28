package de.PhilosophenProblem.Functional;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class Philosoph {

	// Variablen zum ausführen
	private Semaphore linkeGabel, rechteGabel;
	private static Random rand = new Random();
		
	// Variablen für menschliches Verständnis
	private static int philosophen = 0;
	public int id;
	private int nudelnVerzehrt = 0;
	
	
	// Einfache formatierte Textausgabe um den aktuellen Status
	// des Philosophen auszugeben, inkl. Zeitverzögerung.
	/**BinaryOperator<String, Double> log = (String message, double multiplier) -> {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.printf("[%s] [gegessen x%s] Philosoph #%s - %s\n", format.format(new Date(System.currentTimeMillis())), nudelnVerzehrt, id, message);
		
		try {
			Thread.sleep((long) (multiplier * (rand.nextInt(2000)+1000)));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	};**/
	
	public Philosoph id(int id) {
		this.id = id;
		return this;
	}
	
	public Philosoph linkeGabel(Semaphore gabel) {
		this.linkeGabel = gabel;
		return this;
	}
	
	public Philosoph rechteGabel(Semaphore gabel) {
		this.rechteGabel = gabel;
		return this;
	}
	
	public static void start(Consumer<Philosoph> consumer) {
		System.out.println("Added");

	}
	
	/**
		new Thread(() -> {
			
			consumer.accept(phil ->  {
				
			});
			
			System.out.println("Starte # " + consumer.andThen(after -> after.id));
			
			/**
			while(true) {
				
				/**
				// Informationsausgabe
				log("Wurde erweckt", 2.0d);
					
				// Endlosschleife, Abbruchbedingung könnte gegessene Portionen > x sein.
				while(true) {
						
					log("Denkt nach...", 1.5d);
					log("Versucht zu essen", 0d);
						
					// Versuche linke Gabel aufzunehmen, wenn nicht möglich, warte solange
					// bis der linke Sitznachbar sie zurücklegt. 
					try {
							
						linkeGabel.acquire();
							
						// Linke Gabel ist nun im eigenen Besitz -> Versuche rechte Gabel
						// Aufzunehmen
						// Wenn nicht möglich -> Information ausgeben
						//					  -> Sprung zu finally
						//					  -> Nach kurzer Nachdenkepause neuer Versuch
						if(!rechteGabel.tryAcquire()) {
							log("Konnte nicht essen...", 0.5d);
							continue;
						}
					
						// Genießt Nudeln, Anzahl erhöhen
						log("Genießt seine Nudeln", 2.0d);
						nudelnVerzehrt ++;
							
						// Zunächst rechte Gabel zurück geben
						rechteGabel.release();
							
					} 
					catch (InterruptedException e) {
						// .acquire() kann Fehler schmeißen -->
						log("Fehler bei der Gabelaufnahme", 0.5d);
					} 
					finally {
						// Wird immer & muss immer ausgeführt (werden)
						linkeGabel.release();
					}
				}
			}
		
			
		}).start();
	}
	**/
	
}
