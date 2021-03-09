package ev3;



public class Main { // Classe d'execution du scénario. Cette classe utilise les classes Brique et Moteurs.

	
	/* Code du scénario 6 : Ce qu'il manque :
				De nombreuses mesures de temps (Voir commentaire des fonctions de la classe Moteurs)
				Changer les conditions de fin des fonctions SuivreADroite et SuivreAGauche
				Mesures concernant le capteur à ultrason
				
				Concernant cette version : Le code n'a pas encore pu être testé.
																							*/
	
	
	public static void main(String[] args) {
		
		
		
		Brique.console.pretAuDemarrage();// Le robot indique qu'il est prêt. Il faut appuyer sur un bouton.
		Moteurs.AllerAuCentre();// Le robot va au centre de la surface de jeu (Intersection Noire-Noire).
		Moteurs.DetecterEtSaisirPalet(); // Le robot tourne sur lui même jusqu'a détecter un objet situé à une distance inférieure au rayon de la surface de jeu. Il va ensuite tout droit et le saisit.
		Moteurs.DeposerPalet(); // Le robot recherche la ligne blanche. Dès qu'il la trouve, il dépose le palet et s'arrête.
		
	
	}
	

}
