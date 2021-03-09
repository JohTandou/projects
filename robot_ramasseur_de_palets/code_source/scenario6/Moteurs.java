package ev3;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;


public class Moteurs // Concerne les trois moteurs et contient toutes les fonctions de mouvement du robot. Cette classe est utilisée par Main et utilise les classes Brique, CapteurPhotosensible,CapteurTactile et CapteurUltrason.
{
	static UnregulatedMotor moteurGauche = new UnregulatedMotor (MotorPort.A); // CrÃ©ation de l'objet moteurGauche
    static UnregulatedMotor moteurDroit = new UnregulatedMotor (MotorPort.B); // CrÃ©ation de l'objet moteurDroit
    static UnregulatedMotor pinces = new UnregulatedMotor (MotorPort.C); // CrÃ©ation de l'objet pinces
	

    
    
    public static boolean SuivreADroite() // Le robot suit une ligne située à sa droite en zigzaguant. (à refaire entiérement)
   	{
   		
    	
    	
    	//
    	//	En cours de reconception
    	//
    	
    	
    	
   	}

    public static boolean SuivreAGauche() // Le robot suit une ligne située à sa gauche en zigzaguant. (à refaire entiérement)
   	{
   		
    	
    	//
    	//	En cours de reconception
    	//
    	
    	
   	}
    
	public static void fermer3() // LibÃ¨re les ressources des moteurs
	{
		moteurGauche.close();
		moteurDroit.close();
		pinces.close();
	}
	
	public static void rotation(int temps) { //le robot tourne vers la droite pendant une durée donnée en entrée
		moteurGauche.setPower(40);
		moteurDroit.setPower(-40);
		Delay.msDelay(temps); 
	}
	
	public static void rotationGauche(int temps) { // Le robot tourne vers la gauche pendant une durée donnée en entrée
		moteurGauche.setPower(-40);
		moteurDroit.setPower(40);
		Delay.msDelay(temps);
	}
	
	public static void avancerscenario() { // Le robot avance
		moteurGauche.setPower(100);
		moteurDroit.setPower(100);// Ratio à changer pour que le robot aille droit
	}

	// Pour les fonctions AllerAuCentre() et DeposerPalet() il manque : 
	// 			Des mesures de temps selon la distance à effectuer (pour tous les Delay.msDelay)
	// 			De nouvelles fonctions de suivies(SuivreADroite et SuivreAGauche ne fonctionneront pas, il faut changer la condition d'arrêt)
	//			(d'où les signaux d'erreurs)
	
	public static void AllerAuCentre() { // Le robot ira au centre de la surface de jeu (Intersection Noire-Noire) quelque soit sa position de départ (Début du scénario)
		int boucle = 0; // variable qui sera utilisée pour forcer les sorties de boucle
		int indicateurdroite,indicateurgauche,indicateur; // indicateurs de positions du robot (Utile pour savoir vers où tourner au moment désiré)
		float echantillon; // variable de stockage des échantillons
		
		CapteurPhotosensible.initialisation();
		
						
		Moteurs.avancerscenario(); // On fait avancer le robot quelque soit sa position
		
		while(boucle==0) { // tant que l'on ne force pas la sortie de boucle
			echantillon=CapteurPhotosensible.obtenirIDCouleur(); // Prise d'échantillon par le robot
			
			if(echantillon==-1) { // Si le robot arrive au bord de la table, il fait demi tour
				Moteurs.rotation(/*temps nécessaire pour faire 90°*/);
				Moteurs.avancerscenario();
			}
			
			else if(echantillon==7) { // Si le robot détecte du Noir
				Moteurs.rotation(/*temps nécessaire pour faire le test(valeur très petite)*/); // Il effectue un test pour savoir s'il est à droite ou à gauche
				echantillon=CapteurPhotosensible.obtenirIDCouleur();// de la ligne et reprend un échantillon
				if (echantillon!=7) {// Le robot tourne légérement à droite, 
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/)// S'il ne detecte pas de noir,
					indicateurgauche=1; // Il est à droite du robot et doit suivre la ligne à sa gauche
				}	
				else if (echantillon==7) {// S'il détecte du noir, il est à gauche de la ligne,
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/)// et doit suivre sa droite.
					indicateurdroite=1;
				}
				
				while(boucle==0) {//Deuxiéme boucle interne
					echantillon=CapteurPhotosensible.obtenirIDCouleur();//Prise d'échantillon en boucle
					if (echantillon==-1 || echantillon==6){//Détection du bord ou de blanc
						Moteurs.rotation(/*Mesurer le temps nécessaire pour faire demi tour*/); //Demi tour
						if(indicateurgauche==1) {//Indication pour demi tour
							Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Mesurer temps nécessaire pour parcourir la distance BordDeLaLigneNoire-centre*/);//Temps nécessaire
						}// pour arriver du bord jusqu'au centre
						else if(indicateurdroite==1) {//Indication pour demi tour
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Mesurer temps nécessaire pour parcourir la distance BordDeLaLigneNoire-centre*/);// Idem que précédemment
						}
						boucle=1; //force la sortie de boucle
					}
				}		
			}
			
			
			else if(echantillon==0) { // Si le robot détecte du rouge
				// Même test que précédemment pour savoir s'il est a droite ou à gauche de la ligne
				Moteurs.rotation(/*Mesurer temps nécessaire pour faire le test(valeur très petite)*/); 
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=0) {
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==0) {
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {//Deuxiéme boucle interne
					echantillon=CapteurPhotosensible.obtenirIDCouleur();//Prise d'échantillon en boucle
					if(echantillon==1) indicateur=1; // Indication pour savoir si le robot arrive
					else if(echantillon==2) indicateur=0;// par le bas ou par le haut par rapport à la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*Tempspourfairedemitour*/);
					// demi tour si le robot détecte du blanc ou le vide
					if(echantillon==7) {// Si le robot détecte du noir
						if (indicateur==0) {// Si le robot est passé par le bleu c'est qu'il doit tourner à droite
							Moteurs.rotation(/*Temps nécessaire pour faire 90°*/);// Tourne vers la droite
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);// Suit la ligne noire à sa gauche
							Delay.msDelay/*Temps nécessaire pour parcourir la distance "intersection Rouge.Noire" - "Centre"*/); // Jusqu'au centre
							boucle=1;// On force la sortie de boucle
						}
						else if(indicateur==1) {// Si le robot est passé par le vert c'est qu'il doit tourner à gauche
							
							Moteurs.rotationGauche(/*Temps nécessaire pour faire 90°*/);// Tourne vers la gauche
							Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);// Suit la ligne à sa droite
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Rouge.Noire" - "Centre"*/);// Jusqu'au centre
							boucle=1;// On force la sortie de boucle
						}
					
					
					}
				}
			}
			
			// à partir d'ici on execute le même code pour chaque couleur, le commentaire est inutile
			// On change seulement les couleurs d'indications haut/bas ou gauche/droite (voir commentaire)
			// Une ligne non commentée implique qu'elle est exactement identique par rapport au bloc audessus
			else if(echantillon==2) {
				
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=2) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==2) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==0) indicateur=1; // Si le robot rencontre du rouge il devra tourner à gauche une fois qu'il rencontre la ligne noire
					else if(echantillon==3) indicateur=0;// Si le robot rencontre du jaune il devra tourner à droite une fois qu'il rencontre la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsNécessairePourFaireDemitour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Bleue.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Bleue.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
			}
			
			else if(echantillon==1) {
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=1) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==1) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==3) indicateur=1;// Si le robot detecte du jaune il devra tourner à gauche quand il rencontrera la ligne noire
					else if(echantillon==0) indicateur=0; // Si le robot detecte du rouge il devra tourner à droite quand il rencontrera la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsNécessairePourFaireDemi-Tour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Vert.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Vert.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
			}
			
			else if(echantillon==3) {
				
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=3) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==3) { // Test pour connaitre la position du robot par rapport à la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==2) indicateur=1; // Si le robot rencontre du bleu il devra tourner à gauche quand il rencontre la ligne noire
					else if(echantillon==1) indicateur=0; // Si le robot rencontre du vert il devra tourner à droite quand il rencontre la ligne droite
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsNécessairePourFaireDemitour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Jaune.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps nécessaire pour faire 90°*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps nécessaire pour parcourir la distance "intersection Jaune.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
				}
			}
	}

	public static void DetecterEtSaisirPalet() { // Le robot tourne sur lui même jusqu'à détecter un objet à l'intérieur de la surface de jeu. Le robot s'arrêtera dés la détection et ira tout droit puis saisira le palet. Doit être executé à la suite de la fonction AllerAuCentre pour fonctionner correctement. (Milieu du scénario)
		CapteurUltrason.enable();
		float Distance;//Variable de stockage de l'échantillon de distance
		Distance=CapteurUltrason.ObtenirDistance();
		while (Distance>1/*Nombre aléatoire,Mesure à effectuer*/) {// Tant qu'une distance inférieure à celle de la surface de jeu n'a pas été détectée, le robot continue à tourner
			rotation(500/*Nombre aléatoire,Mesure à effectuer*/);
			Distance=CapteurUltrason.ObtenirDistance();
		}
		// Si une distance inférieure est détectée, le robot s'arrête et va tout droit
		boolean pression=false;
		moteurDroit.setPower(100);
		moteurGauche.setPower(100);// Ratio à changer pour que le robot aille tout droit
		while(pression=false) { // On raffraichit la détection de pression en boucle jusqu'à toucher le palet
			pression=CapteurTactile.estPresse();
		}
		
		if(pression==true) { // Si le palet est détecté, le robot s'arrête et le saisit avec la pince
			moteurDroit.setPower(0);
			moteurGauche.setPower(0);
			pinces.setPower(-100);
			Delay.msDelay(1100);
			pinces.setPower(0);
		}
	}	
		
		
	public static void DeposerPalet() { // Le robot recherchera la ligne blanche. Après l'avoir trouvé il dépose le palet et s'arrête. (Fin du scénario)
		CapteurUltrason.disable();
		moteurDroit.setPower(70);
		moteurGauche.setPower(70);
		int boucle;
		
		float echantillon; // variable de stockage des échantillons
		
		CapteurPhotosensible.initialisation();
		avancerscenario(); // On fait avancer le robot quelque soit sa position
		
		while(boucle==0) { // tant que l'on ne force pas la sortie de boucle
			echantillon=CapteurPhotosensible.obtenirIDCouleur(); // Prise d'échantillon par le robot
			
			if(echantillon==-1) { // Si le robot arrive au bord de la table, il fait demi tour
				rotation(/*temps nécessaire pour faire 90°*/);
				avancerscenario();
			}
			
			if(echantillon==6) { // Si le robot détecte la couleur blanche, il s'arrête, dépose le palet et libére toutes les ressources, c'est la fin du scénario.
				moteurDroit.setPower(0);
				moteurGauche.setPower(0);
				pinces.setPower(100);
				Delay.msDelay(1000);
				pinces.setPower(0);
				fermer3();
				CapteurUltrason.disable();
				CapteurPhotosensible.fermer1();
				CapteurTactile.fermer2();
				CapteurUltrason.fermer4();
				
			}
			
		}
	}
	
}