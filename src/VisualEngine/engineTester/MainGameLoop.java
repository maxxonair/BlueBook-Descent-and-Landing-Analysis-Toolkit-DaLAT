package VisualEngine.engineTester;

import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.OBJLoader;
import VisualEngine.renderEngine.Renderer;
import VisualEngine.shaders.StaticShader;
import VisualEngine.textures.ModelTexture;
import VisualEngine.entities.Camera;
import VisualEngine.entities.Entity;
import VisualEngine.entities.Light;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
			
		RawModel model = OBJLoader.loadObjModel("gemini", loader);		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);		
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
			renderer.render(entity,shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
