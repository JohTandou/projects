import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
	private ArrayList<HeapFile> heapFiles;

	public ArrayList<HeapFile> getHeapFiles() {
		return heapFiles;
	}

	public void setHeapFiles(ArrayList<HeapFile> heapFiles) {
		this.heapFiles = heapFiles;
	}

	public FileManager() {
		super();
		heapFiles = new ArrayList<HeapFile>();
	}
	/**
	 * 
	 * Instance unique pre-initialisee
	 *
	 */
	public static class FileManagerHolder {
		private static final FileManager INSTANCE = new FileManager();
	}

	public static FileManager getInstance() {
		return FileManagerHolder.INSTANCE;
	}
	/**
	 * Crée pour chaque RelationInfo un HeapFile et ajoute cette HeapFile au tableau
	 * heapFiles
	 */
	public void init() {
		DBInfo.getInstance();
		for(int i=0; i<DBInfo.getListeRel().size();i++) {
		HeapFile hf = new HeapFile(DBInfo.getListeRel().get(i));
		heapFiles.add(hf);
		
		}
	}
	
	/**
	 * Créee une relation.
	 * 
	 * @param relDef la relation à créer.
	 */
	public void CreateRelationInfo(RelationInfo relationInfo) throws IOException {
		HeapFile hf = new HeapFile(relationInfo);
		heapFiles.add(hf);
		hf.createNewOnDisk();
	}
	
	/**
	 * Insère un Record dans une relation de nom relName.
	 * 
	 * @param record  le Record à insérer
	 * @param nomRel le nom de la relation correspondant au record
	 * @return le rid du Record inséré
	 */
	public Rid InsertRecordInRelation(Record record, String nomRel) throws IOException {
		for (int i = 0; i < heapFiles.size(); i++) {
			if (nomRel.contentEquals(heapFiles.get(i).getRelationInfo().getNomRel())){
				return heapFiles.get(i).insertRecord(record);
			}
		}
		return null;
		
	}
	/**
	 * Retourne une liste contenant tous les records de la relation dont le nom est
	 * relName.
	 * 
	 * @param nomRel le nom d'une relation
	 * @return une liste contenant tous les records de la relation dont le nom est
	 *        nomRel.
	 */
	public ArrayList<Record> selectAllFromRelation(String nomRel) throws IOException {
		for (int i = 0; i < heapFiles.size(); i++) {
			if(this.heapFiles.get(i).getRelationInfo().getNomRel().equals(nomRel)) {
				return this.heapFiles.get(i).getAllRecords();
			}
			
			}
		return null;
	}

}
