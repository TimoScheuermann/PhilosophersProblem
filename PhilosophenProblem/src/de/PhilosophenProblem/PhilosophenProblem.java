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
	
	// Wird sp�ter gesetzt, wenn das Fenster erstellt wird,
	// damit die Philosophen ihre Aktionen verbreiten k�nnen.
	public static RunningScreen runningScreen;
	
	// Main Methode
	public static void main(String[] args) {
		
		// �ffnen des Hauptfensters
		new MainScreen();
		
	}
	
}
