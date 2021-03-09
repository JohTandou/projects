package competition;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Main
{	
	//Description: concerne la fonction main permettant au robot de ramasser les palets lors de la compétition
	//Utilisée par: rien
	//Utilise: Brique, Moteurs, CapteurPhotosensible, CapteurTactile, CapteurUltrason
	
	public static void main(String[] args)
	{
		boolean camp; // Variable qui va contenir le camp (ouest ou est) saisi dans le menu
		int i=0; // Variable qui désigne le numéro des différentes intersections de la surface de jeu (initialisée à 0)
		boolean palet; // Variable qui va indiquer si un palet a été détecté ou non à l'intersection i
		CapteurUltrason.activation();
		CapteurPhotosensible.initialisation();
		Sound.beepSequenceUp();
		camp = Brique.console.choisirCamp();
		System.out.println("Appuyer n'importe où pour démarrer");
		Button.waitForAnyPress();
		Moteurs.depart();
		palet = Moteurs.allerIntersection(i,true,camp);
		for(i=0; i<9; i++)
		{
			if (palet == true) // Si un palet est détecté à l'intersection i, le ramasser et se rendre à l'intersection suivant sachant que le palet a été ramassé
			{
				Moteurs.ramasserPalet(i,camp);
				if (i == 8) // Si le robot se situe à l'intersection 8, sortir de la boucle car l'intersection 8+1 n'existe pas
				{
					break;
				}
				palet = Moteurs.allerIntersection(i+1,true,camp);
			}
			else // Sinon, se rendre à l'intersection suivant sachant que le palet n'a pas été ramassé
			{
				if (i == 8) // Si le robot se situe à l'intersection 8, sortir de la boucle car l'intersection 8+1 n'existe pas
				{
					break;
				}
				palet = Moteurs.allerIntersection(i+1,false,camp);
			}
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