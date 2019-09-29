package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
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
	
	private static double anchorX;
	private static double anchorY;
	private static double anchorAngleX=0;
	private static double anchorAngleY=0;
	private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
	private static double mouseSensitivity =0.1;
	private static double mouseWheelZoomSensitivity = 800;
	private static double targetBodySize = 6000;
	private static double targetBodyInitialDistance = targetBodySize*6;
	
	private static String[] Target = {"earth.jpg",
							   "moon.jpg",
							   "mars.jpg",
							   "venus.jpg",
							   "mercury.jpg"
	};
	private static Sphere targetBody;
	private static SmartGroup TargetBodyGroup= new SmartGroup();
	
	public static void start(JFXPanel fxpanel, int targetInd) {
		targetBody = prepareTargetBody(targetInd);
		//Sphere sphere = prepareSphere(targetInd);
		
	    //TargetBodyGroup = new SmartGroup();
		TargetBodyGroup.getChildren().add(targetBody);
		TargetBodyGroup.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
		Group root = new Group();
		root.getChildren().add(TargetBodyGroup);
		root.getChildren().add(prepareImageView());
		
		Camera camera = new PerspectiveCamera();
		//camera.setNearClip(1);
		//camera.setFarClip(10000);
	
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setFill(Color.BLACK);
		scene.setCamera(camera);
		
		TargetBodyGroup.translateXProperty().set(WIDTH/2);
		TargetBodyGroup.translateYProperty().set(HEIGHT/2);
		TargetBodyGroup.translateZProperty().set(targetBodyInitialDistance);
		
		initMouseControl(TargetBodyGroup, scene, fxpanel);
				
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

	}
	
	
private static void initMouseControl(SmartGroup group, Scene scene,JFXPanel fxpanel) {
	Rotate xRotate;
	Rotate yRotate;
	group.getTransforms().addAll(
			xRotate = new Rotate(0, Rotate.X_AXIS), 
			yRotate = new Rotate(0, Rotate.Y_AXIS));
	xRotate.angleProperty().bind(angleX);
	yRotate.angleProperty().bind(angleY);
	
	scene.setOnMousePressed(event -> {
		anchorX = event.getSceneX();
		anchorY = event.getSceneY();
		anchorAngleX = angleX.get();
		anchorAngleY = angleY.get();
	});
	scene.setOnMouseDragged(event ->{
		angleX.set(anchorAngleX - (anchorY - event.getSceneY())*mouseSensitivity); 
		angleY.set(anchorAngleY + (anchorX - event.getSceneX())*mouseSensitivity); 
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
	
	void rotateByX(int angle) {
		r = new Rotate(angle, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	void rotateByY(int angle) {
		r = new Rotate(angle, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	void rotateByZ(int angle) {
		r = new Rotate(angle, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
}

private static ImageView prepareImageView() {
	ImageView imageView = null ;
	try {
		Image backgroundImage = new Image(new FileInputStream(System.getProperty("user.dir")+"/images/SurfaceTextures/milkyway.jpg"));
	 imageView = new ImageView(backgroundImage);
	//imageView.setPreserveRatio(true);
	imageView.getTransforms().add(new Translate(0,0,-10000));
	imageView.resize(30000, 30000);
	
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
    	Sphere sphere = new Sphere(targetBodySize);
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
    
    public static void changeTargetBody(int targetInd) {
    	Task task = new Task<Void>() {
    	    @Override public Void call() {
    TargetBodyGroup.getChildren().remove(targetBody);
    //	TargetBodyGroup.getChildren().removeAll();
    targetBody = new Sphere(targetBodySize+150);
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
    	
   targetBody.setMaterial(material);
    	
    	TargetBodyGroup.getChildren().add(targetBody);
		return null;

    	    }
    	};
    	new Thread(task).start();
    }
    


	public static Sphere getTargetBody() {
		return targetBody;
	}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
