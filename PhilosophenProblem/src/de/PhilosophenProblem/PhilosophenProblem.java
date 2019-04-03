package de.PhilosophenProblem;

import de.PhilosophenProblem.Frames.MainScreen;
import de.PhilosophenProblem.Frames.RunningScreen;

/**
 * 
 * @author Timo Scheuermann, Andrei Berbescu, Nina Wolff, Julian Homburg
 * @version 2.1.0
 * @since 27.03.2019
 *
 */
public class PhilosophenProblem {
	
	// Wird später gesetzt, wenn das Fenster erstellt wird,
	// damit die Philosophen ihre Aktionen verbreiten können.
	public static RunningScreen runningScreen;
	
	// Main Methode
	public static void main(String[] args) {
		
		// Öffnen des Hauptfensters
		new MainScreen();
		
	}
	
}
