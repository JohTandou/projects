package scenario0;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;

// Identification: P0
// Description: à l’aide du capteur de contact le robot fermera ses pinces au contact d’un palet
// Procédure: placer le robot n’importe où sur la table puis placer le palet devant le robot

public class Main
{
	//Description: concerne la fonction main permettant de réaliser le scenario P0
	//Utilisée par: rien
	//utilise: Moteurs, CapteurTactile
	
	public static void main(String[] args) // Le robot ferme ses pinces au contact du palet
	{
		Sound.beepSequenceUp();
		Button.waitForAnyPress();
		boolean palet = false; // Variable qui va indiquer si un palet a été détecté ou non (initialisé à non)
		while (palet == false)
		{
			palet = CapteurTactile.estPresse();
		}
		Moteurs.pinces.setPower(-100); // Le robot ferme ses pinces, puis les réouvre deux secondes après
		Delay.msDelay(1100);
		Moteurs.pinces.setPower(0);
		Delay.msDelay(2000);
		Moteurs.pinces.setPower(100);
		Delay.msDelay(1000);
		Moteurs.pinces.setPower(0);
		Moteurs.fermer3();
		CapteurTactile.fermer2();
	}
}
