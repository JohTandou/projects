package ev3;



public class Main { // Classe d'execution du sc�nario. Cette classe utilise les classes Brique et Moteurs.

	
	/* Code du sc�nario 6 : Ce qu'il manque :
				De nombreuses mesures de temps (Voir commentaire des fonctions de la classe Moteurs)
				Changer les conditions de fin des fonctions SuivreADroite et SuivreAGauche
				Mesures concernant le capteur � ultrason
				
				Concernant cette version : Le code n'a pas encore pu �tre test�.
																							*/
	
	
	public static void main(String[] args) {
		
		
		
		Brique.console.pretAuDemarrage();// Le robot indique qu'il est pr�t. Il faut appuyer sur un bouton.
		Moteurs.AllerAuCentre();// Le robot va au centre de la surface de jeu (Intersection Noire-Noire).
		Moteurs.DetecterEtSaisirPalet(); // Le robot tourne sur lui m�me jusqu'a d�tecter un objet situ� � une distance inf�rieure au rayon de la surface de jeu. Il va ensuite tout droit et le saisit.
		Moteurs.DeposerPalet(); // Le robot recherche la ligne blanche. D�s qu'il la trouve, il d�pose le palet et s'arr�te.
		
	
	}
	

}
