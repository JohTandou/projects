package scenario4;

import lejos.hardware.Button;

public class Brique
{
	//Description: concerne la brique ev3 et contient une fonction permettant de saisir le camp du robot
	//Utilisée par: Main, Moteurs
	//Utilise: rien
	
	static Brique console = new Brique(); // Création de l'objet console
	
	public boolean choisirCamp()
	{
		//Description: la brique demande à l'utilisateur quel est le camp du robot (ouest ou est)
		//Entrée: rien
		//Sortie: booléen etant le camp choisi (true pour ouest, false pour est)
		
		boolean camp;
		int bouton;
		do
		{
			System.out.println("Quel est le camp du robot ?");
			System.out.println("Appuyer sur gauche pour ouest, droite pour est");
			bouton = Button.waitForAnyPress();
		}
		while (bouton != 16 && bouton != 8);
		if (bouton == 16)
		{
			camp = true;
		}
		else
		{
			camp = false;
		}
		return camp;
	}
}