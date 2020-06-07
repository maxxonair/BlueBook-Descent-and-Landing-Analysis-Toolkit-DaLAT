package utils.Plotter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class PlotXY extends Application{
	
	private static DataContainer dataContainer = new DataContainer();
    private  LineChart<Number,Number> lineChart;
    
    public PlotXY() {
    	
    }
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage stage) {
    	
        stage.setTitle("");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(""+dataContainer.getxAxisLabel());
        yAxis.setLabel(""+dataContainer.getyAxisLabel());
        //creating the chart
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(""+dataContainer.getTitle());
        
        for(int i=0;i<dataContainer.getContent().size();i++) {
        XYChart.Series series = new XYChart.Series();
        series.setName(""+dataContainer.getContent().get(i).getName());
        	for(int k=0;k<dataContainer.getContent().get(i).getDataSet().size();k++) {
				series.getData().add(new XYChart.Data(dataContainer.getContent().get(i).getDataSet().get(k).x, 
													  dataContainer.getContent().get(i).getDataSet().get(k).y));
        	}
        lineChart.getData().add(series);
        }
        /* Allows to disable markers for individual dataSets
        for (XYChart.Series<Number, Number> series : lineChart.getData()) {

            //for all series, take date, each data has Node (symbol) for representing point
            for (XYChart.Data<Number, Number> data : series.getData()) {
              // this node is StackPane
              StackPane stackPane = (StackPane) data.getNode();
              stackPane.setVisible(false);
            }
          }
        */
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(true);
        
        Scene scene  = new Scene(lineChart,800,600);
        stage.setScene(scene);
        stage.show();
    }

 
    public void plot(DataContainer Container) {
    	dataContainer = Container;
        launch();
    }


	public LineChart<Number, Number> getLineChart() {
		return lineChart;
	}

	

}
