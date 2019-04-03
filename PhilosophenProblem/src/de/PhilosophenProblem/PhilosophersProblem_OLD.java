package de.PhilosophenProblem;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import de.PhilosophenProblem.Functional.Philosoph;

/**
 * 
 * @author Timo Scheuermann, Andrei Berbescu, Nina Wolff, Julian Homburg
 * @version 1.0.0
 * @since 27.03.2019
 *
 */
public class PhilosophersProblem_OLD {

	// Main method
	public static void maind(String[] args) {
		
		MainScreen screen = new MainScreen();
		
		if(true) return;
		
		
		// Schnelle Einstellung, welche Variante durchgespielt werden soll
		boolean runOLD = false;
		boolean runFunctional = true;
		
		//Festlegung der Anzahl an Philosophen & Gabeln
		int amount = 5;
		
		// Gabeln (in dem Fall) Semaphoren initialisierien
		Semaphore[] sem = new Semaphore[amount];
		
		// Philosophenarray f�r Anzeige erstellen
		Philosoph[] phils = new Philosoph[amount];
					
		// Block, f�r alte Art Java Quellcode zu schreiben
		if(runOLD) {
			
			// Semaphoren initialisieren
			for(int i = 0; i < amount; i ++) {
				sem[i] = new Semaphore(1, true);
			}
			
			// Philosophen initialisieren und starten
			for(int i = 0; i < amount; i ++) {
				// Jeweils linke und rechte Gabel zuweisen (vereinfacht geschrieben)
				new de.PhilosophenProblem.OldStyle.Philosoph(sem[i], sem[(i+1) % amount]).start();
			}
			
		}
		
		
		if(runFunctional) {
			
			// Semaphoren initlialisieren
			IntStream.range(0, amount)
			.forEach(x -> {
				sem[x] = new Semaphore(1, true);
			});
			
			// Philosophen erstellen und "aufwecken"
			IntStream.range(0, amount)
			.forEach(x -> {
				new Philosoph()
					.id(x)
					.linkeGabel(sem[x])
					.rechteGabel(sem[(x+1) % amount])
					.aufwecken();
				}
			);
			
		}
		
	}
	
}