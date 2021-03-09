
public class PageId
{
	private int FileIdx;
	private int PageIdx;
	
	public PageId(int FileIdx, long pageIdx) {
		this.setFileIdx(FileIdx);
		this.setPageIdx();
	}

	public int getFileIdx() {
		return FileIdx;
	}

	public void setFileIdx(int fileIdx) {
		FileIdx = fileIdx;
	}

	public int getPageIdx() {
		return PageIdx;
	}

	public int setPageIdx( ) {
		return PageIdx ;
	}
	
	
}
