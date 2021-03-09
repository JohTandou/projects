import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
* Permet la gestion du disque
*/
public class DiskManager
{
	PageId p = null;
	
	/**
	 * Permet de créer un singleton
	 *
	 */
	public static class DiskManagerHolder {
		private static final DiskManager INSTANCE = new DiskManager();
	}

	public static DiskManager getInstance() {
		return DiskManagerHolder.INSTANCE;
	}
	private static DiskManager DiskManager = new DiskManager();

	public static DiskManager getDiskManager() {
		return DiskManager;
	}

	
	/**
	 * Permet de créer un nom de fichier à partir d'un identifiant
	 * 
	 * @param fileId un identifiant de fichier entre 0 et n
	 * @return le nom du fichier
	 */
	public String getFichier(int fileIdx) {
		StringBuffer nomFichier = new StringBuffer();
		nomFichier.append(DBParams.DBPath + "Data _");
		nomFichier.append(fileIdx);
		nomFichier.append(".rf");
		return nomFichier.toString();
	}
	
	public static void setDiskManager(DiskManager diskManager) {
		DiskManager = diskManager;
	}
	
	
	/**
	 * Créer un fichier
	 * 
	 * @param fileIdx entier correspondant à un identifiant de fichier
	 * @return le fichier créer
	 */

	public void CreateFile(int fileIdx) throws IOException
	{
		DBInfo.file = new File(DBParams.DBPath + "/" + "Data_" + fileIdx + ".rf");
		
		if (DBInfo.file.createNewFile())
		{
		    System.out.println("Fichier " + DBInfo.file + " créé");
		}
		else
		{
		    System.out.println("Fichier " + DBInfo.file + " déjà existant");
		}
	}
	
	/**
	 * Alloue pageSize octets à un fichier passé en paramètre
	 * 
	 * @param fileIdx identifiant du fichier
	 * @return le PageId correspond à la page nouvellement ajoutée
	 */
	public PageId AddPage(int fileIdx) throws IOException
	{
		
		RandomAccessFile accessFile = new RandomAccessFile(DBParams.DBPath + "/" + "Data_" + fileIdx + ".rf", "rw");
		accessFile.seek(accessFile.length());
		accessFile.write(new byte[DBParams.pageSize]);
		long pageIdx = ((accessFile.length()/DBParams.pageSize)-1);
		PageId idDelaNouvellePage = new PageId(fileIdx, pageIdx);
		accessFile.close();
		return idDelaNouvellePage;
		
	}
	
	
	/**
	 * Rempli l'argument buff avec le contenu disque de la page identifiée par
	 * l'argument pageId
	 * 
	 * @param pageId le PageId pour identifier le fichier et la page de ce fichier
	 * @param buff   le buffer à remplir avec le contenu de la page
	 */
	public void ReadPage(PageId pageId, byte[] buff) throws IOException
	{
		
		try
		{
			
			RandomAccessFile accessFile = new RandomAccessFile(DBParams.DBPath + "/" + "Data_" + pageId.getFileIdx() + ".rf", "r");
			accessFile.seek(DBParams.pageSize*pageId.getPageIdx());
			accessFile.read(buff);
			accessFile.close();	
		}
		catch(Exception e)
		{
			System.out.println("Erreur: lecture impossible");
		}
	}
	
	/**
	 * Ecrit le contenu de l'argument buff dans le fichier à la position indiquée
	 * par l'argument pageID.
	 * 
	 * @param pageId le PageId pour identifier le fichier et la page de ce fichier
	 * @param buff   la buffer à écrire sur la page
	 */
	public void WritePage(PageId pageId,byte[] buff) throws IOException
	{
		
		try
		{
			
			RandomAccessFile accessFile = new RandomAccessFile(DBParams.DBPath + "/" + "Data_" + pageId.getFileIdx() + ".rf", "rw");
			accessFile.seek(DBParams.pageSize*pageId.getPageIdx());
			accessFile.write(buff);
			accessFile.close();
			
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		
		}catch(Exception e) {
			System.out.println("Erreur: écriture impossible");
		}
	}
}
