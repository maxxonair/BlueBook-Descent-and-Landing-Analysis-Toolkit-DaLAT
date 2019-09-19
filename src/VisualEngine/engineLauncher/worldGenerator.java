package VisualEngine.engineLauncher;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


import VisualEngine.entities.Light;
import VisualEngine.entities.Spacecraft;
import VisualEngine.entities.ThirdPersonCamera;
import VisualEngine.fontMeshCreator.FontType;
import VisualEngine.fontMeshCreator.GUIText;
import VisualEngine.fontRendering.TextMaster;
import VisualEngine.gui.GuiRenderer;
import VisualEngine.gui.GuiTexture;
import VisualEngine.models.RawModel;
import VisualEngine.models.TexturedModel;
import VisualEngine.objConverter.ModelData;
import VisualEngine.objConverter.OBJFileLoader;
import VisualEngine.renderEngine.DisplayManager;
import VisualEngine.renderEngine.Loader;
import VisualEngine.renderEngine.MasterRenderer;
import VisualEngine.terrains.Terrain;
import VisualEngine.textures.ModelTexture;
import VisualEngine.textures.TerrainTexture;
import VisualEngine.textures.TerrainTexturePack;

public class worldGenerator {
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<Spacecraft> spaceElements = new ArrayList<Spacecraft>();

	static Loader loader = new Loader();
	static int movableEntity =0;
	
	static float shine_value = 2.1f;
	static float reflectivity_value =0.1f;
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public static void launchVisualEngine() {
		DisplayManager.createDisplay();
	    loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
        TextMaster.init(loader);
        String fontType = "arial";
        FontType font = new FontType(loader.loadFontTexture(fontType), new File("VisualEngine/fonts/"+fontType+".fnt"));
        GUIText speed_t = new GUIText("", 1f, font, new Vector2f(0f, 0f), 1f, false);
        speed_t.setColour(0.5f, 0.5f, 0.5f);
        GUIText speed_h = new GUIText("", 1f, font, new Vector2f(0f, 0.03f), 1f, false);
        speed_h.setColour(0.5f, 0.5f, 0.5f);
        GUIText speed_v = new GUIText("", 1f, font, new Vector2f(0f, 0.06f), 1f, false);
        speed_v.setColour(0.5f, 0.5f, 0.5f);
        GUIText pos_x = new GUIText("", 1f, font, new Vector2f(0f, 0.12f), 1f, false);
        pos_x.setColour(0.5f, 0.5f, 0.5f);
        GUIText pos_y = new GUIText("", 1f, font, new Vector2f(0f, 0.15f), 1f, false);
        pos_y.setColour(0.5f, 0.5f, 0.5f);
        GUIText pos_z = new GUIText("", 1f, font, new Vector2f(0f, 0.18f), 1f, false);
        pos_z.setColour(0.5f, 0.5f, 0.5f);
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));
		
		TerrainTexture rTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));

		Terrain terrain = new Terrain(0,0, loader, texturePack, blendMap, "heightmap",800f,40f);
		//----------------------------------------------------------------
		// 					Spacecraft Setting
		//----------------------------------------------------------------
		ModelData data = OBJFileLoader.loadOBJ("lem2");
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());	
		rawmodels.add(model);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadObjectTexture("gray")));	
		texturedmodels.add(staticModel);	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(shine_value);
		texture.setReflectivity(reflectivity_value);
		Vector3f startPostion = new Vector3f(400,5,400);
		Spacecraft spacecraft = new Spacecraft(staticModel, startPostion,0,0,0,1);	
		spaceElements.add(spacecraft);
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
		GuiTexture gui = new GuiTexture(loader.loadTerrainTexture("space"), 
				new Vector2f(0.8f, 0.7f), new Vector2f(0.2f, 0.3f));
		guis.add(gui);
		
		GuiRenderer guirenderer = new GuiRenderer(loader);
	//-------------------------------------------------------------------------------------------------------------
	//						Visual Engine Loop
	//-------------------------------------------------------------------------------------------------------------
		while(!Display.isCloseRequested()){
			camera.move();	// Do not touch!!!
			//renderer.adjustBrightness();
			renderer.processTerrain(terrain);
			for(Spacecraft sc:spaceElements) {
				sc.move(terrain);
				renderer.processEntity(sc);
			}
			//renderer.render3P(light, camera);
			guirenderer.render(guis);
			
			speed_h.updateTextString("Vel H: "+df2.format(spacecraft.getCurrentHorizontalSpeed()));
			speed_v.updateTextString("Vel V: "+df2.format(spacecraft.getCurrentVerticalSpeed()));
			speed_t.updateTextString("Vel T: "+df2.format(spacecraft.getCurrentSpeed()));
			pos_x.updateTextString("Pos X: "+df2.format(spacecraft.getPosition().x));
			pos_y.updateTextString("Pos Y: "+df2.format(spacecraft.getPosition().y));
			pos_z.updateTextString("Pos Z: "+df2.format(spacecraft.getPosition().z));
			
			TextMaster.render();
			DisplayManager.updateDisplay();
		}
		speed_h.cleanUp();
		speed_v.cleanUp();
		speed_t.cleanUp();
		pos_x.cleanUp();
		pos_y.cleanUp();
		pos_z.cleanUp();
		
		guirenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	//-------------------------------------------------------------------------------------------------------------
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
