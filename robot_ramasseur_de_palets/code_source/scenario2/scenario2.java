public class scenario2
{
	private static final int ROUGE = 0;
	private static final int VERT = 1;
	private static final int BLEU = 2;
	private static final int JAUNE = 3;
	private static final int BLANC = 6;
	private static final int NOIR = 7;
	private static final int GRIS = 9;
	
	private static final boolean EST = true;
	private static final boolean OUEST = false;
	
	private static final int POS1 = 1;
	private static final int POS2 = 2;
	private static final int POS3 = 3;
	
	public static void go(int color, int color1, boolean pression) 
// fonction: avance jusqu'à detection de la couleur visee ou jusqu'a la rencontre de pression
// entree: 
// color: couleur d'arret
// color1: couleur suivie
// pression: definit la pression pour savoir si oui ou non le robot continue
	{
		int col;
		while ((col != color) || (pression == true)) 
		{
			while (col != GRIS )
			{
				moteurGauche.setPower(80);
				moteurDroit.setPower(100);
				col = CapteurPhotosensible.obtenirIDCouleur;
			}	
			while (col != color1)
			{
				moteurGauche.setPower(100);
				moteurDroit.setPower(80);
				col = CapteurPhotosensible.obtenirIDCouleur;
			}
		}
	}
	
	public static void turnd(int color) 
// tourne a droite, jusqu'à color, couleur d'arret
// entree: color, couleur d'arret a detecter en tournant a droite
	{
		int col;
		while (col != color && col != GRIS)
		{
			moteurGauche.setPower(60);
			moteurDroit.setPower(0);
			Delay.msDelay(500);
			col = CapteurPhotosensible.obtenirIDCouleur();
		}
	}
	
	public static void turng(int color) 
// tourne a gauche, jusqu'à color, couleur d'arret
// entree: 
// color, couleur d'arret a detecter en tournant a gauche
	{
		int col;
		while (col != color && col != GRIS)
		{
			moteurGauche.setPower(0);
			moteurDroit.setPower(60);
			Delay.msDelay(500);
			col = CapteurPhotosensible.obtenirIDCouleur();
		}
	}
	
	public static void detected(int color, boolean press) 
// tourne a droite et depose le palet derriere la ligne blanche apres avoir detectee un palet
// entree: 
// color, couleur a detecter pour tourner a droite
// press, correspond a la pression pour savoir si oui ou non le robot continue
	{
		int col;
		col = CapteurPhotosensible.obtenirIDCouleur();

		if (press == true)
		{
			pinces.setPower(-100);
			turnd(color);
			go(BLANC, color, press);
			pinces.setPower(100);
		}	
	}
	
	public static void detectedX2(int color, int color2, boolean press) 
// tourne a droite deux fois et depose le palet derriere la ligne blanche apres avoir dectectee un palet
// entree: 
// color, premiere couleur a detecter pour s'arreter en tournant a droite
// color2, deuxieme couleur a detecter pour s'arreter en tournant a droite
// press, correspond à la pression pour savoir si oui ou non le robot continue	
	{
		int col;
		col = CapteurPhotosensible.obtenirIDCouleur();

		if (press == true)
		{
			pinces.setPower(-100);
			turnd(color);
			turnd(color2);
			go(BLANC, color2, press);
			pinces.setPower(100);
		}	
	}
	
	public static void detecteg(int color, boolean press) 
// tourne a gauche et depose le palet derriere la ligne blanche apres avoir detectee un palet
// entree:
// color, couleur a detecter pour s'arreter en tournant a gauche
// press, correspond à la pression pour savoir si oui ou non le robot continue
	{
		int col;
		col = CapteurPhotosensible.obtenirIDCouleur();
		
		if (pression == true)
		{
			pinces.setPower(-100);
			turng(color);
			go(BLANC, color, press);
			pinces.setPower(100);
		}
	}
	
	public static void detectegX2(int color, int color2, boolean press) 
// tourne a gauche deux fois et depose le palet derriere la ligne blanche apres avoir detectee un palet
// entree: 
// color, premiere couleur a detecter pour s'arreter en tournant a gauche
// color2, deuxieme couleur a detecter pour s'arreter en tournant a gauche
// press, correspond à la pression pour savoir si oui ou non le robot continue
	{
		int col;
		col = CapteurPhotosensible.obtenirIDCouleur();
		
		if (press == true)
		{
			pinces.setPower(-100);
			turng(color);
			turng(color2);
			go(BLANC, color2, press);
			pinces.setPower(100);
		}
	}
	
	public static void main(String[] args)
	{
		int couleurEst1 = ROUGE; 
		int couleurEst2 = VERT; 
		int couleurEst3 = BLEU; 
		int couleurEst4 = JAUNE;
		int couleurEst5 = BLANC;
		int couleurEst6 = NOIR; 
		int couleurEst7 = GRIS; 
		
		boolean cote;
		int pos = POS1 ;
		boolean pression = false;
		
		public static void posEst(int position) 
// trajet 
// entree
// int position, determine le camp de départ
		{
			int couleur = CapteurPhotosensible.obtenirIDCouleur;
			int ligne;
			boolean pression = false;
			pression = CapteurTacile.estPresse();
			
			go(couleurEst3, couleurEst4, pression); 
			// Premiere intersection
			
			detecteg(couleurEst3, pression);
			turng(couleurEst2);
			
			go(couleurEst2, couleurEst6, pression); 
			// Deuxieme intersection
			
			detecteg(couleurEst6, pression);
			
			go(couleurEst2, couleurEst1, pression); 
			// Troisieme intersection
			
			dectecteg(couleurEst2, pression);
			turnd(couleurEst1);
			
			go(couleurEst1, couleurEst6, pression); 
			// Quatrieme intersection
			
			turnd(couleurEst6);
			detectedX2(couleurEst6, couleurEst1, pression);
			
			go(couleurEst6, couleurEst5, pression); 
			// Cinquieme intersection
			
			detected(couleurEst5, pression);
			
			go(couleurEst6, couleurEst4, pression); 
			// Sixieme intersection
			
			detected(couleurEst4, pression);
			turng(couleurEst4);
			
			go(couleurEst4, couleurEst2, pression); 
			// Septieme intersection
			
			detectegX2(couleurEst4, couleurEst2, pression);
			turng(couleurEst2);
			
			go(couleurEst6, couleurEst2,  pression); 
			// Huitieme intersection
			
			dectecteg(couleurEst6);
			
			go(couleurEst2, couleurEst1, pression); 
			// Derniere intersection
			
			detecteg(couleurEst2, pression);
			
		}
		
		if (cote == EST) 
		// position de depart du robot
			
		{
			if (pos == POS1)
			{
				posEst(POS1);
			}
		
			else if (pos == POS2)
			{
				turnd(couleurEst5);
				go(couleurEst5, couleurEst4, pression);
				turng(couleurEst4);
				posEst(POS1);
			}
				
			else if (pos == POS3)
			{
				turnd(couleurEst5);
				go(couleurEst5,couleurEst4, pression);
				turng(couleurEst4);
				posEst(POS1);
			}	
		}
		
		else if (cote == OUEST)
		{
			couleurEst1 = JAUNE; // ROUGE miroir
			couleurEst2 = BLEU; // VERT miroir 
			couleurEst3 = VERT; // BLEU miroir
			couleurEst4 = ROUGE; // JAUNE miroir 
			couleurEst5 = BLANC; // BLANC
			couleurEst6 = NOIR; // NOIR
			couleurEst7 = GRIS; // GRIS
			
			if (pos == POS1)
			{
				posEst(POS1);
			}
			
			else if (pos == POS2)
			{
				turnd(couleurEst5);
				go(couleurEst5 ,couleurEst4,pression);
				turng(couleurEst4);
				posEst(POS1);
			}
			
			else if (pos == POS3)
			{
				turnd(couleurEst5);
				go(couleurEst5 ,couleurEst4, pression);
				turng(couleurEst4);
				posEst(POS1);
			}
			
		}
	}
}
