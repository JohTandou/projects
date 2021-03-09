		import java.util.List;
		
	public class RelationInfo {
		private String nomRel;
		private int nbCol;
		private List<String> nomCol;
		protected List<String> typeCol;
		private int fileIdx;
		private int recordSize;
		private int slotCount;
		
		public RelationInfo(String nomRel, int nbCol, List<String> nomCol, List<String> typeCol, int fileIdx, int recordSize, int slotcount ) {
		{
			this.setNomRel(nomRel);
			this.setNbCol(nbCol);
			this.setTypeCol(typeCol);
			this.setNomCol(nomCol);
			this.fileIdx = DBInfo.getInstance().getCompteur();
			this.recordSize = recordSize;
			this.slotCount = slotcount;
			
			
		}
		}


		public String getNomRel() {
			return nomRel;
		}
		
		public void setNomRel(String nomRel) {
			this.nomRel = nomRel;
		}
		
		public int getNbCol() {
			return nbCol;
		}
		
		public void setNbCol(int nbCol) {
			this.nbCol = nbCol;
		}
		
		public List<String> getNomCol() {
			return nomCol;
		}
		
		public void setNomCol(List<String> nomCol) {
			this.nomCol = nomCol;
		}
		
		public  List<String> getTypeCol() {
			return typeCol;
		}
		
		public void setTypeCol(List<String> typeCol) {
			this.typeCol = typeCol;
		}
		
		public int getFileIdx() {
			return fileIdx;
		}
		
		public void setFileIdx(int fileIdx) {
			this.fileIdx = fileIdx;
		}
		
		public int getRecordSize() {
			return recordSize;
		}
		
		public void setRecordSize(int recordSize) {
			this.recordSize = recordSize;
		}
		
		public int getSlotCount() {
			return slotCount;
		}
		
		public void setSlotCount(int slotCount) {
			this.slotCount = slotCount;
		}
			
		public String toString() {
			return nomRel+ " " + nbCol + " " + typeCol + " " + fileIdx + " " + recordSize + " " + slotCount+ " ";
		}
		
		
	}
