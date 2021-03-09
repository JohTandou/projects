package scenario3;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Moteurs
{
	//Description: concerne les trois moteurs et contient les fonctions permettant la mise en mouvement du robot
	//Utilisée par: Main, CapteurUltrason
	//Utilise: CapteurPhotosensible, CapteurTactile, CapteurUltrason
	
	static UnregulatedMotor moteurGauche = new UnregulatedMotor (MotorPort.A); // Création de l'objet moteurGauche
    static UnregulatedMotor moteurDroit = new UnregulatedMotor (MotorPort.B); // Création de l'objet moteurDroit
    static UnregulatedMotor pinces = new UnregulatedMotor (MotorPort.C); // Création de l'objet pinces
    static final int aucune = -1; // Correspondaces couleur <-> id couleur utiles
    static final int rouge=0;
    static final int vert=1;
    static final int bleu=2;
    static final int jaune=3;
    static final int blanc=6;
    static final int noir=7;
    static final int gris=9;
    static final int scenario3 = -3; // Correspondent à un cas isolé dans allerEmplacement, pour faire aller le robot à l'emplacement 1 à partir de n'importe zone grise de la table
	static final int scenario4 = -4;
	
	public static boolean avancer1()
	{
		//Description: le robot avance droit jusqu'à ce qu'il détecte le vide ou une ligne
		//Entrée: rien
		//Sortie: booléen (pression) indiquant si un palet a été détecté ou non durant le déplacement du robot
		
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		boolean pression = false; // Indique si le capteur tactile est pressé ou non (initialisée à false)
		couleur = CapteurPhotosensible.obtenirIDCouleur();
		while (couleur != aucune && couleur != rouge && couleur != vert && couleur != bleu && couleur != jaune && couleur != blanc && couleur != noir) // Boucle qui s'arrête lorsqu'aucune couleur n'est détectée ou lorsque la couleur détectée est celle d'une ligne
		{
			moteurGauche.setPower(40);
			moteurDroit.setPower(40);
			if (pression == false) // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
			{
				pression = CapteurTactile.estPresse();
			}
			couleur = CapteurPhotosensible.obtenirIDCouleur();
		}
		return pression;
	}
	
	// CREEE POUR LES INTERSECTIONS COUVERTES
    public static boolean avancer2(int a)
	{
		//Description: le robot suit une ligne à allure rapide jusqu'il détecte une couleur donnée
    	//Entrée: entier (a) étant la couleur d'arrêt
    	//Sortie: booléen (pression) indiquant si un palet a été détecté ou non durant le déplacement du robot
    	
    	int couleur; // Variable qui va contenir l'id de la couleur détectée
		int ligne; // Variable qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
		int objet=0;
		float distance;
		boolean pression = false; // Indique si le capteur tactile est pressé ou non (initialisée à false)
		if (a != rouge && a != vert && a != bleu && a != jaune && a != blanc && a != noir) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
		{
			System.out.println("Valeur de a incorrecte");
		}
		else // Sinon, la fonction fait son travail
		{
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			ligne = couleur;
			while (couleur != aucune && couleur != a) // Boucle qui s'arrête lorsque la couleur détectée est celle d'arrêt
			{
				while (couleur != aucune && couleur != gris && couleur != a) // Tant que la couleur détectée est différente du gris et de celle d'arrêt, le robot dévie vers la gauche 
				{
					if (objet == 0)
					{
						objet = CapteurUltrason.detecterObjet();
					}
					if (objet == 2)
					{
						distance = CapteurUltrason.obtenirDistance();
						if (distance <= 0.3)
						{
							while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
							{
								moteurGauche.setPower(60);
								moteurDroit.setPower(0);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							reculer1();
							moteurGauche.setPower(0);
							moteurDroit.setPower(0);
							Delay.msDelay(500);
						}
					}
					moteurGauche.setPower(80);
					moteurDroit.setPower(100);
					if (pression == false) // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
					{
						pression = CapteurTactile.estPresse();
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
				}
				while (couleur != aucune && couleur != ligne && couleur != a) // Tant que la couleur détectée est différente de celle de la ligne et de celle d'arrêt, le robot dévie vers la droite
				{
					if (objet == 0)
					{
						objet = CapteurUltrason.detecterObjet();
					}
					if (objet == 2)
					{
						distance = CapteurUltrason.obtenirDistance();
						if (distance <= 0.2)
						{
							while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
							{
								moteurGauche.setPower(60);
								moteurDroit.setPower(0);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							reculer1();
							moteurGauche.setPower(0);
							moteurDroit.setPower(0);
							Delay.msDelay(500);
						}
					}
					moteurGauche.setPower(100);
					moteurDroit.setPower(80);
					if (pression == false) // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
					{
						pression = CapteurTactile.estPresse();
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
				}
			}
		}
		return pression;
	}
	
	public static boolean avancer3(int n)
	{
		//Description: le robot suit une ligne en effectuant un nombre donné d'oscillations
		//Entrée: entier (n) étant le nombre d'oscillations à effectuer
		//Sortie: booléen (pression) indiquant si un palet a été détecté ou non durant le déplacement du robot
		
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		int ligne; // Variable qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
		int i; // Variable qui compte le nombre d'oscillations effectuées par le robot
		int objet=0;
		float distance;
		boolean pression = false; // Variable qui indique si le capteur tactile est pressé ou non (initialisée à false)
		couleur = CapteurPhotosensible.obtenirIDCouleur();
		ligne = couleur;
		for (i=0; i<n; i++)
		{
			while (couleur != gris) // Tant que la couleur détectée est différente du gris, le robot dévie vers la gauche 
			{
				if (objet == 0)
				{
					objet = CapteurUltrason.detecterObjet();
				}
				if (objet == 2)
				{
					distance = CapteurUltrason.obtenirDistance();
					if (distance <= 0.2)
					{
						while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
						{
							moteurGauche.setPower(60);
							moteurDroit.setPower(0);
							couleur = CapteurPhotosensible.obtenirIDCouleur();
						}
						reculer1();
						moteurGauche.setPower(0);
						moteurDroit.setPower(0);
						Delay.msDelay(500);
					}
				}
				moteurGauche.setPower(80);
				moteurDroit.setPower(100);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
			while (couleur != ligne) // Tant que la couleur détectée est différente de celle de la ligne, le robot dévie vers la droite
			{
				if (objet == 0)
				{
					objet = CapteurUltrason.detecterObjet();
				}
				if (objet == 2)
				{
					distance = CapteurUltrason.obtenirDistance();
					if (distance <= 0.2)
					{
						while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
						{
							moteurGauche.setPower(60);
							moteurDroit.setPower(0);
							couleur = CapteurPhotosensible.obtenirIDCouleur();
						}
						reculer1();
						moteurGauche.setPower(0);
						moteurDroit.setPower(0);
						Delay.msDelay(500);
						i--;
					}
				}
				moteurGauche.setPower(100);
				moteurDroit.setPower(80);
				if (pression == false) // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
				{
					pression = CapteurTactile.estPresse();
				}
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
		}
		return pression;

	}
	
	
	
	// CREEE POUR LES INTERSECTIONS NON-COUVERTES
    public static boolean avancer4(int a)
	{
		//Description: le robot suit une ligne à une allure lente jusqu'à ce qu'il détecte une couleur donnée
    	//Entrée: entier (a) étant la couleur d'arrêt
    	//Sortie: booléen (pression) indiquant si un palet a été détecté ou non durant le déplacement du robot
    	
    	int couleur; // Variable qui va contenir l'id de la couleur détectée
		int ligne; // Variable qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
		int objet=0;
		float distance;
		boolean pression = false; // Variable qui indique si le capteur tactile est pressé ou non (initialisée à false)
		if (a != rouge && a != vert && a != bleu && a != jaune && a != blanc && a != noir) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
		{
			System.out.println("Valeur de a incorrecte");
		}
		else // Sinon, la fonction fait son travail
		{
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			ligne = couleur;
			while (couleur != aucune && couleur != a) // Boucle qui s'arrête lorsque la couleur détectée est celle d'arrêt
			{
				while (couleur != aucune && couleur != gris && couleur != a ) // Tant que la couleur détectée est différente du gris et de celle d'arrêt, le robot dévie vers la gauche 
				{
					if (objet == 0)
					{
						objet = CapteurUltrason.detecterObjet();
					}
					if (objet == 2)
					{
						distance = CapteurUltrason.obtenirDistance();
						if (distance <= 0.2)
						{
							while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
							{
								moteurGauche.setPower(60);
								moteurDroit.setPower(0);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							reculer1();
							moteurGauche.setPower(0);
							moteurDroit.setPower(0);
							Delay.msDelay(500);
						}
					}
					moteurGauche.setPower(0);
					moteurDroit.setPower(60);
					if (pression == false) // // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
					{
						pression = CapteurTactile.estPresse();
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
				}
				while (couleur != aucune && couleur != ligne && couleur != a) // Tant que la couleur détectée est différente de celle de la ligne et de celle d'arrêt, le robot dévie vers la droite
				{
					if (objet == 0)
					{
						objet = CapteurUltrason.detecterObjet();
					}
					if (objet == 2)
					{
						distance = CapteurUltrason.obtenirDistance();
						if (distance <= 0.2)
						{
							while (couleur != ligne) // Permet de repositionner le robot sur sa ligne
							{
								moteurGauche.setPower(60);
								moteurDroit.setPower(0);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							reculer1();
							moteurGauche.setPower(0);
							moteurDroit.setPower(0);
							Delay.msDelay(500);
						}
					}
					moteurGauche.setPower(60);
					moteurDroit.setPower(40);
					if (pression == false) // Permet d'enregistrer la détection d'un palet (s'il y a) pendant le déplacement du robot
					{
						pression = CapteurTactile.estPresse();
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
				}
			}
			while (couleur != aucune && couleur != ligne) // Permet de repositionner le robot sur sa ligne
			{
				moteurGauche.setPower(60);
				moteurDroit.setPower(0);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
		}
		return pression;
	}
    
    // CREEE POUR RAMASSERPALET(0) CAMP OUEST
 	public static void avancer5()
 	{
 		//Description: le robot se déplace jusqu'à détecter la ligne jaune
 		//Entrée: rien
 		//Sortie: rien
 		
 		int couleur; // Variable qui va contenir l'id de la couleur détectée
 		couleur = CapteurPhotosensible.obtenirIDCouleur();
 		while (couleur != noir) // Tant que la couleur détectée est différente du noir, le robot dévie vers la gauche
 		{
 			moteurGauche.setPower(100);
 			moteurDroit.setPower(90);
 			couleur = CapteurPhotosensible.obtenirIDCouleur();
 		}
 		while (couleur != bleu) // Tant que la couleur détectée est différente du blanc, le robot dévie vers la droite
 		{
 			moteurGauche.setPower(85);
 			moteurDroit.setPower(100);
 			couleur = CapteurPhotosensible.obtenirIDCouleur();
 		}
 		while (couleur != jaune) // Tant que la couleur détectée est différente du blanc, le robot dévie vers la droite
 		{
 			moteurGauche.setPower(100);
 			moteurDroit.setPower(90);
 			couleur = CapteurPhotosensible.obtenirIDCouleur();
 		}
 	}
 	
 // CREEE POUR RAMASSERPALET(0) CAMP EST
  	public static void avancer5Bis()
  	{
  		//Description: le robot se déplace jusqu'à détecter la ligne rouge
  		//Entrée: rien
  		//Sortie: rien
  		
  		int couleur; // Variable qui va contenir l'id de la couleur détectée
  		couleur = CapteurPhotosensible.obtenirIDCouleur();
  		while (couleur != noir) // Tant que la couleur détectée est différente du noir, le robot dévie vers la gauche
  		{
  			moteurGauche.setPower(100);
  			moteurDroit.setPower(90);
  			couleur = CapteurPhotosensible.obtenirIDCouleur();
  		}
  		while (couleur != vert) // Tant que la couleur détectée est différente du blanc, le robot dévie vers la droite
  		{
  			moteurGauche.setPower(85);
  			moteurDroit.setPower(100);
  			couleur = CapteurPhotosensible.obtenirIDCouleur();
  		}
  		while (couleur != rouge) // Tant que la couleur détectée est différente du blanc, le robot dévie vers la droite
  		{
  			moteurGauche.setPower(100);
  			moteurDroit.setPower(90);
  			couleur = CapteurPhotosensible.obtenirIDCouleur();
  		}
  	}
    
  	public static void reculer1()
	{
		//Description: le robot recule en effectuant une oscillation sur une ligne
  		//Entrée: rien
  		//Sortie: rien
  		
  		int couleur; // Variable qui va contenir l'id de la couleur détectée
		int ligne; // Variable qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
		couleur = CapteurPhotosensible.obtenirIDCouleur();
		ligne = couleur;
		while (couleur != gris) // Tant que la couleur détectée est différente du gris, le robot dévie vers la gauche 
		{
				moteurGauche.setPower(-80);
				moteurDroit.setPower(-100);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
		}
		while (couleur != ligne) // Tant que la couleur détectée est différente de celle de la ligne, le robot dévie vers la droite
		{
			moteurGauche.setPower(-100);
			moteurDroit.setPower(-80);
			couleur = CapteurPhotosensible.obtenirIDCouleur();
		}
	}
  	
  	public static void reculer2()
	{
		//Description: le robot recule droit jusqu'à ce qu'il détecte une ligne
  		//Entrée: rien
  		//Sortie: rien
  		
  		int couleur; // Variable qui va contenir l'id de la couleur détectée
		couleur = CapteurPhotosensible.obtenirIDCouleur();
		while (couleur != rouge && couleur != vert && couleur != bleu && couleur != jaune && couleur != blanc && couleur != noir) // Boucle qui s'arrête lorsque la couleur détectée est celle d'une ligne
		{
			moteurGauche.setPower(-40);
			moteurDroit.setPower(-40);
			couleur = CapteurPhotosensible.obtenirIDCouleur();
		}
	}
	
	public static void tournerGauche(int a)
	{
		//Description: le robot tourne vers la gauche jusqu'à ce que le robot détecte une couleur donnée
		//Entrée: entier (a) étant la couleur d'arrêt
		//Sortie: rien
		
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		if (a != rouge && a != vert && a != bleu && a != jaune && a != blanc && a != noir) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
		{
			System.out.println("Valeur de a incorrecte");
		}
		else // Sinon, la fonction fait son travail
		{
			moteurGauche.setPower(0); // Le robot tourne sur lui-même vers la gauche pendant une courte durée
			moteurDroit.setPower(60);
			Delay.msDelay(500);
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			while (couleur != a) // Tant que la couleur détectée est différente de celle d'arrêt, le robot tourne vers la gauche
			{
				moteurGauche.setPower(0);
				moteurDroit.setPower(60);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
		}
	}	

	public static void tournerDroite(int a)
	{
		//Description: le robot tourne vers la droite jusqu'à ce que le robot détecte une couleur donnée
		//Entrée: entier (a) étant la couleur d'arrêt
		//Sortie: rien
		
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		if (a != rouge && a != vert && a != bleu && a != jaune && a != blanc && a != noir) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
		{
			System.out.println("Valeur de a incorrecte");
		}
		else // Sinon, la fonction fait son travail
		{
			moteurGauche.setPower(60); // Le robot tourne sur lui-même vers la gauche pendant une courte durée
			moteurDroit.setPower(0);
			Delay.msDelay(500);
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			while (couleur != a) // Tant que la couleur détectée est différente de celle d'arrêt, le robot tourne vers la droite
			{
				moteurGauche.setPower(60);
				moteurDroit.setPower(0);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
		}
	}	

	public static void faireDemiTour(int a)
	{
		//Description: le robot tourne sur lui-même jusqu'à ce qu'il détecte une couleur donnée
		//Entrée: entier (a) étant la couleur d'arrêt
		//Sortie: rien
		
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		if (a != rouge && a != vert && a != bleu && a != jaune && a != blanc && a != noir) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
		{
			System.out.println("Valeur de a incorrecte");
		}
		else // Sinon, la fonction fait son travail
		{
			moteurGauche.setPower(-40); // Le robot tourne sur lui-même vers la gauche pendant une courte durée
			moteurDroit.setPower(40);
			Delay.msDelay(500);
			couleur = CapteurPhotosensible.obtenirIDCouleur();
			while (couleur != a) // Tant que la couleur détectée est différente de celle d'arrêt, le robot tourne sur lui-même vers la gauche
			{
				moteurGauche.setPower(-40);
				moteurDroit.setPower(40);
				couleur = CapteurPhotosensible.obtenirIDCouleur();
			}
		}
	}
	
	public static boolean allerIntersection(int i, boolean x, boolean camp)
	{
		//Description: le robot se déplace jusqu'à une intersection donnée
		//Entrée: entier (i) étant le numéro de l'intersection, booléen (x) indiquant si le robot a ramassé le palet de l'intersection précédente ou non, booléen (camp) étant le camp du robot
		//Sortie: booléen (palet) indiquant si un palet à été détecté à l'intersection donnée
		
		Button.LEDPattern(4); // Le voyant sur la brique clignote vert pour signaler à l'utilisateur que le robot est en train de se rendre à une intersection
		boolean palet = false; // Variable qui va indiquer si un palet a été détecté ou non lors du déplacement du robot, et enregistrer si le robot possède le palet ou non
		int couleur; // Variable qui va contenir l'id de la couleur détectée
		if (camp == true) // Si le camp du robot est ouest, le robot effectue ces instructions
		{
			// Intersection 0 = intersection vert/jaune
			// Intersection 1 = intersection bleu/jaune
			// Intersection 2 = intersection bleu/noir
			// Intersection 3 = intersection bleu/rouge
			// Intersection 4 = intersection noir/rouge
			// Intersection 5 = intersection noir/noir
			// Intersection 6 = intersection noir/jaune
			// Intersection 7 = intersection vert/noir
			// Intersection 8 = intersection vert/rouge
			switch (i)
			{
				case scenario3: case scenario4: // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 1 (scénario 3 et 4 uniquement)
				//EXPLICATION DE L'ALGORITHME
				//Le robot cherche à trouver l'emplacement 1 peu importe d'où il démarre
				//Pour ce faire, voici les 3 étapes qu'il effectue en boucles jusqu'à trouver l'intersection 1:
					//1 - En fonction d'où il se trouve, avancer jusqu'à détecter la ligne recherchée
					//2 - Si le palet a été détecté en chemin, le saisir
					//3 - Si l'il détecte le bord de la table, faire demi-tour
					
					palet = avancer1();
					couleur = CapteurPhotosensible.obtenirIDCouleur();
					if (palet == true)
					{
						moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
						moteurDroit.setPower(0);
						pinces.setPower(-100);
						Delay.msDelay(1100);
						pinces.setPower(-100);
					}
					if (couleur == aucune)
					{
						reculer2();
						couleur = CapteurPhotosensible.obtenirIDCouleur();
					}
					switch (couleur)
					{
						case vert: // Le robot effectue ces instructions dans le cas où la première ligne détectée est verte
							tournerDroite(vert);
							if (palet == false)
							{
								palet = avancer4(jaune);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(jaune);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(vert);
								if (palet == false)
								{
									palet = avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						case bleu: // Le robot effectue ces instructions dans le cas où la première ligne détectée est bleue
							tournerDroite(bleu);
							if (palet == false)
							{
								palet = avancer4(jaune);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(jaune);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(bleu);
								if (palet == false)
								{
									palet = avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						case noir: // Le robot effectue ces instructions dans le cas où la première ligne détectée est noire
							tournerDroite(noir);
							if (palet == false)
							{
								palet = avancer2(blanc);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer2(blanc);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(noir);
								if (palet == false)
								{
									palet = avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(jaune);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						default:
							break;
					}
					switch (couleur)
					{
						case rouge: case jaune: // Le robot effectue ces instructions dans le cas où il rencontre la ligne rouge ou jaune
							tournerDroite(couleur);
							if (palet == false)
							{
								palet = avancer4(blanc);
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(blanc);
							}
							break;
						default:
							break;
					}
					tournerDroite(blanc); // Le robot a rejoint une des deux lignes blanches
					while (couleur != jaune)
					{
						avancer4(jaune);
						couleur = CapteurPhotosensible.obtenirIDCouleur();
						if (couleur == aucune)
						{
							moteurGauche.setPower(-40);
							moteurDroit.setPower(-40);
							Delay.msDelay(1000);
							faireDemiTour(blanc);
						}
					}
					tournerGauche(jaune);
					if (palet == false)
					{
						palet = avancer2(bleu);
						if (palet == true)
						{
							moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
							moteurDroit.setPower(0);
							pinces.setPower(-100);
							Delay.msDelay(1100);
							pinces.setPower(-100);
						}
					}
					else
					{
						avancer2(bleu);
					}
					tournerGauche(bleu);
					if (palet == false)
					{
						palet = avancer4(noir);
						if (palet == true)
						{
							moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
							moteurDroit.setPower(0);
							pinces.setPower(-100);
							Delay.msDelay(1100);
							pinces.setPower(-100);
						}
					}
					else
					{
						avancer4(noir);
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
					if (couleur == aucune)
					{
						moteurGauche.setPower(-40);
						moteurDroit.setPower(-40);
						Delay.msDelay(1000);
						faireDemiTour(bleu);
						if (palet == false)
						{
							palet = avancer4(noir);
							if (palet == true)
							{
								moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
								moteurDroit.setPower(0);
								pinces.setPower(-100);
								Delay.msDelay(1100);
								pinces.setPower(-100);
							}
						}
						else
						{
							avancer4(noir);
						}
					}
					tournerGauche(noir);
					avancer2(blanc); // Le robot a rejoint la ligne blanche adverse
					if (palet == true)
					{
						moteurGauche.setPower(-40); // Le robot lâche le palet en reculant
						moteurDroit.setPower(-40);
						pinces.setPower(100);
						Delay.msDelay(1000);
						pinces.setPower(-100);
					}
					else
					{
						tournerGauche(blanc); //Le robot trouve l'intersection 1
						avancer4(jaune);
						tournerGauche(jaune);
					}
					break;
				case 0 : // Le robot effectue cette instruction dans le cas où il doit se rendre à l'intersection 0
					palet = avancer2(bleu);
					break;
				case 1 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 1
					if (x == true)
					{
						faireDemiTour(jaune);
					}
					palet = avancer2(bleu);
					break;
				case 2 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 2
					if (x == true)
					{
						faireDemiTour(jaune);
						avancer2(bleu);
					}
					tournerGauche(bleu);
					avancer3(2);
					palet = avancer4(noir);
					break;
				case 3 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 3
					if (x == true)
					{
						faireDemiTour(noir);
						avancer2(bleu);
						tournerGauche(bleu);
					}
					avancer3(2);
					palet = avancer4(rouge);
					break;
				case 4 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 4
					if (x == true)
					{
						faireDemiTour(rouge);
					}
					else
					{
						tournerDroite(rouge);
					}
					palet = avancer2(noir);
					break;
				case 5 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 5
					if (x == true)
					{
						faireDemiTour(rouge);
						avancer2(noir);
					}
					tournerDroite(noir);
					palet = avancer3(3);
					break;
				case 6 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 6
					if (x == true)
					{
						faireDemiTour(noir);
						avancer3(6);
						tournerDroite(noir);
					}
					avancer3(2);
					palet = avancer4(jaune);
					break;
				case 7 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 7
					if (x == true)
					{
						faireDemiTour(jaune);
					}
					else
					{
						tournerGauche(jaune);
					}
					avancer2(vert);
					tournerGauche(vert);
					avancer3(2);
					palet = avancer4(noir);
					break;
				case 8 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 8
					if (x == true)
					{
						faireDemiTour(noir);
						avancer2(vert);
						tournerGauche(vert);
					}
					avancer3(2);
					palet = avancer4(rouge);
					break;
				default : // La fonction affiche un message d'erreur dans le cas où le numéro d'intersection entré n'est pas valide
					System.out.println("Valeur de i incorrecte");
					break;
			}
		}
		else // Sinon, le robot effectue ces instructions
		{
			// Intersection 0 = intersection bleu/rouge
			// Intersection 1 = intersection vert/rouge
			// Intersection 2 = intersection vert/noir
			// Intersection 3 = intersection vert/jaune
			// Intersection 4 = intersection noir/jaune
			// Intersection 5 = intersection noir/noir
			// Intersection 6 = intersection noir/rouge
			// Intersection 7 = intersection bleu/noir
			// Intersection 8 = intersection bleu/jaune
			switch (i)
			{
				case scenario3: case scenario4: // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 1 (scénario 3 et 4 uniquement)
				//EXPLICATION DE L'ALGORITHME
				//Le robot cherche à trouver l'emplacement 1 peu importe d'où il démarre
				//Pour ce faire, voici les 3 étapes qu'il effectue en boucles jusqu'à trouver l'intersection 1:
					//1 - En fonction d'où il se trouve, avancer jusqu'à détecter la ligne recherchée
					//2 - Si le palet a été détecté en chemin, le saisir
					//3 - Si l'il détecte le bord de la table, faire demi-tour					
			
					palet = avancer1();
					couleur = CapteurPhotosensible.obtenirIDCouleur();
					if (palet == true)
					{
						moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
						moteurDroit.setPower(0);
						pinces.setPower(-100);
						Delay.msDelay(1100);
						pinces.setPower(-100);
					}
					if (couleur == aucune)
					{
						reculer2();
						couleur = CapteurPhotosensible.obtenirIDCouleur();
					}
					switch (couleur)
					{
						case vert: // Le robot effectue ces instructions dans le cas où la première ligne détectée est verte
							tournerDroite(vert);
							if (palet == false)
							{
								palet = avancer4(rouge);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(rouge);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(vert);
								if (palet == false)
								{
									palet = avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						case bleu: // Le robot effectue ces instructions dans le cas où la première ligne détectée est bleue
							tournerDroite(bleu);
							if (palet == false)
							{
								palet = avancer4(rouge);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(rouge);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(bleu);
								if (palet == false)
								{
									palet = avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						case noir: // Le robot effectue ces instructions dans le cas où la première ligne détectée est noire
							tournerDroite(noir);
							if (palet == false)
							{
								palet = avancer2(blanc);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer2(blanc);
								couleur = CapteurPhotosensible.obtenirIDCouleur();
							}
							if (couleur == aucune)
							{
								moteurGauche.setPower(-40);
								moteurDroit.setPower(-40);
								Delay.msDelay(1000);
								faireDemiTour(noir);
								if (palet == false)
								{
									palet = avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
									if (palet == true)
									{
										moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
										moteurDroit.setPower(0);
										pinces.setPower(-100);
										Delay.msDelay(1100);
										pinces.setPower(-100);
									}
								}
								else
								{
									avancer4(rouge);
									couleur = CapteurPhotosensible.obtenirIDCouleur();
								}
							}
							break;
						default:
							break;
					}
					switch (couleur)
					{
						case rouge: case jaune: // Le robot effectue ces instructions dans le cas où il rencontre la ligne rouge ou jaune
							tournerDroite(couleur);
							if (palet == false)
							{
								palet = avancer4(blanc);
								if (palet == true)
								{
									moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
									moteurDroit.setPower(0);
									pinces.setPower(-100);
									Delay.msDelay(1100);
									pinces.setPower(-100);
								}
							}
							else
							{
								avancer4(blanc);
							}
							break;
						default:
							break;
					}
					tournerDroite(blanc); // Le robot a rejoint une des deux lignes blanches
					while (couleur != rouge)
					{
						avancer4(rouge);
						couleur = CapteurPhotosensible.obtenirIDCouleur();
						if (couleur == aucune)
						{
							moteurGauche.setPower(-40);
							moteurDroit.setPower(-40);
							Delay.msDelay(1000);
							faireDemiTour(blanc);
						}
					}
					tournerGauche(rouge);
					if (palet == false)
					{
						palet = avancer2(vert);
						if (palet == true)
						{
							moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
							moteurDroit.setPower(0);
							pinces.setPower(-100);
							Delay.msDelay(1100);
							pinces.setPower(-100);
						}
					}
					else
					{
						avancer2(vert);
					}
					tournerGauche(vert);
					if (palet == false)
					{
						palet = avancer4(noir);
						if (palet == true)
						{
							moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
							moteurDroit.setPower(0);
							pinces.setPower(-100);
							Delay.msDelay(1100);
							pinces.setPower(-100);
						}
					}
					else
					{
						avancer4(noir);
					}
					couleur = CapteurPhotosensible.obtenirIDCouleur();
					if (couleur == aucune)
					{
						moteurGauche.setPower(-40);
						moteurDroit.setPower(-40);
						Delay.msDelay(1000);
						faireDemiTour(vert);
						if (palet == false)
						{
							palet = avancer4(noir);
							if (palet == true)
							{
								moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
								moteurDroit.setPower(0);
								pinces.setPower(-100);
								Delay.msDelay(1100);
								pinces.setPower(-100);
							}
						}
						else
						{
							avancer4(noir);
						}
					}
					tournerGauche(noir);
					avancer2(blanc); // Le robot a rejoint la ligne blanche adverse
					if (palet == true)
					{
						moteurGauche.setPower(-40); // Le robot lâche le palet en reculant
						moteurDroit.setPower(-40);
						pinces.setPower(100);
						Delay.msDelay(1000);
						pinces.setPower(-100);
					}
					else
					{
						tournerGauche(blanc); // Le robot trouve l'intersection 1
						avancer4(rouge);
						tournerGauche(rouge);
					}
					break;
				case 0 : // Le robot effectue cette instruction dans le cas où il doit se rendre à l'intersection 0
					palet = avancer2(bleu);
					break;
				case 1 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 1
					if (x == true)
					{
						faireDemiTour(rouge);
					}
					palet = avancer2(vert);
					break;
				case 2 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 2
					if (x == true)
					{
						faireDemiTour(rouge);
						avancer2(vert);
					}
					tournerGauche(vert);
					avancer3(2);
					palet = avancer4(noir);
					break;
				case 3 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 3
					if (x == true)
					{
						faireDemiTour(noir);
						avancer2(vert);
						tournerGauche(vert);
					}
					avancer3(2);
					palet = avancer4(jaune);
					break;
				case 4 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 4
					if (x == true)
					{
						faireDemiTour(jaune);
					}
					else
					{
						tournerDroite(jaune);
					}
					palet = avancer2(noir);
					break;
				case 5 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 5
					if (x == true)
					{
						faireDemiTour(jaune);
						avancer2(noir);
					}
					tournerDroite(noir);
					palet = avancer3(3);
					break;
				case 6 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 6
					if (x == true)
					{
						faireDemiTour(noir);
						avancer3(6);
						tournerDroite(noir);
					}
					avancer3(2);
					palet = avancer4(rouge);
					break;
				case 7 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 7
					if (x == true)
					{
						faireDemiTour(rouge);
					}
					else
					{
						tournerGauche(rouge);
					}
					avancer2(bleu);
					tournerGauche(bleu);
					avancer3(2);
					palet = avancer4(noir);
					break;
				case 8 : // Le robot effectue ces instructions dans le cas où il doit se rendre à l'intersection 8
					if (x == true)
					{
						faireDemiTour(noir);
						avancer2(bleu);
						tournerGauche(bleu);
					}
					avancer3(2);
					palet = avancer4(jaune);
					break;
				default : // La fonction affiche un message d'erreur dans le cas où le numéro d'intersection entré n'est pas valide
					System.out.println("Valeur de i incorrecte");
					break;
			}
		}
		return palet;
	}

	public static void ramasserPalet(int i, boolean camp)
	{
		//Description: le robot saisit un palet et le déplace jusqu'à la ligne blanche adverse pour le déposer
		//Entrée: entier (i) étant le numéro de l'intersection, booléen (camp) étant le camp du robot
		//Sortie: rien
		
		Button.LEDPattern(6); // Le voyant sur la brique clignote rouge pour signaler à l'utilisateur que le robot est en train de ramasser un palet
		moteurGauche.setPower(0); // Le robot s'arrête et saisit le palet
		moteurDroit.setPower(0);
		pinces.setPower(-100);
		Delay.msDelay(1100);
		pinces.setPower(0);
		if (camp == true) // Si le camp du robot est ouest, le robot effectue ces instructions
		{
			switch (i)
			{
				case 0 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 0
					avancer5();
					break;
				case 1 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 1
					faireDemiTour(jaune);
					break;
				case 2 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 2
					tournerGauche(noir);
					break;
				case 3 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 3
					tournerGauche(rouge);
					break;
				case 4 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 4
					faireDemiTour(rouge);
					break;
				case 5 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 5
					tournerDroite(noir);
					break;
				case 6 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 6
					tournerDroite(jaune);
					break;
				case 7 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 7
					tournerGauche(noir);
					break;
				case 8 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 8
					tournerGauche(rouge);
					break;
				default : // La fonction affiche un message d'erreur dans le cas où le numéro d'intersection entré n'est pas valide
					System.out.println("Valeur de i incorrecte");
					break;
			}
		}
		else // Sinon, le robot effectue ces instructions
		{
			switch (i)
			{
				case 0 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 0
					avancer5Bis();
					break;
				case 1 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 1
					faireDemiTour(rouge);
					break;
				case 2 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 2
					tournerGauche(noir);
					break;
				case 3 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 3
					tournerGauche(jaune);
					break;
				case 4 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 4
					faireDemiTour(jaune);
					break;
				case 5 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 5
					tournerDroite(noir);
					break;
				case 6 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 6
					tournerDroite(rouge);
					break;
				case 7 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 7
					tournerGauche(noir);
					break;
				case 8 : // Le robot effectue cette instruction dans le cas où il a saisi le palet à l'intersection 8
					tournerGauche(jaune);
					break;
				default : // La fonction affiche un message d'erreur dans le cas où le numéro d'emplacement entré n'est pas valide
					System.out.println("Valeur de i incorrecte");
					break;
			}
		}
		avancer2(blanc);
		moteurGauche.setPower(-40); // Le robot lâche le palet en reculant
		moteurDroit.setPower(-40);
		pinces.setPower(100);
		Delay.msDelay(1000);
		pinces.setPower(0);
	}	

	public static void fermer3() // Libère les ressources des moteurs
	{
		//Description: libère les ressources des moteurs
		//Entrée: rien
		//Sortie: rien
		
		moteurGauche.close();
		moteurDroit.close();
		pinces.close();
	}
}