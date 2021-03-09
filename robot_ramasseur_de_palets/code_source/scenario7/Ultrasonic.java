import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;


public class Ultrasonic extends AbstractFilter {
	/*
	 * Cette classe a pour but de recuperer les echantillons renvoyé par le capteur 
	 */
	float[] echantillon; 
	
	public Ultrasonic(SampleProvider source) {
		super(source);
		echantillon=new float [sampleSize];
	}
	
	public float distance() {
		super.fetchSample(echantillon, 0);
		return echantillon [0];
	}
	

}
