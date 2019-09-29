package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SpaceShipView3D extends Application{
	
	private static final double WIDTH  = 400;
	private static final double HEIGHT = 400;
	
	private static double anchorX;
	private static double anchorY;
	private static double anchorAngleX=0;
	private static double anchorAngleY=0;
	private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
	private static double mouseSensitivity =0.1;
	private static double mouseWheelZoomSensitivity = 8;
	//private static double targetBodySize = 600;
	private static double targetBodyInitialDistance = 0.5;
	
	
	public static void start(JFXPanel fxpanel) {
		//Box box = prepareBox();

		Group model =  loadModel(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/diamond.obj");
		//model.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
		Group root = new Group();
		root.getChildren().add(model);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setFill(Color.WHITE);
		scene.setCamera(camera);
		
		model.translateXProperty().set(WIDTH/2);
		model.translateYProperty().set(HEIGHT/2);
		model.translateZProperty().set(targetBodyInitialDistance);
		
		initMouseControl(model, scene, fxpanel);
			
		fxpanel.setScene(scene);

	}
	
	
private static void initMouseControl(Group group, Scene scene,JFXPanel fxpanel) {
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

private static Group loadModel(String fileString) {
    Group modelRoot = new Group();

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
