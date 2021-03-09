package scenario0;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class Moteurs
{
	//Description: concerne les trois moteurs
	//Utilisée par: Main
	//Utilise: rien
	
	static UnregulatedMotor moteurGauche = new UnregulatedMotor (MotorPort.A); // Création de l'objet moteurGauche
    static UnregulatedMotor moteurDroit = new UnregulatedMotor (MotorPort.B); // Création de l'objet moteurDroit
    static UnregulatedMotor pinces = new UnregulatedMotor (MotorPort.C); // Création de l'objet pinces
	
    public static void fermer3() // Libère les ressources des moteurs
	{
		//Description: libère les ressources des moteurs
		//Entrée: rien
		//Sortie: rien
		
		moteurGauche.close();
		moteurDroit.close();
		pinces.close();
	}
}