public class Frame
{
	/**
	 * Gere une case appelle Frame
	 * 
	 */
	private byte[] buff = new byte[DBParams.pageSize];
	private PageId pageId;
	private int pin_count;
	private byte valdirty;
	private int tPin_count0;
	
	/**
	 * Permet d'instancier un frame
	 * @param buff tableau de byte associee
	 * @param pageId id de la page qui est dans le frame
	 * @param pin_count compteur des pages qui utilisent le frame
	 * @param valdirty entier qui indique s'il faut reecrire le buffer du frame sur la page
	 * @param tPin_count0 
	 */
	public Frame(byte[] buff, PageId pageId, int pin_count, byte valdirty, int tPin_count0)
	{
		this.setBuff(buff);
		this.setPageId(pageId);
		this.setPin_count(pin_count);
		this.setValdirty(valdirty);
		this.setTPin_count0(tPin_count0);
	}

	public byte[] getBuff()
	{
		return buff;
	}

	public void setBuff(byte[] buff)
	{
		this.buff = new byte[DBParams.pageSize];
		for (int i=0; i<this.buff.length; i++)
		{
			this.buff[i] = 0;
		}
		if (buff != null)
		{
			for (int i=0; i<this.buff.length; i++)
			{
				this.buff[i] = buff[i];
			}
		}
	}

	public PageId getPageId()
	{
		return pageId;
	}

	public void setPageId(PageId pageId)
	{
		this.pageId = pageId;
	}

	public int getPin_count()
	{
		return pin_count;
	}

	public void setPin_count(int pin_count)
	{
		this.pin_count = pin_count;
	}

	public byte getValdirty()
	{
		return valdirty;
	}

	public void setValdirty(byte valdirty)
	{
		this.valdirty = valdirty;
	}

	public int getTPin_count0()
	{
		return tPin_count0;
	}

	public void setTPin_count0(int tPin_count0)
	{
		this.tPin_count0 = tPin_count0;
	}
	
	public void incrementerPin_count()
	{
		this.pin_count++;
	}
	
	public void decrementerPin_count()
	{
		this.pin_count--;
	}
	
	/**
	 * Permet d'afficher le frame
	 */
	public static void afficherFrames()
	{
		int i;
		System.out.println(" --------------");
		System.out.println("| Infos frames |");
		
		for (i=0; i<DBParams.frameCount; i++)
		{
			System.out.println(" ------------------------------------------------------------------------");
			System.out.print("| Frame " + i + " | pageId: ");
			
			if (BufferManager.frames[i].getPageId() != null)
			{
				System.out.print("(" + BufferManager.frames[i].getPageId().getFileIdx() + "," + BufferManager.frames[i].getPageId().getPageIdx() + ")");
			}
			else
			{
				System.out.print("-");
			}
			System.out.print(" | pin_count: " + BufferManager.frames[i].getPin_count() + " | valdirty: " + BufferManager.frames[i].valdirty + " | tPin_count0: ");
			
			if (BufferManager.frames[i].getTPin_count0() != -1)
			{
				System.out.println("t" + BufferManager.frames[i].getTPin_count0() + " |");
			}
			else
			{
				System.out.println("- |");
			}
		}
		System.out.println(" ------------------------------------------------------------------------");
	}
}
