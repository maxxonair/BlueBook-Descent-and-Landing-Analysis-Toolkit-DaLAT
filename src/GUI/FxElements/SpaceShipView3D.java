package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import GUI.SimulationSetup.BasicSetup.AttitudeSetting;
import GUI.SimulationSetup.BasicSetup.Vector3;
import javafx.application.Application;
import javafx.application.Platform;
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
	
	private   final double WIDTH  = 450;
	private   final double HEIGHT = 400;
	private   double mouseWheelZoomSensitivity = 8;
	//private   double targetBodySize = 600;
	private   double targetBodyInitialDistance = 0.5;
	private   Group root = new Group();
	private   SmartGroup model;
	private   SmartGroup coordinateSystem;
	  double rotX=0;
	  double rotY=0;
	  double rotZ=0;
	  
	  Vector3 rotState = new Vector3(0,0,0);
	
	
	public   void start(JFXPanel fxpanel) {
		//Box box = prepareBox();

		 model =  loadModel(System.getProperty("user.dir")+"/resourcs/models3D/millenium-falcon.obj");
	    coordinateSystem =  loadCoordinateSystem(System.getProperty("user.dir")+"/images/coordinateSystem2.obj");
		//model.getChildren().add(prepareAmbientLight());
		//group.getChildren().add(prepareSun());
		
		root.getChildren().add(model);
		root.getChildren().add(coordinateSystem);
		
		final Group grid = SpaceShipView3DFrontPage.createGrid(2000, 100);
		
		Group environment = new Group();
		//environment.getChildren().add(axes);
		environment.getChildren().add(grid);
		environment.translateYProperty().set(HEIGHT);
		root.getChildren().add(environment);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		//scene.setFill(Color.WHITE);
		scene.setFill(Color.color(0.15,0.15,0.15));
		scene.setCamera(camera);
		
		model.translateXProperty().set(WIDTH/2);
		model.translateYProperty().set(HEIGHT/2);
		model.translateZProperty().set(targetBodyInitialDistance);
		
		coordinateSystem.translateXProperty().set(WIDTH*4/5);
		coordinateSystem.translateYProperty().set(HEIGHT/5);
		coordinateSystem.translateZProperty().set(targetBodyInitialDistance/2);
		
		initMouseControl(model, coordinateSystem, scene, fxpanel);

		AttitudeSetting.sliderEuler1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                    	
				//double drotX = AttitudeSetting.sliderEuler1.getValue() - rotX;
				rotX=AttitudeSetting.sliderEuler1.getValue();
				setRotationX( rotX);
				//new TurnAction(model.rz, 15);

                    }
                });
			}
			
		});
		AttitudeSetting.sliderEuler2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                    	
				//double drotZ = AttitudeSetting.sliderEuler2.getValue() - rotZ;
				rotZ=AttitudeSetting.sliderEuler2.getValue();
				setRotationZ( rotZ);
				
                    }
                });
			}
			
		});
		AttitudeSetting.sliderEuler3.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                    	
				//double drotY = AttitudeSetting.sliderEuler3.getValue() - rotY;
				rotY=AttitudeSetting.sliderEuler3.getValue();
				
				setRotationY( rotY);
				
                    }
                });
			}
			
		});
		fxpanel.setScene(scene);

	}
	
	
private   void initMouseControl(Group groupOne,Group groupTwo, Scene scene,JFXPanel fxpanel) {
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

public   void setRotationX(double deltaRotX) {
	model.rotateByX(deltaRotX);
	coordinateSystem.rotateByX(deltaRotX);
	//model.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
	//coordinateSystem.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
}
public   void setRotationY(double deltaRotY) {
	model.rotateByY(deltaRotY);
	coordinateSystem.rotateByY(deltaRotY);
	//model.getTransforms().add(new Rotate(deltaRotY, Rotate.Y_AXIS));
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
}
public   void setRotationZ(double deltaRotZ) {
	model.rotateByZ(deltaRotZ);
	coordinateSystem.rotateByZ(deltaRotZ);
	//model.getTransforms().add(new Rotate(deltaRotZ, Rotate.Z_AXIS));
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
}


private   SmartGroup loadModel(String fileString) {
	SmartGroup modelRoot = new SmartGroup();

    ObjModelImporter importer = new ObjModelImporter();
   // importer.read(url);
    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
    }
    double scale=3;
modelRoot.setScaleX(scale);
modelRoot.setScaleY(scale);
modelRoot.setScaleZ(scale);
    return modelRoot;
}


private   SmartGroup loadCoordinateSystem(String fileString) {
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

  class SmartGroup extends Group {
	Rotate r;
	Transform t = new Rotate();
	
	void rotBack() {
		r = new Rotate(-rotState.z, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(-rotState.y, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(-rotState.x, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	void rotateByX(double angle) {
		rotBack();
		r = new Rotate(angle, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(rotState.y, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(rotState.z, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		rotState.x = angle;
	}
	
	void rotateByY(double angle) {
		rotBack();
		r = new Rotate(rotState.x, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(angle, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(rotState.z, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		
		rotState.y = angle;
	}
	void rotateByZ(double angle) {
		rotBack();
		r = new Rotate(rotState.x, Rotate.X_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(rotState.y, Rotate.Y_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		r = new Rotate(angle, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
		rotState.z = angle;
	}
	/*
	void rotateBy(double x, double y, double z) {
		r = new Rotate(angle, Rotate.Z_AXIS);
		t = t.createConcatenation(r);
		this.getTransforms().clear();
		this.getTransforms().addAll(t);
	}
	*/
}


@SuppressWarnings("unused")
private   Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.WHITE);

	return ambientLight;
}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
