package Simulation.Noise;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 
public class chartDemo extends Application {
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override public void start(Stage stage) {
        stage.setTitle("");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iterations");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("1D Walker Test Mark1");
        //defining a series
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        XYChart.Series series4 = new XYChart.Series();
     //   XYChart.Series series5 = new XYChart.Series();
        series1.setName("Setting 1");
        series2.setName("Setting 2");
        series3.setName("Setting 3");
        series4.setName("Setting 4");
         	double currentValue =0.0;
			for(int i=0; i<100;i++) {
				//double value = (1 + PerlinNoise.PerlinNoise1D(i, 0, 1))/2;
				double value = RandomWalker.randomWalker1D(currentValue,0.1,-0.1, 0.002, 0.00000, 0.4);
				currentValue = value;
				series1.getData().add(new XYChart.Data(i, value));
				//System.out.println(i+"|"+value);
			}
			 currentValue =0.0;
			for(int i=0; i<100;i++) {
				//double value = Math.random();
				double value = RandomWalker.randomWalker1D(currentValue,0.1,-0.1, 0.001, 0.0);
				currentValue = value;
				series2.getData().add(new XYChart.Data(i, value));
				//System.out.println(i+"|"+value);
			}
			 currentValue =0.0;
			for(int i=0; i<100;i++) {
				double value = RandomWalker.randomWalker1D(currentValue,0.1,-0.1, 0.002, 0.00000, 0.6);
				currentValue = value;
				series3.getData().add(new XYChart.Data(i, value));
				//System.out.println(i+"|"+value);
			}

        
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
       
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}