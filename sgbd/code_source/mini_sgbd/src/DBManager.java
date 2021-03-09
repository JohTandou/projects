import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Point d'entrée d'une mini-SGBD
 *
 * 
 */

public class DBManager
{
	private static DBManager DBManager = new DBManager();
	RelationInfo rel = null;
	/**
	 * Constructeur de la classe
	 */
	private DBManager()
	{	
	}
	
	public static DBManager getInstance()
	{
		return DBManager;
	}
	
	/**
	 * Méthode qui permet d'initialiser.
	 */
	public void Init() throws IOException
	{
		System.out.println("Démarrage du programme...");
		DBInfo.getInstance().Init();
		BufferManager.getInstance().initialisationFrames();
		FileManager.getInstance().init();
	}
	
	/**
	 * Permet de terminer le programme en appellant des méthodes pout tout
	 * réinitialiser
	 */
	public void Finish() throws IOException
	{
		BufferManager.getInstance().FlushAll();
		DBInfo.getInstance().Finish();
		System.out.println("Arrêt du programme...");
	}
	
	
	/**
	 * Affichages des exemples de commandes
	 */
		public void choixCommande() {
			System.out.println("Pour creer: CREATEREL nomRel C1:type1 C2:ype2 C3:type3 ");
			System.out.println("Pour inserer un element: INSERT INTO nomRel Record (val1,val2,val3)");
			System.out.println("Pour inserer plusieurs elements : BATCHINSERT INTO nomRel FROM FILE nomfichier.csv");
			System.out.println("Pour selectionner plusieurs records: SELECTALL FROM nomRel");
			System.out.println("Pour selectionner un record: SELECTS FROM nomRel WHERE nomcolonne=valeur ");
			System.out.println("Pour supprimer: RESET");
			System.out.println("Pour creer un index: CREATEINDEX");
			
		}
		
		
		/**
		 * Gère les différentes commandes
		 * 
		 * @param commande une commande
		 */
	public void ProcessCommand(String commande) throws IOException
	{
		
		String[] choix = commande.split(" ");
		if(choix[0].indexOf("CREATEREL") == 0) {
			CREATEREL(commande);
		}else if(choix[0].compareToIgnoreCase("INSERT") == 0) {
			INSERT(commande);
		} else if(choix[0].compareToIgnoreCase("BATCHINSERT") == 0) {
			BATCHINSERT(commande);
		}else if(choix[0].compareToIgnoreCase("SELECTALL") == 0) {
			SELECTALL(commande);
		}else if(choix[0].compareToIgnoreCase("SELECTS") == 0) {
			SELECTS(commande);
		} else if(choix[0].compareToIgnoreCase("RESET") == 0) {
			RESET();
		}else if(choix[0].compareToIgnoreCase("SELECTC") == 0) {
				SELECTC(commande);
		} else if (choix[0].compareToIgnoreCase("CREATEINDEX") == 0) {
			CREATEINDEX(commande);
		} else if (choix[0].compareToIgnoreCase("SELECTINDEX") == 0) {
			SELECTINDEX(commande);
		}else {
			System.out.println("Votre choix est incorrect");
			choixCommande();
		}
	}
	
	
	/**
	 * Calcule la taille d'un record en octets
	 * 
	 * @param typeCol une liste avec les types de chaque colonne
	 * @return la taille d'un record en octets
	 */
	public int calculRecordSize(List<String> typeCol) {
		int taille = 0;
		for (int i = 0; i < typeCol.size(); i++) {
			if ((typeCol.get(i).compareToIgnoreCase("float") == 0)
					|| (typeCol.get(i).compareToIgnoreCase("int") == 0)) {
				taille += 4;
			} else if (typeCol.get(i).substring(0, 6).compareToIgnoreCase("string") == 0) {
				taille += Integer.parseInt(typeCol.get(i).substring(6)) * DBParams.taille_String;
			}
		}
		return taille;
	}
	
	/**
	 * Calcule le nombre de cases qu'on peut mettre dans une page.
	 * 
	 * @param typeCol une liste avec les types de chaque colonne
	 * @return le nombre de cases qu'on peut mettre dans une page
	 */
	public static int calculSlotCount(int recordSize) {
		if(recordSize == 0) 
			return 0;
		int slotSizetemp = DBParams.pageSize/((recordSize)+1);
		return slotSizetemp;
		
	}

	
	
	/**
	 * Demande la création d'une relation. 
	 * 
	 * @param commande une commande
	 */
		public void CREATEREL(String commande) throws IOException{
			if (commande.indexOf("CREATEREL ") == 0)
			{
				List<String> nomCol = new ArrayList<String>();
				List<String> typeCol = new ArrayList<String>();
				int i;
				boolean erreur = false;
				String[] relInfo = commande.split(" ");		
				String nomRel = relInfo[1];
				int nbCol = relInfo.length-2;
				
				for (i=0; i<DBInfo.listeRel.size(); i++)
				{
					if (DBInfo.listeRel.get(i).getNomRel().equals(nomRel))
					{
						erreur = true;
						System.out.println("Nom de relation déjà existant.");
					}
				}		
				try
				{
					for(i=2; i<relInfo.length; i++)
					{
						String[] infoCol = relInfo[i].split(":");
						nomCol.add(infoCol[0]);
						if (!infoCol[1].equals("int") && !infoCol[1].equals("float") && !infoCol[1].contains("string"))
						{
							erreur = true;
						}
						else if (infoCol[1].contains("string"))
						{
							if (infoCol[1].substring(6).contains("-"))
							{
								erreur = true;
							}
							else
							{
							 Integer.parseInt(infoCol[1].substring(6));
								typeCol.add(infoCol[1]);
								
							}
						}
						else
						{
							typeCol.add(infoCol[1]);
						
						}
					}
				}
				catch (Exception e)
				{
					erreur = true;
				}
				if (erreur == false)
				{
					
					int recordSize = calculRecordSize(typeCol);
					int slotCount = calculSlotCount(recordSize);
					RelationInfo rel = CreateRelation(nomRel, nbCol, nomCol, typeCol, DBInfo.getInstance().getCompteur(), recordSize, slotCount);
					DBInfo.getInstance().AddRelation(rel);
					System.out.println("Liste des relations:");

					for(i=0; i<DBInfo.listeRel.size(); i++)
					{
						System.out.println((i) + " " + DBInfo.listeRel.get(i).getNomRel() + " " + DBInfo.listeRel.get(i).getNbCol() + " " + DBInfo.listeRel.get(i).getNomCol() + " " + DBInfo.listeRel.get(i).getTypeCol());					
					} 
					System.out.println("Commande effectuée.");
				}
				else
				{
					System.out.println("Commande CREATEREL intraîtable.");
				}
				System.out.println(" ");
			}
			
			else
			{
				System.out.println("Commande inexistante.");
				System.out.println(" ");
			}
		}
		/**
		 * Créer une RelationInfo à partir des arguments et l'ajouter au DBInfo.
		 * 
		 * @param nomRel  nom de la relation
		 * @param nomCol  nom de colonnes
		 * @param nbCol   numero de colonnes
		 * @param typeCol tableau avec les types de colonnes
		 */
		public RelationInfo CreateRelation(String nomRel, int nbCol, List<String> nomCol, List<String> typeCol, int fileIdx, int recordSize, int slotCount) throws IOException
		{
			RelationInfo rel = new RelationInfo(nomRel, nbCol, nomCol, typeCol, fileIdx, recordSize, slotCount);
			return rel;
		}
	
		

		/**
		 * Demande l’insertion d’un record dans une relation, en indiquant les valeurs
		 * (pour chaque colonne) du record et le nom de la relation. 
		 * 
		 * @param commande une commande
		 */
	public void INSERT(String commande) throws IOException {
		try {
			Rid rid;
			String tab [] = commande.split(" ");
			String nomRel = tab[2];
			String[] values = tab[4].split("\\(")[1].split("\\)")[0].split(",");
			for(int i = 0; i<DBInfo.getInstance().getCompteur(); i++) {
				if(DBInfo.getListeRel().get(i).getNomRel().equals(nomRel)) {
					Record record = new Record(DBInfo.getListeRel().get(i),values);
					rid = FileManager.getInstance().InsertRecordInRelation(record, nomRel);
					System.out.println("Rid: " + rid.toString());
					ByteBuffer buff = ByteBuffer.allocate(DBParams.pageSize);
					
					break;	
				}
			}
			System.out.println("Commande effectuee");
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Votre commande est incorrecte");
			choixCommande();
		}
	}
	/**
	 * Permet d'insérer plusieurs record dans une RelationInfo à partir d'un fichier csv.
	 * 
	 * @param commande la commande souhaitée
	 */
	public void BATCHINSERT(String commande) {
		try {
			String tab[] = commande.split(" ");
			String INTO = tab [1];
			String nomRel = tab[2];
			String FROM = tab[3];
			String FILE = tab[4];
			String nomFichier = tab[5];
			Path orderPath = Paths.get(DBParams.DBPath + "/" + nomFichier);
			List<String> lines = Files.readAllLines(orderPath);
			if (lines.size() < 2) {
				System.out.println("ERREUR Il n'y a pas de commande dans le fichier");
				}
			
			for (int i = 0; i < lines.size(); i++) {
				String[] valeurs = lines.get(i).split(",");
				for(int j = 0; j<DBInfo.getInstance().getCompteur(); j++) {
					DBInfo.getInstance();
					if(nomRel.equals(DBInfo.getListeRel().get(j).getNomRel()));
					RelationInfo relationInfo = DBInfo.getListeRel().get(j);
					Record record = new Record(relationInfo, valeurs);
					FileManager.getInstance().InsertRecordInRelation(record, nomRel);	
					}		
				}
			}catch(ArrayIndexOutOfBoundsException e) {
			System.out.println(" Votre commande est incorrecte");
			choixCommande();
		} catch (IOException e) {
			System.out.println("ERREUR Impossible de lire le fichier des commandes");
		
	}
			
	}
				
	/**
	 * Permet de séléctionner tout les Records d'une RelationInfo. 
	 * 
	 * @param commande la commande souhaitée
	 */
			private void SELECTALL(String commande) throws IOException {
	try {
		int nb = 0;
		String[] tab = commande.split(" ");
		String FROM = tab[1];
		String nomRel = tab[2];
		String ligneRecord = " ";
		for(int i = 0; i<FileManager.getInstance().getHeapFiles().size(); i++) 
		{
			if(FileManager.getInstance().getHeapFiles().get(i).getRelationInfo().getNomRel().equals(nomRel))
			{
				ArrayList<Record> recordListe = FileManager.getInstance().getHeapFiles().get(i).getAllRecords();
				
				for(Record intermediaire : recordListe)
					{
						
						for (int j = 0; j<intermediaire.getValues().length; j++) 
							{
								if(j == (intermediaire.getValues().length -1))		
									{
										ligneRecord = ligneRecord + intermediaire.getValues().length + ";";
									}
								else
									{
										ligneRecord = ligneRecord+intermediaire.getValues().length + ";";
												
									}
								
								}
					}
				System.out.println(" Record n" +(nb+1) + " "+ligneRecord);
				nb++;
			}
				
		} 
		System.out.println("Total records = " +nb);
	}	catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Votre commande est incorrecte");
					choixCommande();
		}
	}
	
	/**
	 * Doit afficher tous les records d’une relation qui ont une valeur donnée sur
	 * une colonne donnée.
	 * 
	 * @param commande la commande souhaitée
	 */
	private void SELECTS(String commande) throws IOException {
		try {
				String[] tab = commande.split(" ");
				String FROM = tab[1];
				String nomRel = tab[2];
				String WHERE = tab[3];
				String nomCol = tab[4].split("=")[0];
				String valeur = tab[4].split("=")[1];
				ArrayList<Record> recordListe = FileManager.getInstance().selectAllFromRelation(nomRel);
				for(int i = 0; i<recordListe.size(); i++) {
					if (DBInfo.listeRel.get(i).getNomRel().equals(nomRel))
					{
						for(int j = 0; i<DBInfo.listeRel.get(i).getNomCol().size();j++)
						{
							if(DBInfo.listeRel.get(i).getNomCol().get(j).equals(nomCol))
							{
								for(int k =0; k<recordListe.size(); k++) 
								{
									if (recordListe.get(k).getValues()[j].equals(valeur))
									{
										System.out.println(recordListe.get(k).toString());
									}
								}
							}
						}
					}
				}
				
				System.out.println("Total records = " + recordListe.size());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Votre commande est incorrecte");
				choixCommande(); 
			}
		}
	
	/**
	 * Remet tout dans un état qui correspond à un premier lancement de
	 * l'application
	 * 
	 * @param commande une commande
	 */
	public void RESET() throws IOException {
		BufferManager.getInstance().FlushAll();
		for(int i=0; i<DBInfo.getInstance().getCompteur();i++) {
			File fichier = new File(DiskManager.getInstance().getFichier(i));
			fichier.delete();
		}
		File catalog = new File(DBParams.DBPath + "Catalog.def");
		catalog.delete();
		DBInfo.getInstance();
		DBInfo.getListeRel().clear();
		DBInfo.getInstance().setCompteur(0);
		FileManager.getInstance().getHeapFiles().clear();
	}
	

	private void SELECTC (String commande) throws IOException {
		try {
			ArrayList<Record> record = FileManager.getInstance().selectAllFromRelation(commande);
			
			
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Votre commande est incorrecte");
			choixCommande();
		}
	}
	
	private void UPDATE(String commande) {
		try {
			String[] tab = commande.split(" ");
			String nomRelation = tab[1];
			String SET = tab[2];
			String nomColonne = tab[3];
			
			
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Votre commande est incorrecte");
			choixCommande();
		}
	}
	
	/**
	 * Permet de créer un index. 
	 * 
	 * @param commande une commande
	 */
	private void CREATEINDEX(String commande) throws IOException {
		try {
			String tab[] = commande.split(" ");
			String nomRel = tab[1];
			String KEY = tab[2].split("=")[0];
			String nomCol = tab[2].split("=")[1];
			List<Record> recordListe = FileManager.getInstance().selectAllFromRelation(nomRel);
			TreeMap<Integer, ArrayList<Rid>> treeMap = new TreeMap<>();
			for(int i =0; i< recordListe.size(); i++) {
				enregistrer(treeMap, recordListe.get(i), nomCol);
			}
			String tailleArbre = tab[3];
			//Tree arbre = new Tree(treeMap, Integer.parseInt(tailleArbre));	
		}catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Votre commande est incorrecte");
			choixCommande();	
		}
	}

	
	private void SELECTINDEX(String commande) {
		String tab[] = commande.split(" ");
		String FROM = tab[1];
		String nomRel = tab[2];
		int valeur = Integer.parseInt(tab[3]);
		String KEY = tab[2].split("=")[0];
		String nomCol = tab[2].split("=")[1];
		
		
		
	} 
	private void enregistrer(TreeMap<Integer, ArrayList<Rid>> hashMap, Record record, String nomCol) {
		ArrayList<Rid> liste = hashMap.get(Integer.getInteger(nomCol));
		if(liste == null) {
			liste = new ArrayList<>();
			hashMap.put(Integer.getInteger(nomCol), liste);
		}
		liste.add(record.getRid());	
	}
	
}
