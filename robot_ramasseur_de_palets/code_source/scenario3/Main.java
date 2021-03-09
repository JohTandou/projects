package scenario3;

//Identification: P3
//Description: Le robot doit déposer le palet derrière une des deux lignes blanches
//Procédure: placer le palet sur 1 des 9 intersections de la table puis placer le robot dans une zone grise

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Main
{
	//Description: concerne la fonction main permettant de réaliser le scenario P3
	//Utilisée par: rien
	//Utilise: Moteurs, CapteurPhotosensible, CapteurTactile, CapteurUltrason
	
	public static void main(String[] args)
	{
		int i=-3; // Variable qui désigne le numéro des différentes intersections de la surface de jeu (initialisée à -3)
		boolean palet; // Variable qui va indiquer si un palet a été détecté ou non à l'intersection i
		CapteurUltrason.activation();
		CapteurPhotosensible.initialisation();
		Sound.beepSequenceUp();
		System.out.println("Appuyer n'importe où pour démarrer");
		Button.waitForAnyPress();
		palet = Moteurs.allerIntersection(i, false, true);
		if (palet == false)
		{
			for(i=0; i<9; i++)
			{
				if (palet == true) // Si un palet est détecté à l'intersection i, le ramasser et se rendre à l'intersection suivante sachant que le palet a été ramassé
				{
					Moteurs.ramasserPalet(i,true);
					if (i == 8) // Si le robot se situe à l'intersection 8, sortir de la boucle car l'intersection 8+1 n'existe pas
					{
						break;
					}
					palet = Moteurs.allerIntersection(i+1,true,true);
				}
				else // Sinon, se rendre à l'intersection suivante sachant que le palet n'a pas été ramassé
				{
					if (i == 8) // Si le robot se situe à l'intersection 8, sortir de la boucle car l'intersection 8+1 n'existe pas
					{
						break;
					}
					palet = Moteurs.allerIntersection(i+1,false,true);
				}
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
