package ev3;

import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;


public class Moteurs // Concerne les trois moteurs et contient toutes les fonctions de mouvement du robot. Cette classe est utilis�e par Main et utilise les classes Brique, CapteurPhotosensible,CapteurTactile et CapteurUltrason.
{
	static UnregulatedMotor moteurGauche = new UnregulatedMotor (MotorPort.A); // Création de l'objet moteurGauche
    static UnregulatedMotor moteurDroit = new UnregulatedMotor (MotorPort.B); // Création de l'objet moteurDroit
    static UnregulatedMotor pinces = new UnregulatedMotor (MotorPort.C); // Création de l'objet pinces
	

    
    
    public static boolean SuivreADroite() // Le robot suit une ligne situ�e � sa droite en zigzaguant. (� refaire enti�rement)
   	{
   		
    	
    	
    	//
    	//	En cours de reconception
    	//
    	
    	
    	
   	}

    public static boolean SuivreAGauche() // Le robot suit une ligne situ�e � sa gauche en zigzaguant. (� refaire enti�rement)
   	{
   		
    	
    	//
    	//	En cours de reconception
    	//
    	
    	
   	}
    
	public static void fermer3() // Libère les ressources des moteurs
	{
		moteurGauche.close();
		moteurDroit.close();
		pinces.close();
	}
	
	public static void rotation(int temps) { //le robot tourne vers la droite pendant une dur�e donn�e en entr�e
		moteurGauche.setPower(40);
		moteurDroit.setPower(-40);
		Delay.msDelay(temps); 
	}
	
	public static void rotationGauche(int temps) { // Le robot tourne vers la gauche pendant une dur�e donn�e en entr�e
		moteurGauche.setPower(-40);
		moteurDroit.setPower(40);
		Delay.msDelay(temps);
	}
	
	public static void avancerscenario() { // Le robot avance
		moteurGauche.setPower(100);
		moteurDroit.setPower(100);// Ratio � changer pour que le robot aille droit
	}

	// Pour les fonctions AllerAuCentre() et DeposerPalet() il manque : 
	// 			Des mesures de temps selon la distance � effectuer (pour tous les Delay.msDelay)
	// 			De nouvelles fonctions de suivies(SuivreADroite et SuivreAGauche ne fonctionneront pas, il faut changer la condition d'arr�t)
	//			(d'o� les signaux d'erreurs)
	
	public static void AllerAuCentre() { // Le robot ira au centre de la surface de jeu (Intersection Noire-Noire) quelque soit sa position de d�part (D�but du sc�nario)
		int boucle = 0; // variable qui sera utilis�e pour forcer les sorties de boucle
		int indicateurdroite,indicateurgauche,indicateur; // indicateurs de positions du robot (Utile pour savoir vers o� tourner au moment d�sir�)
		float echantillon; // variable de stockage des �chantillons
		
		CapteurPhotosensible.initialisation();
		
						
		Moteurs.avancerscenario(); // On fait avancer le robot quelque soit sa position
		
		while(boucle==0) { // tant que l'on ne force pas la sortie de boucle
			echantillon=CapteurPhotosensible.obtenirIDCouleur(); // Prise d'�chantillon par le robot
			
			if(echantillon==-1) { // Si le robot arrive au bord de la table, il fait demi tour
				Moteurs.rotation(/*temps n�cessaire pour faire 90�*/);
				Moteurs.avancerscenario();
			}
			
			else if(echantillon==7) { // Si le robot d�tecte du Noir
				Moteurs.rotation(/*temps n�cessaire pour faire le test(valeur tr�s petite)*/); // Il effectue un test pour savoir s'il est � droite ou � gauche
				echantillon=CapteurPhotosensible.obtenirIDCouleur();// de la ligne et reprend un �chantillon
				if (echantillon!=7) {// Le robot tourne l�g�rement � droite, 
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/)// S'il ne detecte pas de noir,
					indicateurgauche=1; // Il est � droite du robot et doit suivre la ligne � sa gauche
				}	
				else if (echantillon==7) {// S'il d�tecte du noir, il est � gauche de la ligne,
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/)// et doit suivre sa droite.
					indicateurdroite=1;
				}
				
				while(boucle==0) {//Deuxi�me boucle interne
					echantillon=CapteurPhotosensible.obtenirIDCouleur();//Prise d'�chantillon en boucle
					if (echantillon==-1 || echantillon==6){//D�tection du bord ou de blanc
						Moteurs.rotation(/*Mesurer le temps n�cessaire pour faire demi tour*/); //Demi tour
						if(indicateurgauche==1) {//Indication pour demi tour
							Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Mesurer temps n�cessaire pour parcourir la distance BordDeLaLigneNoire-centre*/);//Temps n�cessaire
						}// pour arriver du bord jusqu'au centre
						else if(indicateurdroite==1) {//Indication pour demi tour
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Mesurer temps n�cessaire pour parcourir la distance BordDeLaLigneNoire-centre*/);// Idem que pr�c�demment
						}
						boucle=1; //force la sortie de boucle
					}
				}		
			}
			
			
			else if(echantillon==0) { // Si le robot d�tecte du rouge
				// M�me test que pr�c�demment pour savoir s'il est a droite ou � gauche de la ligne
				Moteurs.rotation(/*Mesurer temps n�cessaire pour faire le test(valeur tr�s petite)*/); 
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=0) {
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==0) {
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {//Deuxi�me boucle interne
					echantillon=CapteurPhotosensible.obtenirIDCouleur();//Prise d'�chantillon en boucle
					if(echantillon==1) indicateur=1; // Indication pour savoir si le robot arrive
					else if(echantillon==2) indicateur=0;// par le bas ou par le haut par rapport � la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*Tempspourfairedemitour*/);
					// demi tour si le robot d�tecte du blanc ou le vide
					if(echantillon==7) {// Si le robot d�tecte du noir
						if (indicateur==0) {// Si le robot est pass� par le bleu c'est qu'il doit tourner � droite
							Moteurs.rotation(/*Temps n�cessaire pour faire 90�*/);// Tourne vers la droite
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);// Suit la ligne noire � sa gauche
							Delay.msDelay/*Temps n�cessaire pour parcourir la distance "intersection Rouge.Noire" - "Centre"*/); // Jusqu'au centre
							boucle=1;// On force la sortie de boucle
						}
						else if(indicateur==1) {// Si le robot est pass� par le vert c'est qu'il doit tourner � gauche
							
							Moteurs.rotationGauche(/*Temps n�cessaire pour faire 90�*/);// Tourne vers la gauche
							Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);// Suit la ligne � sa droite
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Rouge.Noire" - "Centre"*/);// Jusqu'au centre
							boucle=1;// On force la sortie de boucle
						}
					
					
					}
				}
			}
			
			// � partir d'ici on execute le m�me code pour chaque couleur, le commentaire est inutile
			// On change seulement les couleurs d'indications haut/bas ou gauche/droite (voir commentaire)
			// Une ligne non comment�e implique qu'elle est exactement identique par rapport au bloc audessus
			else if(echantillon==2) {
				
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=2) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==2) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==0) indicateur=1; // Si le robot rencontre du rouge il devra tourner � gauche une fois qu'il rencontre la ligne noire
					else if(echantillon==3) indicateur=0;// Si le robot rencontre du jaune il devra tourner � droite une fois qu'il rencontre la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsN�cessairePourFaireDemitour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Bleue.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Bleue.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
			}
			
			else if(echantillon==1) {
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=1) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==1) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==3) indicateur=1;// Si le robot detecte du jaune il devra tourner � gauche quand il rencontrera la ligne noire
					else if(echantillon==0) indicateur=0; // Si le robot detecte du rouge il devra tourner � droite quand il rencontrera la ligne noire
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsN�cessairePourFaireDemi-Tour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Vert.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Vert.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
			}
			
			else if(echantillon==3) {
				
				Moteurs.rotation(500);
				echantillon=CapteurPhotosensible.obtenirIDCouleur();
				if (echantillon!=3) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
					indicateurgauche=1;
				}	
				else if (echantillon==3) { // Test pour connaitre la position du robot par rapport � la ligne
					Moteurs.SuivreADroite(/*Changer la condition de fin de la fonction*/);
					indicateurdroite=1;
				}
				while(boucle==0) {
					echantillon=CapteurPhotosensible.obtenirIDCouleur();
					if(echantillon==2) indicateur=1; // Si le robot rencontre du bleu il devra tourner � gauche quand il rencontre la ligne noire
					else if(echantillon==1) indicateur=0; // Si le robot rencontre du vert il devra tourner � droite quand il rencontre la ligne droite
					if(echantillon==6 || echantillon==-1) Moteurs.rotation(/*TempsN�cessairePourFaireDemitour*/);
					
					if(echantillon==7) {
						if (indicateur==0) {
							Moteurs.rotation(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Jaune.Noire" - "Centre"*/);
							boucle=1;
						}
						else if(indicateur==1) {
							
							Moteurs.rotationGauche(/*Temps n�cessaire pour faire 90�*/);
							Moteurs.SuivreAGauche(/*Changer la condition de fin de la fonction*/);
							Delay.msDelay(/*Temps n�cessaire pour parcourir la distance "intersection Jaune.Noire" - "Centre"*/);
							boucle=1;
						}
					
					
					}
				}
				}
			}
	}

	public static void DetecterEtSaisirPalet() { // Le robot tourne sur lui m�me jusqu'� d�tecter un objet � l'int�rieur de la surface de jeu. Le robot s'arr�tera d�s la d�tection et ira tout droit puis saisira le palet. Doit �tre execut� � la suite de la fonction AllerAuCentre pour fonctionner correctement. (Milieu du sc�nario)
		CapteurUltrason.enable();
		float Distance;//Variable de stockage de l'�chantillon de distance
		Distance=CapteurUltrason.ObtenirDistance();
		while (Distance>1/*Nombre al�atoire,Mesure � effectuer*/) {// Tant qu'une distance inf�rieure � celle de la surface de jeu n'a pas �t� d�tect�e, le robot continue � tourner
			rotation(500/*Nombre al�atoire,Mesure � effectuer*/);
			Distance=CapteurUltrason.ObtenirDistance();
		}
		// Si une distance inf�rieure est d�tect�e, le robot s'arr�te et va tout droit
		boolean pression=false;
		moteurDroit.setPower(100);
		moteurGauche.setPower(100);// Ratio � changer pour que le robot aille tout droit
		while(pression=false) { // On raffraichit la d�tection de pression en boucle jusqu'� toucher le palet
			pression=CapteurTactile.estPresse();
		}
		
		if(pression==true) { // Si le palet est d�tect�, le robot s'arr�te et le saisit avec la pince
			moteurDroit.setPower(0);
			moteurGauche.setPower(0);
			pinces.setPower(-100);
			Delay.msDelay(1100);
			pinces.setPower(0);
		}
	}	
		
		
	public static void DeposerPalet() { // Le robot recherchera la ligne blanche. Apr�s l'avoir trouv� il d�pose le palet et s'arr�te. (Fin du sc�nario)
		CapteurUltrason.disable();
		moteurDroit.setPower(70);
		moteurGauche.setPower(70);
		int boucle;
		
		float echantillon; // variable de stockage des �chantillons
		
		CapteurPhotosensible.initialisation();
		avancerscenario(); // On fait avancer le robot quelque soit sa position
		
		while(boucle==0) { // tant que l'on ne force pas la sortie de boucle
			echantillon=CapteurPhotosensible.obtenirIDCouleur(); // Prise d'�chantillon par le robot
			
			if(echantillon==-1) { // Si le robot arrive au bord de la table, il fait demi tour
				rotation(/*temps n�cessaire pour faire 90�*/);
				avancerscenario();
			}
			
			if(echantillon==6) { // Si le robot d�tecte la couleur blanche, il s'arr�te, d�pose le palet et lib�re toutes les ressources, c'est la fin du sc�nario.
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