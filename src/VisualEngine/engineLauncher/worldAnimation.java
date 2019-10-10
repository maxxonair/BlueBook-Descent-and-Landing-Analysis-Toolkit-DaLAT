package VisualEngine.engineLauncher;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import FlightElement.SpaceShip;
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
    static List<GuiTexture> HUD = new ArrayList<GuiTexture>();

	static Loader loader = new Loader();
	static MasterRenderer renderer;
	static Terrain terrain11;
	static Terrain terrain12;
	static Spacecraft spacecraft;
	static Light light;
	static ThirdPersonCamera camera;
	static GuiRenderer guirenderer;
	
	static float animationTime =0;
	
	static int movableEntity =0;
	
	static float shine_value = 1;
	static float reflectivity_value =0;
	
    static double[][] InertiaTensorMatrix   =         {{   27364    ,    0       ,   0},
													   {      0    ,    27314    ,   0},
													   {      0    ,    0       ,   26114}};
    private static double[][] MRCS = {{1900},
			 						  {1900},
			 						  {1900}};
    
	private static double[][] quarternions = {	{-0.2164396},
			{0},
			{0},
			{0.976296}}; 
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public enum State {
		FLY, PAUSED;
	}
	
	private static State state = State.FLY;
	
	@SuppressWarnings("static-access")
	public static void launchVisualEngine(List<AnimationSet> animationSets) {
		DisplayManager.createDisplay();
	    loader = new Loader();
		 renderer = new MasterRenderer(loader);
		MasterRenderer.setEnvColor(0, 0, 0);
        //-----------------------------------------------------------------
        TextMaster.init(loader);
        String fontType = "arial";
        FontType font = new FontType(loader.loadFontTexture(fontType), new File("VisualEngine/fonts/"+fontType+".fnt"));
        List<GUIText> guiTextsLeft = new ArrayList<GUIText>();
        List<GUIText> guiTextsRight = new ArrayList<GUIText>();
        float yGap = 0.03f;
        int numberGuiTexts=17;
        for(int i=0;i<numberGuiTexts;i++) {
            GUIText guiText = new GUIText("", 1f, font, new Vector2f(0f, 0f+i*yGap), 1f, false);
            guiText.setColour(0.9f, 0.9f, 0.9f);
            guiTextsLeft.add(guiText);
        }
        for(int i=0;i<numberGuiTexts;i++) {
            GUIText guiText = new GUIText("", 1f, font, new Vector2f(0.85f, 0.1f+i*yGap-0.24f), 1f, false);
            guiText.setColour(0.9f, 0.9f, 0.9f);
            guiTextsRight.add(guiText);
        }
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("greySand"));			
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, backgroundTexture, backgroundTexture, backgroundTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));
		float tileSize = 2765f; 
		float terrainHeight = 50f;
		 terrain11 = new Terrain(0,0,0,0, loader, texturePack, blendMap, "moon/tile11", tileSize, terrainHeight);
		 terrain12 = new Terrain(0,tileSize,0,0, loader, texturePack, blendMap, "moon/tile12", tileSize, terrainHeight);
		//----------------------------------------------------------------
		// 					Spacecraft Setting
		//----------------------------------------------------------------
		ModelData data = OBJFileLoader.loadOBJ("lem2");
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());	
		rawmodels.add(model);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadObjectTexture("orange")));	
		texturedmodels.add(staticModel);	
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(shine_value);
		texture.setReflectivity(reflectivity_value);
		Vector3f startPostion = null;
		if(isAnimate) {
		 startPostion = new Vector3f(0,animationSets.get(0).getAlt_init(),0);
		} else {
		 startPostion = new Vector3f(tileSize/2,200,0);
		}
		SpaceShip spaceShip = new SpaceShip();
		spaceShip.setMass(10000);
		spaceShip.getPropulsion().setPrimaryPropellant(340);
		spaceShip.setInertiaTensorMatrix(InertiaTensorMatrix);
		spaceShip.getPropulsion().setPrimaryISPMax(311);
		spaceShip.getPropulsion().setPrimaryThrustMax(45000);
		spaceShip.setInitialQuarterions(quarternions);
	    spacecraft = new Spacecraft(spaceShip,staticModel, startPostion,0,0,0,1);	
		spacecraft.setQuarternions(quarternions);
		spaceElements.add(spacecraft);
		//spacecraft.setRotX(-25);
		//----------------------------------------------------------------
		// 					  Light Setting
		//----------------------------------------------------------------
		Vector3f lightPostion = new Vector3f(tileSize/2,650,-400);
		 light = new Light(lightPostion, new Vector3f(1,1,1));		
		//----------------------------------------------------------------
		//					Camera Settings
		//----------------------------------------------------------------
		//Camera camera = new Camera(new Vector3f(400,15,400),180,15,0);
		 camera = new ThirdPersonCamera(spacecraft);
		//----------------------------------------------------------------
		//					Logo Settings
		//----------------------------------------------------------------
		List<GuiTexture> Logos = new ArrayList<GuiTexture>();
		GuiTexture logo = new GuiTexture(loader.loadTerrainTexture("spaceLogo2"), 
				new Vector2f(0.86f, -0.86f), new Vector2f(0.16f, 0.45f));
		Logos.add(logo);
		GuiTexture BackgroundFading = new GuiTexture(loader.loadTerrainTexture("FadeBackground"), 
				new Vector2f(0.82f, 0.8f), new Vector2f(0.5f, 0.5f));
		Logos.add(BackgroundFading);
		addHudElements();
		 guirenderer = new GuiRenderer(loader);
	//-------------------------------------------------------------------------------------------------------------
	//						Visual Engine Loop
	//-------------------------------------------------------------------------------------------------------------
		 animationTime =0;
		int j=0;
		//System.out.println(Spacecraft.getQuarternions()[0][0]);
     //-----------------------------------------------------------
		while( !Display.isCloseRequested() ){
			checkInput();
			System.out.println(state);
			switch (state) {
			case FLY:
				checkInput();
				renderer.processTerrain(terrain11);
				renderer.processTerrain(terrain12);
			    animationTime += DisplayManager.getFrameTimeSeconds();
			    //System.out.println(animationSets.get(j).getTime());
			    if(j<animationSets.size()) {
				if(animationTime > animationSets.get(j).getTime() ) {j++;}}
			    if(isAnimate) {
						if(animationSets != null && j<(animationSets.size()-1) && isAnimate) {
							AnimationSet animationSet = animationSets.get(j);
							spacecraft.animate(terrain11, animationSet);
						}
				} else {
					if(spacecraft.getPosition().x<tileSize) {
						spacecraft.fly(terrain11);
					} else {
						spacecraft.fly(terrain12);
					}
				}
				camera.move();	// Do not touch!!!
				renderer.processEntity(spacecraft);
				renderer.render(light, camera);
				guirenderer.render(Logos);
				guirenderer.render(HUD);
				//----------------------------------------------------------------------------------------------
				// 			Overlay Display
				//----------------------------------------------------------------------------------------------
				if(isOverlayDisplay) {
					updateOverlayDisplayTextLeft(guiTextsLeft, spacecraft, animationTime);
					updateOverlayDisplayTextRight(guiTextsRight, spacecraft);
				TextMaster.render();
				//----------------------------------------------------------------------------------------------
				}
				DisplayManager.updateDisplay(); 
				break;
			case PAUSED:
				checkInput();
				if(isOverlayDisplay) {
					updateOverlayDisplayTextLeft(guiTextsLeft, spacecraft, animationTime);
					updateOverlayDisplayTextRight(guiTextsRight, spacecraft);
				TextMaster.render();
				}
				break;
			}
		}
		//---------------------------------------------------------------------------------------------------
		//				Clean up 
		//---------------------------------------------------------------------------------------------------
		for(int i=0;i<guiTextsLeft.size();i++) {
			guiTextsLeft.get(i).cleanUp();
		}
		for(int i=0;i<guiTextsLeft.size();i++) {
			guiTextsRight.get(i).cleanUp();
		}
		TextMaster.cleanUp();		
		guirenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public static void render() {

	}
	
private static void checkInput() {
	switch(state) {
	case PAUSED:
		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
				state = State.FLY;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			TextMaster.cleanUp();		
			loader.cleanUp();
			DisplayManager.closeDisplay();
		}
	case FLY:
		if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
			state = State.PAUSED;
		}
	}
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
	public static void addHudElements() {
		GuiTexture HudBoxLeft = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_box"), 
				new Vector2f(-0.8f, 0.0f), new Vector2f(0.08f, 0.12f));
		HUD.add(HudBoxLeft);
		GuiTexture HudBoxRight = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_box"), 
				new Vector2f(0.7f, 0.0f), new Vector2f(0.08f, 0.12f));
		HUD.add(HudBoxRight);
		GuiTexture HudBarLeft = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_barLeft"), 
				new Vector2f(-0.76f, -0.15f), new Vector2f(0.12f, 0.85f));
		HUD.add(HudBarLeft);
		GuiTexture HudBarRight = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_barRight"), 
				new Vector2f(0.67f, -0.15f), new Vector2f(0.12f, 0.85f));
		HUD.add(HudBarRight);
		GuiTexture Hud_HorizonBar = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_horizonBar"), 
				new Vector2f(0.33f, -0.0f), new Vector2f(0.7f, 0.2f));
		HUD.add(Hud_HorizonBar);
	}
	
	public static void updateHudElements() {
		
	}
	
	@SuppressWarnings("static-access")
	public static void updateOverlayDisplayTextRight(List<GUIText> guiTextsRight, Spacecraft spacecraft) {
		guiTextsRight.get(5).updateTextString("Roll [deg]:"+df2.format(spacecraft.getRotX()));
		guiTextsRight.get(5).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(6).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(7).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(6).updateTextString("Pitch [deg]:"+df2.format(spacecraft.getRotZ()));
		guiTextsRight.get(7).updateTextString("Yaw [deg]:"+df2.format(spacecraft.getRotY()));	
		
		double rotTreshold = 3;
		guiTextsRight.get(9).updateTextString("Pitch Rate [deg/s]:" +df2.format(Math.toDegrees(-spacecraft.getPQR()[0][0])));
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[0][0]))>rotTreshold) {
			guiTextsRight.get(9).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(9).setColour(0.2f, 0.2f, 1f);
		}
		guiTextsRight.get(10).updateTextString("Roll Rate [deg/s]:"+df2.format(Math.toDegrees(-spacecraft.getPQR()[1][0])));
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[1][0]))>rotTreshold) {
			guiTextsRight.get(10).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(10).setColour(0.2f, 0.2f, 1f);
		}
		guiTextsRight.get(11).updateTextString("Yaw Rate [deg/s]:"  +df2.format(Math.toDegrees(-spacecraft.getPQR()[2][0])));	
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[2][0]))>rotTreshold) {
			guiTextsRight.get(11).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(11).setColour(0.2f, 0.2f, 1f);
		}
		
		//guiTextsRight.get(13).updateTextString("Thrust X [N]:"  +df2.format(spacecraft.getThrust_NED()[0][0]));	
		//guiTextsRight.get(14).updateTextString("Thrust Y [N]:"  +df2.format(spacecraft.getThrust_NED()[2][0]));	
		//guiTextsRight.get(15).updateTextString("Thrust Z [N]:"  +df2.format(spacecraft.getThrust_NED()[1][0]));	
	}
	
	@SuppressWarnings("static-access")
	public static void updateOverlayDisplayTextLeft(List<GUIText> guiTextsLeft, Spacecraft spacecraft, float animationTime) {
	    guiTextsLeft.get(0).updateTextString("Vel H [m/s]: "+df2.format(spacecraft.getCurrentHorizontalSpeed()));
		guiTextsLeft.get(1).updateTextString("Vel V [m/s]: "+df2.format(spacecraft.getCurrentVerticalSpeed()));
		if(spacecraft.getCurrentVerticalSpeed()<-10) {
			guiTextsLeft.get(1).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsLeft.get(1).setColour(0.9f, 0.9f, 0.9f);
		}
		guiTextsLeft.get(2).updateTextString("Vel T [m/s]: "+df2.format(spacecraft.getCurrentSpeed()));
		guiTextsLeft.get(4).updateTextString("Pos X [m]: "+df2.format(spacecraft.getPosition().x));
		guiTextsLeft.get(5).updateTextString("Pos Y [m]: "+df2.format(spacecraft.getPosition().y));
		if(spacecraft.getPosition().y<30) {
			guiTextsLeft.get(5).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsLeft.get(5).setColour(0.9f, 0.9f, 0.9f);
		}
		guiTextsLeft.get(6).updateTextString("Pos Z [m]: "+df2.format(spacecraft.getPosition().z));
		guiTextsLeft.get(8).updateTextString("Time [s]: "+df2.format(animationTime));
		guiTextsLeft.get(8).setColour(0.5f, 0.5f, 0.8f);
		guiTextsLeft.get(10).updateTextString("Propellant [kg]: "+df2.format(spacecraft.getSCPropMass()));
		if(spacecraft.getisThrust()) {
			guiTextsLeft.get(10).setColour(1.0f, 0.647f, 0.5f);
		} else {
			guiTextsLeft.get(10).setColour(0.9f, 0.9f, 0.9f);
		}
	}
}
