package de.PhilosophenProblem;

import java.util.concurrent.Semaphore;

import de.PhilosophenProblem.OldStyle.Philosoph;

/**
 * 
 * @author Timo Scheuermann, Andrei Berbescu, Nina Wolff, Julian Homburg
 * @version 1.0.0
 * @since 27.03.2019
 *
 */
public class PhilosophersProblem {

	public static void main(String[] args) throws InterruptedException {
		
		
		// Schnelle Einstellung, welche Variante durchgespielt werden soll
		boolean runOLD = true;
		//boolean runFunctional = false;
		
		// Block, für alte Art Java Quellcode zu schreiben
		if(runOLD) {
			
			//Festlegung der Anzahl an Philosophen & Gabeln
			int amount = 5;
			
			// Gabeln (in dem Fall) Semaphoren initialisierien
			Semaphore[] sem = new Semaphore[amount];
			for(int i = 0; i < amount; i ++) {
				sem[i] = new Semaphore(1, true);
			}
			
			// Philosophen initialisieren und starten
			for(int i = 0; i < amount; i ++) {
				// Jeweils linke und rechte Gabel zu weisen (vereinfacht geschrieben)
				new Philosoph(sem[i], sem[(i+1) % amount]).start();
			}
			
		}
		
	}
	
}
