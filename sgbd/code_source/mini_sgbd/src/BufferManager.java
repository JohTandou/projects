import java.io.IOException;
/**
 * Classe qui comporte une seule instance et qui gère le bufferPool constitué de
 * frames
 */

public class BufferManager
{
	/**
	 * Un tableau de Frame
	 */
	public static Frame[] frames = new Frame[DBParams.frameCount];
	private static BufferManager BufferManager = new BufferManager();
	public static int t = 0;
	/**
	 * Instancie le BufferManager
	 */
	public static BufferManager getInstance()
	{
		return BufferManager;
	}
	
	/**
	 * Initialise les Frames
	 */
	public void initialisationFrames()
	{
		int i;
		System.out.println("Initialisation des frames");
		
		for (i=0; i<DBParams.frameCount; i++)
		{
			frames[i] = new Frame(null, null, 0, (byte)0, -1);
		}
	}
	
	/**
	 * Méthode qui permet de répondre à une demande de page 
	 * 
	 * @param pageId un PageId correspondant à la page qu'on veut ajouter
	 * @return le buffer de la pageId
	 */
	public byte[] GetPage(PageId pageId) throws IOException
	{
		int i = 0;
		int framesOccupees = 0;
		int tMinPin_count0;
		int frameTMin;
		byte[] buff = new byte[DBParams.pageSize];
		DiskManager.getDiskManager().ReadPage(pageId, buff);
		
		for (i=0; i<DBParams.frameCount; i++)
		{
			if (frames[i].getPageId() != null && frames[i].getPageId().getFileIdx() == pageId.getFileIdx() && frames[i].getPageId().getPageIdx() == pageId.getPageIdx())
			{
				frames[i].incrementerPin_count();
				break;
			}
			else if (frames[i].getPageId() == null)
			{
				frames[i] = new Frame(buff, pageId, 1, (byte)0, -1);
				break;
			}
			else
			{
				framesOccupees++;
			}
		}
		if (framesOccupees == DBParams.frameCount)
		{
			tMinPin_count0 = t;
			frameTMin = 0;
			
			for (i=0; i<DBParams.frameCount; i++)
			{
				if (frames[i].getTPin_count0() > 0 && frames[i].getTPin_count0() < tMinPin_count0)
				{
					tMinPin_count0 = frames[i].getTPin_count0();
					frameTMin = i;
				}
			}
			if (tMinPin_count0 == t)
			{
				System.out.println("Aucune frame n'est remplaçable, veuillez en libérer une.");
			}
			else
			{
				frames[frameTMin] = new Frame(buff, pageId, 1, (byte)0, -1);

			}
		}
		t++;
			
		return buff;
	}
	
	/**
	 * Permet de libérer une page du Frame
	 * 
	 * @param pageId   la page
	 * @param valDirty le valdirty pour savoir si la page a été modifiée ou non
	 */
	
	public void FreePage (PageId pageId, byte valdirty) throws IOException
	{
		int i;
		int compteur = 0;
		
		
		for (i=0; i<DBParams.frameCount; i++)
		{
			if (frames[i].getPageId() != null && frames[i].getPin_count() > 0 && frames[i].getPageId().getFileIdx() == pageId.getFileIdx() && frames[i].getPageId().getPageIdx() == pageId.getPageIdx())
			{
				
				frames[i].decrementerPin_count();
				frames[i].setValdirty(valdirty);
				
				if (frames[i].getValdirty() == 1)
				{
					DiskManager.getDiskManager().WritePage(pageId, frames[i].getBuff());
				}
				if (frames[i].getPin_count() == 0)
				{
					frames[i].setBuff(null);
					frames[i].setTPin_count0(t);
				}
				break;
			}
			else
			{
				compteur++;
			}
		}
		if (compteur == DBParams.frameCount)
		{
			System.out.println("Page non trouvée");
		}
		
		
		t++;
	}
	
	/**
	 * Réinitialise le frame. Ecrit toutes les pages modifiées
	 * dont le valdirty =1 sur le disque
	 */
	
	public void FlushAll() throws IOException
	{
		int i;
		System.out.println("Réinitialisation des frames");
		
		for (i=0; i<DBParams.frameCount; i++)
		{
			if (frames[i].getValdirty() == 1)
			{
				DiskManager.getDiskManager().WritePage(frames[i].getPageId(), frames[i].getBuff());
			}
			frames[i] = new Frame(null, null, 0, (byte)0, -1);
		}
		t = 0;
	}
}
