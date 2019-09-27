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

public class worldFly {
	
	static final boolean isOverlayDisplay=true;
	static final boolean isHUD=true;
	static final boolean isAnimate=false;
	
    static List<RawModel> rawmodels = new ArrayList<RawModel>();
    static List<TexturedModel> texturedmodels = new ArrayList<TexturedModel>();
    static List<GuiTexture> HUD = new ArrayList<GuiTexture>();
    static List<GUIText> HudVerticalSpeed = new ArrayList<GUIText>();
    static List<GUIText> HudAltitude = new ArrayList<GUIText>();
	static List<GuiTexture> Logos = new ArrayList<GuiTexture>();
    static List<GUIText> guiTextsLeft = new ArrayList<GUIText>();
    static List<GUIText> guiTextsRight = new ArrayList<GUIText>();

	static Loader loader = new Loader();
	static MasterRenderer renderer;
	static Terrain terrain11;
	static Terrain terrain12;
	static Spacecraft spacecraft;
	static Light light;
	static ThirdPersonCamera camera;
	static GuiRenderer guirenderer;
	
	
	static float animationTime =0;
	
	static float tileSize = 2765f; 
	static float terrainHeight = 50f;
	
	static int movableEntity =0;
	

	static float HudVerticalSpeedLengthAdjustment=20;
	static float HudAltitudeLengthAdjustment=100;
	
	static float shine_value = 1;
	static float reflectivity_value =0;
	
    static double[][] InertiaTensorMatrix   =         {{   27364    ,    0       ,   0},
													   {      0     ,    27314    ,   0},
													   {      0     ,    0        ,   26114}};
    private static double[][] MRCS = {{1900},
			 						  {1900},
			 						  {1900}};
    
	private static double[][] quarternions = {	{-0.2164396},
												{0},
												{0},
												{0.976296}}; 
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public enum State {
		FLY, PAUSED, CLEANDISPLAY;
	}
	
	private static State state = State.FLY;
	
	@SuppressWarnings("static-access")
	public static void launchVisualEngine() {
		prepareSimulation();
	//-------------------------------------------------------------------------------------------------------------
	//						Visual Engine Loop
	//-------------------------------------------------------------------------------------------------------------
		 animationTime =0;
     //-----------------------------------------------------------
		while( !Display.isCloseRequested() ){
			checkInput();
			run();
		}
		//---------------------------------------------------------------------------------------------------
		//				Clean up 
		//---------------------------------------------------------------------------------------------------
		finalCleanUp();
	}
	
	public static void prepareSimulation() {
		DisplayManager.createDisplay();
	    loader = new Loader();
		 renderer = new MasterRenderer(loader);
		MasterRenderer.setEnvColor(0, 0, 0);
        //-----------------------------------------------------------------
        TextMaster.init(loader);
        String fontType = "arial";
        FontType font = new FontType(loader.loadFontTexture(fontType), new File("VisualEngine/fonts/"+fontType+".fnt"));
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
        GUIText HudTest1 = new GUIText("", 1f, font, new Vector2f(0.5f, 0.5f), 1f, false);
        HudTest1.setColour(0.0f, 0.832f, 0.152f);


        
        
        
        for(int i=100;i>=0;i--) {
        	float number = i*10-30;
            GUIText guiText = new GUIText(Float.toString(number), 1f, font, new Vector2f(-0.92f, 0.0f), 1f, false);
            guiText.setColour(0.0f, 0.8f, 0.152f);
            HudAltitude.add(guiText);   
        }  
        for(int i=80;i>=0;i--) {
        	float number = i*2-80;
            GUIText guiText = new GUIText(Float.toString(number), 1f, font, new Vector2f(-0.92f, 0.0f), 1f, false);
            guiText.setColour(0.0f, 0.8f, 0.152f);
            HudVerticalSpeed.add(guiText);   
        }  
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("greySand"));			
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, backgroundTexture, backgroundTexture, backgroundTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));
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
		 startPostion = new Vector3f(tileSize/2,200,0);
		SpaceShip spaceShip = new SpaceShip();
		spaceShip.setMass(10000);
		spaceShip.getPropulsion().setPrimaryPropellant(340);
		spaceShip.setInertiaTensorMatrix(InertiaTensorMatrix);
		spaceShip.getPropulsion().setPrimaryISPMax(311);
		spaceShip.getPropulsion().setSecondaryMomentum(MRCS);
		spaceShip.getPropulsion().setPrimaryThrustMax(45000);
		spaceShip.setInitialQuarterions(quarternions);
	    spacecraft = new Spacecraft(spaceShip,staticModel, startPostion,0,0,0,1);	
		Spacecraft.setQuarternions(quarternions);
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
		GuiTexture logo = new GuiTexture(loader.loadTerrainTexture("spaceLogo2"), 
				new Vector2f(0.86f, -0.86f), new Vector2f(0.16f, 0.45f));
		Logos.add(logo);
		GuiTexture BackgroundFadingLeft = new GuiTexture(loader.loadTerrainTexture("FadeBackground"), 
				new Vector2f(0.95f, 0.6f), new Vector2f(0.3f, 1.8f));
		Logos.add(BackgroundFadingLeft);
		GuiTexture BackgroundFadingRight = new GuiTexture(loader.loadTerrainTexture("FadeBackgroundLeft"), 
				new Vector2f(-0.93f, 0.6f), new Vector2f(0.3f, 2.8f));
		Logos.add(BackgroundFadingRight);
		addHudElements();
		 guirenderer = new GuiRenderer(loader);
	}
	
	@SuppressWarnings("static-access")
	public static void render() {
		renderer.processTerrain(terrain11);
		renderer.processTerrain(terrain12);
	    animationTime += DisplayManager.getFrameTimeSeconds();
			if(spacecraft.getPosition().x<tileSize) {
				spacecraft.fly(terrain11);
			} else {
				spacecraft.fly(terrain12);
			}
		camera.move();	// Do not touch!!!
		renderer.processEntity(spacecraft);
		renderer.render(light, camera);
		guirenderer.render(Logos);
		if(isHUD) {
		guirenderer.render(HUD);
		//HudTest.get(0).updateTextString("Test");
		//---------------------------------------------------
		  
            
        updateHudElements();
            
          //---------------------------------------------------
		}
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
	}
	
	public static void run() {
		switch(state) {
		case FLY:
			render();
			break;
		case PAUSED:
			renderer.processTerrain(terrain11);
			renderer.processTerrain(terrain12);
		    animationTime += DisplayManager.getFrameTimeSeconds();
			camera.move();	// Do not touch!!!
			renderer.processEntity(spacecraft);
			renderer.render(light, camera);
			DisplayManager.updateDisplay(); 
			break;
		case CLEANDISPLAY:
			render(); 
			break;
		}
	}
	private static void finalCleanUp() {
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
	
	private static void checkInput() {
		switch(state) {
		case FLY:
			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
				state = State.PAUSED;
				Display.setTitle("3D Visual Environment Mark1 - Paused");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case PAUSED:
			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
				state = State.FLY;
				Display.setTitle("3D Visual Environment Mark1");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case CLEANDISPLAY:
			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
				//state = State.FLY;
			}
		break;
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
				new Vector2f(-0.92f, 0.3f), new Vector2f(0.08f, 0.12f));
		HUD.add(HudBoxLeft);
		GuiTexture HudBoxRight = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_box"), 
				new Vector2f(0.955f, 0.3f), new Vector2f(0.08f, 0.12f));
		HUD.add(HudBoxRight);
		GuiTexture HudBarLeft = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_barLeft"), 
				new Vector2f(-0.9f, 0.1f), new Vector2f(0.18f, 1.0f));
		HUD.add(HudBarLeft);
		GuiTexture HudBarRight = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_barRight"), 
				new Vector2f(0.95f, 0.1f), new Vector2f(0.15f, 0.95f));
		HUD.add(HudBarRight);
		GuiTexture Hud_HorizonBar = new GuiTexture(loader.loadTerrainTexture("HUD_Display/hud_horizonBar"), 
				new Vector2f(0.33f, -0.0f), new Vector2f(0.7f, 0.2f));
		HUD.add(Hud_HorizonBar);
	}
	
	public static void updateHudElements() {
	
		float deltaSpeed = Spacecraft.getCurrentVerticalSpeed()/HudVerticalSpeedLengthAdjustment;
	        for(int i=0;i<=80;i++) {
	        	float number = i*2-80;
	        	float newZ = 0.33f+number/HudVerticalSpeedLengthAdjustment+deltaSpeed;
	        	if(newZ>0.6 || newZ<-0.0) {
	        	newZ = -10;
	        	} 
	    		Vector2f newPosition = new Vector2f(0.02f, newZ);
	            HudVerticalSpeed.get(i).setPosition(newPosition);
	        } 
	 //--------------------------------------------------------------------------------------------       
			float deltaAlt = Spacecraft.getPosition().y/HudAltitudeLengthAdjustment;
	        for(int i=100;i>=0;i--) {
	        	float number = i*10-30;
	        	float newZ = -9.08f+number/HudAltitudeLengthAdjustment+deltaAlt;
	        	if(newZ>0.6 || newZ<0.0) {
	        	newZ = -10;
	        	//HudAltitude.get(i).remove();
	        	} 
	    		Vector2f newPosition = new Vector2f(0.95f, newZ);
	            HudAltitude.get(i).setPosition(newPosition);
	        } 
	        HUD.get(4).setRot(spacecraft.getRotZ());
	}
	
	@SuppressWarnings("static-access")
	public static void updateOverlayDisplayTextRight(List<GUIText> guiTextsRight, Spacecraft spacecraft) {
		guiTextsRight.get(5).updateTextString("Roll [deg]:"+df2.format(spacecraft.getRotX()));
		guiTextsRight.get(5).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(6).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(7).setColour(0.9f, 0.9f, 0.9f);
		guiTextsRight.get(5).setPosition(new Vector2f(0.2f,0.03f));
		guiTextsRight.get(6).setPosition(new Vector2f(0.35f,0.03f));
		guiTextsRight.get(7).setPosition(new Vector2f(0.5f,0.03f));
		guiTextsRight.get(6).updateTextString("Pitch [deg]:"+df2.format(spacecraft.getRotZ()));
		guiTextsRight.get(7).updateTextString("Yaw [deg]:"+df2.format(spacecraft.getRotY()));	
		guiTextsRight.get(9).setPosition(new Vector2f(0.2f,0.06f));
		guiTextsRight.get(10).setPosition(new Vector2f(0.35f,0.06f));
		guiTextsRight.get(11).setPosition(new Vector2f(0.5f,0.06f));
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
		guiTextsLeft.get(0).setPosition(new Vector2f(0.2f,0.9f));
		guiTextsLeft.get(1).setPosition(new Vector2f(0.35f,0.9f));
		guiTextsLeft.get(2).setPosition(new Vector2f(0.5f,0.9f));
		guiTextsLeft.get(4).updateTextString("Pos X [m]: "+df2.format(spacecraft.getPosition().x));
		guiTextsLeft.get(4).setPosition(new Vector2f(0.2f,0.0f));
		guiTextsLeft.get(5).updateTextString("Pos Y [m]: "+df2.format(spacecraft.getPosition().y));
		if(spacecraft.getPosition().y<30) {
			guiTextsLeft.get(5).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsLeft.get(5).setColour(0.9f, 0.9f, 0.9f);
		}
		guiTextsLeft.get(6).updateTextString("Pos Z [m]: "+df2.format(spacecraft.getPosition().z));
		guiTextsLeft.get(5).setPosition(new Vector2f(0.35f,0.0f));
		guiTextsLeft.get(6).setPosition(new Vector2f(0.5f,0.0f));
		guiTextsLeft.get(8).updateTextString("Time [s]: "+df2.format(animationTime));
		guiTextsLeft.get(8).setColour(0.1f, 0.1f, 0.9f);
		guiTextsLeft.get(8).setPosition(new Vector2f(0.87f,0.95f));
		guiTextsLeft.get(10).updateTextString("Propellant [kg]: "+df2.format(spacecraft.getSCPropMass()));
		if(spacecraft.getisThrust()) {
			guiTextsLeft.get(10).setColour(1.0f, 0.647f, 0.5f);
		} else {
			guiTextsLeft.get(10).setColour(0.2f, 0.2f, 0.2f);
		}
		guiTextsLeft.get(10).setPosition(new Vector2f(0.0f,0.9f));
	}
}

