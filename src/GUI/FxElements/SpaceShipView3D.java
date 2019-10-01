package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class SpaceShipView3D extends Application{
	
	private static final double WIDTH  = 400;
	private static final double HEIGHT = 400;
	
	private static double anchorX;
	private static double anchorY;
	private static double anchorAngleX=0;
	private static double anchorAngleY=0;
	private static double anchorX2;
	private static double anchorY2;
	private static double anchorAngleX2=0;
	private static double anchorAngleY2=0;
	private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleY = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleX2 = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleY2 = new SimpleDoubleProperty(0);
	
	private static double mouseSensitivity =0.1;
	private static double mouseWheelZoomSensitivity = 8;
	//private static double targetBodySize = 600;
	private static double targetBodyInitialDistance = 0.5;
	private static Group root = new Group();
	private static SmartGroup model;
	private static SmartGroup coordinateSystem;
	
	
	public static void start(JFXPanel fxpanel) {
		//Box box = prepareBox();

		 model =  loadModel(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/diamond.obj");
	    coordinateSystem =  loadCoordinateSystem(System.getProperty("user.dir")+"/images/coordinateSystem2.obj");
		//model.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
		root.getChildren().add(model);
		root.getChildren().add(coordinateSystem);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
		
		model.translateXProperty().set(WIDTH/2);
		model.translateYProperty().set(HEIGHT/2);
		model.translateZProperty().set(targetBodyInitialDistance);
		
		coordinateSystem.translateXProperty().set(WIDTH*3/4);
		coordinateSystem.translateYProperty().set(HEIGHT/4);
		coordinateSystem.translateZProperty().set(targetBodyInitialDistance/2);
		
		initMouseControl(model, coordinateSystem, scene, fxpanel);
			
		fxpanel.setScene(scene);

	}
	
	
private static void initMouseControl(Group groupOne,Group groupTwo, Scene scene,JFXPanel fxpanel) {
	/*
	Rotate xRotate;
	Rotate yRotate;
	Rotate xRotate2;
	Rotate yRotate2;
	groupOne.getTransforms().addAll(
			xRotate = new Rotate(0, Rotate.X_AXIS), 
			yRotate = new Rotate(0, Rotate.Y_AXIS));
	xRotate.angleProperty().bind(angleX);
	yRotate.angleProperty().bind(angleY);
	groupTwo.getTransforms().addAll(
			xRotate2 = new Rotate(0, Rotate.X_AXIS), 
			yRotate2 = new Rotate(0, Rotate.Y_AXIS));
	xRotate2.angleProperty().bind(angleX2);
	yRotate2.angleProperty().bind(angleY2);
	
	scene.setOnMousePressed(event -> {
		anchorX = event.getSceneX();
		anchorY = event.getSceneY();
		anchorAngleX = angleX.get();
		anchorAngleY = angleY.get();
		anchorX2 = event.getSceneX();
		anchorY2 = event.getSceneY();
		anchorAngleX2 = angleX2.get();
		anchorAngleY2 = angleY2.get();
	});
	scene.setOnMouseDragged(event ->{
		angleX.set(anchorAngleX - (anchorY - event.getSceneY())*mouseSensitivity); 
		angleY.set(anchorAngleY + (anchorX - event.getSceneX())*mouseSensitivity); 
		angleX2.set(anchorAngleX2 + (anchorY2 - event.getSceneY())*mouseSensitivity); 
		angleY2.set(anchorAngleY2 - (anchorX2 - event.getSceneX())*mouseSensitivity); 
	});
	*/
	fxpanel.addMouseWheelListener(new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
				double wheelSpeed = arg0.getPreciseWheelRotation();
					if(wheelSpeed>0) {
						groupOne.translateZProperty().set(groupOne.getTranslateZ() + mouseWheelZoomSensitivity);
					} else {
						groupOne.translateZProperty().set(groupOne.getTranslateZ() - mouseWheelZoomSensitivity);
					}

		}
		
	});

}

public static void setRotationX(int deltaRotX) {
	model.rotateByX(deltaRotX);
	coordinateSystem.rotateByX(deltaRotX);
	//model.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
	//coordinateSystem.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
}
public static void setRotationY(int deltaRotY) {
	model.rotateByY(deltaRotY);
	coordinateSystem.rotateByY(deltaRotY);
	//model.getTransforms().add(new Rotate(deltaRotY, Rotate.Y_AXIS));
}
public static void setRotationZ(int deltaRotZ) {
	model.rotateByZ(deltaRotZ);
	coordinateSystem.rotateByZ(deltaRotZ);
	//model.getTransforms().add(new Rotate(deltaRotZ, Rotate.Z_AXIS));
}

private static SmartGroup loadModel(String fileString) {
    SmartGroup modelRoot = new SmartGroup();

    ObjModelImporter importer = new ObjModelImporter();
   // importer.read(url);
    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
    }
    double scale=30;
modelRoot.setScaleX(scale);
modelRoot.setScaleY(scale);
modelRoot.setScaleZ(scale);
    return modelRoot;
}

private static SmartGroup loadCoordinateSystem(String fileString) {
    SmartGroup modelRoot = new SmartGroup();
	PhongMaterial material = new PhongMaterial();
    material.setDiffuseColor(Color.BLUE);
    ObjModelImporter importer = new ObjModelImporter();
   // importer.read(url);
    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
        view.setMaterial(material);
    }
	//material.setDiffuseMap( new Image(dir+"/resources/moonTexture.jpg") );
	
    double scale=5;
modelRoot.setScaleX(scale);
modelRoot.setScaleY(scale);
modelRoot.setScaleZ(scale);
    return modelRoot;
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


@SuppressWarnings("unused")
private static Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.WHITE);

	return ambientLight;
}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
