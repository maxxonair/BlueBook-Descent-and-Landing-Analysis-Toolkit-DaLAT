package VisualEngine.engineLauncher;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.entities.Entity;
import VisualEngine.entities.Light;
import VisualEngine.entities.Spacecraft;
import VisualEngine.entities.ThirdPersonCamera;
import VisualEngine.gui.GuiRenderer;
import VisualEngine.gui.GuiTexture;
import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;
import VisualEngine.objConverter.ModelData;
import VisualEngine.objConverter.OBJFileLoader;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.MasterRenderer;
import VisualEngine.shaders.StaticShader;
import VisualEngine.terrains.Terrain;
import VisualEngine.textures.ModelTexture;
import VisualEngine.textures.TerrainTexture;
import VisualEngine.textures.TerrainTexturePack;

public class worldGenerator {
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<Entity> entities = new ArrayList<Entity>();

	static Loader loader = new Loader();
	static int movableEntity =0;
	
	static float shine_value = 1;
	static float reflectivity_value =0;
	
	public static void launchVisualEngine() {
		DisplayManager.createDisplay();
	    loader = new Loader();
		StaticShader shader = new StaticShader();
		MasterRenderer renderer = new MasterRenderer(loader);
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));
		
		TerrainTexture rTexture = new TerrainTexture(loader.loadTerrainTexture("grass"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTerrainTexture("mud"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTerrainTexture("path"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));

		Terrain terrain = new Terrain(0,0, loader, texturePack, blendMap, "heightmap");
		
		//renderer.adjustTerrainVisibility(0.07f, 1.5f);
		//----------------------------------------------------------------
		// 					Spacecraft Setting
		//----------------------------------------------------------------
		ModelData data = OBJFileLoader.loadOBJ("lem");
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());	
		rawmodels.add(model);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadObjectTexture("gray")));	
		texturedmodels.add(staticModel);	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(shine_value);
		texture.setReflectivity(reflectivity_value);
		Vector3f startPostion = new Vector3f(400,5,400);
		Spacecraft spacecraft = new Spacecraft(staticModel, startPostion,0,0,0,1);	
		entities.add(spacecraft);
		//------------
		
		//----------------------------------------------------------------
		// 					Light Setting
		//----------------------------------------------------------------
		Vector3f lightPostion = new Vector3f(400,80,200);
		Light light = new Light(lightPostion, new Vector3f(1,1,1));		
		//----------------------------------------------------------------
		//					Camera Settings
		//----------------------------------------------------------------
		//Camera camera = new Camera(new Vector3f(400,15,400),180,15,0);
		ThirdPersonCamera camera = new ThirdPersonCamera(spacecraft);
		//----------------------------------------------------------------
		//					GUI Settings
		//----------------------------------------------------------------
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTerrainTexture("nasa"), 
				new Vector2f(0.8f, 0.7f), new Vector2f(0.3f, 0.2f));
		guis.add(gui);
		
		GuiRenderer guirenderer = new GuiRenderer(loader);
		//----------------------------------------------------------------
		while(!Display.isCloseRequested()){
			spacecraft.move(terrain);
			if(entities.size()>movableEntity) {
			//entities.get(0).move();
			}
			camera.move();
			renderer.adjustBrightness();
			renderer.processTerrain(terrain);
			/*
			for(int i=0;i<entities.size();i++){
				renderer.processEntity(entities.get(i));
			}*/
			renderer.processEntity(spacecraft);
			renderer.render3P(light, camera);
			guirenderer.render(guis);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		guirenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public static void addEntity(String fileName, String textureFile) {

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
