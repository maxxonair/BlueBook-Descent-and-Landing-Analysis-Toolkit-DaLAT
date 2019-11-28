package GUI.PropulsionDraw.ComponentMetaFileTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import GUI.PropulsionDraw.ReadWrite;


public class ComponentMetaFile {
	
	private UUID ID;
	private int elementType;
	private String name;
	private String description="";
	
	private int positionX=50;
	private int positionY=50;
	
	private ReadWrite readWrite;
	
	protected  List<MetaDataLine> elementMetaList;
	
	//private BoxElement element;
	
	public ComponentMetaFile(int elementType, ReadWrite readWrite) {
		this.elementType = elementType;
		this.readWrite=readWrite;
		//this.element = element;
		this.elementMetaList =  new ArrayList<>();
		name = "Element";
		elementMetaList = updateMetaDataLine(elementMetaList, "Name", name);
		elementMetaList = updateMetaDataLine(elementMetaList, "Description", description);
	}

	public UUID getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		elementMetaList = updateMetaDataLine(elementMetaList, "Name", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		elementMetaList = updateMetaDataLine(elementMetaList, "Description", description);
	}
	
	
	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public List<MetaDataLine> getElementMetaList() {
		return elementMetaList;
	}
	
	public void getMetaListUpdate(String name, String value) {
		updateMetaDataLine(elementMetaList,  name,  value);
	}

	protected List<MetaDataLine> updateMetaDataLine(List<MetaDataLine> list, String name, String value) {
		boolean lineExists=false;
		//System.out.println("update file");
		for (MetaDataLine line : list) {
			if(line.name.equals(name)) {
				line.value=value;
				lineExists=true;
				//System.out.println(name+"|"+value);
			}	
		}
		if(!lineExists) {
			MetaDataLine line = new MetaDataLine(name, value);
			list.add(line);
		}
		readWrite.writeFile();
		return list;
	}

	public int getElementType() {
		return elementType;
	}

	public void setID(UUID iD) {
		ID = iD;
	}
	
	

}
