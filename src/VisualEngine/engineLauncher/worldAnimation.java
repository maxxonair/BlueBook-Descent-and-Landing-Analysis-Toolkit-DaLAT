package VisualEngine.engineLauncher;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.animation.AnimationSet;
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

public class worldAnimation {
	
	static final boolean isOverlayDisplay=true;
	static final boolean isAnimate=false;
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<Spacecraft> spaceElements = new ArrayList<Spacecraft>();

	static Loader loader = new Loader();
	static int movableEntity =0;
	
	static float shine_value = 1;
	static float reflectivity_value =0;
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public static void launchVisualEngine(List<AnimationSet> animationSets) {
		DisplayManager.createDisplay();
	    loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		MasterRenderer.setEnvColor(0, 0, 0);
        //-----------------------------------------------------------------
        TextMaster.init(loader);
        String fontType = "arial";
        FontType font = new FontType(loader.loadFontTexture(fontType), new File("VisualEngine/fonts/"+fontType+".fnt"));
        List<GUIText> guiTexts = new ArrayList<GUIText>();
        float yGap = 0.03f;
        int numberGuiTexts=15;
        for(int i=0;i<numberGuiTexts;i++) {
            GUIText guiText = new GUIText("", 1f, font, new Vector2f(0f, 0f+i*yGap), 1f, false);
            guiText.setColour(0.5f, 0.5f, 0.5f);
            guiTexts.add(guiText);
        }
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("grass"));		
		TerrainTexture rTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));  
		TerrainTexture gTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTerrainTexture("moonSurface3"));		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));
		Terrain terrain = new Terrain(0,0, loader, texturePack, blendMap, "heightmap2", 1400f, 30f);
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
		Vector3f startPostion = null;
		if(isAnimate) {
		 startPostion = new Vector3f(0,animationSets.get(0).getAlt_init(),0);
		} else {
		 startPostion = new Vector3f(0,150,0);
		}
		Spacecraft spacecraft = new Spacecraft(staticModel, startPostion,0,45,0,1);	
		spaceElements.add(spacecraft);
		//----------------------------------------------------------------
		// 					  Light Setting
		//----------------------------------------------------------------
		Vector3f lightPostion = new Vector3f(0,200,0);
		Light light = new Light(lightPostion, new Vector3f(1,1,1));		
		//----------------------------------------------------------------
		//					Camera Settings
		//----------------------------------------------------------------
		//Camera camera = new Camera(new Vector3f(400,15,400),180,15,0);
		ThirdPersonCamera camera = new ThirdPersonCamera(spacecraft);
		//----------------------------------------------------------------
		//					Logo Settings
		//----------------------------------------------------------------
		List<GuiTexture> Logos = new ArrayList<GuiTexture>();
		GuiTexture logo = new GuiTexture(loader.loadTerrainTexture("space"), 
				new Vector2f(0.8f, 0.7f), new Vector2f(0.2f, 0.3f));
		Logos.add(logo);
		
		GuiRenderer guirenderer = new GuiRenderer(loader);
	//-------------------------------------------------------------------------------------------------------------
	//						Visual Engine Loop
	//-------------------------------------------------------------------------------------------------------------
		float animationTime =0;
		int j=0;
		while(!Display.isCloseRequested()){

			//renderer.adjustBrightness();
			renderer.processTerrain(terrain);
		    animationTime += DisplayManager.getFrameTimeSeconds();
		    //System.out.println(animationSets.get(j).getTime());
		    if(j<animationSets.size()) {
			if(animationTime > animationSets.get(j).getTime() ) {j++;}}
		    if(isAnimate) {
					if(animationSets != null && j<(animationSets.size()-1) && isAnimate) {
						AnimationSet animationSet = animationSets.get(j);
						spacecraft.animate(terrain, animationSet);
					}
			} else {
					spacecraft.fly(terrain);
			}
			camera.move();	// Do not touch!!!
			renderer.processEntity(spacecraft);
			renderer.render(light, camera);
			guirenderer.render(Logos);
			//----------------------------------------------------------------------------------------------
			if(isOverlayDisplay) {
		    guiTexts.get(0).updateTextString("Vel H: "+df2.format(spacecraft.getCurrentHorizontalSpeed()));
			guiTexts.get(1).updateTextString("Vel V: "+df2.format(spacecraft.getCurrentVerticalSpeed()));
			if(spacecraft.getCurrentVerticalSpeed()<-10) {
				guiTexts.get(1).setColour(0.8f, 0.5f, 0.5f);
			} else {
				guiTexts.get(1).setColour(0.5f, 0.8f, 0.5f);
			}
			guiTexts.get(2).updateTextString("Vel T: "+df2.format(spacecraft.getCurrentSpeed()));
			guiTexts.get(4).updateTextString("Pos X: "+df2.format(spacecraft.getPosition().x));
			guiTexts.get(5).updateTextString("Pos Y: "+df2.format(spacecraft.getPosition().y));
			if(spacecraft.getPosition().y<30) {
				guiTexts.get(5).setColour(0.8f, 0.5f, 0.5f);
			} else {
				guiTexts.get(5).setColour(0.5f, 0.8f, 0.5f);
			}
			guiTexts.get(6).updateTextString("Pos Z: "+df2.format(spacecraft.getPosition().z));
			guiTexts.get(8).updateTextString("Time : "+df2.format(animationTime));
			if(animationSets != null && j<(animationSets.size()-1)) {
				guiTexts.get(10).updateTextString("Roll :"+df2.format(spacecraft.getRotZ()));
			}
			guiTexts.get(11).updateTextString("Propellant: "+df2.format(Spacecraft.getSCPropMass()));
			if(spacecraft.getisThrust()) {
				guiTexts.get(11).setColour(1.0f, 0.647f, 0.5f);
			} else {
				guiTexts.get(11).setColour(0.5f, 0.5f, 0.5f);
			}
			//----------------------------------------------------------------------------------------------
			TextMaster.render();
			}
			DisplayManager.updateDisplay(); 
		}
		for(int i=0;i<numberGuiTexts;i++) {
			guiTexts.get(i).cleanUp();
		}
		TextMaster.cleanUp();
		
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
		worldAnimation.shine_value = shine_value;
	}

	public static float getReflectivity_value() {
		return reflectivity_value;
	}

	public static void setReflectivity_value(float reflectivity_value) {
		worldAnimation.reflectivity_value = reflectivity_value;
	}
}
