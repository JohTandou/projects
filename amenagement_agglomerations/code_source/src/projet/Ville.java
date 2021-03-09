package projet;

import java.util.ArrayList;

/**
 * Cette classe regroupe le constructeur Ville, la liste des villes et les méthodes associées aux villes.
 */
public class Ville
{	
	/**
	 * Nom de la ville.
	 */
	private String nomVille;
	
	/**
	 * Noms des voisines de la ville.
	 */
	private ArrayList<String> nomsVillesVoisines;
	
	/**
	 * La ville a une école ou pas.
	 */
	private boolean ecole;
	
	/**
	 * Villes de la communauté d'agglomération.
	 */
	public static ArrayList<Ville> agglomeration = new ArrayList<Ville>();
	
	/**
	 * Noms des villes qui n'ont pas d'école dans leur voisinage.
	 */
	public static ArrayList<String> nomsVillesSansEcole = new ArrayList<String>();
	
	/**
	 * Meilleur nombre d'écoles atteint.
	 */
	public static int nbEcolesRecord = 0;
	
	/**
	 * Constructeur de la classe {@link Ville}, crée une ville.
	 * 
	 * @param nomVille nom de la ville
	 * @param nomsVillesVoisines liste des noms des voisines de la ville
	 * @param ecole oui ou non
	 */
	public Ville(String nomVille, ArrayList<String> nomsVillesVoisines, boolean ecole)
	{
		this.setNomVille(nomVille);
		this.setNomsVillesVoisines(nomsVillesVoisines);
		this.setEcole(ecole);
	}

	/**
	 * Retourne le nom d'une ville.
	 * 
	 * @return nom de la ville
	 */
	public String getNomVille()
	{
		return nomVille;
	}

	/**
	 * Remplace le nom d'une ville par celui en entrée.
	 * 
	 * @param nomVille nom de la ville
	 */
	public void setNomVille(String nomVille)
	{
		this.nomVille = nomVille;
	}

	/**
	 * Retourne la liste des noms des voisines d'une ville.
	 * 
	 * @return liste des noms des voisines de la ville
	 */
	public ArrayList<String> getNomsVillesVoisines()
	{
		return nomsVillesVoisines;
	}

	/**
	 * Remplace la liste des noms des voisines d'une ville par celle en entrée.
	 * 
	 * @param nomsVillesVoisines liste des noms des voisines de la ville
	 */
	public void setNomsVillesVoisines(ArrayList<String> nomsVillesVoisines)
	{
		this.nomsVillesVoisines = nomsVillesVoisines;
	}

	/**
	 * Retourne un booléen qui indique si une ville a une école.
	 * 
	 * @return <b>true</b> si oui, <b>false</b> si non 
	 */
	public boolean isEcole()
	{
		return ecole;
	}

	/**
	 * Remplace le booléen associé à une école par celui en entrée.
	 * 
	 * @param ecole oui ou non 
	 */
	public void setEcole(boolean ecole)
	{
		this.ecole = ecole;
	}
	
	/**
	 * <p>Affiche les informations d'une ville.</p>
	 * 
	 * Affichage de son nom, de la liste de ses voisines et si elle a une école ou pas.
	 */
	public String toString()
	{
		return "| Nom: " + this.getNomVille() + " | Voisines: " + this.getNomsVillesVoisines() + " | Ecole: " + this.isEcole();
	}
	
	/**
	 * <p>Ajoute une route entre une ville et une autre dont le nom est entré.</p>
	 * 
	 * Création d'une liste et ajout du nom de l'autre ville entré dans celle-ci.<br>
	 * Si la ville a des voisines, ajout des noms de ses voisines dans la liste créée.<br>
	 * Remplacement de l'ancienne liste des voisines de la ville par la nouvelle liste.
	 * 
	 * @param nomVille nom de l'autre ville
	 * @return route existe déjà ou pas
	 */
	public boolean ajoutRoute(String nomVille)
	{
		// Compteur
		int i;
		
		// Initialement, aucune route n'existe entre les deux villes tant qu'on ne l'a pas vérifié
		boolean routeExistante = false;
		
		// Contiendra le nom de chaque voisine
		String nomVilleVoisine;
		
		// Contiendra la liste des voisines actualisée de la ville
		ArrayList<String> newNomsVillesVoisines = new ArrayList<String>();	
		newNomsVillesVoisines.add(nomVille);
		
		if (this.getNomsVillesVoisines() != null)
		{
			for (i=0; i<this.getNomsVillesVoisines().size(); i++)
			{	
				nomVilleVoisine = this.getNomsVillesVoisines().get(i);
				
				if (nomVilleVoisine == newNomsVillesVoisines.get(0))
				{
					routeExistante = true;
					continue;
				}
				newNomsVillesVoisines.add(nomVilleVoisine);
			}
		}
		this.setNomsVillesVoisines(newNomsVillesVoisines);
		
		return routeExistante;
	}
	
	/**
	 * <p>Vérifie si une ville a une école dans son voisinage.</p>
	 * 
	 * Si la ville à des voisines, vérification si l'une d'entre-elles a une école.
	 * 
	 * @return <b>true</b> si oui, <b>false</b> sinon
	 */
	public boolean villeVoisineAUneEcole()
	{
		// Compteur
		int i;
		
		// Contiendra le nom de chaque voisine
		String nomVilleVoisine;
		
		if (this.getNomsVillesVoisines() != null)
		{
			for (i=0; i<this.getNomsVillesVoisines().size(); i++)
			{
				nomVilleVoisine = this.getNomsVillesVoisines().get(i);
				
				if (agglomeration.get(positionVille(nomVilleVoisine)).isEcole())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <p>Vérifie si la contrainte d'accessibilité est respectée pour une ville.</p>
	 * 
	 * Contrainte d'accessibilité: chaque ville doit posséder son école, ou être directement reliée à une ville qui possède une école.<br>
	 * Si la ville a des voisines: si la ville n'a pas d'école et qu'aucune de ses voisines n'a d'école,
	 * ou qu'une voisine n'a pas d'école et qu'aucune de ses voisines n'a d'école, la contrainte n'est pas respectée.<br>
	 * Si la ville n'a pas de voisine: si la ville n'a pas d'école, la contrainte n'est pas respectée.<br>
	 * Ajout dans {@link Ville#nomsVillesSansEcole} les villes qui n'ont pas d'école dans leur voisinage.
	 * 
	 * @return <b>true</b> si oui, <b>false</b> si non
	 */
	public boolean contrainteRespectee()
	{		
		// Contiendra l'identifiant de chaque voisine
		String nomVilleVoisine;
		
		// Initialement, la contrainte est respectée tant qu'on ne l'a pas vérifié
		boolean contrainteRespectee = true;
		
		if (this.getNomsVillesVoisines() != null)
		{
			if (!this.isEcole() && !this.villeVoisineAUneEcole())
			{
				contrainteRespectee = false;
				if (!nomsVillesSansEcole.contains(this.getNomVille()))
				{
					nomsVillesSansEcole.add(this.getNomVille());
				}
			}
			for (int i=0; i<this.getNomsVillesVoisines().size(); i++)
			{
				nomVilleVoisine = this.getNomsVillesVoisines().get(i);
				
				if (!agglomeration.get(positionVille(nomVilleVoisine)).isEcole() && !agglomeration.get(positionVille(nomVilleVoisine)).villeVoisineAUneEcole())
				{
					contrainteRespectee = false;
					if (!nomsVillesSansEcole.contains(nomVilleVoisine))
					{
						nomsVillesSansEcole.add(nomVilleVoisine);
					}
				}
			}
		}
		else
		{
			if (!this.isEcole())
			{
				contrainteRespectee = false;
				nomsVillesSansEcole.add(this.getNomVille());
			}
		}
		return contrainteRespectee;
	}
	
	/**
	 * <p>Calcule l'importance d'une ville dans la communauté d'agglomération.</p>
	 * 
	 * Addition du nombre de voisines de chaque voisine de la ville.
	 * 
	 * @return importance de la ville
	 */
	public int importance()
	{
		// Initialement, l'importance de la ville est nulle tant qu'on ne l'a pas calculée
		int importance = 0;
		
		// Contiendra l'identifiant de chaque voisine
		String nomVilleVoisine;
		
		for (int i = 0; i < this.getNomsVillesVoisines().size(); i++)
		{
			nomVilleVoisine = this.getNomsVillesVoisines().get(i);
			importance += agglomeration.get(positionVille(nomVilleVoisine)).getNomsVillesVoisines().size();
		}
		return importance;
	}
	
	/**
	 * Vérifie si le nom de ville entré correspond à une ville de la communauté d'agglomération.
	 * 
	 * @param nomVille nom de la ville
	 * @return <b>true</b> si oui, <b>false</b> si non
	 */
	public static boolean villeExistante(String nomVille)
	{
		// Compteur
		int i;
		
		if (agglomeration != null)
		{
			for (i=0; i<agglomeration.size(); i++)
			{
				if (nomVille.equals(agglomeration.get(i).getNomVille()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <p>Affiche les informations concernant la communauté d'agglomération.</p>
	 * 
	 * Mise à jour de {@link Ville#nbEcolesRecord} si le record a été battu.<br>
	 * Affichage du nombre d'écoles actuel et de {@link Ville#nbEcolesRecord}.<br>
	 * Affichage des informations des villes via {@link Ville#toString()}.
	 */
	public static void infosAgglomeration()
	{
		// Compteur
		int i;
		
		// Initialement, le nombre d'écoles actuel de la solution est nul tant qu'on ne l'a pas calculé
		int nbEcolesActuel = 0;
		
		for (i = 0; i < agglomeration.size(); i++)
		{
			if (agglomeration.get(i).isEcole())
			{
				nbEcolesActuel += 1;
			}
		}
		if (nbEcolesActuel < nbEcolesRecord)
		{
			nbEcolesRecord = nbEcolesActuel;
		}
		System.out.println();
		System.out.println("Infos agglomération:");
		System.out.println("Solution actuelle: " + nbEcolesActuel + " école(s) | Solution record: " + nbEcolesRecord + " école(s)");
		
		for (i=0; i<agglomeration.size(); i++)
		{
			System.out.println(" ----------------------------------------------------------");
			System.out.println(agglomeration.get(i).toString());
		}
		System.out.println(" ----------------------------------------------------------");
	}
	
	/**
	 * Retrouve la position de la ville dans {@link Ville#agglomeration} dont le nom est entré.
	 * 
	 * @param nomVille nom de la ville
	 * @return position de la ville
	 */
	public static int positionVille(String nomVille)
	{
		for (int i = 0; i < agglomeration.size(); i++)
		{
			if (nomVille.equals(agglomeration.get(i).getNomVille()))
			{
				return i;
			}
		}
		return -1;
	}
}