package GUI.PropulsionDraw.ComponentMetaFileTypes;

import java.util.ArrayList;
import java.util.List;


public class ComponentMetaFile {
	
	private int ID;
	private String name;
	private String description="";
	
	protected  List<MetaDataLine> elementMetaList;
	
	//private BoxElement element;
	
	public ComponentMetaFile(int ID) {
		this.ID = ID;
		//this.element = element;
		this.elementMetaList =  new ArrayList<>();
		name = "Element";
		elementMetaList = updateMetaDataLine(elementMetaList, "Name", name);
		elementMetaList = updateMetaDataLine(elementMetaList, "Description", description);
	}

	public int getID() {
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
		return list;
	}

}
