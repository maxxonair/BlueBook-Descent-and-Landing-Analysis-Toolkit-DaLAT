package GUI.PropulsionDraw;

import GUI.PropulsionDraw.ComponentMetaFileTypes.ComponentMetaFile;

public class CatalogueElement {
	
	private String name; 
	private ComponentMetaFile metaFile;
	private String logoFilePath;

	public CatalogueElement(String name, ComponentMetaFile metaFile, String logoFilePath) {
		this.name=name;
		this.metaFile=metaFile;
		this.logoFilePath=logoFilePath;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ComponentMetaFile getMetaFile() {
		return metaFile;
	}


	public void setMetaFile(ComponentMetaFile metaFile) {
		this.metaFile = metaFile;
	}


	public String getLogoFilePath() {
		return logoFilePath;
	}


	public void setLogoFilePath(String logoFilePath) {
		this.logoFilePath = logoFilePath;
	}

}
