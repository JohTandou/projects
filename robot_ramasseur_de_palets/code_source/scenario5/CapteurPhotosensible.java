package scenario5;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class CapteurPhotosensible
{
	//Description: concerne le capteur de couleurs et contient la fonction permettant au robot de détecter les couleurs
	//Utilisée par: Main, Moteurs
	//Utilise: rien
	
	static EV3ColorSensor capteur = new EV3ColorSensor (SensorPort.S3); // Création de l'objet capteur
	static float[] echantillon = new float[capteur.sampleSize()]; // Création de l'objet echantillon

	public static void initialisation() // Initialise le capteur de couleurs
	{
		capteur.setFloodlight(true);
	}
	
	public static int obtenirIDCouleur()
	{
		//Description: scanne le point se situant sous le capteur photosensible
		//Entrée: rien
		//Sortie: int (compris entre -1 et 13) étant l'id de la couleur détectée
		
		int id; // Variable qui va contenir l'id de la couleur scannée
		float red; // Variable qui va contenir le niveau de rouge de la couleur scannée
		capteur.setCurrentMode("ColorID"); // Fait appel au mode ColorID
		capteur.fetchSample(echantillon, 0);
		id = (int) echantillon[0];
		//Le robot confond le gris et le bleu. Pour les différencier, voici le procédé ci-dessous
		if (id == 2) // Si la couleur détectée est bleue, la fonction fait appel au mode Red
		{
			capteur.setCurrentMode("Red");
			capteur.fetchSample(echantillon, 0);
			red = echantillon[0];
			if (red > 0.18) // Si la valeur de lumière rouge est supérieure à 0.18, il s'agit de gris et la fonction retourne 9
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
	
	public static void fermer1()
	{
		//Description: libère les ressources du capteur de couleurs
		//Entrée: rien
		//Sortie: rien
		
		capteur.close();
	}
}