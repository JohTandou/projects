package projet;

import java.util.Scanner;

/**
 * Cette classe regroupe le scanner et les méthodes qui demandent des éléments d'un certain type à l'utilisateur.
 */
public class Scan
{
	/**
	 * Scanner.
	 */
	public static Scanner sc = new Scanner (System.in);
	
	/**
	 * Instance de la classe {@link #Scan} pour accéder aux différentes méthodes de saisie.
	 */
	private static Scan scan = new Scan();
	
	/**
	 * Retourne l'instance de la classe {@link #Scan}.
	 * 
	 * @return instance de la classe {@link #Scan}
	 */
	public static Scan getScan()
	{
		return scan;
	}
	
	/**
	 * Demande un entier à l'utilisateur.
	 * 
	 * @return <b>valeur de l'entier</b> si la réponse tapée est un entier, <b>-1</b> sinon.
	 */
	public int saisieEntier()
	{
		// Contiendra l'entier tapé, vaudra toujours -1 si autre chose qu'un entier est tapé
		int entier = -1;
		
		try
		{
			entier = sc.nextInt();
		}
		catch (Exception e)
		{
			sc.nextLine();
		}	
		return entier;
	}
	
	/**
	 * Demande un nom de ville à l'utilisateur.
	 * 
	 * @return nom de la ville
	 */
	public String saisieNomVille()
	{
		// Nom de la ville
		String nomVille;
		nomVille = sc.nextLine();
		
		return nomVille;
	}
}