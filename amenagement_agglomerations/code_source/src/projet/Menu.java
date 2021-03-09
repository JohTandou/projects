package projet;

/**
 * Cette classe regroupe des méthodes qui sont les menus du programme.
 */
public class Menu
{	
	/**
	 * Instance de la classe {@link #Menu} pour accéder aux différents menus.
	 */
	private static Menu menu = new Menu();
	
	/**
	 * Retourne l'instance de la classe {@link #Menu}.
	 * 
	 * @return instance de la classe {@link #Menu}
	 */
	public static Menu getMenu()
	{
		return menu;
	}
	
	/**
	 * <p>Menu concernant la création des villes de la communauté d'agglomération.</p>
	 * 
	 * Demande du nombre de villes via {@link Scan#saisieEntier()} en vérifiant qu'il est supérieur à 1,
	 * puis demande du nom de chaque ville via {@link Scan#saisieNomVille()}
	 * et ajout dans {@link Ville#agglomeration} en vérifiant qu'elle n'existe pas déjà via {@link Ville#villeExistante(String)}.
	 * Enfin, affichage des informations concernant l'agglomération via {@link Ville#infosAgglomeration()}.
	 */
	public void creationVilles()
	{
		// Compteur
		int i = 0;
		
		// Nombre de villes que contiendra la communauté d'agglomération
		int n;
		
		// Nom de la ville à créer
		String nomVille;
		
		do
		{
			System.out.println();
			System.out.println("Entrez le nombre de villes (> 1 et <27) que va contenir la communauté d'agglomération: ");
			n = Scan.getScan().saisieEntier();
			
			if (n < 2 || n > 26)
			{
				System.out.println("Ce que vous avez entré est incorrect.");
			}
		}
		while (n < 2 ||  n > 26);
		
		Scan.sc.nextLine();
		
		for (i=0; i<n; i++)
		{	
			nomVille = String.valueOf((char) (65+i));
			Ville.agglomeration.add(new Ville(nomVille, null, true));
		}
		if (Ville.nbEcolesRecord == 0)
		{
			for (i = 0; i < Ville.agglomeration.size(); i++)
			{
				if (Ville.agglomeration.get(i).isEcole())
				{
					Ville.nbEcolesRecord += 1;
				}
			}
		}
		Ville.infosAgglomeration();
	}
	
	/**
	 * <p>Menu concernant la configuration des routes entres les villes de la communauté d'agglomération.</p>
	 * 
	 * Demande du choix via {@link Scan#saisieEntier()} en vérifiant qu'il est égal à 1 ou 2.<br>
	 * Si le choix est 1: demande des noms des villes à lier par une route en vérifiant qu'ils sont différents et font partie de l'agglomération via {@link Ville#villeExistante(String)},
	 * puis ajout de la route via {@link Ville#ajoutRoute(String)},
	 * affichage des informations concernant l'agglomération via {@link Ville#infosAgglomeration()}
	 * et réaffichage du menu.<br>
	 * Si le choix est 2: sortie du menu.
	 */
	public void configurationRoutes()
	{
		// Choix de l'action à effectuer
		int choix;
		
		// Nom de la 1ère ville à lier
		String nomVille1;
		
		// Nom de la 2ème ville à lier
		String nomVille2;
		
		// Indiquera si une route existe déjà entre deux villes
		boolean routeExistante;
		
		do
		{
			do
			{
				System.out.println();
				System.out.println("Que voulez-vous faire ?");
				System.out.println("1) Ajouter une route");
				System.out.println("2) Fin");
				choix = Scan.getScan().saisieEntier();
				
				if (choix != 1 && choix != 2)
				{
					System.out.println("Choix incorrect.");
				}
			}
			while (choix != 1 && choix != 2);
			
			Scan.sc.nextLine();
			
			switch (choix)
			{
				case 1:
					
					do
					{
						System.out.println();
						System.out.println("Entrez le nom de la 1ère ville à lier: ");
						nomVille1 = Scan.getScan().saisieNomVille();
						
						if (nomVille1 == null || !Ville.villeExistante(nomVille1))
						{
							System.out.println("Cette ville ne fait pas partie de la communauté d'agglomération.");
						}
					}
					while (nomVille1 == null || !Ville.villeExistante(nomVille1));
					
					do
					{
						System.out.println();
						System.out.println("Entrez le nom de la 2ème ville à lier: ");
						nomVille2 = Scan.getScan().saisieNomVille();
						
						if (nomVille2 == null || !Ville.villeExistante(nomVille2))
						{
							System.out.println("Cette ville ne fait pas partie de la communauté d'agglomération.");
						}
						else if (nomVille2 != null && nomVille2.equals(nomVille1))
						{
							System.out.println("Entrez une ville différente de " + nomVille1 + ".");
						}
					}
					while (nomVille2 == null || !Ville.villeExistante(nomVille2) || nomVille2.equals(nomVille1));
					
					routeExistante = Ville.agglomeration.get(Ville.positionVille(nomVille1)).ajoutRoute(nomVille2);
					
					if (routeExistante == true)
					{
						System.out.println("Il y a déjà une route entre " + nomVille1 + " et " + nomVille2 + ".");
					}
					else
					{
						Ville.agglomeration.get(Ville.positionVille(nomVille2)).ajoutRoute(nomVille1);
						System.out.println("Route entre " + nomVille1 + " et " + nomVille2 + " ajoutée.");
						Ville.infosAgglomeration();
					}
			}
		}
		while(choix != 2);
	}
	
	/**
	 * <p>Menu concernant la configuration manuelle des écoles des villes de la communauté d'agglomération.</p>
	 * 
	 * Si la contrainte d'accessibilité n'est pas respectée, affichage des villes sans école dans leur voisinage et ajout d'une école dans chaque ville.<br>
	 * Demande du choix via {@link Scan#saisieEntier()} en vérifiant qu'il est égal à 1, 2 ou 3.<br>
	 * Si le choix est 1: demande du nom de la ville à laquelle on va ajouter une école en vérifiant qu'elle partie de l'agglomération via {@link Ville#villeExistante(String)},
	 * puis ajout de l'école si cela est possible,
	 * affichage des informations concernant l'agglomération via {@link Ville#infosAgglomeration()}
	 * et réaffichage du menu.<br>
	 * Si le choix est 2: demande du nom de la ville à laquelle on va retirer une école en vérifiant qu'elle partie de l'agglomération via {@link Ville#villeExistante(String)},
	 * puis retrait de l'école si cela est possible et si {@link Ville#contrainteRespectee()} est vrai,
	 * affichage des informations concernant l'agglomération via {@link Ville#infosAgglomeration()}
	 * et réaffichage du menu.<br>
	 * Si le choix est 3: sortie du menu.
	 */
	public void configurationEcolesManu()
	{
		// Choix de l'action à effectuer
		int choix;
		
		// Identifiant de la ville qui permettra l'ajout/retrait d'une école
		String nomVille;
		
		// Initialement, la contrainte est respectée tant qu'on ne l'a pas vérifié
		boolean contrainteRespectee = true;
		
		for (int i = 0; i < Ville.agglomeration.size(); i++)
		{	
			
			if (!Ville.agglomeration.get(i).contrainteRespectee())
			{
				contrainteRespectee = false;
			}
		}
		if (contrainteRespectee == false)
		{
			System.out.println("La contrainte d'accessibilité n'est pas respectée, " + Ville.nomsVillesSansEcole + " n'a/ont pas d'école dans son/leur voisinage. Une école sera ajoutée dans chaque ville.");
			Ville.nomsVillesSansEcole.clear();
			for (int j = 0; j < Ville.agglomeration.size(); j++)
			{	
				Ville.agglomeration.get(j).setEcole(true);
			}
		}
		
		if (Ville.nbEcolesRecord == 0)
		{
			for (int i = 0; i < Ville.agglomeration.size(); i++)
			{
				if (Ville.agglomeration.get(i).isEcole())
				{
					Ville.nbEcolesRecord += 1;
				}
			}
		}
		Ville.infosAgglomeration();
		
		do
		{
			do
			{
				System.out.println();
				System.out.println("Que voulez-vous faire ?");
				System.out.println("1) Ajouter une école");
				System.out.println("2) Retirer une école");
				System.out.println("3) Fin");
				choix = Scan.getScan().saisieEntier();
				
				if (choix != 1 && choix != 2 && choix != 3)
				{
					System.out.println("Choix incorrect.");
				}
			}
			while (choix != 1 && choix != 2 && choix != 3);
			
			Scan.sc.nextLine();
			
			switch (choix)
			{
				case 1:
					do
					{
						System.out.println();
						System.out.println("Entrez le nom de la ville: ");
						nomVille = Scan.getScan().saisieNomVille();
						
						if (nomVille == null || !Ville.villeExistante(nomVille))
						{
							System.out.println("Cette ville ne fait pas partie de la communauté d'agglomération.");
						}
					}
					while (nomVille == null || !Ville.villeExistante(nomVille));
					
					if (Ville.agglomeration.get(Ville.positionVille(nomVille)).isEcole())
					{
						System.out.println("Il y a déjà une école dans " + nomVille + ".");
					}
					else
					{
						Ville.agglomeration.get(Ville.positionVille(nomVille)).setEcole(true);
						System.out.println("Ecole ajoutée dans " + nomVille + ".");
						Ville.infosAgglomeration();
					}
					break;
					
				case 2:
					do
					{
						System.out.println();
						System.out.println("Entrez le nom de la ville: ");
						nomVille = Scan.getScan().saisieNomVille();
						
						if (nomVille == null || !Ville.villeExistante(nomVille))
						{
							System.out.println("Cette ville ne fait pas partie de la communauté d'agglomération.");
						}
					}
					while (nomVille == null || !Ville.villeExistante(nomVille));
					
					if (!Ville.agglomeration.get(Ville.positionVille(nomVille)).isEcole())
					{
						System.out.println("Il n'y a déjà pas d'école dans " + nomVille + ".");
					}
					else
					{
						Ville.agglomeration.get(Ville.positionVille(nomVille)).setEcole(false);
						
						if (Ville.agglomeration.get(Ville.positionVille(nomVille)).contrainteRespectee())
						{
							System.out.println("Ecole de " + nomVille + " retirée.");
							Ville.infosAgglomeration();
						}
						else
						{
							Ville.agglomeration.get(Ville.positionVille(nomVille)).setEcole(true);
							System.out.println("Retrait de l'école de " + nomVille + " impossible, " + Ville.nomsVillesSansEcole + " se retrouverai(en)t sans école dans son/leur voisinage (contrainte d'accessibilité).");
							Ville.nomsVillesSansEcole.clear();
						}
					}
			}
		}
		while (choix !=3);
	}
	
	/**
	 * <p>Configure automatiquement les écoles des villes de la communauté d'agglomération.</p>
	 * 
	 * Retrait des écoles dans toutes les villes.<br>
	 * Si l'agglomération est de taille 2: 1 école dans chaque ville s'il n'y a pas de route, 1 école dans une des villes sinon.<br>
	 * Si l'agglomération est de taille supérieure à 2:<br>
	 * - Pour chaque ville: ajout d'une école si elle n'a pas de voisine, ou si elle est liée à une ville qui n'a qu'une voisine et n'a pas d'école, ou si elle a une voisine qui n'a qu'une voisine.<br>
	 * - Puis pour chaque ville: ajout d'une école si aucune de ses voisines n'a d'école et qu'aucune voisine de ses voisines n'a d'école.<br>
	 * - Puis pour chaque ville: ajout d'une école dans la ville de son entourage ayant l'{@link Ville#importance()} la plus grande, si aucune de ses voisines n'a d'école.<br>
	 * Affichage des informations concernant l'agglomération via {@link Ville#infosAgglomeration()}.
	 */
	public void configurationEcolesAuto()
	{
		// Contiendra l'identifiant de chaque voisine
		String nomVilleVoisine;
		
		// Indiquera s'il est nécessaire de mettre une école dans une ville
		boolean ecoleNecessaire = true;
		
		// Contiendra la valeur d'importance la plus élevée
		int importanceMax;
		
		// Contiendra le nom de la ville la plus importante
		String nomVilleImportante;
		
		System.out.println();
		System.out.println("Configuration automatique...");
		for (int i = 0; i < Ville.agglomeration.size(); i++)
		{			
			Ville.agglomeration.get(i).setEcole(false);
		}
		switch (Ville.agglomeration.size())
		{
			case 2:
				if (Ville.agglomeration.get(0).getNomsVillesVoisines() == null)
				{
					Ville.agglomeration.get(0).setEcole(true);
					Ville.agglomeration.get(1).setEcole(true);
				}
				else
				{
					Ville.agglomeration.get(0).setEcole(true);
					Ville.agglomeration.get(1).setEcole(false);
				}
				break;
			
			default:
				for (int i = 0; i < Ville.agglomeration.size(); i++)
				{
					if (Ville.agglomeration.get(i).getNomsVillesVoisines() == null)
					{
						Ville.agglomeration.get(i).setEcole(true);
					}
					else
					{	
						switch (Ville.agglomeration.get(i).getNomsVillesVoisines().size())
						{
							case 1:
								nomVilleVoisine = Ville.agglomeration.get(i).getNomsVillesVoisines().get(0);
								
								if (Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).getNomsVillesVoisines().size() == 1)
								{
									if (!Ville.agglomeration.get(i).isEcole() && !Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).isEcole())
									{
										Ville.agglomeration.get(i).setEcole(true);
									}
								}
								break;
							
							default:
								for (int j = 0; j < Ville.agglomeration.get(i).getNomsVillesVoisines().size(); j++)
								{
									nomVilleVoisine = Ville.agglomeration.get(i).getNomsVillesVoisines().get(j);
									
									if (Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).getNomsVillesVoisines().size() == 1)
									{
										Ville.agglomeration.get(i).setEcole(true);
									}
								}
						}
					}
				}
				for (int i = 0; i < Ville.agglomeration.size(); i++)
				{
					if (Ville.agglomeration.get(i).getNomsVillesVoisines() != null && Ville.agglomeration.get(i).getNomsVillesVoisines().size() > 1 && !Ville.agglomeration.get(i).isEcole() && !Ville.agglomeration.get(i).villeVoisineAUneEcole())
					{
						for (int j = 0; j < Ville.agglomeration.get(i).getNomsVillesVoisines().size(); j++)
						{
							nomVilleVoisine = Ville.agglomeration.get(i).getNomsVillesVoisines().get(j);
							
							if (Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).villeVoisineAUneEcole())
							{
								ecoleNecessaire = false;
							}
						}
						if (ecoleNecessaire == true)
						{
							Ville.agglomeration.get(i).setEcole(true);
						}
						ecoleNecessaire = true;
					}
				}
				for (int i = 0; i < Ville.agglomeration.size(); i++)
				{
					if (Ville.agglomeration.get(i).getNomsVillesVoisines() != null && Ville.agglomeration.get(i).getNomsVillesVoisines().size() > 1 && !Ville.agglomeration.get(i).isEcole() && !Ville.agglomeration.get(i).villeVoisineAUneEcole())
					{
						importanceMax = Ville.agglomeration.get(i).importance();
						nomVilleImportante = Ville.agglomeration.get(i).getNomVille();
						
						for (int j = 0; j < Ville.agglomeration.get(i).getNomsVillesVoisines().size(); j++)
						{
							nomVilleVoisine = Ville.agglomeration.get(i).getNomsVillesVoisines().get(j);
							
							if (Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).importance() > importanceMax)
							{
								importanceMax = Ville.agglomeration.get(Ville.positionVille(nomVilleVoisine)).importance();
								nomVilleImportante = nomVilleVoisine;
							}
						}
						Ville.agglomeration.get(Ville.positionVille(nomVilleImportante)).setEcole(true);
					}

				}
		}
		if (Ville.nbEcolesRecord == 0)
		{
			for (int i = 0; i < Ville.agglomeration.size(); i++)
			{
				if (Ville.agglomeration.get(i).isEcole())
				{
					Ville.nbEcolesRecord += 1;
				}
			}
		}
		Ville.infosAgglomeration();
	}
}