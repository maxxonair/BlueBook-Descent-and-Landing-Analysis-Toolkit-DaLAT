package GUI.FxElements;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import GUI.Dashboard.AttitudeView;
import GUI.Dashboard.DashboardPlotArea;
import GUI.Dashboard.Data2DPlot;
import GUI.Dashboard.VariableList2;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point3D;
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
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import utils.Quaternion;
import utils.Vector3;
import utils.Vector4;

/**
 * In JavaFX, the camera coordinate system is as follows:

		• X-axis pointing to the right
		
		• Y-axis pointing down
		
		• Z-axis pointing away from the viewer or into the screen.
 */
public class AttitudeFx {
	
	private boolean isZoom = false;
	
	double holdValx=0;
	double holdValy=0;
	
	 double frameX=610;
	 double frameY=550;
	
	 double ref1 = ( - frameX/1.24);
	 double ref2 = ( - frameY/2);

	private   double anchorAngleCameraY=0;
	private   double anchorAngleCameraX=0;
	private   final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);	
	private   final DoubleProperty angleCameraX = new SimpleDoubleProperty(0);
	
	private   double mouseWheelZoomSensitivity = 0.5;
	private   double mouseSensitivity =0.05;
	
	private   double cameraToTargetDistance = 0;
	public   SmartGroup model;
	//public   SmartGroup coordinateSystem;
	
    @SuppressWarnings("unused")
	private Translate translate;
    private Rotate rotateX,rotateY;
	
    double gridScale=5;
    double modelScale=3;
    
    JFXPanel fxPanel;
    private PerspectiveCamera camera;

	private   String modelObjectPath;
	 Vector3 rotState = new Vector3(0,0,0);
	 
	 private Quaternion quatTemp = new Quaternion(1,0,0,0);
	
	public void setModelObjectPath(String modelObjectPath) {
		this.modelObjectPath = modelObjectPath;
	}
	
	public void updateFrameSize(double framex, double framey) {
		this.frameX = framex;
		this.frameY = framey;
		this.ref1 = ( - frameX/3);
		this.ref2 = ( - frameY/2);

	}


	public  void start(JFXPanel fxpanel, String objectFilePath) {
		 this.modelObjectPath=objectFilePath;
		 this.fxPanel = fxpanel;
		 model =  loadModel(modelObjectPath);
		
		Group root = new Group();
		root.getChildren().add(model);
		//root.getChildren().add(coordinateSystem);
		
	    camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		
		SmartGroup cameraGroup = new SmartGroup();
		cameraGroup.getChildren().add(camera);

		camera.getTransforms().addAll(
                rotateY = new Rotate(0, Rotate.Y_AXIS),
                rotateX = new Rotate(0, Rotate.X_AXIS),
                //rotateZ = new Rotate(0, Rotate.Z_AXIS),
                translate = new Translate(ref1, ref2, -cameraToTargetDistance)
        );
		
		//final Group axes = getAxes(2.5);
		final Group grid = createGrid(2000, 100);
		
		Group environment = new Group();
		//environment.getChildren().add(axes);
		environment.getChildren().add(grid);
		
		environment.translateXProperty().set(0);
		environment.translateYProperty().set(0);
		environment.translateZProperty().set(0);
		
		root.getChildren().add(environment);
		root.getChildren().add(camera);
		
		Scene scene = new Scene(root, 450, 450, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.color(0.15,0.15,0.15));
		scene.setCamera(camera);		
		

		initMouseControl(scene, fxpanel, camera);

	
		fxpanel.setScene(scene);
		
		if( DashboardPlotArea.getContentPanelList().get(0).getID()==0) {
			Data2DPlot plotElement = (Data2DPlot) DashboardPlotArea.getContentPanelList().get(0);
			ChartPanel chartPanel = plotElement.getPlotElement().getChartPanel();
			VariableList2 varX = plotElement.getPlotElement().getVariableListX();
			//VariableList2 varY = plotElement.getPlotElement().getVariableListY();
			
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
						        	            Quaternion q = DashboardPlotArea.getResultSet().get(indx).getQuaternion();
						        	            AttitudeView.setQuatTemp(q);
					                    }
					                });
					            }
				        	}
			        }
			});
		
		}	

	}
	public void modelRotation(Quaternion q) {
		int version =3;
			if(version==1) {
				Quaternion qInverse = quatTemp;
				qInverse.conjugate();
				matrixRotateNode(qInverse);

				matrixRotateNode(q);
				quatTemp = q;	
			} else if (version ==2) {
				Quaternion qInverse = quatTemp;
				qInverse.conjugate();
				
				matrixRotateNode(qInverse);
				Quaternion qRot = q;
				qRot.conjugate();
				matrixRotateNode(qRot);
				quatTemp = qRot;
			} else if (version ==3) {
				Quaternion qInverse = quatTemp;
				qInverse.inverse();
				Vector4 vec = matrixRotateNode(qInverse,0);
				Point3D point = new Point3D(vec.x, vec.y, vec.z);
	            model.setRotationAxis(point);
	            model.setRotate(vec.w);   

	            vec = matrixRotateNode(q,0);
	            if (vec != new Vector4(0,0,0,0)) {
		            model.setRotationAxis(point);
		            model.setRotate(vec.w); 
		            
					quatTemp = q;
	            }
			}
	}
	
    public void matrixRotateNode(Quaternion q){
    	
    	double a=q.w;
    	double b=q.x;
    	double c=q.y;
    	double d=q.z;

        // double A11=Math.cos(alf)*Math.cos(gam);
        double A11=a*a+b*b-c*c-d*d;
        // double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A12=2*(b*c-a*d);
        // double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A13=2*(b*d+a*c);
        
        //double A21=-Math.cos(gam)*Math.sin(alf);
        double A21=2*(b*c+a*d);
        //double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A22=(a*a-b*b+c*c-d*d);
        //double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A23=2*(c*d-a*b);
        
       // double A31=Math.sin(gam);
        double A31=2*(b*d-a*c);
       // double A32=-Math.cos(gam)*Math.sin(bet);
        double A32=2*(c*d+a*b);
       // double A33=Math.cos(bet)*Math.cos(gam);
        double A33=(a*a-b*b-c*c+d*d);
         
        double dd = Math.acos((A11+A22+A33-1d)/2d);
        if(dd!=0d){
            double den=2d*Math.sin(dd);
            Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            model.setRotationAxis(p);
            model.setRotate(Math.toDegrees(dd));                   
        }
    }
    
    
    public Vector4 matrixRotateNode(Quaternion q, int indx){
    	
    	Vector4 result = new Vector4(0,0,0,0);
    	
    	double a=q.w;
    	double b=q.x;
    	double c=q.y;
    	double d=q.z;

        // double A11=Math.cos(alf)*Math.cos(gam);
        double A11=a*a+b*b-c*c-d*d;
        // double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A12=2*(b*c-a*d);
        // double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A13=2*(b*d+a*c);
        
        //double A21=-Math.cos(gam)*Math.sin(alf);
        double A21=2*(b*c+a*d);
        //double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A22=(a*a-b*b+c*c-d*d);
        //double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A23=2*(c*d-a*b);
        
       // double A31=Math.sin(gam);
        double A31=2*(b*d-a*c);
       // double A32=-Math.cos(gam)*Math.sin(bet);
        double A32=2*(c*d+a*b);
       // double A33=Math.cos(bet)*Math.cos(gam);
        double A33=(a*a-b*b-c*c+d*d);
         
        double dd = Math.acos((A11+A22+A33-1d)/2d);
        if(dd!=0d){
            double den=2d*Math.sin(dd);   
            result.w = Math.toDegrees(dd);
            result.x = (A32-A23)/den;
            result.y = (A13-A31);
            result.z = (A21-A12)/den;
            
        }
        return result;
    }
	
	
	
	private   void initMouseControl(Scene scene,JFXPanel fxpanel, Camera camera) {
		
		/**
		 * 
		 *  		Camera movement using smartgroup camera 
		 *  
		 *  
		 */
		

		rotateX.angleProperty().bind(angleCameraY);
		rotateY.angleProperty().bind(angleCameraX);
		
		scene.setOnMousePressed(event -> {
			holdValx = event.getSceneX();
			holdValy = event.getSceneY();
			anchorAngleCameraY = angleCameraY.get();
			anchorAngleCameraX = angleCameraX.get();
		});
		scene.setOnMouseDragged(event ->{
			//angleCameraX.set(anchorAngleCameraX - (anchorCameraX - event.getSceneX())*mouseSensitivity); 
			double cameraAngleX = anchorAngleCameraY + (holdValy - event.getSceneY())*mouseSensitivity ; 
			angleCameraY.set(cameraAngleX);
			double cameraAngleY = anchorAngleCameraX - (holdValx - event.getSceneX())*mouseSensitivity ;
			angleCameraX.set(cameraAngleY);
			//setCameraRotationX(camera, cameraAngleX);
			
			//System.out.println(cameraAngleX+"|"+cameraAngleY);
			
			//camera.translateZProperty().set(cameraToTargetDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
			//camera.translateYProperty().set(cameraToTargetDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
			double CY = - cameraToTargetDistance*Math.sin(Math.toRadians(-cameraAngleX)) ;
			double CZ = - cameraToTargetDistance*(Math.cos(Math.toRadians(-cameraAngleX)));
			double CX =  CZ / Math.sin(Math.toRadians(-cameraAngleY));
			
			camera.translateZProperty().set(CZ);
			camera.translateYProperty().set(CY);
			camera.translateXProperty().set(CX);
			//System.out.println(cameraToTargetDistance+"|"+CX+"|"+CY+"|"+CZ);
			
		});
		
		if(isZoom) {
			fxpanel.addMouseWheelListener(new MouseWheelListener() {
	
				@Override
				public void mouseWheelMoved(MouseWheelEvent arg0) {
					// TODO Auto-generated method stub
						double wheelSpeed = arg0.getPreciseWheelRotation();
							if(wheelSpeed>0) {
								cameraToTargetDistance += mouseWheelZoomSensitivity;
							} else {
								if(cameraToTargetDistance>0.5) {
									cameraToTargetDistance -= mouseWheelZoomSensitivity;
								}
							}
							//group.translateZProperty().set(cameraToTargetDistance);
							//camera.translateZProperty().set(cameraToTargetDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
							//camera.translateYProperty().set(cameraToTargetDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
				double CY = - cameraToTargetDistance*Math.sin(Math.toRadians(-angleCameraY.get()));
				double CZ = - cameraToTargetDistance*(Math.cos(Math.toRadians(-angleCameraY.get())));
				double CX = CZ / Math.sin(Math.toRadians(-angleCameraX.get()));
				camera.translateZProperty().set(CZ);
				camera.translateYProperty().set(CY);
				camera.translateXProperty().set(CX);
				System.out.println(cameraToTargetDistance+"|"+CX+"|"+CY+"|"+CZ);
				}
				
			});
		}
	}
	
	public   void setCameraRotationX(SmartGroup camera, double angleX) {
		camera.rotateByX(angleX);
	}

private   SmartGroup loadModel(String fileString) {
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

modelRoot.setScaleX(modelScale);
modelRoot.setScaleY(modelScale);
modelRoot.setScaleZ(modelScale);
    return modelRoot;
}


public   class SmartGroup extends Group {
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
}

public static   Group createGrid(float size, float delta) {
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

private static   PolygonMesh createQuadrilateralMesh(float width, float height, int subDivX, int subDivY) {
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


@SuppressWarnings("unused")
private   Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.GRAY);

	return ambientLight;
}


	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
