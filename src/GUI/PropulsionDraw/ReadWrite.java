package GUI.PropulsionDraw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import GUI.FileWatcher;
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
	
	private FileWatcher fileWatcher ;
	private Timer timer;
	private boolean isFileWatcher=false;
	
	@SuppressWarnings("unused")
	private PartsCatalogue partsCatalogue; 
	
	public ReadWrite(String ReadWriteFilePath, Canvas canvas) {
		this.ReadWriteFilePath = ReadWriteFilePath;
		this.canvas = canvas;
		setFileWatcher();
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
	            	double rotAngle = ((ComponentElement) canvasElement.getElement()).getRotAngle();
	            	List<MetaDataLine> list = metaFile.getElementMetaList();
	            	wr.write(boxElementIdentifier+writeDelimiter+name+writeDelimiter+type+writeDelimiter+metaFile.getPositionX()+writeDelimiter+metaFile.getPositionY()+writeDelimiter+ID+writeDelimiter+rotAngle+System.getProperty( "line.separator" ));	
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
		clearToWrite=false; 				// Disable writing while reading
		canvas.deleteAllContent();
		BufferedReader br;
		int addedElements=0;				// Counter for elements
		int addedRelations=0;			// Counter for relations
	try {
		//System.out.println(ReadWriteFilePath);
			br = new BufferedReader(new FileReader(ReadWriteFilePath));
	       String strLine;
	       BoxElement boxElement = null;
	       try {
	       while ((strLine = br.readLine()) != null )   {
		       	String[] tokens = strLine.split(readDelimiter);
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
		       		try { boxElement.getElement().setRotAngle(Double.parseDouble(tokens[6])); } catch (ArrayIndexOutOfBoundsException | NullPointerException e234) {}
		       		addedElements++;
		       		} catch (ArrayIndexOutOfBoundsException e) {
		       			System.out.println("ERROR: Reading Element Head failed. Element "+addedElements);
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
		      		addedRelations++;
		       		canvas.relate(parent, child);
		       	} else {
		       		// Element meta data line:
		       		try {
			       		ComponentMetaFile file = boxElement.getMetaFile();
			       		List<MetaDataLine> metaFileList = file.getElementMetaList(); 
			       		String name = tokens[0];
			       		String value = tokens[1];
			       		for(int i=0;i<metaFileList.size();i++) {
			       			if(metaFileList.get(i).name.equals(name)) {
			       				metaFileList.get(i).value=value;
			       			} 
			       			file.setElementMetaList(metaFileList);
			       			file.readMetaList();
			       			boxElement.setMetaFile(file);
			       		}
			     
		       		} catch (ArrayIndexOutOfBoundsException e) {
		       			
		       		}
		       	}

	       }
	       br.close();
	       System.out.println("Propulsion Editor - Read complete");
	       System.out.println(addedElements+" Propulsion Elements.");
	       System.out.println(addedRelations+" Propulsion Relations.");
	       } catch(NullPointerException | IOException eNPE) {
	    	   System.out.println("ERROR: FileReader Nullpointerexception.");
	       }
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found.");
			System.out.println("File path: "+ReadWriteFilePath);
		}
		if(addedElements==0 && addedRelations==0) { // Read Error -> Block to write false results
			clearToWrite=false;
			System.out.println("ERROR: Reading failed. File is empty or corrupt.");
		} else { // Reading successful -> clear to write 
			clearToWrite=true;	
		} 
	}
	
	public boolean isClearToWrite() {
		return clearToWrite;
	}

	public void linkPartsCatalogue(PartsCatalogue partsCatalogue) {
		this.partsCatalogue = partsCatalogue;
	}

	public void setReadWriteFilePath(String readWriteFilePath) {
		ReadWriteFilePath = readWriteFilePath;
		updateFileWatcher();
	}

	public String getReadWriteFilePath() {
		return ReadWriteFilePath;
	}
	
	public void setFileWatcher() {
  	  fileWatcher = new FileWatcher( new File(ReadWriteFilePath) ) {
		    protected void onChange( File file ) {
		    		readFile();
		    }
		  };
	    timer = new Timer();
	  // repeat the check every second
	    if(isFileWatcher) {
	   timer.schedule( fileWatcher , new Date(), 10000 );
	    }
	}
	
	public void updateFileWatcher() {
		timer.cancel();
		timer.purge();
	  	  fileWatcher = new FileWatcher( new File(ReadWriteFilePath) ) {
			    protected void onChange( File file ) {
			    		readFile();
			    }
			  };
			    if(isFileWatcher) {
			 	   timer.schedule( fileWatcher , new Date(), 10000 );
			 	}
	}
}
