package scenario5;

import lejos.hardware.Button;
import lejos.hardware.Sound;

// Identification: P5
// Description: Le robot doit déposer chacun des 9 palets derrière la ligne blanche adverse
// Procédure: placer 9 palets sur les 9 intersections de la table, désigner le camp adverse au robot puis placer le robot sur une des 3 positions de départ de son camp

public class Main
{	
	//Description: concerne la fonction main permettant de réaliser le scenario P5
	//Utilisée par: rien
	//Utilise: Brique, Moteurs, CapteurPhotosensible, CapteurTactile, CapteurUltrason
	
	public static void main(String[] args)
	{
		boolean camp; // Variable qui va contenir le camp (ouest ou est) saisi dans le menu
		int i=0; // Variable qui désigne le numéro des différentes intersections de la surface de jeu
		CapteurUltrason.activation();
		CapteurPhotosensible.initialisation();
		Sound.beepSequenceUp();
		camp = Brique.console.choisirCamp();
		System.out.println("Appuyer n'importe où pour démarrer");
		Button.waitForAnyPress();
		Moteurs.depart();
		Moteurs.allerIntersection(i,true,camp);
		for(i=0; i<9; i++)
		{
			Moteurs.ramasserPalet(i,camp);
			if (i == 8) // Si le robot se situe à l'intersection 8, sortir de la boucle car l'intersection 8+1 n'existe pas
			{
				break;
			}
			Moteurs.allerIntersection(i+1,true,camp);
		}
		Moteurs.moteurGauche.setPower(0); // Le moteur gauche s'arrête
		Moteurs.moteurDroit.setPower(0); // Le moteur droit s'arrête
		CapteurUltrason.desactivation();
		CapteurUltrason.fermer4();
		Moteurs.fermer3();
		CapteurTactile.fermer2();
		CapteurPhotosensible.fermer1();
	}
}
