package projet;

import java.io.IOException;

import projet2.Fichier;

/**
 * Cette classe contient la méthode qui lance le programme.
 */
public class Main
{
	/**
	 * Chemin vers un fichier texte qui représente une communauté d’agglomération.
	 */
	public static String cheminFichier;
	
	/**
	 * <p>Lance le programme.</p>
	 * 
	 * Chargeement du fichier dont le chemin est pris en entrée sur la ligne de commande via {@link Fichier#chargerFichier()}.<br>
	 * Si elle retourne <b>-2</b>: arrêt du programme.<br>
	 * Si elle retourne <b>-1</b>: avertissement informations manquantes et demande si poursuite ou pas avec complétion des informations manquantes via {@link Scan#saisieEntier()}, en vérifiant que le choix est égal à 1 ou 2. Si le choix est 2, arrêt du programme. Sinon, affichage du menu principal.<br>
	 * Si elle retourne <b>0</b>: affichage du menu principal.
	 * Demande du choix via {@link Scan#saisieEntier()} en vérifiant qu'il est égal à 1,2, 3 ou 4.<br>
	 * Si le choix est 1: configuration des écoles manuellement via {@link Menu#configurationEcolesManu()} et réaffichage du menu principal.<br>
	 * SI le choix est 2: configuration des écoles automatiquement via {@link Menu#configurationEcolesAuto()} et réaffichage du menu principal.<br>
	 * Si le choix est 3: sauvegarde les informations de la communauté d'agglomération dans un fichier via {@link Fichier#chargerFichier()} et réaffichage du menu principal.<br>
	 * Si le choix est 4: arrêt du programme.
	 * 
	 * @param args paramètres de la ligne de commande
	 * @throws IOException exception d'entrée/sortie
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println("Lancement du programme...");
		
		// PARTIE 2 DU PROJET
		
		cheminFichier = args[0];
		
		int erreur = Fichier.getFichier().chargerFichier();
		
		// Choix de l'action à effectuer
		int choix;
		
		switch (erreur)
		{
			case -2:
				System.out.println("Le fichier ne peut pas être chargé, veuillez le modifier.");
				break;
			
			case -1:
				System.out.println("Le fichier contient des informations manquantes, ces dernières seront supposées. Voulez-vous continuer ?");
				
				do
				{
					System.out.println("1) Oui");
					System.out.println("2) Non");
					choix = Scan.getScan().saisieEntier();	
				}
				while (choix != 1 && choix != 2);
				
				if (choix == 2)
				{	
					break;
				}
			case 0:				
				do
				{
					do
					{
						System.out.println();
						System.out.println("Que voulez-vous faire ?");
						System.out.println("1) Résoudre manuellement");
						System.out.println("2) Résoudre automatiquement");
						System.out.println("3) Sauvegarder");
						System.out.println("4) Fin");
						choix = Scan.getScan().saisieEntier();
						
						if (choix != 1 && choix != 2 && choix != 3 && choix != 4)
						{
							System.out.println("Choix incorrect.");
						}
					}
					while (choix != 1 && choix != 2 && choix != 3 && choix != 4);
					
					switch (choix)
					{
						case 1:
							Menu.getMenu().configurationEcolesManu();
							break;
							
						case 2:
							Menu.getMenu().configurationEcolesAuto();
							break;
						
						case 3:
							Fichier.getFichier().sauvegarderFichier();
					}
				}
				while (choix != 4);
				
		}
		// PARTIE 1 DU PROJET
		/*Menu.getMenu().creationVilles();
		Menu.getMenu().configurationRoutes();
		Menu.getMenu().configurationEcolesManu();*/
		System.out.println("Arrêt du programme...");
	}
}