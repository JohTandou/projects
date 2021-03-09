package projet2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import projet.Main;
import projet.Ville;

/**
 * Cette classe regroupe des méthodes qui permettent le chargement d'un fichier et la sauvegarde des informations de la communauté d'agglomération dans celui-ci.
 */
public class Fichier
{
	/**
	 * Instance de la classe {@link #Fichier} pour accéder aux méthodes de chargement et de sauvegarde.
	 */
	private static Fichier fichier = new Fichier();
	
	/**
	 * Retourne l'instance de la classe {@link #Fichier}.
	 * 
	 * @return instance de la classe {@link #Fichier}
	 */
	public static Fichier getFichier()
	{
		return fichier;
	}
	
	/**
	 * <p>Lit un fichier, analyse ses informations et les incorpore dans le programme.</p>
	 * 
	 * Pour chaque ligne du fichier,<br>
	 * Si elle contient "ville": création d'une ville ayant pour nom ce qu'il y a entre parenthèses si elle n'existe pas déjà.<br>
	 * Si elle contient "route": ajout d'une une route via {@link Ville#ajoutRoute(String)} entre les villes entre parenthèses si elle n'existe pas déjà.<br>
	 * Si elle contient "ecole": ajout d'une école dans la ville entre parenthèses si elle n'existe pas déjà.<br>
	 * 
	 * @return <b>-2</b> si problème de lecture ou si agglomarétion est de taille inférieure à 2, <b>-1</b> si information manquante, <b>0</b> sinon
	 */
	public int chargerFichier()
	{
		// Sera le retour de la méthode, initialement 0 car il n'y a pas d'erreur tant qu'on a pas lu le fichier
		int erreur = 0;
		
		// Nom de la ville à créer
		String nomVille;
		
		// Nom de la ville 1 (pour l'ajout de route)
		String nomVille1;
		
		// Nom de la ville 2 (pour l'ajout de route)
		String nomVille2;
		
		// Contiendra les lignes du fichier une par une, initialement null car rien n'a encore été lu
		String ligne = null;
         
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(Main.cheminFichier)))
		{
			while ((ligne = bufferedReader.readLine()) != null) 
		    {	  	    	
		    	switch (ligne.split("\\(")[0])
		    	{
		    		case "ville":
		    			nomVille = ligne.split("\\(")[1].split("\\)")[0];
			    		
			    		if (!Ville.villeExistante(nomVille))
			    		{
			    			Ville.agglomeration.add(new Ville(nomVille, null, false));
			    		}
			    		break;
			    		
		    		case "route":
		    			nomVille1 = ligne.split("\\(")[1].split("\\)")[0].split(",")[0];
			    		nomVille2 = ligne.split("\\(")[1].split("\\)")[0].split(",")[1];
			    		
			    		if (!Ville.villeExistante(nomVille1))
			    		{
			    			erreur = -1;
			    			Ville.agglomeration.add(new Ville(nomVille1, null, false));			
			    		}
			    		if (!Ville.villeExistante(nomVille2))
			    		{
			    			erreur = -1;
			    			Ville.agglomeration.add(new Ville(nomVille2, null, false));	
			    		}
			    		if (Ville.agglomeration.get(Ville.positionVille(nomVille1)).getNomsVillesVoisines() == null || !Ville.agglomeration.get(Ville.positionVille(nomVille1)).getNomsVillesVoisines().contains(nomVille2))
			    		{
			    			Ville.agglomeration.get(Ville.positionVille(nomVille1)).ajoutRoute(nomVille2);
							Ville.agglomeration.get(Ville.positionVille(nomVille2)).ajoutRoute(nomVille1);
			    		}
			    		break;
			    	
		    		case "ecole":
		    			nomVille = ligne.split("\\(")[1].split("\\)")[0];
			    		
			    		if (!Ville.villeExistante(nomVille))
			    		{
			    			erreur = -1;
			    			Ville.agglomeration.add(new Ville(nomVille, null, false));
			    		}
			    		Ville.agglomeration.get(Ville.positionVille(nomVille)).setEcole(true);
			    		break;
		    	}
		    }
			if (Ville.agglomeration.size() < 2)
			{	
				throw new Exception();
			}
		}
		catch (Exception e)
		{
			erreur = -2;
		}
		return erreur;
	}
	
	/**
	 * <p>Sauvegarde les informations de la communauté d'agglomération dans un fichier.</p>
	 * 
	 * Pour chaque ville:<br>
	 * Ecriture de "ville(nomVille)." dans le fichier.<br>
	 * Ecriture pour chacune de ses voisines de "route(nomVille,nomVilleVoisine)." dans le fichier en évitant les doublons.<br>
	 * Ecriture si elle a une école de "ecole(nomVille)." dans le fichier.
	 * 
	 * @throws IOException exception d'entrée/sortie
	 */
	public void sauvegarderFichier() throws IOException
	{
		// Contiendra le nom de chaque voisine
		String nomVilleVoisine;
		
		// Contiendra les villes dont on a déjà écrit les routes dans le fichier
		ArrayList <String> nomsVillesTraitees = new ArrayList<>();
		
		// Permettra d'écrire dans le fichier
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Main.cheminFichier));
		
		for (int i = 0; i < Ville.agglomeration.size(); i++)
		{
			bufferedWriter.write("ville(" + Ville.agglomeration.get(i).getNomVille() + ")." + System.lineSeparator());
		}
		for (int i = 0; i < Ville.agglomeration.size(); i++)
		{
			if (Ville.agglomeration.get(i).getNomsVillesVoisines() != null)
			{
				for (int j = 0; j < Ville.agglomeration.get(i).getNomsVillesVoisines().size(); j++)
				{
					nomVilleVoisine = Ville.agglomeration.get(i).getNomsVillesVoisines().get(j);
					
					if (!nomsVillesTraitees.contains(nomVilleVoisine))
					{
						bufferedWriter.write("route(" + Ville.agglomeration.get(i).getNomVille() + "," + nomVilleVoisine + ")." + System.lineSeparator());
					}
				}
			}
			nomsVillesTraitees.add(Ville.agglomeration.get(i).getNomVille());
		}
		for (int i = 0; i < Ville.agglomeration.size(); i++)
		{
			if (Ville.agglomeration.get(i).isEcole())
			{
				bufferedWriter.write("ecole(" + Ville.agglomeration.get(i).getNomVille() + ")." + System.lineSeparator());
			}
		}
		System.out.println();
		System.out.println("Solution sauvegardée dans " + Main.cheminFichier + ".");
		bufferedWriter.close();
	}
}