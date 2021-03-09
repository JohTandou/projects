package ev3;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.SampleProvider;

public class CapteurUltrason implements RangeFinder // Concerne le capteur à ultrason et contient les fonctions d'initialisation du capteur à ultrason et de détection de distance du robot par rapport à un objet. Cette classe est utilisée par la classe Moteurs.
{
		
		static SampleProvider		sp; // Nécessaire pour initialiser le capteur à Ultrason
    	static float [] 			echantillon3; // Variable de stockage de l'échantillon de distance
    	static EV3UltrasonicSensor capteur3 = new EV3UltrasonicSensor(SensorPort.S2); // Création de l'objet capteur3
    	
    	// Vérifier que le capteur à ultrason est sur le port 2
    	
    	public CapteurUltrason(Port port) // Construction de l'objet CapteurUltrason
    	{
    		capteur3 = new EV3UltrasonicSensor(port);
    		sp = capteur3.getDistanceMode();
    	    echantillon3 = new float[sp.sampleSize()];
    	}
    	
    	
    	public float getRange() // non utilisée pour le moment
    	{
           		sp.fetchSample(echantillon3, 0);

           		return echantillon3[0];
    	}
    	
    	public static float ObtenirDistance() { //Renvoi la distance de l'objet détecté par le capteur à ultrason
    		
    		sp.fetchSample(echantillon3, 0);
    		float Distance;
       		Distance = (float) echantillon3[0];
       		return Distance;
    	}
    	
    	public float[] getRanges()// non utilisée pour le moment
    	{
           		sp.fetchSample(echantillon3, 0);

           		return echantillon3;
    	}
    	
    	public static void enable() // Active le capteur à ultrason
    	{
    		capteur3.enable();
    	}
    	
    	public static void disable() // Désactive le capteur à ultrason
    	{
    		capteur3.disable();
    	}
    	
    	public static void fermer4() // Libere les ressources utilisée par le capteur à ultrason
    	{
    		capteur3.close();
    	}
    	
    	
}
    