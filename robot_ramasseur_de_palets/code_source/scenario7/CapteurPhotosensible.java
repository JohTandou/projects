import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class CapteurPhotosensible {
	static EV3ColorSensor capteur = new EV3ColorSensor (SensorPort.S3); // Cr�ation de l'objet capteur
	static float[] echantillon = new float[capteur.sampleSize()]; // Cr�ation de l'objet echantillon
    public static void initialisation() // Initialise le capteur de couleurs
    {
            capteur.setFloodlight(true);
    }
    
    // FONCTION PREDEFINIE
    public int getColorID() // Scanne le point se situant sous le capteur photosensible et renvoie l'id (int compris entre -1 et 13) de la couleur d�tect�e. Ne prend rien en entr�e
    {        
            capteur.setCurrentMode("ColorID");
            capteur.fetchSample(echantillon, 0);
            return (int) echantillon[0];
    }
    
    // FONCTION CREEE QUE NOUS UTILISONS
    public static int obtenirIDCouleur() // M�me fonction que getColorID mais diff�rencie le gris du bleu via le mode lumi�re rouge
    {
            int id; // D�claration de la variable id de type int qui va contenir l'id de la couleur scann�e
            float red; // D�claration de la variable red de type float qui va contenir le niveau de rouge de la couleur scann�e
            capteur.setCurrentMode("ColorID");
            capteur.fetchSample(echantillon, 0);
            id = (int) echantillon[0];
            if (id == 2) // Si la couleur d�tect�e est bleue, la fonction fait appel au mode lumi�re rouge
            {
                    capteur.setCurrentMode("Red");
                    capteur.fetchSample(echantillon, 0);
                    red = echantillon[0];
                    if (red > 0.2) // Si la valeur de lumi�re rouge est sup�rieure � 0.2, il s'agit de gris et la fonction retourne 9
                    {
                            return 9;
                    }
                    else // Sinon, il s'agit toujours de bleu et la fonction retourne 2
                    {
                            return 2;
                    }
            }
            else // Sinon, la fonction retourne l'id de la couleur d�tect�e
            {
                    return id;
            }
    }        
    
    public static void fermer1() // Lib�re les ressources du capteur de couleurs
    {
            capteur.close();
    }
}
