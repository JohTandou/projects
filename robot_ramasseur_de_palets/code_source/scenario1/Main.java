package scenario1;

import lejos.hardware.Button;
import lejos.hardware.Sound;

// Identification: P1
// Description: le robot doit pouvoir donner la couleur de la ligne sur laquelle il a été déposé
// Procédure: placer le robot sur n'importe quelle ligne de la table

public class Main
{
	//Description: concerne la fonction main permettant de réaliser le scenario P1
	//Utilisée par: rien
	//Utilise: CapteurPhotosensible, CapteurTactile
	
	public static void main(String[] args)
	{
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		boolean pression = false; // Variable qui indique si le capteur tactile est pressé ou non (initialisé à non)
		CapteurPhotosensible.initialisation();
		Sound.beepSequenceUp();
		Button.waitForAnyPress();
		while (pression == false) // Tant que le capteur tactile n'est pas pressé, effectuer le scénario
		{
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			switch (couleur) // Affiche une phrase à l'écran en fonction de l'id de la couleur détectée
			{
				case -1:
					System.out.println("Aucune couleur n'est détectée");
					break;
				case 0:
					System.out.println("La ligne est de couleur rouge");
					break;
				case 1:
					System.out.println("La ligne est de couleur verte");
					break;
				case 2:
					System.out.println("La ligne est de couleur bleue");
					break;
				case 3:
					System.out.println("La ligne est de couleur jaune");
					break;
				case 6:
					System.out.println("La ligne est de couleur blanche");
					break;
				case 7:
					System.out.println("La ligne est de couleur noire");
					break;
				default:
					System.out.println("Il ne s'agit pas d'une ligne de la table");
					break;
			}
			pression = CapteurTactile.estPresse();
		}
		CapteurPhotosensible.fermer1();
	}
}
