
public class Rid {
	private PageId pageId;
	private int slotIdx;
	public PageId getPageId() {
		return pageId;
	}
	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}
	public int getSlotIdx() {
		return slotIdx;
	}
	public void setSlotIdx(int slotIdx) {
		this.slotIdx = slotIdx;
	}
	
	public Rid(PageId pageId, int slotIdx) {
		super();
		this.pageId = pageId;
		this.slotIdx = slotIdx;
	}
	
	public String toString() {
		return "<(" + pageId.getFileIdx() + "," + pageId.getPageIdx() + "),"+ slotIdx + ">";
	}

}
