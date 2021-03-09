import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.hardware.Brick;

public class UltrasonicD {
	/*
	 * Cette classe gére les mouvements du robot en fonction de la distance le separant d'un autre objet  
	 */
	//attributs
	DifferentialPilot pilot ;
	Ultrasonic ultrasonic;
	//constructeur 
	public UltrasonicD() {
		/*
		 * 24/creer un objet de stype DifferentialPilot et définir les parametres physique de ce dernier 
		 * 27/creer un objet capteur U de typpe EV3UltrasonicSensor et l'associer au port s2
		 * 28/creer un objet de type Ultrasonic qui vas contenir toute les methodes definit dans la classe ultrasonic et donc son role c'est de reuperer les echantillons renvoyé par le capteurU
		 */
		pilot = new DifferentialPilot(1.5f, 6, Motor.A, Motor.B);
		Brick b = BrickFinder.getDefault();
		Port s2 = b.getPort("S2");
		EV3UltrasonicSensor capteurU = new EV3UltrasonicSensor(s2);
		ultrasonic = new Ultrasonic(capteurU.getMode("Distance"));
		
		while  (true) {
			Delay.msDelay(2); //attendre 2ms
			float distance =ultrasonic.distance();//recupere la distance renvoyé par le capteurU
			if(distance<0.3) {
				pilot.backward();//revenir en arriére si la distance<0.3
				
			}else if (distance>0.4) {
				pilot.forward();//avancer si la distance>0.4
			} else {
				pilot.stop();//arrete le robot 
			}
			
			
			if (Button.ESCAPE.isDown()) {
				pilot.stop();
				capteurU.close();
				System.exit(0);
				
			}
		}
		
		
	}

}
