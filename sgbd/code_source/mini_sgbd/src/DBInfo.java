	import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
	import java.io.IOException;
	
	/**
	 * Contient les informations de schéma pour l'ensemble de notre base de données.
	 */
	public class DBInfo {
		/**
		 * La liste de toutes les relations créées
		 */
	public static ArrayList<RelationInfo> listeRel= new ArrayList<RelationInfo>();
	
	/**
	 * Le nombre de relation crée dans la base de données
	 */
	private int compteur;

	/**
	 * Permet de retourner la valeur du compteur de relation
	 * 
	 * @return le nombre de relations enregistrer
	 */
	public int getCompteur() {
		return compteur;
	}
	
	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
	
	/**
	 * Instancie une DBInfo
	 */
	public DBInfo()
	{
		super();
		listeRel = new ArrayList<RelationInfo>();
		compteur = 0;
	}
	
	private static DBInfo DBInfo = new DBInfo();
	 protected static File file;
	
	 public static DBInfo getInstance() {
		return DBInfo;
	}
	 
	 	/**
		 * Méthode appelée dès le début. Permet d'initialiser les variables de la classe
		 * à partir de données dans un fichier.
		 */
	public void Init() throws IOException  {
		int i;
		String line;
		String[] infoLigne;
		String[] infoLigne2;
		String nomRel;
		int nbCol;
		int fileIdx;
		int recordSize = 0;
		int slotCount;
		file = new File(DBParams.DBPath + "/" + "Catalog.txt");
		
		if (file.exists())
		{
			System.out.println("Chargement de Catalog.def...");
			BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
			
			while ((line = bufferedreader.readLine()) != null)
			{
				ArrayList<String> nomCol = new ArrayList<>();
				ArrayList<String> typeCol = new ArrayList<>();
				infoLigne = line.split(" ");
				nomRel = infoLigne[0];
				nbCol = Integer.parseInt(infoLigne[1]);
				infoLigne2 = infoLigne[2].split("\\.");
				
				for (i=0; i<infoLigne2.length; i++)
				{
					nomCol.add(infoLigne2[i]);
				}
				infoLigne2 = infoLigne[3].split("\\.");
				
				for (i=0; i<infoLigne2.length; i++)
				{
					typeCol.add(infoLigne2[i]);
				}
				fileIdx = Integer.parseInt(infoLigne[4]);
				recordSize = Integer.parseInt(infoLigne[5]);
				slotCount = Integer.parseInt(infoLigne[6]);
				listeRel.add(new RelationInfo (nomRel,nbCol, nomCol, typeCol, fileIdx, recordSize, slotCount));
				
			}
			bufferedreader.close();
			compteur = listeRel.size();
		}

			
			}
	
	/**
	 * Méthode appelée à la fin. Ecrit toutes les informations enregistrées dans
	 * DBInfo dans le Catalog
	 */
		
	public void Finish() throws IOException {
		int i;
		int j;
		
		try (FileWriter writer = new FileWriter(DBParams.DBPath + "/" + "Catalog.def"))
		{
			System.out.println("Liste des relations enregistrée dans Catalog.def.");
			
			for (i=0; i<listeRel.size(); i++)
			{
				writer.write(listeRel.get(i).getNomRel() + " " + listeRel.get(i).getNbCol() + " ");
				
				for (j=0; j<listeRel.get(i).getNomCol().size(); j++)
				{
					writer.write(listeRel.get(i).getNomCol().get(j) + ".");
				}
				writer.write(" ");
				
				for (j=0; j<listeRel.get(i).getTypeCol().size(); j++)
				{
					writer.write(listeRel.get(i).getTypeCol().get(j) + ".");
				}
				writer.write(" " + listeRel.get(i).getFileIdx() + " " + listeRel.get(i).getRecordSize() + " " + listeRel.get(i).getSlotCount());
				writer.write(System.lineSeparator());
			}
		}
		
	}
	
	/**
	 * Rajoute une relationInfo à la liste et actualise le compteur.
	 * 
	 * @param r une RelationInfo
	 */
	public void AddRelation(RelationInfo rel) throws IOException {
		listeRel.add(rel);
		HeapFile hf = new HeapFile(listeRel.get(compteur));
		FileManager.getInstance().getHeapFiles().add(hf);
		file = new File(DBParams.DBPath + "/" + "Data_" + compteur + ".rf");
		System.out.println("Fichier " + "Data_" + compteur + ".txt" + " créé.");
		DiskManager.getInstance().AddPage(compteur);
		PageId headerPage = new PageId(0,0);
		byte[] buff = BufferManager.getInstance().GetPage(headerPage);
		buff[0] = (byte) 1;
		BufferManager.getInstance().FreePage(headerPage, (byte) 1);
		compteur++;
				
		}
	
	/**
	 * Permet de retourner la liste des RelationInfo
	 * 
	 * @return la liste des RelationInfo
	 */
	public static ArrayList<RelationInfo> getListeRel() {
		return listeRel;
	}

	
	public byte[] getFileContent()
	{
		return getFileContent();
	}

	public void setFileContent(byte[] fileContent)
	{
	}
		
	}
