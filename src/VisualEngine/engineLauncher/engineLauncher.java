package VisualEngine.engineLauncher;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.entities.Camera;
import VisualEngine.entities.Entity;
import VisualEngine.entities.Light;
import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.OBJLoader;
import VisualEngine.renderEngine.Renderer;
import VisualEngine.shaders.StaticShader;
import VisualEngine.textures.ModelTexture;

public class engineLauncher {
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<Entity> entities = new ArrayList<Entity>();

	static Loader loader = new Loader();
	static int distance =0;
	
	public static void launchVisualEngine() {
		DisplayManager.createDisplay();
	    loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
			
		RawModel model = OBJLoader.loadObjModel("gemini", loader);	
		rawmodels.add(model);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));	
		texturedmodels.add(staticModel);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);	
		entities.add(entity);
		
		Light light = new Light(new Vector3f(10,10, -20), new Vector3f(1,1,1));		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 0.05f, 0);
			entity.move();
			camera.move();
			renderer.adjust_brightness();
			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			for(int i=0;i<entities.size();i++){
				renderer.render(entities.get(i),shader);
			}
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public static void addEntity() {
		Entity entity = null;
			 RawModel model = OBJLoader.loadObjModel("gemini", loader);	
			 rawmodels.add(model);
			 TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));	
			 texturedmodels.add(staticModel);
			 entity = new Entity(staticModel, new Vector3f(0+distance,0,-50),0,0,0,1);
			 distance+=5;
			 entities.add(entity);
	}
}
