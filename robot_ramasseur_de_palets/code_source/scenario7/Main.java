import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
public class Main {
	// Identification: P7
			// Description: Le robot doit déposer un palet derriere le camp adverse 
			// Procédure: placer un palet n'importe ou sur la table , désigner le camp adverse au robot ,  placer le robot n'importe ou sur la table 

		boolean palet;
		int button;
		int cVert = 0, cBleu = 0, cv, cb; //declaration des compteurs pour differencier les camps
		cv=0; cb=0;
		UltrasonicClosest CapteurU = new UltrasonicClosest();
		CapteurPhotosensible Color = new CapteurPhotosensible();
		CapteurTactile touch = new CapteurTactile();
		Color.initialisation();
		Brique.console.pretAuDemarrage();
		DifferentialPilot pilot =new DifferentialPilot(1.968f,4.35f, Motor.A, Motor.B);
		button=Button.waitForAnyPress();
		CapteurPhotosensible.obtenirIDCouleur(); // Scanne le point se situant sous le capteur photosensible et renvoie l'id de la couleur detectée 
		/*
		 * il y a deux if  qui servent a lancer le programme du coté Est ou Ouest 
		 */
			if(button==16) {
			Moteurs.pinces.forward();//ouvrir les pinces 
			CapteurU.Ultrasonicproche(360f,2.54f); // faire une detection d'objets  sur un axe de  360 degré 
			//avancer le robot 
			Moteurs.moteurGauche.forward();
			Moteurs.moteurDroit.forward();
			//chercher sur un axe de 360 degré s'il y a un objet autour du robot 
			palet=CapteurU.Ultrasonicproche(360f,2.54f);
			/*
			 * si le robot detecte le palet des la premiere fois 
			 */
			if (palet==true) {
				/*
				 * arreter le robot 
				 */
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				/*
				 * fermer les pinces
				 */
				Moteurs.pinces.backward();
				/*
				 * arreter le robot
				 */
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
			/*
			 * detecter  l'element le plus proche du robot 
			 */
				CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
				/*
				 * arreter le robot 
				 */
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				/*
				 * le robot tourne a 90 degré
				 */
				pilot.rotate(90);
				/*
				 * faire avancer le robot 
				 */
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				
				while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
					{
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
				}
				//arreter le robot 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//le robot depasse un peut la ligne blanche
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward(); 
				Delay.msDelay(100);
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				/*
				 * mettre le robot sur le bon axe 
				 */
				//tourner le robot sur la ligne blanche 
				Moteurs.moteurDroit.forward();
				while (!(Color.getColorID()==6)) {
					Moteurs.moteurDroit.forward();
				}
				//arreter le robot 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//avancer a droite 
				Moteurs.moteurGauche.setPower(60);
		        Moteurs.moteurDroit.setPower(0);
				//le robot tourne jusqu'au gris 
				while(!(Color.getColorID()==9)) {
					Moteurs.moteurDroit.forward();
				}
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//le robot avance jusqu'au mur pour tourner 
				// avancer le robot  a droite 
				Moteurs.moteurGauche.setPower(60);
		        Moteurs.moteurDroit.setPower(0);
		        while(!CapteurU.Detecter(0.135f)) {
		        	//suivre la ligne blanche 
		        	if (Color.getColorID()==6) {
		        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
		                Moteurs.moteurDroit.setPower(60);
		        	}
		        	if (Color.getColorID()==9) {
		        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
		                Moteurs.moteurDroit.setPower(0);
		        	}
		        }
		        //tourner pour eviter le mur
				pilot.rotate(95);
				//le robot avance
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				/*
				 * Ceci est pour tester le camp 
				 */
				while(!CapteurU.Detecter(0.15f)) {
					cv++;
					cb++;
					if (Color.getColorID()==2) 
					{cBleu=cb;}
					if(Color.getColorID()==1)
					{cVert=cv;}
					if(Color.getColorID()==6)
					{break;}
				}
				/*
				 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
				 * tour pour avancer jusqu'au blanc et deposer le palet
				 */
				if (cVert > cBleu) {
					pilot.rotate(173);		
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					while(!(Color.getColorID()==6)) {
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();}		
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					Moteurs.pinces.forward();	
					System.exit(0);		// fin du programme
						
				}
				/*
				 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
				 */
			
				if(cVert<cBleu) { 
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					while(!(Color.getColorID()==6)) {
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();}		
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					Moteurs.pinces.forward();	
					System.exit(0);		// fin du programme	
				}
				
			}
			
			/*
			 * si la premiere detection n'est pas un palet 
			 */
			else {
				//arreter le robot 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//tourner pour eviter le mur 
				pilot.rotate(90);		
				/*
				 * mettre le robot sur le bon axe 
				 */
				//tourner le robot sur la ligne blanche 
				Moteurs.moteurDroit.forward();
				while (!(Color.getColorID()==6)) {
					Moteurs.moteurDroit.forward();
				}
				//arreter le robot 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//avancer a droite 
				Moteurs.moteurGauche.setPower(60);
		        Moteurs.moteurDroit.setPower(0);
				//le robot tourne jusqu'au gris 
				while(!(Color.getColorID()==9)) {
					Moteurs.moteurDroit.forward();
				}
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				/*
				 * le robot avance sur la ligne blanche tant qu'il ne rencontre pas un palet ou un mur 
				 */
				//tourner a droite 
				Moteurs.moteurGauche.setPower(60);
				Moteurs.moteurDroit.setPower(0);
				while(!CapteurU.Detecter(0.19f))
				{
					
					if(Color.getColorID()==6) {
						Moteurs.moteurGauche.setPower(0); 
						Moteurs.moteurDroit.setPower(60);
					}
					if(Color.getColorID()==9) {
						Moteurs.moteurGauche.setPower(60);
						Moteurs.moteurDroit.setPower(0);	
					}
					/*
					 * Si le capteur tactile a été pressé , le robot fait le test de camp  pour pouvoir deposer le palet
					 */
					if(touch.estPresse()) {
						
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						
						Moteurs.pinces.backward();
						
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						//il avance un peut pour se decaler du mur 
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						
						Delay.msDelay(1000);
						
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();		
						//il tourne a 90
						pilot.rotate(90);		
						//il avance
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						/*
						 * Ceci est pour tester le camp 
						 */
						while(!CapteurU.Detecter(0.15f)) {
							cv++;
							cb++;
							if (Color.getColorID()==2) 
							{cBleu=cb;}
							if(Color.getColorID()==1)//couleur verte *********** 
							{cVert=cv;}
							if(Color.getColorID()==6)
							{break;}
						}
						/*
						 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
						 * tour pour avancer jusqu'au blanc et deposer le palet
						 */
						if (cVert > cBleu) {
							pilot.rotate(173);		
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							while(!(Color.getColorID()==6)) {
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();}		
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							Moteurs.pinces.forward();	
							System.exit(0);		// fin du programme
								
						}
						/*
						 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
						 */
					
						if(cVert<cBleu) { 
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							while(!(Color.getColorID()==6)) {
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();}		
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							Moteurs.pinces.forward();	
							System.exit(0);		// fin du programme	
						}
						
						
					}
			}
				}
				/*
				 * le robot arrive jusqu'au mur , tourne et avance jusqu'a la ligne noir 
				 */
				pilot.rotate(90);		// il tourne a 90 degré
				//le robot avance 
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				//avancer jusqu'au noir 
				while(!(Color.getColorID()==7)) {
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					}		
				
				//le robot s'arrete 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//detection  d'objets sur un axe de 180
				palet=CapteurU.Ultrasonicproche(180, 1.1f);
					if (palet == true) {
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();

						Moteurs.pinces.backward();

						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						/*
						 * detecter l'element le plus proche de lui
						 */
						CapteurU.Ultrasonicproche(360f, 2.54f);// le robot redetecte l'objet le plus proche de lui, ça sera un
																// mur car le palet il sera deja saisi
						/*
						 * arreter le robot
						 */
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						/*
						 * le robot tourne a 90 degré
						 */
						pilot.rotate(90);
						/*
						 * faire avancer le robot
						 */
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						/*
						 * 
						 */
						while (Color.getColorID() != 6)// tant que la couleur detectée est differente du blanc on fait avancer
														// le robot
						{
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
						}
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						// le robot depasse un peut la ligne blanche
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						Delay.msDelay(100);
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						/*
						 * mettre le robot sur le bon axe
						 */
						// tourner le robot sur la ligne blanche
						Moteurs.moteurDroit.forward();
						while (!(Color.getColorID() == 6)) {
							Moteurs.moteurDroit.forward();
						}
						// arreter le robot
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						// avancer a droite
						Moteurs.moteurGauche.setPower(60);
						Moteurs.moteurDroit.setPower(0);
						// le robot tourne jusqu'au gris
						while (!(Color.getColorID() == 9)) {
							Moteurs.moteurDroit.forward();
						}
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						// le robot avance jusqu'au mur pour tourner
						// avancer le robot a droite
						Moteurs.moteurGauche.setPower(60);
						Moteurs.moteurDroit.setPower(0);
						while (!CapteurU.Detecter(0.135f)) {
							if (Color.getColorID() == 6) {
								Moteurs.moteurGauche.setPower(0);// tourner vers la gauche
								Moteurs.moteurDroit.setPower(60);
							}
							if (Color.getColorID() == 9) {
								Moteurs.moteurGauche.setPower(60);// tourner vers la droite
								Moteurs.moteurDroit.setPower(0);
							}
						}
						// tourner pour eviter le mur
						pilot.rotate(95);
						// le robot avance
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						/*
						 * Ceci est pour tester le camp
						 */
						while (!CapteurU.Detecter(0.15f)) {
							cv++;
							cb++;
							if (Color.getColorID() == 2) {
								cBleu = cb;
							}
							if (Color.getColorID() == 1)
							{
								cVert = cv;
							}
							if (Color.getColorID() == 6) {
								break;
							}
						}
						/*
						 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et
						 * donc le robot fait demi tour pour avancer jusqu'au blanc et deposer le palet
						 */
						if (cVert > cBleu) {
							pilot.rotate(173);
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							while (!(Color.getColorID() == 6)) {
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
							}
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							Moteurs.pinces.forward();
							System.exit(0); // fin du programme

						}
						// Si la variable pour le vert est plus petite c'est qu'il est du bon coté
						if (cVert < cBleu) {
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							while (!(Color.getColorID() == 6)) {
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
							}
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							Moteurs.pinces.forward();
							System.exit(0); // fin du programme
						}
					}
				/*
				 * si le robot n'a pas detecter de palet 
				 */
				else {//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//tourner pour eviter le mur 
					pilot.rotate(90);		
					/*
					 * mettre le robot sur le bon axe 
					 */
					//tourner le robot sur la ligne blanche 
					Moteurs.moteurDroit.forward();
					while (!(Color.getColorID()==6)) {
						Moteurs.moteurDroit.forward();
					}
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//avancer a droite 
					Moteurs.moteurGauche.setPower(60);
			        Moteurs.moteurDroit.setPower(0);
					//le robot tourne jusqu'au gris 
					while(!(Color.getColorID()==9)) {
						Moteurs.moteurDroit.forward();
					}
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * le robot avance sur la ligne blanche tant qu'il ne rencontre pas un palet ou un mur 
					 */
					//tourner a droite 
					Moteurs.moteurGauche.setPower(60);
					Moteurs.moteurDroit.setPower(0);
					while(!CapteurU.Detecter(0.19f))
					{
						
						if(Color.getColorID()==6) {
							Moteurs.moteurGauche.setPower(0); 
							Moteurs.moteurDroit.setPower(60);
						}
						if(Color.getColorID()==9) {
							Moteurs.moteurGauche.setPower(60);
							Moteurs.moteurDroit.setPower(0);	
						}
						/*
						 * Si le capteur tactile a été pressé , le robot detecte le camp adverse pour pouvoir deposer le palet
						 */
						if(touch.estPresse()) {
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							
							Moteurs.pinces.backward();
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//il avance un peut pour se decaler du mur 
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							
							Delay.msDelay(1000);
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();		
							//il tourne a 90
							pilot.rotate(90);		
							//il avance
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							/*
							 * Ceci est pour tester le camp 
							 */
							while(!CapteurU.Detecter(0.15f)) {
								cv++;
								cb++;
								if (Color.getColorID()==2) 
								{cBleu=cb;}
								if(Color.getColorID()==1)//couleur verte *********** 
								{cVert=cv;}
								if(Color.getColorID()==6)
								{break;}
							}
							/*
							 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
							 * tour pour avancer jusqu'au blanc et deposer le palet
							 */
							if (cVert > cBleu) {
								pilot.rotate(173);		
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								while(!(Color.getColorID()==6)) {
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();}		
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								Moteurs.pinces.forward();	
								System.exit(0);		// fin du programme
									
							}
							/*
							 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
							 */
							
							if(cVert<cBleu) { 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								while(!(Color.getColorID()==6)) {
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();}		
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								Moteurs.pinces.forward();	
								System.exit(0);		// fin du programme	
							}
						}
						
						/*
						 * il arrive au mur 
						 * le robot tourne a 90 et avance jusqu'a la ligne noir 
						 */
								
								pilot.rotate(90);		// il tourne a 90 degré
								//le robot avance 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								while(!(Color.getColorID()==7)) {//le robot avance jusqu'au noir
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									}		
								//le robot s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								palet=CapteurU.Ultrasonicproche(180, 1.1f);//effectue une detection sur 180 degré 
								if(palet==true) {
									//le robot s'arrete 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									//attrape le palet 
									Moteurs.pinces.backward();
									//s'arrete 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									/*
									 * detecter  l'element le plus proche de lui 
									 */
										CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
										/*
										 * arreter le robot 
										 */
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										/*
										 * le robot tourne a 90 degré
										 */
										pilot.rotate(90);
										/*
										 * faire avancer le robot 
										 */
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										/*
										 * 
										 */
										while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
											{
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
										}
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot depasse un peut la ligne blanche
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward(); 
										Delay.msDelay(100);
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										/*
										 * mettre le robot sur le bon axe 
										 */
										//tourner le robot sur la ligne blanche 
										Moteurs.moteurDroit.forward();
										while (!(Color.getColorID()==6)) {
											Moteurs.moteurDroit.forward();
										}
										//arreter le robot 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//avancer a droite 
										Moteurs.moteurGauche.setPower(60);
								        Moteurs.moteurDroit.setPower(0);
										//le robot tourne jusqu'au gris 
										while(!(Color.getColorID()==9)) {
											Moteurs.moteurDroit.forward();
										}
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot avance jusqu'au mur pour tourner 
										// avancer le robot  a droite 
										Moteurs.moteurGauche.setPower(60);
								        Moteurs.moteurDroit.setPower(0);
								        while(!CapteurU.Detecter(0.135f)) {
								        	if (Color.getColorID()==6) {
								        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
								                Moteurs.moteurDroit.setPower(60);
								        	}
								        	if (Color.getColorID()==9) {
								        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
								                Moteurs.moteurDroit.setPower(0);
								        	}
								        }
								        //tourner pour eviter le mur
										pilot.rotate(95);
										//le robot avance
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										/*
										 * Ceci est pour tester le camp 
										 */
										while(!CapteurU.Detecter(0.15f)) {
											cv++;
											cb++;
											if (Color.getColorID()==2) 
											{cBleu=cb;}
											if(Color.getColorID()==1)//couleur verte *********** 
											{cVert=cv;}
											if(Color.getColorID()==6)
											{break;}
										}
										/*
										 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
										 * tour pour avancer jusqu'au blanc et deposer le palet
										 */
										if (cVert > cBleu) {
											pilot.rotate(173);		
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											while(!(Color.getColorID()==6)) {
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();}		
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											Moteurs.pinces.forward();	
											System.exit(0);		// fin du programme
												
										}
										/*
										 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
										 */
										
										if(cVert<cBleu) { 
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											while(!(Color.getColorID()==6)) {
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();}		
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											Moteurs.pinces.forward();	
											System.exit(0);		// fin du programme	
										}}
								/*
								 * si le robot ne detecte rien 
								 * il avance jusqu'a la ligne blanche 
								 */
								else {
									//le robot s'arrete 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									//le robot avance 
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									//le robot avance tant qu'il n'a pas trouver la ligne blanche 
									while (!(Color.getColorID()==6)) {
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
									}
									//le robot s'arrete 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									//le robot avance 
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									//depasse un peut la ligne 
									Delay.msDelay(100);
									//le robot s'arrete 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									/*
									 * mettre le robot sur le bon axe 
									 */
									//tourner le robot sur la ligne blanche 
									Moteurs.moteurDroit.forward();
									while (!(Color.getColorID()==6)) {
										Moteurs.moteurDroit.forward();
									}
									//arreter le robot 
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									//avancer a droite 
									Moteurs.moteurGauche.setPower(60);
							        Moteurs.moteurDroit.setPower(0);
									//le robot tourne jusqu'au gris 
									while(!(Color.getColorID()==9)) {
										Moteurs.moteurDroit.forward();
									}
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									/*
									 * le robot avance sur la ligne blanche jusuq'au mur
									 */
									//tourner a droite 
									Moteurs.moteurGauche.setPower(60);
									Moteurs.moteurDroit.setPower(0);
									while(!CapteurU.Detecter(0.135f))
									{
										
										if(Color.getColorID()==6) {
											Moteurs.moteurGauche.setPower(0); 
											Moteurs.moteurDroit.setPower(60);
										}
										if(Color.getColorID()==9) {
											Moteurs.moteurGauche.setPower(60);
											Moteurs.moteurDroit.setPower(0);	
										}
										/*
										 * le robot se place le long de la ligne noire 
										 */
										//tourne a 90 degré 
										pilot.rotate(90);	
										//avance 
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										Delay.msDelay(500);		// avance un petit peu
										//s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										// tourne a 90 degré
										pilot.rotate(90);		
										//avance 
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										while(!(Color.getColorID()==7)) {
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											}		
										//le robot s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot avance un petit peu pour depasser la ligne noir 
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										Delay.msDelay(50);
										//le robot s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();	
										//avance 
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										Delay.msDelay(100);
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot tourne jusqu'au noir 
										Moteurs.moteurGauche.forward();
										while(!(Color.getColorID()==7)) {
											Moteurs.moteurGauche.forward();
										}
										//le robot s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot tourne jusqu'au gris
										Moteurs.moteurGauche.forward();
										while(!(Color.getColorID()==9)) {
											Moteurs.moteurGauche.forward();
										}
										//le robot s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//suivre la ligne noir jusu'au bleu 
										while(!CapteurU.Detecter(0.135f))
										{
											
											if(Color.getColorID()==7) {
												//tourner a droite 
												Moteurs.moteurGauche.setPower(60);
												Moteurs.moteurDroit.setPower(0);
											}
											if(Color.getColorID()==9) {
												//tourner a gauuche 
												Moteurs.moteurGauche.setPower(0); 
												Moteurs.moteurDroit.setPower(60);

											}
											if(Color.getColorID()==2){
												//le robot s'arrete 
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												break;
											}
											
										}
										//faire une detection  d'objets sur un axe de 360 degré 
										palet=CapteurU.Ultrasonicproche(360f, 0.6f);
										/*
										 * si le robot detecte un palet 
										 */
										if(palet==true) {
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											
											Moteurs.pinces.backward();
											
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											/*
											 * detecter  l'element le plus proche de lui 
											 */
												CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
												/*
												 * arreter le robot 
												 */
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												/*
												 * le robot tourne a 90 degré
												 */
												pilot.rotate(90);
												/*
												 * faire avancer le robot 
												 */
												Moteurs.moteurGauche.forward();
												Moteurs.moteurDroit.forward();
												while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
													{
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();
												}
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												//le robot depasse un peut la ligne blanche
												Moteurs.moteurGauche.forward();
												Moteurs.moteurDroit.forward(); 
												Delay.msDelay(100);
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												/*
												 * mettre le robot sur le bon axe 
												 */
												//tourner le robot sur la ligne blanche 
												Moteurs.moteurDroit.forward();
												while (!(Color.getColorID()==6)) {
													Moteurs.moteurDroit.forward();
												}
												//arreter le robot 
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												//avancer a droite 
												Moteurs.moteurGauche.setPower(60);
										        Moteurs.moteurDroit.setPower(0);
												//le robot tourne jusqu'au gris 
												while(!(Color.getColorID()==9)) {
													Moteurs.moteurDroit.forward();
												}
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												//le robot avance jusqu'au mur pour tourner 
												// avancer le robot  à droite 
												Moteurs.moteurGauche.setPower(60);
										        Moteurs.moteurDroit.setPower(0);
										        while(!CapteurU.Detecter(0.135f)) {
										        	if (Color.getColorID()==6) {
										        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
										                Moteurs.moteurDroit.setPower(60);
										        	}
										        	if (Color.getColorID()==9) {
										        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
										                Moteurs.moteurDroit.setPower(0);
										        	}
										        }
										      //tourner pour eviter le mur
												pilot.rotate(95);
												//le robot avance
												Moteurs.moteurGauche.forward();
												Moteurs.moteurDroit.forward();
												/*
												 * Ceci est pour tester le camp 
												 */
												while(!CapteurU.Detecter(0.15f)) {
													cv++;
													cb++;
													if (Color.getColorID()==2) 
													{cBleu=cb;}
													if(Color.getColorID()==1)//couleur verte *********** 
													{cVert=cv;}
													if(Color.getColorID()==6)
													{break;}
												}
												/*
												 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
												 * tour pour avancer jusqu'au blanc et deposer le palet
												 */
												if (cVert > cBleu) {
													pilot.rotate(173);		
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();
													while(!(Color.getColorID()==6)) {
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();}		
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													Moteurs.pinces.forward();	
													System.exit(0);		// fin du programme
														
												}
												/*
												 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
												 */
												
												if(cVert<cBleu) { 
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();
													while(!(Color.getColorID()==6)) {
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();}		
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													Moteurs.pinces.forward();	
													System.exit(0);		// fin du programme	
												}}
										else {
											//le robot vance 
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											// le robot avance jusqu'a la ligne blanche en suivant la ligne noir 
											while(!CapteurU.Detecter(0.135f))
											{
												
												
												if(Color.getColorID()==7) {//tourner à droite 
													Moteurs.moteurGauche.setPower(60);
													Moteurs.moteurDroit.setPower(0);
												}
												if(Color.getColorID()==9) {//tourner à gauche 
													Moteurs.moteurGauche.setPower(0); 
													Moteurs.moteurDroit.setPower(60);

												}
												if(Color.getColorID()==2) {//tourner à gauche 
													Moteurs.moteurGauche.setPower(0); 
													Moteurs.moteurDroit.setPower(60);

												}
												
												// sortir de la boucle quand le robot rencontre du blanc
												if(Color.getColorID()==6) {
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													break;
												}
														
												
											}
											
											
											pilot.rotate(170); //parcourir la ligne noir de l'autre sens 
											Moteurs.moteurDroit.forward();
											//le robot tourne jusqu'au noir 
											while(!(Color.getColorID()==7)) {
												Moteurs.moteurDroit.forward();
											}
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											//le robot tourne jusqu'au gris
											while (!(Color.getColorID()==9)) {
												Moteurs.moteurDroit.forward();
											}
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											//le robot suit la ligne noire et s'arrete au vert 
											//le robot tourne a droite 
											Moteurs.moteurGauche.setPower(60);
											Moteurs.moteurDroit.setPower(0);
											//suivre la ligne 
											while (CapteurU.Detecter(0.135f)) {
												
												if(Color.getColorID()==7) {//tourner à gauche
													Moteurs.moteurGauche.setPower(0); 
													Moteurs.moteurDroit.setPower(60);
												}
												if(Color.getColorID()==9) {//tourner à droite
													Moteurs.moteurGauche.setPower(60);
													Moteurs.moteurDroit.setPower(0);
												}
													
												
												
												// sortir de la boucle quand le robot va rencontrer du vert
												if(Color.getColorID()==1) {
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													break;
												}
											}
											//faire une detection 
											palet=CapteurU.Ultrasonicproche(360f, 0.6f);
											/*
											 * si le robot detecte un palet 
											 */
											if(palet==true) {
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												
												Moteurs.pinces.backward();
												
												Moteurs.moteurGauche.stop();
												Moteurs.moteurDroit.stop();
												/*
												 * detecter  l'element le plus proche de lui 
												 */
													CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
													/*
													 * arreter le robot 
													 */
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													/*
													 * le robot tourne a 90 degré
													 */
													pilot.rotate(90);
													/*
													 * faire avancer le robot 
													 */
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();
													while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
														{
														Moteurs.moteurGauche.forward();
														Moteurs.moteurDroit.forward();
													}
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													//le robot depasse un peut la ligne blanche
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward(); 
													Delay.msDelay(100);
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													/*
													 * mettre le robot sur le bon axe 
													 */
													//tourner le robot sur la ligne blanche 
													Moteurs.moteurDroit.forward();
													while (!(Color.getColorID()==6)) {
														Moteurs.moteurDroit.forward();
													}
													//arreter le robot 
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													//avancer a droite 
													Moteurs.moteurGauche.setPower(60);
											        Moteurs.moteurDroit.setPower(0);
													//le robot tourne jusqu'au gris 
													while(!(Color.getColorID()==9)) {
														Moteurs.moteurDroit.forward();
													}
													Moteurs.moteurGauche.stop();
													Moteurs.moteurDroit.stop();
													//le robot avance jusqu'au mur pour tourner 
													// avancer le robot  a droite 
													Moteurs.moteurGauche.setPower(60);
											        Moteurs.moteurDroit.setPower(0);
											        while(!CapteurU.Detecter(0.135f)) {
											        	if (Color.getColorID()==6) {
											        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
											                Moteurs.moteurDroit.setPower(60);
											        	}
											        	if (Color.getColorID()==9) {
											        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
											                Moteurs.moteurDroit.setPower(0);
											        	}
											        }
											      //tourner pour eviter le mur
													pilot.rotate(95);
													//le robot avance
													Moteurs.moteurGauche.forward();
													Moteurs.moteurDroit.forward();
													/*
													 * Ceci est pour tester le camp 
													 */
													while(!CapteurU.Detecter(0.15f)) {
														cv++;
														cb++;
														if (Color.getColorID()==2) 
														{cBleu=cb;}
														if(Color.getColorID()==1)//couleur verte *********** 
														{cVert=cv;}
														if(Color.getColorID()==6)
														{break;}
													}
													/*
													 * si le compteur pour le vert est plus grand c'est qu'il est du mauvais coté et donc le robot fait demi 
													 * tour pour avancer jusqu'au blanc et deposer le palet
													 */
													if (cVert > cBleu) {
														pilot.rotate(173);		
														Moteurs.moteurGauche.forward();
														Moteurs.moteurDroit.forward();
														while(!(Color.getColorID()==6)) {
														Moteurs.moteurGauche.forward();
														Moteurs.moteurDroit.forward();}		
														Moteurs.moteurGauche.stop();
														Moteurs.moteurDroit.stop();
														Moteurs.pinces.forward();	
														System.exit(0);		// fin du programme
															
													}
													/*
													 * si le compteur pour le bleu est plus grand c'est qu'il est du bon coté
													 */
													
													if(cVert<cBleu) { 
														Moteurs.moteurGauche.forward();
														Moteurs.moteurDroit.forward();
														while(!(Color.getColorID()==6)) {
														Moteurs.moteurGauche.forward();
														Moteurs.moteurDroit.forward();}		
														Moteurs.moteurGauche.stop();
														Moteurs.moteurDroit.stop();
														Moteurs.pinces.forward();	
														System.exit(0);		// fin du programme	
													}}
											
										}
										
										
										
										
									}	
					}
				}
			}
		}
			if(button==8) {
				Moteurs.pinces.forward();//ouvrir les pinces 
				CapteurU.Ultrasonicproche(360f,2.54f);
				//avancer le robot 
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				//chercher sur un axe de 360 degré s'il y a un objet autour du robot 
				palet=CapteurU.Ultrasonicproche(360f,2.54f);
				/*
				 * si le robot detecte le palet des la premiere fois 
				 */
				if (palet==true) {
					/*
					 * arreter le robot 
					 */
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * fermer les pinces
					 */
					Moteurs.pinces.backward();
					/*
					 * arreter le robot
					 */
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
				/*
				 * detecter  l'element le plus proche de lui 
				 */
					CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
					/*
					 * arreter le robot 
					 */
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * le robot tourne a 90 degré
					 */
					pilot.rotate(90);
					/*
					 * faire avancer le robot 
					 */
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
						{
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
					}
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//le robot depasse un peut la ligne blanche
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward(); 
					Delay.msDelay(100);
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * mettre le robot sur le bon axe 
					 */
					//tourner le robot sur la ligne blanche 
					Moteurs.moteurDroit.forward();
					while (!(Color.getColorID()==6)) {
						Moteurs.moteurDroit.forward();
					}
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//avancer a droite 
					Moteurs.moteurGauche.setPower(60);
			        Moteurs.moteurDroit.setPower(0);
					//le robot tourne jusqu'au gris 
					while(!(Color.getColorID()==9)) {
						Moteurs.moteurDroit.forward();
					}
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//le robot avance jusqu'au mur pour tourner 
					// avancer le robot  a droite 
					Moteurs.moteurGauche.setPower(60);
			        Moteurs.moteurDroit.setPower(0);
			        while(!CapteurU.Detecter(0.135f)) {
			        	//suivre la ligne blanche 
			        	if (Color.getColorID()==6) {
			        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
			                Moteurs.moteurDroit.setPower(60);
			        	}
			        	if (Color.getColorID()==9) {
			        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
			                Moteurs.moteurDroit.setPower(0);
			        	}
			        }
			        //tourner pour eviter le mur
					pilot.rotate(95);
					//le robot avance
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					/*
					 * Ceci est pour tester le camp 
					 */
		while(!CapteurU.Detecter(0.15f)) {
		cv++;
		cb++;
		if (Color.getColorID()==2) 
		{cBleu=cb;}
		if(Color.getColorID()==1)//couleur verte *********** 
		{cVert=cv;}
		if(Color.getColorID()==6)
		{break;}
		}
		/*
		* si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
		* tour pour avancer jusqu'au blanc et deposer le palet
		*/
		if (cVert < cBleu) {
		pilot.rotate(173);		
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();
		while(!(Color.getColorID()==6)) {
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();}		
		Moteurs.moteurGauche.stop();
		Moteurs.moteurDroit.stop();
		Moteurs.pinces.forward();	
		System.exit(0);		// fin du programme
			
		}
		/*
		* si le compteur pour le bleu est plus petit c'est qu'il est du bon coté
		*/
		if(cVert>cBleu) { 
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();
		while(!(Color.getColorID()==6)) {
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();}		
		Moteurs.moteurGauche.stop();
		Moteurs.moteurDroit.stop();
		Moteurs.pinces.forward();	
		System.exit(0);		// fin du programme	
		}
					
				}
				else {
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//tourner pour eviter le mur 
					pilot.rotate(90);		
					/*
					 * mettre le robot sur le bon axe 
					 */
					//tourner le robot sur la ligne blanche 
					Moteurs.moteurDroit.forward();
					while (!(Color.getColorID()==6)) {
						Moteurs.moteurDroit.forward();
					}
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//avancer a droite 
					Moteurs.moteurGauche.setPower(60);
			        Moteurs.moteurDroit.setPower(0);
					//le robot tourne jusqu'au gris 
					while(!(Color.getColorID()==9)) {
						Moteurs.moteurDroit.forward();
					}
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * le robot avance sur la ligne blanche tant qu'il ne rencontre pas un palet ou un mur 
					 */
					//tourner a droite 
					Moteurs.moteurGauche.setPower(60);
					Moteurs.moteurDroit.setPower(0);
					while(!CapteurU.Detecter(0.19f))
					{
						
						if(Color.getColorID()==6) {
							Moteurs.moteurGauche.setPower(0); 
							Moteurs.moteurDroit.setPower(60);
						}
						if(Color.getColorID()==9) {
							Moteurs.moteurGauche.setPower(60);
							Moteurs.moteurDroit.setPower(0);	
						}
						/*
						 * Si le capteur tactile a été pressé , fait le test de camp  pour pouvoir deposer le palet
						 */
						if(touch.estPresse()) {
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							
							Moteurs.pinces.backward();
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//il avance un peut pour se decaler du mur 
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							
							Delay.msDelay(1000);
							
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();		
							//il tourne a 90
							pilot.rotate(90);		
							//il avance
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							/*
							 * Ceci est pour tester le camp 
							 */
			while(!CapteurU.Detecter(0.15f)) {
				cv++;
				cb++;
				if (Color.getColorID()==2) 
				{cBleu=cb;}
				if(Color.getColorID()==1)//couleur verte *********** 
				{cVert=cv;}
				if(Color.getColorID()==6)
				{break;}
			}
			/*
			 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
			 * tour pour avancer jusqu'au blanc et deposer le palet
			 */
			if (cVert < cBleu) {
				pilot.rotate(173);		
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				while(!(Color.getColorID()==6)) {
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();}		
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				Moteurs.pinces.forward();	
				System.exit(0);		// fin du programme
					
			}
			/*
			 * si le compteur pour le bleu est plus ^petit c'est qu'il est du bon coté
			 */
			
			if(cVert>cBleu) { 
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				while(!(Color.getColorID()==6)) {
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();}		
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				Moteurs.pinces.forward();	
				System.exit(0);		// fin du programme	
			}
							
							
						}
				}
					}
				/*
				 * le robot arrive jusqu'au mur , tourne et avance jusqu'a la ligne noir 
				 */
				pilot.rotate(90);		// il tourne a 90 degré
				//le robot avance 
				Moteurs.moteurGauche.forward();
				Moteurs.moteurDroit.forward();
				//avancer jusqu'au noir 
				while(!(Color.getColorID()==7)) {
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					}		
				
				//le robot s'arrete 
				Moteurs.moteurGauche.stop();
				Moteurs.moteurDroit.stop();
				//detection sur un axe de 180
				palet=CapteurU.Ultrasonicproche(180, 1.1f);
				if(palet==true) {
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					
					Moteurs.pinces.backward();
					
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * detecter  l'element le plus proche de lui 
					 */
						CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
						/*
						 * arreter le robot 
						 */
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						/*
						 * le robot tourne a 90 degré
						 */
						pilot.rotate(90);
						/*
						 * faire avancer le robot 
						 */
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						
						while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
							{
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
						}
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						//le robot depasse un peut la ligne blanche
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward(); 
						Delay.msDelay(100);
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						/*
						 * mettre le robot sur le bon axe 
						 */
						//tourner le robot sur la ligne blanche 
						Moteurs.moteurDroit.forward();
						while (!(Color.getColorID()==6)) {
							Moteurs.moteurDroit.forward();
						}
						//arreter le robot 
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						//avancer a droite 
						Moteurs.moteurGauche.setPower(60);
				        Moteurs.moteurDroit.setPower(0);
						//le robot tourne jusqu'au gris 
						while(!(Color.getColorID()==9)) {
							Moteurs.moteurDroit.forward();
						}
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						//le robot avance jusqu'au mur pour tourner 
						// avancer le robot  a droite 
						Moteurs.moteurGauche.setPower(60);
				        Moteurs.moteurDroit.setPower(0);
				        while(!CapteurU.Detecter(0.135f)) {
				        	if (Color.getColorID()==6) {
				        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
				                Moteurs.moteurDroit.setPower(60);
				        	}
				        	if (Color.getColorID()==9) {
				        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
				                Moteurs.moteurDroit.setPower(0);
				        	}
				        }
				        //tourner pour eviter le mur
						pilot.rotate(95);
						//le robot avance
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						/*
						 * Ceci est pour tester le camp 
						 */
		while(!CapteurU.Detecter(0.15f)) {
			cv++;
			cb++;
			if (Color.getColorID()==2) 
			{cBleu=cb;}
			if(Color.getColorID()==1)//couleur verte  
			{cVert=cv;}
			if(Color.getColorID()==6)
			{break;}
		}
		/*
		 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
		 * tour pour avancer jusqu'au blanc et deposer le palet
		 */
		if (cVert < cBleu) {
			pilot.rotate(173);		
			Moteurs.moteurGauche.forward();
			Moteurs.moteurDroit.forward();
			while(!(Color.getColorID()==6)) {
			Moteurs.moteurGauche.forward();
			Moteurs.moteurDroit.forward();}		
			Moteurs.moteurGauche.stop();
			Moteurs.moteurDroit.stop();
			Moteurs.pinces.forward();	
			System.exit(0);		// fin du programme
				
		}
		/*
		 * si le compteur pour le bleu est plus ^petit c'est qu'il est du bon coté
		 */
			if(cVert>cBleu) { 
			Moteurs.moteurGauche.forward();
			Moteurs.moteurDroit.forward();
			while(!(Color.getColorID()==6)) {
			Moteurs.moteurGauche.forward();
			Moteurs.moteurDroit.forward();}		
			Moteurs.moteurGauche.stop();
			Moteurs.moteurDroit.stop();
			Moteurs.pinces.forward();	
			System.exit(0);		// fin du programme	
		}}
				/*
				 * si le robot n'a pas detecter de palet 
				 */
				else {
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//tourner pour eviter le mur 
					pilot.rotate(90);		
					/*
					 * mettre le robot sur le bon axe 
					 */
					//tourner le robot sur la ligne blanche 
					Moteurs.moteurDroit.forward();
					while (!(Color.getColorID()==6)) {
						Moteurs.moteurDroit.forward();
					}
					//arreter le robot 
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//avancer a droite 
					Moteurs.moteurGauche.setPower(60);
			        Moteurs.moteurDroit.setPower(0);
					//le robot tourne jusqu'au gris 
					while(!(Color.getColorID()==9)) {
						Moteurs.moteurDroit.forward();
					}
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					/*
					 * le robot avance sur la ligne blanche tant qu'il ne rencontre pas un palet ou un mur 
					 */
					//tourner a droite 
					Moteurs.moteurGauche.setPower(60);
					Moteurs.moteurDroit.setPower(0);
					while(!CapteurU.Detecter(0.19f))
					{
						
						if(Color.getColorID()==6) {
							Moteurs.moteurGauche.setPower(0); 
							Moteurs.moteurDroit.setPower(60);
						}
						if(Color.getColorID()==9) {
							Moteurs.moteurGauche.setPower(60);
							Moteurs.moteurDroit.setPower(0);	
						}
			
}
					}
				/*
				 * Si le capteur tactile a été pressé , le robot detecte le camp adverse pour pouvoir deposer le palet
				 */
				if(touch.estPresse()) {
					
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					
					Moteurs.pinces.backward();
					
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();
					//il avance un peut pour se decaler du mur 
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					
					Delay.msDelay(1000);
					
					Moteurs.moteurGauche.stop();
					Moteurs.moteurDroit.stop();		
					//il tourne a 90
					pilot.rotate(90);		
					//il avance
					Moteurs.moteurGauche.forward();
					Moteurs.moteurDroit.forward();
					/*
					 * Ceci est pour tester le camp 
					 */
	while(!CapteurU.Detecter(0.15f)) {
		cv++;
		cb++;
		if (Color.getColorID()==2) 
		{cBleu=cb;}
		if(Color.getColorID()==1)//couleur verte *********** 
		{cVert=cv;}
		if(Color.getColorID()==6)
		{break;}
	}
	/*
	 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
	 * tour pour avancer jusqu'au blanc et deposer le palet
	 */
	if (cVert < cBleu) {
		pilot.rotate(173);		
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();
		while(!(Color.getColorID()==6)) {
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();}		
		Moteurs.moteurGauche.stop();
		Moteurs.moteurDroit.stop();
		Moteurs.pinces.forward();	
		System.exit(0);		// fin du programme
			
	}
	/*
	 * si le compteur pour le bleu est plus petit c'est qu'il est du bon coté
	 */
	
	if(cVert>cBleu) { 
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();
		while(!(Color.getColorID()==6)) {
		Moteurs.moteurGauche.forward();
		Moteurs.moteurDroit.forward();}		
		Moteurs.moteurGauche.stop();
		Moteurs.moteurDroit.stop();
		Moteurs.pinces.forward();	
		System.exit(0);		// fin du programme	
	}
				}
				/*
				 * il arrive au mur 
				 * le robot tourne a 90 et avance jusqu'a la ligne noir 
				 */
						
						pilot.rotate(90);		// il tourne a 90 degré
						//le robot avance 
						Moteurs.moteurGauche.forward();
						Moteurs.moteurDroit.forward();
						while(!(Color.getColorID()==7)) {//le robot avance jusqu'au noir
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							}		
						//le robot s'arrete 
						Moteurs.moteurGauche.stop();
						Moteurs.moteurDroit.stop();
						//effectue une detection sur 180 degré 
						palet=CapteurU.Ultrasonicproche(180, 1.1f); 
						if(palet==true) {
							//le robot s'arrete 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//attrape le palet 
							Moteurs.pinces.backward();
							//s'arrete 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							/*
							 * detecter  l'element le plus proche de lui 
							 */
								CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
								/*
								 * arreter le robot 
								 */
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								/*
								 * le robot tourne a 90 degré
								 */
								pilot.rotate(90);
								/*
								 * faire avancer le robot 
								 */
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
									{
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
								}
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//le robot depasse un peut la ligne blanche
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward(); 
								Delay.msDelay(100);
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								/*
								 * mettre le robot sur le bon axe 
								 */
								//tourner le robot sur la ligne blanche 
								Moteurs.moteurDroit.forward();
								while (!(Color.getColorID()==6)) {
									Moteurs.moteurDroit.forward();
								}
								//arreter le robot 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//avancer a droite 
								Moteurs.moteurGauche.setPower(60);
						        Moteurs.moteurDroit.setPower(0);
								//le robot tourne jusqu'au gris 
								while(!(Color.getColorID()==9)) {
									Moteurs.moteurDroit.forward();
								}
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//le robot avance jusqu'au mur pour tourner 
								// avancer le robot  a droite 
								Moteurs.moteurGauche.setPower(60);
						        Moteurs.moteurDroit.setPower(0);
						        while(!CapteurU.Detecter(0.135f)) {
						        	if (Color.getColorID()==6) {
						        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
						                Moteurs.moteurDroit.setPower(60);
						        	}
						        	if (Color.getColorID()==9) {
						        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
						                Moteurs.moteurDroit.setPower(0);
						        	}
						        }
						        //tourner pour eviter le mur
								pilot.rotate(95);
								//le robot avance
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								/*
				    			 * Ceci est pour tester le camp 
				    			 */
				    			while(!CapteurU.Detecter(0.15f)) {
				    				cv++;
				    				cb++;
				    				if (Color.getColorID()==2) 
				    				{cBleu=cb;}
				    				if(Color.getColorID()==1)//couleur verte *********** 
				    				{cVert=cv;}
				    				if(Color.getColorID()==6)
				    				{break;}
				    			}
				    			/*
				    			 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
				    			 * tour pour avancer jusqu'au blanc et deposer le palet
				    			 */
				    			if (cVert < cBleu) {
				    				pilot.rotate(173);		
				    				Moteurs.moteurGauche.forward();
				    				Moteurs.moteurDroit.forward();
				    				while(!(Color.getColorID()==6)) {
				    				Moteurs.moteurGauche.forward();
				    				Moteurs.moteurDroit.forward();}		
				    				Moteurs.moteurGauche.stop();
				    				Moteurs.moteurDroit.stop();
				    				Moteurs.pinces.forward();	
				    				System.exit(0);		// fin du programme
				    					
				    			}
				    			/*
				    			 * si le compteur pour le bleu est plus ^petit c'est qu'il est du bon coté
				    			 */
				    			
				    			if(cVert>cBleu) { 
				    				Moteurs.moteurGauche.forward();
				    				Moteurs.moteurDroit.forward();
				    				while(!(Color.getColorID()==6)) {
				    				Moteurs.moteurGauche.forward();
				    				Moteurs.moteurDroit.forward();}		
				    				Moteurs.moteurGauche.stop();
				    				Moteurs.moteurDroit.stop();
				    				Moteurs.pinces.forward();	
				    				System.exit(0);		// fin du programme	
				    			}}
						/*
						 * si le robot ne detecte rien 
						 * il avance jusqu'a la ligne blanche 
						 */
						else {
							//le robot s'arrete 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//le robot avance 
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							//le robot avance tant qu'il n'a pas trouver la ligne blanche 
							while (!(Color.getColorID()==6)) {
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
							}
							//le robot s'arrete 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//le robot avance 
							Moteurs.moteurGauche.forward();
							Moteurs.moteurDroit.forward();
							//depasse un peut la ligne 
							Delay.msDelay(100);
							//le robot s'arrete 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							/*
							 * mettre le robot sur le bon axe 
							 */
							//tourner le robot sur la ligne blanche 
							Moteurs.moteurDroit.forward();
							while (!(Color.getColorID()==6)) {
								Moteurs.moteurDroit.forward();
							}
							//arreter le robot 
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							//avancer a droite 
							Moteurs.moteurGauche.setPower(60);
					        Moteurs.moteurDroit.setPower(0);
							//le robot tourne jusqu'au gris 
							while(!(Color.getColorID()==9)) {
								Moteurs.moteurDroit.forward();
							}
							Moteurs.moteurGauche.stop();
							Moteurs.moteurDroit.stop();
							/*
							 * le robot avance sur la ligne blanche jusuq'au mur
							 */
							//tourner a droite 
							Moteurs.moteurGauche.setPower(60);
							Moteurs.moteurDroit.setPower(0);
							while(!CapteurU.Detecter(0.135f))
							{
								
								if(Color.getColorID()==6) {
									Moteurs.moteurGauche.setPower(0); 
									Moteurs.moteurDroit.setPower(60);
								}
								if(Color.getColorID()==9) {
									Moteurs.moteurGauche.setPower(60);
									Moteurs.moteurDroit.setPower(0);	
								}
								/*
								 * le robot se place le long de la ligne noire 
								 */
								//tourne a 90 degré 
								pilot.rotate(90);	
								//avance 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								Delay.msDelay(500);		// avance un petit peu
								//s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								// tourne a 90 degré
								pilot.rotate(90);		
								//avance 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								while(!(Color.getColorID()==7)) {
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									}		
								//le robot s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//le robot avance un petit peu pour depasser la ligne noir 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								Delay.msDelay(50);
								//le robot s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();	
								//avance 
								Moteurs.moteurGauche.forward();
								Moteurs.moteurDroit.forward();
								Delay.msDelay(100);
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//le robot tourne jusqu'au noir 
								Moteurs.moteurGauche.forward();
								while(!(Color.getColorID()==7)) {
									Moteurs.moteurGauche.forward();
								}
								//le robot s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//le robot tourne jusqu'au gris
								Moteurs.moteurGauche.forward();
								while(!(Color.getColorID()==9)) {
									Moteurs.moteurGauche.forward();
								}
								//le robot s'arrete 
								Moteurs.moteurGauche.stop();
								Moteurs.moteurDroit.stop();
								//suivre la ligne noir jusu'au bleu 
								while(!CapteurU.Detecter(0.135f))
								{
									
									if(Color.getColorID()==7) {
										//tourner a droite 
										Moteurs.moteurGauche.setPower(60);
										Moteurs.moteurDroit.setPower(0);
									}
									if(Color.getColorID()==9) {
										//tourner a gauuche 
										Moteurs.moteurGauche.setPower(0); 
										Moteurs.moteurDroit.setPower(60);

									}
									if(Color.getColorID()==2){
										//le robot s'arrete 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										break;
									}
									
								}
								//faire une detection 
								palet=CapteurU.Ultrasonicproche(360f, 0.6f);
								/*
								 * si le robot detecte un palet 
								 */
								if(palet==true) {
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									
									Moteurs.pinces.backward();
									
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									/*
									 * detecter  l'element le plus proche de lui 
									 */
										CapteurU.Ultrasonicproche(360f,2.54f);
										/*
										 * arreter le robot 
										 */
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										/*
										 * le robot tourne a 90 degré
										 */
										pilot.rotate(90);
										/*
										 * faire avancer le robot 
										 */
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										/*
										 * 
										 */
										while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
											{
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
										}
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot depasse un peut la ligne blanche
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward(); 
										Delay.msDelay(100);
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										/*
										 * mettre le robot sur le bon axe 
										 */
										//tourner le robot sur la ligne blanche 
										Moteurs.moteurDroit.forward();
										while (!(Color.getColorID()==6)) {
											Moteurs.moteurDroit.forward();
										}
										//arreter le robot 
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//avancer a droite 
										Moteurs.moteurGauche.setPower(60);
								        Moteurs.moteurDroit.setPower(0);
										//le robot tourne jusqu'au gris 
										while(!(Color.getColorID()==9)) {
											Moteurs.moteurDroit.forward();
										}
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										//le robot avance jusqu'au mur pour tourner 
										// avancer le robot  a droite 
										Moteurs.moteurGauche.setPower(60);
								        Moteurs.moteurDroit.setPower(0);
								        while(!CapteurU.Detecter(0.135f)) {
								        	if (Color.getColorID()==6) {
								        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
								                Moteurs.moteurDroit.setPower(60);
								        	}
								        	if (Color.getColorID()==9) {
								        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
								                Moteurs.moteurDroit.setPower(0);
								        	}
								        }
								      //tourner pour eviter le mur
										pilot.rotate(95);
										//le robot avance
										Moteurs.moteurGauche.forward();
										Moteurs.moteurDroit.forward();
										/*
						    			 * Ceci est pour tester le camp 
						    			 */
						    			while(!CapteurU.Detecter(0.15f)) {
						    				cv++;
						    				cb++;
						    				if (Color.getColorID()==2) 
						    				{cBleu=cb;}
						    				if(Color.getColorID()==1)//couleur verte *********** 
						    				{cVert=cv;}
						    				if(Color.getColorID()==6)
						    				{break;}
						    			}
						    			/*
						    			 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
						    			 * tour pour avancer jusqu'au blanc et deposer le palet
						    			 */
						    			if (cVert < cBleu) {
						    				pilot.rotate(173);		
						    				Moteurs.moteurGauche.forward();
						    				Moteurs.moteurDroit.forward();
						    				while(!(Color.getColorID()==6)) {
						    				Moteurs.moteurGauche.forward();
						    				Moteurs.moteurDroit.forward();}		
						    				Moteurs.moteurGauche.stop();
						    				Moteurs.moteurDroit.stop();
						    				Moteurs.pinces.forward();	
						    				System.exit(0);		// fin du programme
						    					
						    			}
						    			/*
						    			 * si le compteur pour le bleu est plus petit c'est qu'il est du bon coté
						    			 */
						    			
						    			if(cVert>cBleu) { 
						    				Moteurs.moteurGauche.forward();
						    				Moteurs.moteurDroit.forward();
						    				while(!(Color.getColorID()==6)) {
						    				Moteurs.moteurGauche.forward();
						    				Moteurs.moteurDroit.forward();}		
						    				Moteurs.moteurGauche.stop();
						    				Moteurs.moteurDroit.stop();
						    				Moteurs.pinces.forward();	
						    				System.exit(0);		// fin du programme	
						    			}}
								else {
									//le robot 	avance 
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									// le robot avance jusqu'a la ligne blanche en suivant la ligne noir 
									while(!CapteurU.Detecter(0.135f))
									{
										
										
										if(Color.getColorID()==7) {//tourner a droite 
											Moteurs.moteurGauche.setPower(60);
											Moteurs.moteurDroit.setPower(0);
										}
										if(Color.getColorID()==9) {//tourner a gauche 
											Moteurs.moteurGauche.setPower(0); 
											Moteurs.moteurDroit.setPower(60);

										}
										if(Color.getColorID()==2) {//tourner a gauche 
											Moteurs.moteurGauche.setPower(0); 
											Moteurs.moteurDroit.setPower(60);

										}
										
										// sortir de la boucle quand le robot rencontre du blanc
										if(Color.getColorID()==6) {
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											break;
										}
												
										
									}
									
									
									pilot.rotate(170); //parcourir la ligne noir de l'autre sens 
									Moteurs.moteurDroit.forward();
									//le robot tourne jusqu'au noir 
									while(!(Color.getColorID()==7)) {
										Moteurs.moteurDroit.forward();
									}
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									
									Moteurs.moteurGauche.forward();
									Moteurs.moteurDroit.forward();
									//le robot tourne jusqu'au gris
									while (!(Color.getColorID()==9)) {
										Moteurs.moteurDroit.forward();
									}
									Moteurs.moteurGauche.stop();
									Moteurs.moteurDroit.stop();
									//le robot suit la ligne noire et s'arrete au vert 
									//le robot tourne a droite 
									Moteurs.moteurGauche.setPower(60);
									Moteurs.moteurDroit.setPower(0);
									//suivre la ligne 
									while (CapteurU.Detecter(0.135f)) {
										
										if(Color.getColorID()==7) {//tourner a gauche
											Moteurs.moteurGauche.setPower(0); 
											Moteurs.moteurDroit.setPower(60);
										}
										if(Color.getColorID()==9) {//tourner a droite
											Moteurs.moteurGauche.setPower(60);
											Moteurs.moteurDroit.setPower(0);
										}
											
										
										
										// sortir de la boucle quand le robot va rencontrer du vert
										if(Color.getColorID()==1) {
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											break;
										}
									}
									//faire une detection sur un axe de 360 degré 
									palet=CapteurU.Ultrasonicproche(360f, 0.6f);
									/*
									 * si le robot detecte un palet 
									 */
									if(palet==true) {
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										
										Moteurs.pinces.backward();
										
										Moteurs.moteurGauche.stop();
										Moteurs.moteurDroit.stop();
										/*
										 * detecter  l'element le plus proche de lui 
										 */
											CapteurU.Ultrasonicproche(360f,2.54f);//le robot redetecte l'objet le plus proche de lui, ça sera un mur car le palet il sera deja saisi 
											/*
											 * arreter le robot 
											 */
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											/*
											 * le robot tourne a 90 degré
											 */
											pilot.rotate(90);
											/*
											 * faire avancer le robot 
											 */
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											
											while(Color.getColorID()!=6)//tant que la couleur detectée est differente du blanc on fait avancer le robot 
												{
												Moteurs.moteurGauche.forward();
												Moteurs.moteurDroit.forward();
											}
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											//le robot depasse un peut la ligne blanche
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward(); 
											Delay.msDelay(100);
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											/*
											 * mettre le robot sur le bon axe 
											 */
											//tourner le robot sur la ligne blanche 
											Moteurs.moteurDroit.forward();
											while (!(Color.getColorID()==6)) {
												Moteurs.moteurDroit.forward();
											}
											//arreter le robot 
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											//avancer a droite 
											Moteurs.moteurGauche.setPower(60);
									        Moteurs.moteurDroit.setPower(0);
											//le robot tourne jusqu'au gris 
											while(!(Color.getColorID()==9)) {
												Moteurs.moteurDroit.forward();
											}
											Moteurs.moteurGauche.stop();
											Moteurs.moteurDroit.stop();
											//le robot avance jusqu'au mur pour tourner 
											// avancer le robot  a droite 
											Moteurs.moteurGauche.setPower(60);
									        Moteurs.moteurDroit.setPower(0);
									        while(!CapteurU.Detecter(0.135f)) {
									        	if (Color.getColorID()==6) {
									        		Moteurs.moteurGauche.setPower(0);//tourner vers la gauche 
									                Moteurs.moteurDroit.setPower(60);
									        	}
									        	if (Color.getColorID()==9) {
									        		Moteurs.moteurGauche.setPower(60);//tourner vers la droite 
									                Moteurs.moteurDroit.setPower(0);
									        	}
									        }
									      //tourner pour eviter le mur
											pilot.rotate(95);
											//le robot avance
											Moteurs.moteurGauche.forward();
											Moteurs.moteurDroit.forward();
											/*
							    			 * Ceci est pour tester le camp 
							    			 */
							    			while(!CapteurU.Detecter(0.15f)) {
							    				cv++;
							    				cb++;
							    				if (Color.getColorID()==2) 
							    				{cBleu=cb;}
							    				if(Color.getColorID()==1)//couleur verte *********** 
							    				{cVert=cv;}
							    				if(Color.getColorID()==6)
							    				{break;}
							    			}
							    			/*
							    			 * si le compteur pour le vert est plus petit c'est qu'il est du mauvais coté et donc le robot fait demi 
							    			 * tour pour avancer jusqu'au blanc et deposer le palet
							    			 */
							    			if (cVert < cBleu) {
							    				pilot.rotate(173);		
							    				Moteurs.moteurGauche.forward();
							    				Moteurs.moteurDroit.forward();
							    				while(!(Color.getColorID()==6)) {
							    				Moteurs.moteurGauche.forward();
							    				Moteurs.moteurDroit.forward();}		
							    				Moteurs.moteurGauche.stop();
							    				Moteurs.moteurDroit.stop();
							    				Moteurs.pinces.forward();	
							    				System.exit(0);		// fin du programme
							    					
							    			}
							    			/*
							    			 * si le compteur pour le bleu est plus ^petit c'est qu'il est du bon coté
							    			 */
							    			//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
							    			if(cVert>cBleu) { 
							    				Moteurs.moteurGauche.forward();
							    				Moteurs.moteurDroit.forward();
							    				while(!(Color.getColorID()==6)) {
							    				Moteurs.moteurGauche.forward();
							    				Moteurs.moteurDroit.forward();}		
							    				Moteurs.moteurGauche.stop();
							    				Moteurs.moteurDroit.stop();
							    				Moteurs.pinces.forward();	
							    				System.exit(0);		// fin du programme	
							    			}
							    			}
				
				
}
								}
							}
						}
			}
}
