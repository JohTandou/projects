	import java.nio.ByteBuffer;
	
	public class Record {
		
		private RelationInfo relationInfo;
		private String[] values;
		private Rid rid;
		
	
		public Record(RelationInfo relationInfo, String[] values) {
			this.relationInfo = relationInfo;
			this.values = values;
			if (values == null)
				this.values = new String[relationInfo.getNbCol()];
			
		}
	
		public String[] getValues() {
			return values;
		}
	
	
		public void setValues(String[] values) {
			this.values = values;
		}
	
	
	
	
		public RelationInfo getRelationInfo() {
			return relationInfo;
		}


		public void setRelationInfo(RelationInfo relationInfo) {
			this.relationInfo = relationInfo;
		}


		public void setRelInfo(RelationInfo relInfo) {
			this.relationInfo = relInfo;
		}
	
	
		public void writeToBuffer(ByteBuffer buff, int position) {
			buff.position(position);
			if (values.length == relationInfo.getNbCol()) {
				for(int i = 0; i<values.length; i++) {
					String typeCol = relationInfo.getTypeCol().get(i);
					if(typeCol.compareToIgnoreCase("int") == 0) {
						try {
							int b = Integer.decode(values[i]);
							buff.putInt(b);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return;
						}
					}else if(typeCol.compareToIgnoreCase("float") == 0) {
						try {
								float b = Float.parseFloat(values[i]);
								buff.putFloat(b);
								
							}catch(NumberFormatException e) {
								e.printStackTrace();
								return;
						}
					} else if(typeCol.contains("string")) {
						for (int j = 0; j < values[i].length(); j++) {
							buff.putChar(values[i].charAt(j));
						}
					} else {
						System.out.println("le type du tuple est inconnue");
	
					}
				}
			}else {
				System.out.println("Les nombre d'élements à ajouter est incorrect");
			}		
		}
		
		public void readFromBuffer(ByteBuffer buff, int position) {
		buff.position(position);
		for(int i =0; i<relationInfo.getTypeCol().size();i++) {
				String typeCol = relationInfo.getTypeCol().get(i);
				if (typeCol.compareToIgnoreCase("int") == 0) {
				
					values[i] = Integer.toString(buff.getInt());
					try {
						values[i] = Integer.toString(buff.getInt());
					} catch (NumberFormatException e) {
						e.printStackTrace();
						return;
					}
				}
			 else if (typeCol.compareToIgnoreCase("float") == 0) {
				try {
					values[i] = Float.toString((buff.getFloat()));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return;
				}
			 }
			 else if((typeCol.substring(0, "string".length())).equals("string") ||
			 (typeCol.substring(0, "String".length())).equals("String")){
				 String s2 = typeCol.substring("string".length(), typeCol.length());
				 int x = Integer.getInteger(s2);
				 StringBuilder s = new StringBuilder();
				 for(int j=0; j<x; j++) {
					 char c = buff.getChar(position);
					 s.append(c);
				 }
				 values[i] = s.toString();
			 }
			 
			 else 
				System.out.println("le type du tuple est inconnue ");
			}
		}
	 
		public String toString() {
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				s.append(values[i] + " ");
			}
			return s.toString().substring(0, s.length() - 3);
		}
	
		public Rid getRid() {
			return rid;
		}
		public void setRid(Rid rid) {
			this.rid = rid;
		}
		
	}
