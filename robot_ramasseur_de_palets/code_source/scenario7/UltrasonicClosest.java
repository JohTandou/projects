import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;


public class UltrasonicClosest {
	//attributs
	DifferentialPilot pilot ;
	Ultrasonic Ultrasonic;
	float closestAngle=-1.0f;
	float closestDistance= 1000.0f;
	EV3UltrasonicSensor capteurU;
	//constructeurs
	public UltrasonicClosest() {
		pilot=new DifferentialPilot(1.5f, 6, Motor.A, Motor.B);
		setupUltrasonic();
	}
	//methodes
	private void setupUltrasonic() {
		Brick b= BrickFinder.getDefault();
		Port s2 = b.getPort("S2");
		 capteurU = new EV3UltrasonicSensor(s2);
		Ultrasonic = new Ultrasonic(capteurU.getMode("Distance"));
		
	}
	
	public boolean Ultrasonicproche(float degre,float taille) {
		float closestAngle = -1.0f;										
		float closestDistance = 1000.0f	;								
		DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B); 	
		boolean palet2 = false;													
		pilot.reset();													
		pilot.setAngularSpeed(45);										
		pilot.rotate(degre,true);										
		while(pilot.isMoving()) {										
			Delay.msDelay(10);											
			
	    
			if( Ultrasonic.distance()<closestDistance && Ultrasonic.distance()<taille) { 
				closestDistance=Ultrasonic.distance();							
				closestAngle=pilot.getAngleIncrement();			
																						}
								}

		pilot.setAngularSpeed(50);
		if(closestDistance>500) {
			pilot.rotate(-degre);	
		}
		
		else {
			
				{if(degre==360) 
					{pilot.rotate(closestAngle-((degre*0.99)+(closestDistance*2.65))); 
					pilot.travel((closestDistance*75)/2.54);						
					if(!Detecter(closestDistance*0.25f)) {pilot.travel((closestDistance*25)/2.54); palet2 =true ;}  
					}
		
				if(degre==180) 
					{pilot.rotate(closestAngle-((degre*0.99)+(closestDistance)));
					pilot.travel((closestDistance*75)/2.54);					
					if(!Detecter(closestDistance*0.25f)) {pilot.travel((closestDistance*25)/2.54); palet2 =true ;}
					}
				}	
			}
		return palet2;
	}
	
	public boolean Detecter (float Dmin) {
		boolean Detecter = false;  // on initialise a faux 
		if (Ultrasonic.distance() < Dmin) {
			Detecter = true;		// si le robot detecte un objet a moins de Dmin  il renvoie vrai
		} else	
			Detecter = false;		// sinon il renvoi faux
		return Detecter;			// on renvoie alors si le robot a detecter un objet ou non
	}

}
