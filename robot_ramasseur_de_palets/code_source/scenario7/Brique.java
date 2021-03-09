
import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Brique // Concerne la brique ev3 et contient une fonction permettant de signaler que le robot est pr�t � d�marrer. Cette classe est utilis�e par les classes Main et Moteurs
	{	
	        static Brique console = new Brique(); // Cr�ation de l'objet console
	        //methodes
	        public void pretAuDemarrage() // La brique signale � l'utilisateur que le robot est pr�t � d�marrer
	        {
	            Button.LEDPattern(4);
	            Sound.beepSequenceUp();
	            Button.waitForAnyPress();
	        }
	}

