import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Moteurs {
	static 	UnregulatedMotor moteurGauche = new UnregulatedMotor (MotorPort.A); // Cr�ation de l'objet moteurGauche
    static UnregulatedMotor moteurDroit = new UnregulatedMotor (MotorPort.B); // Cr�ation de l'objet moteurDroit
    static UnregulatedMotor pinces = new UnregulatedMotor (MotorPort.C); // Cr�ation de l'objet pinces
        
        // CREEE POUR LES INTERSECTIONS NON-COUVERTES
    public static boolean avancer1(int a) // Fait suivre une ligne en zigzaguant peu, puis beaucoup � l'approche d'une couleur � d�tecter donn�e. Prend en entr�e un int qui est l'id de la couleur d'arr�t (a) et retourne un bool�en (pression) indiquant si un palet a �t� d�tect� ou non durant le d�placement du robot
        {
                int i; // D�claration de la variable i de type int qui d�signe le nombre d'aller-retours effectu�s par le robot entre le gris et la ligne � suivre
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                int ligne; // D�claration  de la variable ligne de type int qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
                boolean pression = false; // D�claration de la variable pression de type bool�en qui indique si le capteur tactile est press� ou non
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        ligne = couleur;
                        // PARTIE ZIGZAGS FAIBLES
                        for (i=0; i<2; i++)
                        {
                                while (couleur != 9) // Tant que la couleur d�tect�e est diff�rente du gris, le robot d�vie vers la gauche 
                                {
                                        moteurGauche.setPower(75);
                                        moteurDroit.setPower(100);
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                                while (couleur != ligne) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne, le robot d�vie vers la droite
                                {
                                        moteurGauche.setPower(100);
                                        moteurDroit.setPower(75);
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                        }
                        // PARTIE ZIGZAGS ELEVES
                        while (couleur != a) // Boucle qui s'arr�te lorsque la couleur d�tect�e est celle d'arr�t
                        {
                                while (couleur != 9 && couleur != a ) // Tant que la couleur d�tect�e est diff�rente du gris et de celle d'arr�t, le robot d�vie vers la gauche 
                                {
                                        moteurGauche.setPower(0);
                                        moteurDroit.setPower(60);
                                        if (pression == false) // // Permet d'enregistrer la d�tection d'un palet (s'il y a) pendant le d�placement du robot
                                        {
                                                pression = CapteurTactile.estPresse();
                                        }
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                                while (couleur != ligne && couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne et de celle d'arr�t, le robot d�vie vers la droite
                                {
                                        moteurGauche.setPower(60);
                                        moteurDroit.setPower(0);
                                        if (pression == false) // Permet d'enregistrer la d�tection d'un palet (s'il y a) pendant le d�placement du robot
                                        {
                                                pression = CapteurTactile.estPresse();
                                        }
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                        }
                        while (couleur != ligne) // Permet de replacer le robot sur sa ligne
                        {
                                moteurGauche.setPower(60);
                                moteurDroit.setPower(0);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                }
                return pression;
        }
    // CREEE POUR LES INTERSECTIONS COUVERTES
    public static boolean avancer2(int a) // Fait suivre une ligne zigzaguant peu jusqu'� ce que le robot d�tecte couleur donn�e. Prend en entr�e l'id de la couleur d'arr�t (a) et retourne un bool�en (pression) indiquant si un palet a �t� d�tect� ou non durant le d�placement du robot
        {
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                int ligne; // D�claration  de la variable ligne de type int qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
                boolean pression = false; // D�claration de la variable pression de type bool�en qui indique si le capteur tactile est press� ou non
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        ligne = couleur;
                        while (couleur != a) // Boucle qui s'arr�te lorsque la couleur d�tect�e est celle d'arr�t
                        {
                                while (couleur != 9 && couleur != a) // Tant que la couleur d�tect�e est diff�rente du gris et de celle d'arr�t, le robot d�vie vers la gauche 
                                {
                                        moteurGauche.setPower(75);
                                        moteurDroit.setPower(100);
                                        if (pression == false) // Permet d'enregistrer la d�tection d'un palet (s'il y a) pendant le d�placement du robot
                                        {
                                                pression = CapteurTactile.estPresse();
                                        }
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                                while (couleur != ligne && couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne et de celle d'arr�t, le robot d�vie vers la droite
                                {
                                        moteurGauche.setPower(100);
                                        moteurDroit.setPower(75);
                                        if (pression == false) // Permet d'enregistrer la d�tection d'un palet (s'il y a) pendant le d�placement du robot
                                        {
                                                pression = CapteurTactile.estPresse();
                                        }
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                        }
                }
                return pression;
        }
        
        public static void faireMarcheArriere(int a) // Fait suivre une ligne zigzaguant peu jusqu'� ce que le robot d�tecte couleur donn�e. Prend en entr�e l'id de la couleur d'arr�t (a) et ne retourne rien
        {
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                int ligne; // D�claration  de la variable ligne de type int qui va enregistrer l'id de la couleur de la ligne que le robot va suivre
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        ligne = couleur;
                        while (couleur != a) // Boucle qui s'arr�te lorsque la couleur d�tect�e est celle d'arr�t
                        {
                                while (couleur != 9 && couleur != a) // Tant que la couleur d�tect�e est diff�rente du gris et de celle d'arr�t, le robot d�vie vers la gauche 
                                {
                                        moteurGauche.setPower(-30);
                                        moteurDroit.setPower(-40);
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                                while (couleur != ligne && couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne et de celle d'arr�t, le robot d�vie vers la droite
                                {
                                        moteurGauche.setPower(-40);
                                        moteurDroit.setPower(-30);
                                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                                }
                        }
                }
        }
        
        public static void tournerGauche(int a) // Le robot tourne vers la gauche jusqu'� ce qu'il d�tecte la couleur d'arr�t. Prend en entr�e l'id de la couleur d'arr�t (a) et ne retourne rien
        {
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        while (couleur != 9) // Tant que la couleur d�tect�e est diff�rente du gris, le robot tourne vers la gauche 
                        {
                                moteurGauche.setPower(0);
                                moteurDroit.setPower(60);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                        while (couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle d'arr�t, le robot tourne vers la gauche
                        {
                                moteurGauche.setPower(0);
                                moteurDroit.setPower(60);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                }
        }        
        public static void tournerDroite(int a) // Le robot tourne vers la droite jusqu'� ce qu'il d�tecte la couleur d'arr�t. Prend en entr�e l'id de la couleur d'arr�t (a) et ne retourne rien
        {
                int couleur; // D�claration de la variable couleur qui va contenir l'id de la couleur d�tect�e
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        while (couleur != 9) // Tant que la couleur d�tect�e est diff�rente du gris, le robot tourne vers la droite 
                        {
                                moteurGauche.setPower(60);
                                moteurDroit.setPower(0);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                        while (couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle d'arr�t, le robot tourne vers la droite
                        {
                                moteurGauche.setPower(60);
                                moteurDroit.setPower(0);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                }
        }        
        public static void faireDemiTour(int a) // Fait tourner le robot sur lui-m�me jusqu'� ce qu'il d�tecte la couleur d'arr�t. Prend en entr�e l'id de la couleur d'arr�t (a) et ne retourne rien
        {
                int couleur; // D�claration de la variable couleur qui va contenir l'id de la couleur d�tect�e
                if (a != 0 && a != 1 && a != 2 && a != 3 && a != 6 && a != 7) // Si a n'est pas une couleur de ligne, la fonction affiche un message d'erreur
                {
                        System.out.println("Valeur de a incorrecte");
                }
                else // Sinon, la fonction fait son travail
                {
                        moteurGauche.setPower(-40); // Le robot tourne sur lui-m�me vers la gauche pendant une courte dur�e
                        moteurDroit.setPower(40);
                        Delay.msDelay(500);
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                        while (couleur != a) // Tant que la couleur d�tect�e est diff�rente de celle d'arr�t, le robot tourne sur lui-m�me vers la gauche
                        {
                                moteurGauche.setPower(-40);
                                moteurDroit.setPower(40);
                                couleur = CapteurPhotosensible.obtenirIDCouleur();
                        }
                }
        }
        
        public static boolean allerEmplacement(int i, boolean x) // Le robot se d�place jusqu'� l'emplacement donn�. Prend en entr�e le num�ro de l'emplacement (i) et le bool�en (x) indiquant si le robot a ramass� le palet de l'emplacement pr�c�dent ou non et retourne un bool�en (palet) indiquant si un palet � �t� d�tect� � l'emplacement donn�
        {
                Button.LEDPattern(4);
                boolean palet = false; // D�claration de la variable palet de type bool�en qui va indiquer si un palet a �t� d�tect� ou non lors du d�placement du robot
                switch (i)
                {
                        case 0 : // Le robot effectue cette instruction dans le cas o� il doit se rendre � l'emplacement 0
                                palet = avancer1(3);
                                break;
                        case 1 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 1
                                if (x == true)
                                {
                                        faireDemiTour(3);
                                }
                                palet = avancer2(2);
                                break;
                        case 2 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 2
                                if (x == true)
                                {
                                        faireDemiTour(3);
                                        avancer2(2);
                                }
                                tournerGauche(2);
                                palet = avancer1(7);
                                break;
                        case 3 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 3
                                if (x == true)
                                {
                                        faireDemiTour(7);
                                        avancer2(2);
                                        tournerGauche(2);
                                }
                                palet = avancer1(0);
                                break;
                        case 4 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 4
                                if (x == true)
                                {
                                        faireDemiTour(0);
                                }
                                else
                                {
                                        tournerDroite(0);
                                }
                                palet = avancer2(7);
                                break;
                        case 5 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 5
                                if (x == true)
                                {
                                        faireDemiTour(0);
                                        avancer2(7);
                                }
                                tournerDroite(7);
                                palet = avancer4();
                                break;
                        case 6 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 6
                                if (x == true)
                                {
                                        faireDemiTour(7);
                                        avancer2(2);
                                        avancer4();
                                        tournerDroite(7);
                                }
                                palet = avancer1(3);
                                break;
                        case 7 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 7
                                if (x == true)
                                {
                                        faireDemiTour(3);
                                }
                                else
                                {
                                        tournerGauche(3);
                                }
                                avancer2(1);
                                tournerGauche(1);
                                palet = avancer1(7);
                                break;
                        case 8 : // Le robot effectue ces instructions dans le cas o� il doit se rendre � l'emplacement 8
                                if (x == true)
                                {
                                        faireDemiTour(7);
                                        avancer2(1);
                                        tournerGauche(1);
                                }
                                palet = avancer1(0);
                                break;
                        default : // La fonction affiche un message d'erreur dans le cas o� le num�ro d'emplacement entr� n'est pas valide
                                System.out.println("Valeur de i incorrecte");
                                break;
                }
                return palet;
        }
        public static void ramasserPalet(int i) // Le robot saisit un palet et le d�place jusqu'� la ligne blanche adverse pour le d�poser. Prend en entr�e le num�ro de l'emplacement (i) et ne retourne rien
        {
                Button.LEDPattern(6);
                moteurGauche.setPower(0); // Le robot s'arr�te et saisit le palet
                moteurDroit.setPower(0);
                pinces.setPower(-100);
                Delay.msDelay(1100);
                pinces.setPower(0);
                switch (i)
                {
                        case 0 : // Le robot effectue cette instruction dans le cas o� il a saisi le palet � l'emplacement 0
                                avancer3();
                                break;
                        case 1 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 1
                                faireDemiTour(3);
                                avancer2(6);
                                break;
                        case 2 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 2
                                tournerGauche(7);
                                avancer2(6);
                                break;
                        case 3 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 3
                                tournerGauche(0);
                                avancer2(6);
                                break;
                        case 4 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 4
                                faireDemiTour(0);
                                avancer2(6);
                                break;
                        case 5 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 5
                                tournerDroite(7);
                                avancer1(6);
                                break;
                        case 6 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 6
                                tournerDroite(3);
                                avancer1(6);
                                break;
                        case 7 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 7
                                tournerGauche(7);
                                avancer1(6);
                                break;
                        case 8 : // Le robot effectue ces instructions dans le cas o� il a saisi le palet � l'emplacement 8
                                tournerGauche(0);
                                avancer1(6);
                                break;
                        default : // La fonction affiche un message d'erreur dans le cas o� le num�ro d'emplacement entr� n'est pas valide
                                System.out.println("Valeur de i incorrecte");
                                break;
                }
                moteurGauche.setPower(-40); // Le robot l�che le palet en reculant
                moteurDroit.setPower(-40);
                pinces.setPower(100);
                Delay.msDelay(1000);
                pinces.setPower(0);
        }        
        
        // CREEE POUR RAMASSERPALET(0)
        public static void avancer3() // Le robot se d�place jusqu'� la ligne blanche adverse. Ne prend rien en entr�e et ne retourne rien
        {
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                couleur = CapteurPhotosensible.obtenirIDCouleur();
                while (couleur != 7) // Tant que la couleur d�tect�e est diff�rente du noir, le robot d�vie vers la gauche
                {
                        moteurGauche.setPower(75);
                        moteurDroit.setPower(100);
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                }
                while (couleur != 6) // Tant que la couleur d�tect�e est diff�rente du blanc, le robot d�vie vers la droite
                {
                        moteurGauche.setPower(100);
                        moteurDroit.setPower(90);
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                }
        }
        
        // CREEE POUR L'INTERSECTION NOIRE/NOIRE
        public static boolean avancer4() // Le robot suit une ligne noire jusqu'� ce qu'il d�tecte une autre ligne noire. Ne prend rien en entr�e et retourne un bool�en (pression) indiquant si un palet a �t� d�tect� ou non durant le d�placement du robot
        {
                int couleur; // D�claration de la variable couleur de type int qui va contenir l'id de la couleur d�tect�e
                boolean pression = false; // D�claration de la variable pression de type bool�en qui indique si le capteur tactile est press� ou non
                couleur = CapteurPhotosensible.obtenirIDCouleur();
                while (couleur != 7) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne, le robot d�vie vers la droite
                {
                        moteurGauche.setPower(60);
                        moteurDroit.setPower(0);
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                }
                while (couleur != 9) // Tant que la couleur d�tect�e est diff�rente du gris, le robot d�vie vers la gauche 
                {
                        moteurGauche.setPower(0);
                        moteurDroit.setPower(100);
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                }
                while (couleur != 7) // Tant que la couleur d�tect�e est diff�rente de celle de la ligne, le robot d�vie vers la droite
                {
                        moteurGauche.setPower(100);
                        moteurDroit.setPower(95);
                        if (pression == false) // Permet d'enregistrer la d�tection d'un palet (s'il y a) pendant le d�placement du robot
                        {
                                pression = CapteurTactile.estPresse();
                        }
                        couleur = CapteurPhotosensible.obtenirIDCouleur();
                }
                return pression;
        }
      //Cette classe permet de faire arreter le robot
    	public void arreter1() {
    		
    		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
    		Motor.B.startSynchronization();
    		Motor.B.stop();			// on arrete les deux moteurs
    		Motor.A.stop();
    		Motor.B.endSynchronization();		// on arrete la synchronisation
    		Delay.msDelay(100);				// on laisse un petit delai de 100 ms pour un peu plus de fluidit�
    	}
        public static void fermer3() // Lib�re les ressources des moteurs
        {
                moteurGauche.close();
                moteurDroit.close();
                pinces.close();
        }
}
