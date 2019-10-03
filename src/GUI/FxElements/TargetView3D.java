package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import GUI.BlueBookVisual;
import Simulator_main.RealTimeResultSet;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class TargetView3D extends Application{
	
	private static final double WIDTH  = 720;
	private static final double HEIGHT = 800;
	
	private static double anchorCameraY;
	private static double anchorCameraYSlider;
	private static double anchorCameraX;
	private static double anchorAngleCameraY=0;
	private static double anchorAngleCameraYSlider=0;
	private static double anchorAngleCameraX=0;
	private static final DoubleProperty angleCameraX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleCameraYSlider = new SimpleDoubleProperty(0);
	
	private static double mouseSensitivity =0.1;
	private static double mouseWheelZoomSensitivity = 800;
	private static double targetBodyRadius = 6000;
	private static double targetBodyInitialDistance;
	private static double targetBodyRotSpeed = 0.04;
	
	private static String[] Target = {"earth.jpg",
							          "moon.jpg",
							          "mars.jpg",
							          "venus.jpg",
							          "mercury.jpg"
	};
	public static SmartGroup TargetBodyGroup= new SmartGroup();
	static ImageView imageView = null ;
	static Image backgroundImage;
	static Slider slider ;
	public static SmartGroup Spacecraft = new SmartGroup();
	static AnimationTimer timer;
	static boolean animationSwitch=true; 
	
	public static void start(JFXPanel fxpanel, int targetInd) {
    	TargetView3D.TargetBodyGroup.getChildren().clear();;
		Sphere targetBody = prepareTargetBody(targetInd);
		SmartGroup trajectorySet = prepareTrajectory();
		 Spacecraft = prepareSpacecraft(0);
		TargetBodyGroup.getChildren().add(targetBody);
		TargetBodyGroup.getChildren().add(trajectorySet);
		TargetBodyGroup.getChildren().add(Spacecraft);
		//TargetBodyGroup.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
	  slider = prepareSlider();

		
		Group root = new Group();
		root.getChildren().add(prepareImageView());
		root.getChildren().add(TargetBodyGroup);
		root.getChildren().add(slider);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(1);
		camera.setFarClip(10000);
		

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);
		
		TargetBodyGroup.translateXProperty().set(WIDTH/2);
		TargetBodyGroup.translateYProperty().set(HEIGHT/2);
		targetBodyInitialDistance = targetBodyRadius*6;
		TargetBodyGroup.translateZProperty().set(targetBodyInitialDistance);
		
		initMouseControl(TargetBodyGroup, scene, fxpanel, camera);
				
		scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
			case A:
				rotatePlanet(false);
				break;
			case D:
				rotatePlanet(true);
				break;
			case S:
				if(animationSwitch) {
					timer.stop();
				} else {
					timer.start();
				}
				animationSwitch = !animationSwitch;
				break;
			}
		});
		fxpanel.setScene(scene);
		
		prepareAnimation(slider);

	}
	
	
private static void initMouseControl(SmartGroup group, Scene scene,JFXPanel fxpanel, Camera camera) {
	//Rotate xRotate;
	Rotate yRotate;
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
						targetBodyInitialDistance -= mouseWheelZoomSensitivity;
					}
					group.translateZProperty().set(targetBodyInitialDistance);
					camera.translateZProperty().set(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
					camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
		}
		
	});

}

public static class SmartGroup extends Group {
	Rotate r;
	Transform t = new Rotate();
	
	void rotateByX(double angle) {
		r = new Rotate(angle, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	void rotateByY(double angle) {
		r = new Rotate(angle, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	void rotateByZ(double angle) {
		r = new Rotate(angle, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
}

private static Slider prepareSlider() {
	Slider slider = new Slider();
	slider.setMax(1.0);
	slider.setMin(-0.5);
	slider.setPrefWidth(300);
	slider.setLayoutX(100);
	slider.setLayoutY(350);
	slider.setValue(targetBodyRotSpeed);
	//slider.setShowTickLabels(false);
	slider.setDisable(false);
	slider.setStyle("-fx-base: black");
	return slider;
}

private static void prepareAnimation(Slider slider) {
	 timer = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
		
			targetBodyRotSpeed = slider.getValue();
			TargetBodyGroup.rotateByY(TargetBodyGroup.getRotate()+targetBodyRotSpeed);
			if(imageView.getTranslateX()<(backgroundImage.getWidth()*imageView.getScaleX()*2/5)){
				imageView.setTranslateX(imageView.getTranslateX()+targetBodyRotSpeed*80);	
			} else {
			imageView.setTranslateX(-(backgroundImage.getWidth()*imageView.getScaleX()*2/5));
			}
		}
		
	};
	timer.start();
}

private static void rotatePlanet(boolean direction) {
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

private static ImageView prepareImageView() {
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

private static SmartGroup prepareTrajectory() {
	List<RealTimeResultSet> resultSet = BlueBookVisual.getResultSet();
	int trajectoryElementSize  = 9;
	SmartGroup trajectorySet = new SmartGroup();
	
		for(int i=0;i<resultSet.size();i+=10) {
	    	Sphere sphere = new Sphere(trajectoryElementSize);
	    	PhongMaterial material = new PhongMaterial();
	    material.setSpecularColor(Color.GREEN); 	
	    material.setDiffuseColor(Color.GREEN);
	    	sphere.setMaterial(material);	
	    	sphere.translateXProperty().set( (resultSet.get(i).getCartesianPosECEF()[0][0]/1000) );
	    	sphere.translateYProperty().set( (-resultSet.get(i).getCartesianPosECEF()[2][0]/1000) );
	    	sphere.translateZProperty().set( (resultSet.get(i).getCartesianPosECEF()[1][0]/1000) );
	    	trajectorySet.getChildren().add(sphere);
		}
		
	return trajectorySet;
}

public static SmartGroup prepareSpacecraft(int indx) {
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
				TargetView3D.Spacecraft.getChildren().clear();
				List<RealTimeResultSet> resultSet = BlueBookVisual.getResultSet();
				int trajectoryElementSize  = 15;
				
				    	Sphere sphere = new Sphere(trajectoryElementSize);
				    	PhongMaterial material = new PhongMaterial();
				    material.setSpecularColor(Color.RED); 	
				    material.setDiffuseColor(Color.RED);
				    	sphere.setMaterial(material);	
				    	sphere.translateXProperty().set( (resultSet.get(indx).getCartesianPosECEF()[0][0]/1000) );
				    	sphere.translateYProperty().set( (-resultSet.get(indx).getCartesianPosECEF()[2][0]/1000) );
				    	sphere.translateZProperty().set( (resultSet.get(indx).getCartesianPosECEF()[1][0]/1000) );
				    	Spacecraft.getChildren().add(sphere);
        }
    });	
	return Spacecraft;		
}

private static Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.WHITE);

	return ambientLight;
}

@SuppressWarnings("unused")
private static Node prepareSun(){
	
	PointLight sun = new PointLight();
	sun.setColor(Color.WHITE);
	sun.getTransforms().add(new Translate(0,0,-100));


	Sphere sunMockup = new Sphere(100);
	sunMockup.getTransforms().setAll(sun.getTransforms());

	return sun;
}
    
    public static Sphere prepareTargetBody(int targetInd) {
    	targetBodyRadius = BlueBookVisual.getRM()/1000;
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
