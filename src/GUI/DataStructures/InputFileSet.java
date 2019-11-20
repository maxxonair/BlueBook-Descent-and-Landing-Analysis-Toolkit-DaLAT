package GUI.DataStructures;

import java.awt.Color;
import java.util.List;


public class InputFileSet {
	
	private int ID;
	private String inputDataFilePath;
	private List<String> inputVariableList;
	private Color dataColor;
	private String inputDataFileName="";
	
	private boolean legend=false; 
	
	public InputFileSet() {
		
	}
	

	public boolean isLegend() {
		return legend;
	}


	public void setLegend(boolean legend) {
		this.legend = legend;
	}


	public String getInputDataFileName() {
		return inputDataFileName;
	}



	public void setInputDataFileName(String inputDataFileName) {
		this.inputDataFileName = inputDataFileName;
	}



	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getInputDataFilePath() {
		return inputDataFilePath;
	}

	public void setInputDataFilePath(String inputDataFilePath) {
		this.inputDataFilePath = inputDataFilePath;
	}

	public List<String> getInputVariableList() {
		return inputVariableList;
	}

	public void setInputVariableList(List<String> inputVariableList) {
		this.inputVariableList = inputVariableList;
	}

	public Color getDataColor() {
		return dataColor;
	}

	public void setDataColor(Color dataColor) {
		this.dataColor = dataColor;
		try {
		//BlueBookPlot.getPlotPanelManager().refresh(BlueBookPlot.getVariableList(), BlueBookPlot.getInputFileSet());
		} catch (Exception e) {}
	}
	
	

}
