package utils.Plotter;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {
	
	private List<DataSetXY> dataSet = new ArrayList<DataSetXY>();
	
	private String title ; 
	private String xAxisLabel;
	private String yAxisLabel;
	
	public DataContainer() {
		
	}
	
	public void addDataSet(DataSetXY set) {
		dataSet.add(set);
	}

	public List<DataSetXY> getContent() {
		return dataSet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getxAxisLabel() {
		return xAxisLabel;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}


	
	

}
