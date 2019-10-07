package GUI.FxElements;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class robotTest extends Application {
private final int width = 800;
private final int height = 500;
private XGroup torsoGroup;
private final double torsoX = 50;
private final double torsoY = 80;

public Parent createRobot() {
    Box torso = new Box(torsoX, torsoY, 20);
    torso.setMaterial(new PhongMaterial(Color.RED));
    Box head = new Box(20, 20, 20);
    head.setMaterial(new PhongMaterial(Color.YELLOW.darker()));
    head.setTranslateY(-torsoY / 2 -10);

    Box x = new Box(200, 2, 2);
    x.setMaterial(new PhongMaterial(Color.BLUE));
    Box y = new Box(2, 200, 2);
    y.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
    Box z = new Box(2, 2, 200);
    z.setMaterial(new PhongMaterial(Color.BURLYWOOD));

    torsoGroup = new XGroup();
    torsoGroup.getChildren().addAll(torso, head, x, y, z);
    return torsoGroup;
}

public Parent createUI() {
    HBox buttonBox = new HBox();

    Button b;
    buttonBox.getChildren().add(b = new Button("Exit"));
    b.setOnAction( (ActionEvent arg0) -> { Platform.exit(); } );

    buttonBox.getChildren().add(b = new Button("pitch up"));
    b.setOnAction(new TurnAction(torsoGroup.rx, -15) );

    buttonBox.getChildren().add(b = new Button("pitch down"));
    b.setOnAction(new TurnAction(torsoGroup.rx, 15) );

    buttonBox.getChildren().add(b = new Button("Yaw left"));
    b.setOnAction(new TurnAction(torsoGroup.ry, -15) );

    buttonBox.getChildren().add(b = new Button("Yaw right"));
    b.setOnAction(new TurnAction(torsoGroup.ry, 15) );

    buttonBox.getChildren().add(b = new Button("Roll right"));
    b.setOnAction(new TurnAction(torsoGroup.rz, -15) );

    buttonBox.getChildren().add(b = new Button("Roll left"));
    b.setOnAction(new TurnAction(torsoGroup.rz, 15) );

    return buttonBox;
}

class TurnAction implements EventHandler<ActionEvent> {
    final Rotate rotate;
    double deltaAngle;

    public TurnAction(Rotate rotate, double targetAngle) {
        this.rotate = rotate;
        this.deltaAngle = targetAngle;
    }

    @Override
    public void handle(ActionEvent arg0) {
        addRotate(torsoGroup, rotate, deltaAngle);
    } 
}

private void addRotate(XGroup node, Rotate rotate, double angle) {
    Affine affine = node.getTransforms().isEmpty() ? new Affine() : new Affine(node.getTransforms().get(0));
    double A11 = affine.getMxx(), A12 = affine.getMxy(), A13 = affine.getMxz(); 
    double A21 = affine.getMyx(), A22 = affine.getMyy(), A23 = affine.getMyz(); 
    double A31 = affine.getMzx(), A32 = affine.getMzy(), A33 = affine.getMzz(); 

    Rotate newRotateX = new Rotate(angle, new Point3D(A11, A21, A31));
    Rotate newRotateY = new Rotate(angle, new Point3D(A12, A22, A32));
    Rotate newRotateZ = new Rotate(angle, new Point3D(A13, A23, A33));

    affine.prepend(rotate.getAxis() == Rotate.X_AXIS ? newRotateX : 
            rotate.getAxis() == Rotate.Y_AXIS ? newRotateY : newRotateZ);

    node.getTransforms().setAll(affine);
}

public class XGroup extends Group {
    public Rotate rx = new Rotate(0, Rotate.X_AXIS);
    public Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    public Rotate rz = new Rotate(0, Rotate.Z_AXIS);
}

@Override 
public void start(Stage stage) throws Exception {
    Parent robot = createRobot();
    SubScene subScene = new SubScene(robot, width, height, true, SceneAntialiasing.BALANCED);
    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setNearClip(0.01);
    camera.setFarClip(100000);
    camera.setTranslateZ(-400);
    subScene.setCamera(camera);

    Parent ui = createUI();
    StackPane combined = new StackPane(ui, subScene);
    combined.setStyle("-fx-background-color: linear-gradient(to bottom, cornsilk, midnightblue);");

    Scene scene = new Scene(combined, width, height);
    stage.setScene(scene);
    stage.show();
}

public static void main(String[] args) {
    launch(args);
}
}
