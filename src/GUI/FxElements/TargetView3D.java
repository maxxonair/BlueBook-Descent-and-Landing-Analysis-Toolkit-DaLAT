package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

import GUI.Dashboard.DashboardPlotArea;
import GUI.Dashboard.Data2DPlot;
import GUI.Dashboard.VariableList2;
import Simulator_main.DataSets.RealTimeResultSet;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.transform.Translate;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TargetView3D extends Application{
	
	private   final double WIDTH  = 400;
	private   final double HEIGHT = 350;
	
	private   double anchorCameraY;
	@SuppressWarnings("unused")
	private   double anchorCameraYSlider;
	@SuppressWarnings("unused")
	private   double anchorCameraX;
	private   double anchorAngleCameraY=0;
	@SuppressWarnings("unused")
	private   double anchorAngleCameraYSlider=0;
	@SuppressWarnings("unused")
	private   double anchorAngleCameraX=0;
	@SuppressWarnings("unused")
	private   final DoubleProperty angleCameraX = new SimpleDoubleProperty(0);
	private   final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);
	private   final DoubleProperty angleCameraYSlider = new SimpleDoubleProperty(0);
	
	private   double mouseSensitivity =0.05;
	private   double mouseWheelZoomSensitivity = 300;
	private   double targetBodyRadius = 0;
	private   double targetBodyInitialDistance;
	public   double targetBodyRotSpeed = 0.08;
	
	private   String[] Target = {"earth.jpg",
							          "moon.jpg",
							          "mars.jpg",
							          "venus.jpg",
							          "mercury.jpg"
	};
	
    private static double[][] dataMain = { // RM (average radius) [m] || Gravitational Parameter [m3/s2] || Rotational speed [rad/s] || Average Collision Diameter [m]
			{6371000,3.9860044189E14,7.2921150539E-5,1.6311e-9}, 	// Earth
			{1737400,4903E9,2.661861E-6,320},						// Moon (Earth)
			{3389500,4.2838372E13,7.0882711437E-5,1.6311e-9},		// Mars
			{0,0,0,0},												// Venus
	 };
	
	public   SmartGroup TargetBodyGroup= new SmartGroup();
	  ImageView imageView = null ;
	  Image backgroundImage;
	  Slider slider ;
	   static SmartGroup Spacecraft = new SmartGroup();
	//  AnimationTimer timer;
	  boolean animationSwitch=true; 
	  AnimationTimer timer ;
	  private int targetInd;
	  
	  private static List<RealTimeResultSet> resultSet;
	
	
	public TargetView3D(int targetInd, List<RealTimeResultSet> resultSett) {
		this.targetInd = targetInd;
		resultSet = resultSett;
		targetBodyRadius = dataMain[targetInd][0]/1000;
	}
	

	public   void start(JFXPanel fxpanel) {
		
    	this.TargetBodyGroup.getChildren().clear();
    	
		Sphere targetBody = prepareTargetBody(targetInd);
		SmartGroup trajectorySet = prepareTrajectory();
		 Spacecraft = prepareSpacecraft(0);
		 
		TargetBodyGroup.getChildren().add(targetBody);
		TargetBodyGroup.getChildren().add(trajectorySet);
		TargetBodyGroup.getChildren().add(Spacecraft);
		//TargetBodyGroup.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());

		
		Group root = new Group();
		root.getChildren().add(prepareImageView());
		root.getChildren().add(TargetBodyGroup);
		//root.getChildren().add(slider);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(1);
		camera.setFarClip(100000);

		
		PointLight sun = (PointLight) prepareSun();
		//AmbientLight ambientLight = (AmbientLight) prepareAmbientLight();
		
		Group lightGroup = new Group();
		lightGroup.getChildren().add(sun);
		//lightGroup.getChildren().add(ambientLight);
		root.getChildren().add(lightGroup);
		
		Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);
		
		TargetBodyGroup.translateXProperty().set(WIDTH/2);
		TargetBodyGroup.translateYProperty().set(HEIGHT/2);
		targetBodyInitialDistance = targetBodyRadius*6;
		TargetBodyGroup.translateZProperty().set(targetBodyInitialDistance);
		
		initMouseControl(TargetBodyGroup, scene, fxpanel, camera);
				
		fxpanel.setScene(scene);
		
	    timer = prepareAnimation();
	    
		if( DashboardPlotArea.getContentPanelList().get(0).getID()==1) {
			addMouseLink();
		}
	}
	
	public void addMouseLink() {
		Data2DPlot plotElement = (Data2DPlot) DashboardPlotArea.getContentPanelList().get(0);
		ChartPanel chartPanel = plotElement.getPlotElement().getChartPanel();
		VariableList2 varX = plotElement.getPlotElement().getVariableListX();
		
		chartPanel.addChartMouseListener(new ChartMouseListener() {
		        @Override
		        public void chartMouseClicked(ChartMouseEvent event) {
		            // ignore
		        }
		
		        @Override
		        public void chartMouseMoved(ChartMouseEvent event) {
			        	if(varX.getSelectedIndx()==0) {
			            Rectangle2D dataArea = chartPanel.getScreenDataArea();
			            JFreeChart chart = event.getChart();
			            XYPlot plot = (XYPlot) chart.getPlot();
			            ValueAxis xAxis = plot.getDomainAxis();
			            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
			                    RectangleEdge.BOTTOM);
		
			            double max = xAxis.getUpperBound();
			            double min = xAxis.getLowerBound();
			            int indx = (int) ( (x/(max-min))*DashboardPlotArea.getResultSet().size());
				            if(indx>0 && indx<DashboardPlotArea.getResultSet().size()) {
				                Platform.runLater(new Runnable() {
				                    @Override
				                    public void run() {
				                    	 TargetView3D.prepareSpacecraft(indx);
				                    }
				                });
				            }
			        	}
		        }
		});
	}
	
	public   void playPauseAnimation() {
		if(animationSwitch) {
			timer.stop();
		} else {
			timer.start();
		}
		animationSwitch = !animationSwitch;
	}
private   void initMouseControl(SmartGroup group, Scene scene,JFXPanel fxpanel, Camera camera) {
	//Rotate xRotate;
	Rotate yRotate;
	@SuppressWarnings("unused")
	Rotate yRotateSlider;

	camera.getTransforms().addAll(
			//xRotate = new Rotate(0, Rotate.Y_AXIS),
			yRotate = new Rotate(0, Rotate.X_AXIS)	,
			yRotateSlider = new Rotate(0, Rotate.X_AXIS)
			);
	yRotate.angleProperty().bind(angleCameraY);
	//yRotateSlider.angleProperty().bind(angleCameraY);
	//xRotate.angleProperty().bind(angleCameraX);
	//camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(90-yRotate.getAngle())));
	
	scene.setOnMousePressed(event -> {
		//anchorCameraX = event.getSceneX();
		anchorCameraY = event.getSceneY();
		anchorCameraYSlider = event.getSceneY();
		//anchorAngleCameraX = angleCameraX.get();
		anchorAngleCameraY = angleCameraY.get();
		anchorAngleCameraYSlider = angleCameraYSlider.get();
	});
	scene.setOnMouseDragged(event ->{
		//angleCameraX.set(anchorAngleCameraX - (anchorCameraX - event.getSceneX())*mouseSensitivity); 
		angleCameraY.set(anchorAngleCameraY + (anchorCameraY - event.getSceneY())*mouseSensitivity); 
		camera.translateZProperty().set(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
		camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
	});
	
	fxpanel.addMouseWheelListener(new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
				double wheelSpeed = arg0.getPreciseWheelRotation();
					if(wheelSpeed>0) {
						targetBodyInitialDistance += mouseWheelZoomSensitivity;
					} else {
						if(targetBodyInitialDistance>1.05*targetBodyRadius) {
						targetBodyInitialDistance -= mouseWheelZoomSensitivity;
						}
					}
					group.translateZProperty().set(targetBodyInitialDistance);
					camera.translateZProperty().set(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
					camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
		}
		
	});

}


private   AnimationTimer prepareAnimation() {
	
	AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
		
			//targetBodyRotSpeed = slider.getValue();
			TargetBodyGroup.rotateByY(TargetBodyGroup.getRotate()-targetBodyRotSpeed);
			if(imageView.getTranslateX()<(backgroundImage.getWidth()*imageView.getScaleX()*2/5)){
				imageView.setTranslateX(imageView.getTranslateX()+targetBodyRotSpeed*80);	
			} else {
			imageView.setTranslateX(-(backgroundImage.getWidth()*imageView.getScaleX()*2/5));
			}
		}
		
	};
	timer.start();
	return timer;
}

public   void refreshTargetGroup(int targetInd) {
	this.TargetBodyGroup.getChildren().clear();;
	Sphere targetBody = prepareTargetBody(targetInd);
	SmartGroup trajectorySet = prepareTrajectory();
	 Spacecraft = prepareSpacecraft(0);
	TargetBodyGroup.getChildren().add(targetBody);
	TargetBodyGroup.getChildren().add(trajectorySet);
	TargetBodyGroup.getChildren().add(Spacecraft);
}

@SuppressWarnings("unused")
private   void rotatePlanet(boolean direction) {
	double rotSpeed=0.03;
	if(direction) {
		rotSpeed =  1  ;
	} else {
		rotSpeed = -1;
	}
	TargetBodyGroup.rotateByY(TargetBodyGroup.getRotate()+rotSpeed);
	if(imageView.getTranslateX()<(backgroundImage.getWidth()*imageView.getScaleX()*2/5)){
		imageView.setTranslateX(imageView.getTranslateX()+rotSpeed*80);	
	} else {
	imageView.setTranslateX(-(backgroundImage.getWidth()*imageView.getScaleX()*2/5));
	}	
}

private   ImageView prepareImageView() {
	try {
	backgroundImage = new Image(new FileInputStream(System.getProperty("user.dir")+"/images/SurfaceTextures/milkyway.jpg"));
	imageView = new ImageView(backgroundImage);
	//imageView.setPreserveRatio(true);
	imageView.getTransforms().add(new Translate(0,0,200000));
	//imageView.setTranslateZ(targetBodySize*10);
	imageView.setScaleX(100);
	imageView.setScaleY(100);
	//imageView.resize(300000, 300000);
	
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return imageView;
}

private   SmartGroup prepareTrajectory() {
	//List<RealTimeResultSet> resultSet = DashboardPlotArea.getResultSet();
	int trajectoryElementSize  = 9;
	SmartGroup trajectorySet = new SmartGroup();
	
		for(int i=0;i<resultSet.size();i+=10) {
	    	Sphere sphere = new Sphere(trajectoryElementSize);
	    	PhongMaterial material = new PhongMaterial();
	    material.setSpecularColor(Color.GREEN); 	
	    material.setDiffuseColor(Color.GREEN);
	    	sphere.setMaterial(material);	
	    //	System.out.println( (resultSet.get(i).getCartesianPosECEF()[0]/1000)  );
	    	sphere.translateXProperty().set( (resultSet.get(i).getCartesianPosECEF()[0]/1000) );
	    	sphere.translateYProperty().set( (-resultSet.get(i).getCartesianPosECEF()[2]/1000) );
	    	sphere.translateZProperty().set( (resultSet.get(i).getCartesianPosECEF()[1]/1000) );
	    	trajectorySet.getChildren().add(sphere);
		}
		
	return trajectorySet;
}

public static   SmartGroup prepareSpacecraft(int indx) {
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
				Spacecraft.getChildren().clear();
				
				int trajectoryElementSize  = 15;
				
				    	Sphere sphere = new Sphere(trajectoryElementSize);
				    	PhongMaterial material = new PhongMaterial();
				    material.setSpecularColor(Color.RED); 	
				    material.setDiffuseColor(Color.RED);
				    	sphere.setMaterial(material);	
				    	try {
				    	sphere.translateXProperty().set( ( resultSet.get(indx).getCartesianPosECEF()[0]/1000) );
				    	sphere.translateYProperty().set( (-resultSet.get(indx).getCartesianPosECEF()[2]/1000) );
				    	sphere.translateZProperty().set( ( resultSet.get(indx).getCartesianPosECEF()[1]/1000) );
				    	Spacecraft.getChildren().add(sphere);
				    	} catch (IndexOutOfBoundsException exception) {
				    		System.out.println("Error/TargetView3D: Index out of bound > result set empty.");
				    	}
        }
    });	
	return Spacecraft;		
}

@SuppressWarnings("unused")
private   Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.WHITE);

	return ambientLight;
}

@SuppressWarnings("unused")
private   Node prepareSun(){
	
	PointLight sun = new PointLight();
	sun.setColor(Color.WHITE);
	sun.getTransforms().add(new Translate(0,0,-100));


	Sphere sunMockup = new Sphere(100);
	sunMockup.getTransforms().setAll(sun.getTransforms());

	return sun;
}
    
    public   Sphere prepareTargetBody(int targetInd) {
   // 	targetBodyRadius = BlueBookVisual.getRM()/1000;
    	Sphere sphere = new Sphere(targetBodyRadius);
    	PhongMaterial material = new PhongMaterial();

    	String dir = System.getProperty("user.dir");
    	//System.out.println(dir+"/resources/moonTexture.jpg");
    //	material.setDiffuseMap( new Image(getClass().getResourceAsStream("/resources/moonTexture.jpg") ));
    //	material.setDiffuseMap(new Image(getClass().getResourceAsStream("moonTexture.jpg")));
    	try {
			Image texture = new Image(new FileInputStream(dir+"/images/SurfaceTextures/"+Target[targetInd]));
			material.setDiffuseMap(texture);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Loading surface texture failed.");
		}
   //material.setSpecularColor(Color.valueOf("#222222"));
    	//material.setDiffuseMap( new Image(dir+"/resources/moonTexture.jpg") );
    	
    	sphere.setMaterial(material);
    	
    	return sphere;
    }

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
