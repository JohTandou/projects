package scenario3;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.port.SensorPort;

public class CapteurUltrason
{
	//Description: concerne le capteur ultrason et contient les fonctions permettant la détection d'un objet ainsi que la mesure de sa distance par rapport au robot
	//Utilisée par: Main, Moteurs
	//Utilise: Moteurs
	
	static EV3UltrasonicSensor capteur3 = new EV3UltrasonicSensor(SensorPort.S2); //Création de l'objet capteur3
	static SampleProvider echantillon3 = capteur3.getDistanceMode(); //Création de l'objet echantillon3
	static float[] echantillon4 = new float[echantillon3.sampleSize()]; //Création de l'objet echantillon4
	
	public static void activation() 
	{
		//Description: active le capteur ultrason
		//Entrée: rien
		//Sortie: rien
		
		capteur3.enable();
	}
	
	public static float obtenirDistance()
	{
		//Descrption: évalue la distance séparant le robot de l'obstacle le plus proche
		//Entrée: rien 
		//Sortie: float étant la distance trouvée par le capteur
		
		echantillon3.fetchSample(echantillon4,0);

       		return echantillon4[0];
	}
	
	public static int detecterObjet()
	{
		//Description: détermine si un objet est proche, et s'il s'agit d'un palet ou du robot adverse
		//Entrée: rien
		//Sortie: int (0 si pas d'objet proche, 1 si palet, 2 si robot adverse)
		
		float distance1; //Variable qui va contenir la 1e distance obtenue
		float distance2; //Variable qui va contenir la 2e distance obtenue
		distance1 = obtenirDistance();
		if (distance1 > 0.2) //Si la distance obtenue est supérieure à 20cm, il n'y a pas d'objet à proximité et la fonction retourne 0
		{
			return 0;
		}
		else //Sinon il y a un objet à proximité et le robot effectue 2 nouvelles mesures puis les compare
		{
			Moteurs.moteurGauche.setPower(0); //Le robot s'arrête puis prend une 1ère mesure au bout de 0,5s
			Moteurs.moteurDroit.setPower(0);
			Delay.msDelay(500);
			distance1 = obtenirDistance();
			Delay.msDelay(500); //Le robot prend une 2ème mesure 0,5s après
			distance2 = obtenirDistance();
			if (distance2 > distance1-0.005 && distance2 < distance1+0.005) //Si les 2 mesures sont égales à 5mm près, l'objet est considéré comme palet et la fonction retourne 1
			{
				return 1;
			}
			else //Sinon l'objet est considéré comme robot adverse et la fonction retourne 2
			{
				return 2;
			}
		}
	}
	
	public static void desactivation()
	{
		//Description: désactive le capteur ultrason
		//Entrée: rien
		//Sortie: rien
		
		capteur3.disable();
	}
	
	public static void fermer4()
	{
		//Description: libère les ressources du capteur ultrason
		//Entrée: rien
		//Sortie: rien
		
		capteur3.close();
	}
}