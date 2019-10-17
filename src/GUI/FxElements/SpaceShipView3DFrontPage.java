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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SpaceShipView3DFrontPage {
	
	private static double anchorCameraY;
	@SuppressWarnings("unused")
	private static double anchorCameraYSlider;
	private static double anchorAngleCameraY=0;
	private static final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);
	private static final DoubleProperty angleCameraYSlider = new SimpleDoubleProperty(0);
	@SuppressWarnings("unused")
	private static double anchorAngleCameraYSlider=0;
	private static double anchorX;
	private static double anchorXCamerSlider=0; 
	private static final DoubleProperty posCameraX = new SimpleDoubleProperty(0);
	private static double test ;
	
	private static final double WIDTH  = 450;
	private static final double HEIGHT = 400;
	private static double mouseWheelZoomSensitivity = 100;
	private static double mouseSensitivity =0.05;
	private static double targetBodyInitialDistance = 0.5;
	public static SmartGroup model;
	public static SmartGroup coordinateSystem;
	static double rotX=0;
	static double rotY=0;
	static double rotZ=0;
	private static String modelObjectPath = System.getProperty("user.dir")+"/INP/SpacecraftModelLibrary/millenium-falcon.obj";
	
	
	public static void setModelObjectPath(String modelObjectPath) {
		SpaceShipView3DFrontPage.modelObjectPath = modelObjectPath;
	}


	public static void start(JFXPanel fxpanel) {
		//Box box = prepareBox();

		 model =  loadModel(modelObjectPath);
	    coordinateSystem =  loadCoordinateSystem(System.getProperty("user.dir")+"/images/coordinateSystem2.obj");
		
		Group root = new Group();
		root.getChildren().add(model);
		root.getChildren().add(coordinateSystem);
		
		Camera camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		
		//final Group axes = getAxes(2.5);
		final Group grid = createGrid(2000, 100);
		
		Group environment = new Group();
		//environment.getChildren().add(axes);
		environment.getChildren().add(grid);
		
		environment.translateXProperty().set(WIDTH);
		environment.translateYProperty().set(HEIGHT*2/3);
		environment.translateZProperty().set(targetBodyInitialDistance);
		
		root.getChildren().add(environment);
		
		Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.color(0.15,0.15,0.15));
		scene.setCamera(camera);
		
		double dis = targetBodyInitialDistance;
		model.translateXProperty().set(WIDTH/2);
		model.translateYProperty().set(HEIGHT/2);
		model.translateZProperty().set(targetBodyInitialDistance);
		
		coordinateSystem.translateXProperty().set(WIDTH*4/5);
		coordinateSystem.translateYProperty().set(HEIGHT/5);
		coordinateSystem.translateZProperty().set(dis/2);
		/*
		SmartGroup modelCoord = new SmartGroup();
		modelCoord.getChildren().add(model);
		modelCoord.getChildren().add(coordinateSystem);
		*/
		//initMouseControl(model, coordinateSystem, scene, fxpanel);
		initMouseControl(model, scene, fxpanel, camera);
		
		
		BlueBookVisual.ChartPanel_DashBoardFlexibleChart.addChartMouseListener(new ChartMouseListener() {
	        @Override
	        public void chartMouseClicked(ChartMouseEvent event) {
	            // ignore
	        }
	
	        @Override
	        public void chartMouseMoved(ChartMouseEvent event) {
		        	if(BlueBookVisual.axis_chooser.getSelectedIndex()==0) {
		            Rectangle2D dataArea = BlueBookVisual.ChartPanel_DashBoardFlexibleChart.getScreenDataArea();
		            JFreeChart chart = event.getChart();
		            XYPlot plot = (XYPlot) chart.getPlot();
		            ValueAxis xAxis = plot.getDomainAxis();
		            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
		                    RectangleEdge.BOTTOM);
	
		            double max = xAxis.getUpperBound();
		            double min = xAxis.getLowerBound();
		            int indx = (int) ( (x/(max-min))*BlueBookVisual.getResultSet().size());
			            if(indx>0 && indx<BlueBookVisual.getResultSet().size()) {
			                Platform.runLater(new Runnable() {
			                    @Override
			                    public void run() {
	
			                    	double getRotx = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerX());
			                    	//System.out.println(BlueBookVisual.getResultSet().get(indx).getEulerX());
			                    	double getRoty = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerY());
			                    	double getRotz = Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerZ());
			        				try {
			                    	if(!Double.isNaN(getRotx)) {
			        				double drotX = getRotx - rotX;
			        				setRotationX(drotX);
			        				rotX = getRotx;
			                    	}
			                    	if(!Double.isNaN(getRotz)) {
			        				double drotY = getRoty - rotY;
			        				setRotationY(drotY);
			        				rotY = getRoty;
			                    	}
			                    	if(!Double.isNaN(getRotz)){
			        				double drotZ = getRotz - rotZ;
			        				setRotationZ(drotZ);
			        				rotZ = getRotz;
			                    	}

			        				} catch (Exception e) {
			        					System.err.println("Error: Rotation not possible");
			        				}
					            	/*
					            	System.out.println(Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerX())+" | "+
					            					   Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerY())+" | "+
					            					   Math.toDegrees(BlueBookVisual.getResultSet().get(indx).getEulerZ())+" | ");
			                    */
			                    }
			                });
			            }
		        	}
	        }
	});
		
		
		fxpanel.setScene(scene);

	}
	
	
	private static void initMouseControl(SmartGroup group, Scene scene,JFXPanel fxpanel, Camera camera) {
		//Rotate xRotate;
		Rotate yRotate;
		@SuppressWarnings("unused")
		Rotate yRotateSlider;
		Translate translateX;

		camera.getTransforms().addAll(
				//xRotate = new Rotate(0, Rotate.Y_AXIS),
				yRotate = new Rotate(0, Rotate.X_AXIS)	,
				yRotateSlider = new Rotate(0, Rotate.X_AXIS),
				translateX = new Translate(0,0,0)
				);
		yRotate.angleProperty().bind(angleCameraY);
		translateX.xProperty().bind(posCameraX);
		
		scene.setOnMousePressed(event -> {
			//anchorCameraX = event.getSceneX();
			anchorCameraY = event.getSceneY();
			anchorCameraYSlider = event.getSceneY();
			//anchorAngleCameraX = angleCameraX.get();
			anchorAngleCameraY = angleCameraY.get();
			anchorAngleCameraYSlider = angleCameraYSlider.get();
			anchorXCamerSlider = posCameraX.get();
			anchorX = event.getSceneX();
		});
		scene.setOnMouseDragged(event ->{
			//angleCameraX.set(anchorAngleCameraX - (anchorCameraX - event.getSceneX())*mouseSensitivity); 
			angleCameraY.set(anchorAngleCameraY + (anchorCameraY - event.getSceneY())*mouseSensitivity); 
			camera.translateZProperty().set(targetBodyInitialDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
			camera.translateYProperty().set(targetBodyInitialDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
			//camera.translateXProperty().set(anchorXCamerSlider + (anchorX - mouseSensitivity*event.getSceneX()));
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
	PhongMaterial material = new PhongMaterial();
    material.setDiffuseColor(Color.SILVER);
    ObjModelImporter importer = new ObjModelImporter();
   // importer.read(url);
    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
        view.setMaterial(material);
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

public static Group createGrid(float size, float delta) {
    if (delta < 1) {
        delta = 1;
    }
    final PolygonMesh plane = createQuadrilateralMesh(size, size, (int) (size / delta), (int) (size / delta));

    final PolygonMesh plane2 = createQuadrilateralMesh(size, size, (int) (size / delta / 5), (int) (size / delta / 5));

    PolygonMeshView meshViewXY = new PolygonMeshView(plane);
    meshViewXY.setDrawMode(DrawMode.LINE);
    meshViewXY.setCullFace(CullFace.NONE);

    PolygonMeshView meshViewXZ = new PolygonMeshView(plane);
    meshViewXZ.setDrawMode(DrawMode.LINE);
    meshViewXZ.setCullFace(CullFace.NONE);
    meshViewXZ.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    
	PhongMaterial material = new PhongMaterial();
    material.setDiffuseColor(Color.BLACK);
    meshViewXZ.setMaterial(material);

    PolygonMeshView meshViewYZ = new PolygonMeshView(plane);
    meshViewYZ.setDrawMode(DrawMode.LINE);
    meshViewYZ.setCullFace(CullFace.NONE);
    meshViewYZ.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

    PolygonMeshView meshViewXY2 = new PolygonMeshView(plane2);
    meshViewXY2.setDrawMode(DrawMode.LINE);
    meshViewXY2.setCullFace(CullFace.NONE);
    meshViewXY2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));

    PolygonMeshView meshViewXZ2 = new PolygonMeshView(plane2);
    meshViewXZ2.setDrawMode(DrawMode.LINE);
    meshViewXZ2.setCullFace(CullFace.NONE);
    meshViewXZ2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));
    meshViewXZ2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    meshViewXZ2.setMaterial(material);

    PolygonMeshView meshViewYZ2 = new PolygonMeshView(plane2);
    meshViewYZ2.setDrawMode(DrawMode.LINE);
    meshViewYZ2.setCullFace(CullFace.NONE);
    meshViewYZ2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));
    meshViewYZ2.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

   // return new Group(meshViewXY, meshViewXY2, meshViewXZ, meshViewXZ2 /*, meshViewYZ, meshViewYZ2 */);
    return new Group( meshViewXZ/*, meshViewXZ2 , meshViewYZ, meshViewYZ2 */);
}

private static PolygonMesh createQuadrilateralMesh(float width, float height, int subDivX, int subDivY) {
    final float minX = - width / 2f;
    final float minY = - height / 2f;
    final float maxX =   width / 2f;
    final float maxY =   height / 2f;

    final int pointSize = 3;
    final int texCoordSize = 2;
    // 4 point indices and 4 texCoord indices per face
    final int faceSize = 8;
    int numDivX = subDivX + 1;
    int numVerts = (subDivY + 1) * numDivX;
    float points[] = new float[numVerts * pointSize];
    float texCoords[] = new float[numVerts * texCoordSize];
    int faceCount = subDivX * subDivY;
    int faces[][] = new int[faceCount][faceSize];

    // Create points and texCoords
    for (int y = 0; y <= subDivY; y++) {
        float dy = (float) y / subDivY;
        double fy = (1 - dy) * minY + dy * maxY;

        for (int x = 0; x <= subDivX; x++) {
            float dx = (float) x / subDivX;
            double fx = (1 - dx) * minX + dx * maxX;

            int index = y * numDivX * pointSize + (x * pointSize);
            points[index] = (float) fx;
            points[index + 1] = (float) fy;
            points[index + 2] = 0.0f;

            index = y * numDivX * texCoordSize + (x * texCoordSize);
            texCoords[index] = dx;
            texCoords[index + 1] = dy;
        }
    }
    
    

    // Create faces
    int index = 0;
    for (int y = 0; y < subDivY; y++) {
        for (int x = 0; x < subDivX; x++) {
            int p00 = y * numDivX + x;
            int p01 = p00 + 1;
            int p10 = p00 + numDivX;
            int p11 = p10 + 1;
            int tc00 = y * numDivX + x;
            int tc01 = tc00 + 1;
            int tc10 = tc00 + numDivX;
            int tc11 = tc10 + 1;

            faces[index][0] = p00;
            faces[index][1] = tc00;
            faces[index][2] = p10;
            faces[index][3] = tc10;
            faces[index][4] = p11;
            faces[index][5] = tc11;
            faces[index][6] = p01;
            faces[index++][7] = tc01;
        }
    }

    int[] smooth = new int[faceCount];

    PolygonMesh mesh = new PolygonMesh(points, texCoords, faces);
    mesh.getFaceSmoothingGroups().addAll(smooth);
    return mesh;
}

public static Group getAxes(double scale) {
    Cylinder axisX = new Cylinder(1, 200);
    axisX.getTransforms().addAll(new Rotate(90, Rotate.Z_AXIS), new Translate(0, -100, 0));
    axisX.setMaterial(new PhongMaterial(Color.RED));

    Cylinder axisY = new Cylinder(1, 200);
    axisY.getTransforms().add(new Translate(0, 100, 0));
    axisY.setMaterial(new PhongMaterial(Color.GREEN));

    Cylinder axisZ = new Cylinder(1, 200);
    axisZ.setMaterial(new PhongMaterial(Color.BLUE));
    axisZ.getTransforms().addAll(new Rotate(90, Rotate.X_AXIS), new Translate(0, 100, 0));

    Group group = new Group(axisX, axisY, axisZ);
    group.getTransforms().add(new Scale(scale, scale, scale));
    return group;
}
@SuppressWarnings("unused")
private static Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.GRAY);

	return ambientLight;
}


	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
