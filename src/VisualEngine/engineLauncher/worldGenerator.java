package VisualEngine.engineLauncher;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.entities.Camera;
import VisualEngine.entities.Entity;
import VisualEngine.entities.Light;
import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.MasterRenderer;
import VisualEngine.renderEngine.OBJLoader;
import VisualEngine.shaders.StaticShader;
import VisualEngine.terrains.Terrain;
import VisualEngine.textures.ModelTexture;

public class worldGenerator {
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<Entity> entities = new ArrayList<Entity>();

	static Loader loader = new Loader();
	static int movableEntity =0;
	
	static float shine_value =10;
	static float reflectivity_value =1;
	
	public static void launchVisualEngine() {
		DisplayManager.createDisplay();
	    loader = new Loader();
		StaticShader shader = new StaticShader();
		MasterRenderer renderer = new MasterRenderer();
		
		Terrain terrain = new Terrain(0,0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		RawModel model = OBJLoader.loadObjModel("gemini", loader);	
		rawmodels.add(model);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));	
		texturedmodels.add(staticModel);	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(shine_value);
		texture.setReflectivity(reflectivity_value);
		Entity entity = new Entity(staticModel, new Vector3f(100,5,180),0,1,0,1);	
		entities.add(entity);
		
		Light light = new Light(new Vector3f(10,10, -20), new Vector3f(1,1,1));		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 0.05f, 0);
			entity.move();
			if(entities.size()>movableEntity) {
			//entities.get(0).move();
			}
			camera.move();
			renderer.adjust_brightness();

			renderer.processTerrain(terrain);
			for(int i=0;i<entities.size();i++){
				renderer.processEntity(entities.get(i));
			}
			renderer.render(light, camera);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public static void addEntity(String fileName, String textureFile) {
		Entity entity = null;
			Loader loader = new Loader();
			 RawModel model = OBJLoader.loadObjModel("gemini", loader);	
			 rawmodels.add(model);
			 TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("gray")));	
			 texturedmodels.add(staticModel);
			 entity = new Entity(staticModel, new Vector3f(0,0,-50),0,0,0,1);
			 entities.add(entity);
	}
	
	public static void setmovableEntity(int value) {
		movableEntity = value;
	}
	public static int getmoableEntity() {
		return movableEntity;
	}

	public static float getShine_value() {
		return shine_value;
	}

	public static void setShine_value(float shine_value) {
		worldGenerator.shine_value = shine_value;
	}

	public static float getReflectivity_value() {
		return reflectivity_value;
	}

	public static void setReflectivity_value(float reflectivity_value) {
		worldGenerator.reflectivity_value = reflectivity_value;
	}
}
