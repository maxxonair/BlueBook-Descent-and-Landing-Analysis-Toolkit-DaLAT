
package VisualEngine.engineTester;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.entities.Camera;
import VisualEngine.entities.Entity;
import VisualEngine.entities.Light;
import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.OBJLoader;
import VisualEngine.renderEngine.Renderer;
import VisualEngine.shaders.StaticShader;
import VisualEngine.textures.ModelTexture;


public class CanvasTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static Canvas canvas;
    static Canvas glCanvas;
	
	static private Loader loader;
	static private StaticShader shader ;
	static private Renderer renderer;
	static private RawModel model ;	
	static private TexturedModel staticModel ;	
	static private Entity entity ;	
	static private Light light ;	
	static private Camera camera ;
	private static final int FPS_CAP = 120;

	public static void main(String[] args) throws LWJGLException, InterruptedException  {
		//System.out.println("LWJGL Version: "+Sys.getVersion());
		
		JFrame frame = new JFrame();
		frame.setSize(640,480);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(400, 400));
        panel.setVisible(true);
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Embedded swing method mark1 ");

		 
	            glCanvas = new Canvas();
	            glCanvas.setSize(new Dimension(400,400));
	            glCanvas.setFocusable(true);
	            glCanvas.requestFocus();
	            glCanvas.setIgnoreRepaint(true);
	            glCanvas.setVisible(true);
	            
	            Display.setParent(glCanvas);	
	            panel.add(glCanvas);
	            glCanvas.addNotify();
		        //glCanvas.makeCurrent();
	         //------------------------------------------------------------------------------------------  
	         //					Create Display 
	         //------------------------------------------------------------------------------------------
	   	     ContextAttribs attribs = new ContextAttribs(3,2)
						.withForwardCompatible(true)
						.withProfileCore(true);
			 Display.setDisplayMode(new DisplayMode(400,400));
			 Display.create(new PixelFormat(), attribs);
			//------------------------------------------------------------------------------------------
	        //------------------------------------------------------------------------------------------

		 loader = new Loader();
		 shader = new StaticShader();
		 renderer = new Renderer(shader);			
		 model = OBJLoader.loadObjModel("lem", loader);		
		 staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));		
		 entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);		
		 light = new Light(new Vector3f(10,10, -20), new Vector3f(1,1,1));
		 camera = new Camera();	

		while(frame!=null){
			//entity.increaseRotation(0, 0.05f, 0);
			entity.move();
			camera.move();
			renderer.adjust_brightness();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity,shader);
			shader.stop();
	         //------------------------------------------------------------------------------------------  
	         //					Update Display 
	         //------------------------------------------------------------------------------------------
			Display.sync(FPS_CAP);
			Display.update();
			//------------------------------------------------------------------------------------------
			//TimeUnit.MILLISECONDS.sleep(200);
			//k++;
		}
System.out.println("Display end");
		shader.cleanUp();
		loader.cleanUp();
        //------------------------------------------------------------------------------------------  
        //					Close Display 
        //------------------------------------------------------------------------------------------
		Display.destroy();
		//------------------------------------------------------------------------------------------
	}
	
}