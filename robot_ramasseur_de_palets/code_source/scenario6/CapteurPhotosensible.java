package ev3;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.ColorIdentifier;

public class CapteurPhotosensible implements ColorIdentifier // Concerne le capteur de couleurs et contient la fonction permettant au robot de détecter les couleurs. Cette classe est utilisée par la classe Moteurs
{
	static EV3ColorSensor capteur = new EV3ColorSensor (SensorPort.S3); // Création de l'objet capteur
	static float[] echantillon = new float[capteur.sampleSize()]; // Création de l'objet echantillon

	public static void initialisation() // Initialise le capteur de couleurs
	{
		capteur.setFloodlight(true);
	}
	
	// FONCTION PREDEFINIE
	public int getColorID() // Scanne le point se situant sous le capteur photosensible et renvoie l'id (int compris entre -1 et 13) de la couleur détectée. Ne prend rien en entrée
	{	
		capteur.setCurrentMode("ColorID");
		capteur.fetchSample(echantillon, 0);
		return (int) echantillon[0];
	}
	
	// FONCTION CREEE QUE NOUS UTILISONS
	public static int obtenirIDCouleur() // Même fonction que getColorID mais différencie le gris du bleu via le mode lumière rouge
	{
		int id; // Déclaration de la variable id de type int qui va contenir l'id de la couleur scannée
		float red; // Déclaration de la variable red de type float qui va contenir le niveau de rouge de la couleur scannée
		capteur.setCurrentMode("ColorID");
		capteur.fetchSample(echantillon, 0);
		id = (int) echantillon[0];
		if (id == 2) // Si la couleur détectée est bleue, la fonction fait appel au mode lumière rouge
		{
			capteur.setCurrentMode("Red");
			capteur.fetchSample(echantillon, 0);
			red = echantillon[0];
			if (red > 0.2) // Si la valeur de lumière rouge est supérieure à 0.2, il s'agit de gris et la fonction retourne 9
			{
				return 9;
			}
			else // Sinon, il s'agit toujours de bleu et la fonction retourne 2
			{
				return 2;
			}
		}
		else // Sinon, la fonction retourne l'id de la couleur détectée
		{
			return id;
		}
	}	
	
	public static void fermer1() // Libère les ressources du capteur de couleurs
	{
		capteur.close();
	}
}
