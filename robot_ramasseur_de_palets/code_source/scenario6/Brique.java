package ev3;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Brique // Concerne la brique ev3 et contient une fonction permettant de signaler que le robot est prêt à démarrer. Cette classe est utilisée par les classes Main et Moteurs
{
	static Brique console = new Brique(); // Création de l'objet console
	
	public void pretAuDemarrage() // La brique signale à l'utilisateur que le robot est prêt à démarrer
	{
	    Button.LEDPattern(4);
	    Sound.beepSequenceUp();
	    Button.waitForAnyPress();
	}
}
