import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class HeapFile {
	private RelationInfo relationInfo;
	private PageId headerPage;
	
	public HeapFile(RelationInfo relationInfo) {
		this.relationInfo = relationInfo;
		this.headerPage = new PageId(relationInfo.getFileIdx(), 0);
	}

	public void createNewOnDisk() throws IOException {
		try{
			DiskManager.getDiskManager().CreateFile(this.relationInfo.getFileIdx());
			PageId headerPage = DiskManager.getDiskManager().AddPage(this.relationInfo.getFileIdx());
			ByteBuffer buffer = ByteBuffer.wrap(BufferManager.getInstance().GetPage(headerPage));
			buffer.flip();
			buffer.putInt(0);
			DiskManager.getDiskManager().WritePage(headerPage,new byte[DBParams.pageSize]);
			BufferManager.getInstance().FreePage(headerPage, (byte) 1);
		}catch (Exception e) {
				e.printStackTrace();
		}

	}

	public PageId addDataPage() throws IOException {
		try{
			PageId newPageID = DiskManager.getDiskManager().AddPage(relationInfo.getFileIdx());
			ByteBuffer bufHeaderPage = ByteBuffer.wrap(BufferManager.getInstance().GetPage(headerPage));
			bufHeaderPage.position(0);
			byte nbrPage = bufHeaderPage.get();
			if (nbrPage == 0)
			{
				nbrPage++;
			}
			bufHeaderPage.position(0);
			bufHeaderPage.put((byte) (nbrPage+1));
			nbrPage++;
			bufHeaderPage.position(4*(nbrPage-2)+1);
			bufHeaderPage.putInt(relationInfo.getSlotCount());
			BufferManager.getInstance().FreePage(headerPage, (byte) 1);
			return newPageID;
			
		}catch (Exception e){
	            e.printStackTrace();
	            System.exit(0);
        }
		return null;
	
	}
	
	public PageId getFreeDataPageId() throws IOException {
		ByteBuffer bufHeaderPage = ByteBuffer.wrap(BufferManager.getInstance().GetPage(headerPage));
		BufferManager.getInstance().FreePage(headerPage, (byte) 0);
		bufHeaderPage.position(0);
		int nbrPage = bufHeaderPage.getInt();
		for (int i = 0; i < nbrPage; i++) {
			if (bufHeaderPage.getInt() != 0) {
				return new PageId(relationInfo.getFileIdx(), i + 1);
			}
		}
		
		return null;
		
	}
	
	public Rid writeRecordToDataPage(Record record, PageId pageId) throws IOException {
		boolean slotLibre = true;
		int i;
		ByteBuffer bufPageLibre = ByteBuffer.wrap(BufferManager.getInstance().GetPage(pageId));
		bufPageLibre.position(0);
		for (i = 0; i < relationInfo.getSlotCount(); i++)
		{
			for (int j = 0; j < relationInfo.getRecordSize(); j++)
			{
				if (bufPageLibre.get() != 0)
				{
					slotLibre = false;
				}
			}
			if (slotLibre == true)
			{
				record.writeToBuffer(bufPageLibre, i*relationInfo.getRecordSize());
				break;
			}
			slotLibre = true;
		}
		BufferManager.getInstance().FreePage(pageId,(byte) 1);
		ByteBuffer bufHeaderPage = ByteBuffer.wrap(BufferManager.getInstance().GetPage(headerPage));
		bufHeaderPage.position(4*(pageId.getPageIdx())+1);
		int nbrSlotsLibres = bufHeaderPage.getInt();
		bufHeaderPage.position(4*(pageId.getPageIdx())+1);
		bufHeaderPage.putInt(nbrSlotsLibres-1);
		BufferManager.getInstance().FreePage(headerPage, (byte) 1);
		Rid rid = new Rid(new PageId(pageId.getFileIdx(), pageId.getPageIdx()+1), i+1);
		
		return rid;
	}
	
	public ArrayList<Record> getRecordsInDataPage(PageId pageId) throws IOException {
		ArrayList<Record> rec = new ArrayList<Record>();
		ByteBuffer bufPageId = ByteBuffer.wrap(BufferManager.getInstance().GetPage(pageId));
		for (int i = 0; i < this.relationInfo.getSlotCount(); i++) {
			// 0 libre et 1 occupe
			int nbrByteMap = this.relationInfo.getSlotCount();
			bufPageId.position(i);
			if (bufPageId.get() != (byte) 0) {
				Record r = new Record(this.relationInfo, null);
				r.readFromBuffer(bufPageId, nbrByteMap+(i*this.relationInfo.getRecordSize()));
				rec.add(r);
			}
		}
		BufferManager.getInstance().FreePage(pageId, (byte) 0);
		return rec;
	}
	
	/**
	 * On recupere une page libre
	 * @param record
	 * @return
	 * @throws IOException
	 */
	public Rid insertRecord(Record record) throws IOException {
		PageId pageIdLibre = this.getFreeDataPageId();
		if (pageIdLibre == null) {
			pageIdLibre = this.addDataPage();
		}
		
		return  this.writeRecordToDataPage(record, pageIdLibre);
		
	}

	
	public ArrayList<Record> getAllRecords() throws IOException {
		ArrayList<Record> rec = new ArrayList<Record>();
		try {
			ByteBuffer bufHeaderPage = ByteBuffer.wrap(BufferManager.getInstance().GetPage(headerPage));
			BufferManager.getInstance().FreePage(headerPage, (byte) 0);
			bufHeaderPage.position(0);
			int nbrPage = bufHeaderPage.getInt();
			for (int i = 0; i < nbrPage; i++) {
				PageId pageId = new PageId(relationInfo.getFileIdx(), i + 1);
				ArrayList<Record> recordList = this.getRecordsInDataPage(pageId);
				rec.addAll(recordList);
			}
		} catch (IOException e) {
			e.printStackTrace();
            System.exit(0);
		}
		return rec;
	}

	
	
	public String toString() {
		return " " + relationInfo;
	}

	public RelationInfo getRelationInfo() {
		return relationInfo;
	}
	
	

}
