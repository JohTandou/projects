package ev3;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.SampleProvider;

public class CapteurUltrason implements RangeFinder // Concerne le capteur � ultrason et contient les fonctions d'initialisation du capteur � ultrason et de d�tection de distance du robot par rapport � un objet. Cette classe est utilis�e par la classe Moteurs.
{
		
		static SampleProvider		sp; // N�cessaire pour initialiser le capteur � Ultrason
    	static float [] 			echantillon3; // Variable de stockage de l'�chantillon de distance
    	static EV3UltrasonicSensor capteur3 = new EV3UltrasonicSensor(SensorPort.S2); // Cr�ation de l'objet capteur3
    	
    	// V�rifier que le capteur � ultrason est sur le port 2
    	
    	public CapteurUltrason(Port port) // Construction de l'objet CapteurUltrason
    	{
    		capteur3 = new EV3UltrasonicSensor(port);
    		sp = capteur3.getDistanceMode();
    	    echantillon3 = new float[sp.sampleSize()];
    	}
    	
    	
    	public float getRange() // non utilis�e pour le moment
    	{
           		sp.fetchSample(echantillon3, 0);

           		return echantillon3[0];
    	}
    	
    	public static float ObtenirDistance() { //Renvoi la distance de l'objet d�tect� par le capteur � ultrason
    		
    		sp.fetchSample(echantillon3, 0);
    		float Distance;
       		Distance = (float) echantillon3[0];
       		return Distance;
    	}
    	
    	public float[] getRanges()// non utilis�e pour le moment
    	{
           		sp.fetchSample(echantillon3, 0);

           		return echantillon3;
    	}
    	
    	public static void enable() // Active le capteur � ultrason
    	{
    		capteur3.enable();
    	}
    	
    	public static void disable() // D�sactive le capteur � ultrason
    	{
    		capteur3.disable();
    	}
    	
    	public static void fermer4() // Libere les ressources utilis�e par le capteur � ultrason
    	{
    		capteur3.close();
    	}
    	
    	
}
    