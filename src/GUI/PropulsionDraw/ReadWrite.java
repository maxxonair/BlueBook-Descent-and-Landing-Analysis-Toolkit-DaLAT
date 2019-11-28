package GUI.PropulsionDraw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import GUI.PropulsionDraw.ComponentMetaFileTypes.ComponentMetaFile;
import GUI.PropulsionDraw.ComponentMetaFileTypes.MetaDataLine;

public class ReadWrite {
	
	private  String ReadWriteFilePath;
	
	private Canvas canvas;
	
	private  String boxElementIdentifier = "Box_54*&^{}";
	private  String relationElementIdentifier = "Relation_74*&^{}";
	private  String writeDelimiter =  "|";
	private  String readDelimiter   = "\\|";
	
	private boolean clearToWrite=false;
	
	@SuppressWarnings("unused")
	private PartsCatalogue partsCatalogue; 
	
	public ReadWrite(String ReadWriteFilePath, Canvas canvas) {
		this.ReadWriteFilePath = ReadWriteFilePath;
		this.canvas = canvas;
	}

	public void writeFile() {
	if(clearToWrite) {
			 List<BoxElement> canvasElements= canvas.getCanvasElements();
			 List<Relationship> relationships = canvas.getRelationships();
	        try {
	            File fac = new File(ReadWriteFilePath);
	            if (!fac.exists())
	            {
	                fac.createNewFile();
	            } else {
	            	fac.delete();
	            	fac.createNewFile();
	            }
	            //-----------------------------------------------------
	            FileWriter wr = new FileWriter(fac);
	            for (int i=0; i<canvasElements.size(); i++){
	            	BoxElement canvasElement = canvasElements.get(i);
	            	ComponentMetaFile metaFile = canvasElement.getMetaFile();
	            	String name = metaFile.getName();
	            	int type = metaFile.getElementType();
	            	String ID = metaFile.getID().toString();
	            	List<MetaDataLine> list = metaFile.getElementMetaList();
	            	wr.write(boxElementIdentifier+writeDelimiter+name+writeDelimiter+type+writeDelimiter+metaFile.getPositionX()+writeDelimiter+metaFile.getPositionY()+writeDelimiter+ID+System.getProperty( "line.separator" ));	
	            		for(MetaDataLine line : list) {
	            			wr.write(line.name+writeDelimiter+line.value+System.getProperty( "line.separator" ));	
	            		}        	
				} 
	            for(Relationship rel : relationships) {
	            	wr.write(relationElementIdentifier+writeDelimiter+rel.getParent().getMetaFile().getID()+writeDelimiter+
	            			 rel.getChild().getMetaFile().getID()+System.getProperty( "line.separator" ));	
	            }
		            wr.close();
	            } catch (IOException eIO) {System.out.println(eIO);}
	}
	}
	
	
	
	
	public void readFile() {
		BufferedReader br;
		//System.out.println("read");
	try {
			br = new BufferedReader(new FileReader(ReadWriteFilePath));

	       String strLine;
	       BoxElement boxElement = null;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(readDelimiter);
		       	//System.out.println(tokens[0]);
		       	if(tokens[0].equals(boxElementIdentifier)){
		       		// Element head detected: 
		       		String name = tokens[1];
		       		try {
		       		int type = Integer.parseInt(tokens[2]);
		       		int x = Integer.parseInt(tokens[3]);
		       		int y = Integer.parseInt(tokens[4]);
		       		UUID ID = UUID.fromString(tokens[5]);
		       		//System.out.println(name+"|"+x+"|"+y);
		       	    boxElement = canvas.addElement(this, type, ID);
		       		boxElement.getElement().setLocation(x, y);
		       		((ComponentElement) boxElement.getElement()).updatePosition(x, y);
		       		boxElement.setName(name);
		       		boxElement.getMetaFile().setName(name);
		       		boxElement.getMetaFile().setID(ID);
		       		} catch (ArrayIndexOutOfBoundsException e) {
		       			System.out.println("ERROR: Reading Element Head failed.");
		       		}
		       	} else if(tokens[0].equals(relationElementIdentifier)){
		       		// Relationship detected: 
		       		UUID parentID = java.util.UUID.fromString(tokens[1]);
		       		UUID childID = java.util.UUID.fromString(tokens[2]);
		       		BoxElement child = null, parent = null;
		       		for(BoxElement element : canvas.getCanvasElements()) {
		       			if( element.getMetaFile().getID().equals(parentID) ) {
		       				parent = element;
		       			} else if ( element.getMetaFile().getID().equals(childID)) {
		       				child = element; 
		       			}
		       		}
		       		//System.out.println(child.getMetaFile().getID());
		       		canvas.relate(parent, child);
		       	} else {
		       		// Element meta data line:
		       		try {
			       		ComponentMetaFile file = boxElement.getMetaFile();
			       		List<MetaDataLine> metaFile = file.getElementMetaList(); 
			       		String name = tokens[0];
			       		String value = tokens[1];
			       		for(int i=0;i<metaFile.size();i++) {
			       			if(metaFile.get(i).name.equals(name)) {
			       				metaFile.get(i).value=value;
			       			}
			       			boxElement.setMetaFile(file);
			       		}
		       		} catch (ArrayIndexOutOfBoundsException e) {
		       			
		       		}
		       	}

	       }
	       br.close();
	       } catch(NullPointerException | IOException eNPE) {
	    	   System.out.println("ERROR: FileReader Nullpointerexception.");
	       }
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found.");
		}
	clearToWrite=true; 
	}
	
	public void linkPartsCatalogue(PartsCatalogue partsCatalogue) {
		this.partsCatalogue = partsCatalogue;
	}
}
