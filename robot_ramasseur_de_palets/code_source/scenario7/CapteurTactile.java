import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class CapteurTactile {
	 static EV3TouchSensor capteur2 = new EV3TouchSensor (SensorPort.S1); //Creation de l'objet capteur2
     static SampleProvider echantillon2 = capteur2.getTouchMode(); // Cr�ation de l'objet echantillon2
 public static boolean estPresse() // Indique si le capteur de contact est press� ou non en prenant rien en entr�e et en retournant un bool�en
 {
         float [] sample = new float[echantillon2.sampleSize()];
         echantillon2.fetchSample(sample, 0);
         if (sample[0] == 0) // Si le sample vaut 0, le capteur n'est pas touch� et la fonction retourne le bool�en false
         {
                 return false;
         }
         else // Sinon le sample vaut 1, donc le capteur est touch� et la fonction retourne le bool�en true 
         {
                 return true;
         }
 }
 public static void fermer2() // Lib�re les ressources du capteur de contact
 {
         capteur2.close();
 }
}
