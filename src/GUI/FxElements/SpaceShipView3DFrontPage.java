package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import GUI.BlueBookVisual;
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

public class SpaceShipView3DFrontPage {
	
	private static final double WIDTH  = 450;
	private static final double HEIGHT = 400;
	private static double mouseWheelZoomSensitivity = 8;
	//private static double targetBodySize = 600;
	private static double targetBodyInitialDistance = 0.5;
	public static SmartGroup model;
	public static SmartGroup coordinateSystem;
	static double rotX=0;
	static double rotY=0;
	static double rotZ=0;
	
	
	public static void start(JFXPanel fxpanel) {
		//Box box = prepareBox();

		 model =  loadModel(System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/millenium-falcon.obj");
	    coordinateSystem =  loadCoordinateSystem(System.getProperty("user.dir")+"/images/coordinateSystem2.obj");
		
		Group root = new Group();
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
		
		coordinateSystem.translateXProperty().set(WIDTH*4/5);
		coordinateSystem.translateYProperty().set(HEIGHT/5);
		coordinateSystem.translateZProperty().set(targetBodyInitialDistance/2);
		
		initMouseControl(model, coordinateSystem, scene, fxpanel);
			/*
		BlueBookVisual.sliderEuler1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
				double drotX = BlueBookVisual.sliderEuler1.getValue() - rotX;
				rotX=BlueBookVisual.sliderEuler1.getValue();
				
				setRotationX( drotX);
                    }
                });
			}
			
		});
*/
		BlueBookVisual.ChartPanel_DashBoardOverviewChart_Altitude_Velocity.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
	            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardOverviewChart_Altitude_Velocity.getScreenDataArea();
	            JFreeChart chart = event.getChart();
	            XYPlot plot = (XYPlot) chart.getPlot();
	            ValueAxis xAxis = plot.getDomainAxis();
	            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
	                    RectangleEdge.BOTTOM);

	            double max = xAxis.getUpperBound();
	            double min = xAxis.getLowerBound();
	            int indx = (int) ( (1- x/(max-min))*BlueBookVisual.getResultSet().size());
		            if(indx>0 && indx<BlueBookVisual.getResultSet().size()) {
		                Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
		        	          //  setRotationZ(0);
		        	           // setRotationY(0);
		        	           // setRotationX(0);
		        				double drotX = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerX())- rotX;
		        				rotX=Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerX());
		        				double drotZ = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerY())- rotZ;
		        				rotZ=Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerY());
		        				double drotY = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerZ())- rotY;
		        				rotY=Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerZ());
				            	setRotationX(drotX);
				            	setRotationZ(drotZ);
				            	setRotationX(drotY);
				            	//System.out.println(BlueBookVisual.getResultSet().get(indx).getEulerX()+" | "+)
		                    }
		                });
		            }
	        }
	});
		
		
		fxpanel.setScene(scene);

	}
	
	
private static void initMouseControl(Group groupOne,Group groupTwo, Scene scene,JFXPanel fxpanel) {
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

public static void setRotationX(double deltaRotX) {
	model.rotateByX(deltaRotX);
	coordinateSystem.rotateByX(deltaRotX);
	//model.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
	//coordinateSystem.getTransforms().add(new Rotate(deltaRotX, Rotate.X_AXIS));
	/*
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
	*/
}
public static void setRotationY(double deltaRotY) {
	model.rotateByY(deltaRotY);
	coordinateSystem.rotateByY(deltaRotY);
	//model.getTransforms().add(new Rotate(deltaRotY, Rotate.Y_AXIS));
	/*
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
	*/
}
public static void setRotationZ(double deltaRotZ) {
	model.rotateByZ(deltaRotZ);
	coordinateSystem.rotateByZ(deltaRotZ);
	//model.getTransforms().add(new Rotate(deltaRotZ, Rotate.Z_AXIS));
	/*
	model.translateZProperty().set(model.getTranslateZ() + 0.1);
	model.translateZProperty().set(model.getTranslateZ() - 0.1);
	*/
}

private static SmartGroup loadModel(String fileString) {
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


@SuppressWarnings("unused")
private static Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.WHITE);

	return ambientLight;
}


	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
