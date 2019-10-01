package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

public class TargetView3D extends Application{
	
	private static final double WIDTH  = 720;
	private static final double HEIGHT = 800;
	
	private static double anchorCameraY;
	private static double anchorCameraX;
	private static double anchorAngleCameraY=0;
	private static double anchorAngleCameraX=0;
	private static final DoubleProperty angleCameraX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);
	
	private static double mouseSensitivity =0.1;
	private static double mouseWheelZoomSensitivity = 800;
	private static double targetBodyRadius = 6000;
	private static double targetBodyInitialDistance = targetBodyRadius*6;
	private static double targetBodyRotSpeed = 0.1;
	
	private static String[] Target = {"earth.jpg",
							          "moon.jpg",
							          "mars.jpg",
							          "venus.jpg",
							          "mercury.jpg"
	};
	private static Sphere targetBody;
	private static SmartGroup TargetBodyGroup= new SmartGroup();
	static ImageView imageView = null ;
	
	public static void start(JFXPanel fxpanel, int targetInd) {
		targetBody = prepareTargetBody(targetInd);
		//Sphere sphere = prepareSphere(targetInd);
		
	    //TargetBodyGroup = new SmartGroup();
		TargetBodyGroup.getChildren().add(targetBody);
		TargetBodyGroup.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
		Slider slider = prepareSlider();

		
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
		TargetBodyGroup.translateZProperty().set(targetBodyInitialDistance);
		
		initMouseControl(TargetBodyGroup, scene, fxpanel, camera);
				
		/*
		scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode())
			{
				case W:
					sphere.translateZProperty().set(sphere.getTranslateZ() + 30);
					break;
				case S:
					sphere.translateZProperty().set(sphere.getTranslateZ() - 30);
					break;
				case A:
					//camera.translateXProperty().set(camera.getTranslateX()+ 30);
					sphere.rotateProperty().set(sphere.getRotate()+20);
					break;
				case D:
					//camera.translateXProperty().set(camera.getTranslateX()- 30);
					Transform transform2 = new Rotate(sphere.getRotate()+2, new Point3D(0,1,0));
					sphere.getTransforms().add(transform2);
					break;
			default:
				break;
			}
		});
		*/
		
		//scene.setTitle("JavaFx Tut Mark 1");
		fxpanel.setScene(scene);
		
		prepareAnimation(slider);

	}
	
	
private static void initMouseControl(SmartGroup group, Scene scene,JFXPanel fxpanel, Camera camera) {
	Rotate xRotate;
	Rotate yRotate;

	camera.getTransforms().addAll(
			xRotate = new Rotate(0, Rotate.Y_AXIS),
			yRotate = new Rotate(0, Rotate.X_AXIS)	
			);
	yRotate.angleProperty().bind(angleCameraY);
	xRotate.angleProperty().bind(angleCameraX);
	//camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(90-yRotate.getAngle())));
	
	scene.setOnMousePressed(event -> {
		anchorCameraX = event.getSceneX();
		anchorCameraY = event.getSceneY();
		anchorAngleCameraX = angleCameraX.get();
		anchorAngleCameraY = angleCameraY.get();
	});
	scene.setOnMouseDragged(event ->{
		angleCameraX.set(anchorAngleCameraX - (anchorCameraX - event.getSceneX())*mouseSensitivity); 
		angleCameraY.set(anchorAngleCameraY - (anchorCameraY - event.getSceneY())*mouseSensitivity); 
		camera.translateZProperty().set(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(angleCameraY.getValue()))));
		//System.out.println(angleCameraX.getValue());		
		//System.out.println(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(-angleCameraX.getValue()))));
		camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(angleCameraX.getValue())));
	});
	
	fxpanel.addMouseWheelListener(new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
				double wheelSpeed = arg0.getPreciseWheelRotation();
					if(wheelSpeed>0) {
						group.translateZProperty().set(group.getTranslateZ() + mouseWheelZoomSensitivity);
					} else {
						group.translateZProperty().set(group.getTranslateZ() - mouseWheelZoomSensitivity);
					}

		}
		
	});

}

static class SmartGroup extends Group {
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
	slider.setMin(0);
	slider.setPrefWidth(300);
	slider.setLayoutX(100);
	slider.setLayoutY(350);
	slider.setValue(targetBodyRotSpeed);
	//slider.setShowTickLabels(false);
	slider.setStyle("-fx-base: black");
	return slider;
}

private static void prepareAnimation(Slider slider) {
	AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long arg0) {
		
			targetBodyRotSpeed = slider.getValue();
			TargetBodyGroup.rotateByY(TargetBodyGroup.getRotate()+targetBodyRotSpeed);
			imageView.setTranslateX(imageView.getTranslateX()+targetBodyRotSpeed*80);
		}
		
	};
	timer.start();
}

private static ImageView prepareImageView() {
	try {
		Image backgroundImage = new Image(new FileInputStream(System.getProperty("user.dir")+"/images/SurfaceTextures/milkyway.jpg"));
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
   material.setSpecularColor(Color.valueOf("#222222"));
    	//material.setDiffuseMap( new Image(dir+"/resources/moonTexture.jpg") );
    	
    	sphere.setMaterial(material);
    	
    	return sphere;
    }
    


	public static Sphere getTargetBody() {
		return targetBody;
	}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
