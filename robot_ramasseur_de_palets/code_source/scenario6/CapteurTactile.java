package ev3;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class CapteurTactile // Concerne le capteur de contact et contient la fonction permettant de d�tecter une pression. Cette classe est utilis�e par la classe Moteurs
{
	static EV3TouchSensor capteur2 = new EV3TouchSensor (SensorPort.S1); // Création de l'objet capteur2
	static SampleProvider echantillon2 = capteur2.getTouchMode(); // Création de l'objet echantillon2

    public static boolean estPresse() // Indique si le capteur de contact est pressé ou non en prenant rien en entrée et en retournant un booléen
    {
    	float [] sample = new float[echantillon2.sampleSize()];
    	echantillon2.fetchSample(sample, 0);
    	if (sample[0] == 0) // Si le sample vaut 0, le capteur n'est pas touché et la fonction retourne le booléen false
    	{
    		return false;
    	}
    	else // Sinon le sample vaut 1, donc le capteur est touché et la fonction retourne le booléen true 
    	{
    		return true;
    	}
    }

    public static void fermer2() // Libère les ressources du capteur de contact
    {
    	capteur2.close();
    }
}