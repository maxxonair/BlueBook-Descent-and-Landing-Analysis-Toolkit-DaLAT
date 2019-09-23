package VisualEngine.engineLauncher;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

	static Loader loader = new Loader();
	static int movableEntity =0;
	
	static float shine_value = 1;
	static float reflectivity_value =0;
	
    static double[][] InertiaTensorMatrix   =         {{   8000    ,    0       ,   0},
													   {      0    ,    8000    ,   0},
													   {      0    ,    0       ,   8000}};
    private static double[][] MRCS = {{500},
			 						  {500},
			 						  {500}};
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	@SuppressWarnings("static-access")
	public static void launchVisualEngine(List<AnimationSet> animationSets) {
		DisplayManager.createDisplay();
	    loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		MasterRenderer.setEnvColor(0, 0, 0);
        //-----------------------------------------------------------------
        TextMaster.init(loader);
        String fontType = "arial";
        FontType font = new FontType(loader.loadFontTexture(fontType), new File("VisualEngine/fonts/"+fontType+".fnt"));
        List<GUIText> guiTextsLeft = new ArrayList<GUIText>();
        List<GUIText> guiTextsRight = new ArrayList<GUIText>();
        float yGap = 0.03f;
        int numberGuiTexts=15;
        for(int i=0;i<numberGuiTexts;i++) {
            GUIText guiText = new GUIText("", 1f, font, new Vector2f(0f, 0f+i*yGap), 1f, false);
            guiText.setColour(0.5f, 0.5f, 0.5f);
            guiTextsLeft.add(guiText);
        }
        for(int i=0;i<numberGuiTexts;i++) {
            GUIText guiText = new GUIText("", 1f, font, new Vector2f(0.82f, 0.1f+i*yGap), 1f, false);
            guiText.setColour(0.5f, 0.5f, 0.5f);
            guiTextsRight.add(guiText);
        }
		//----------------------------------------------------------------
		// 					Terrain Setting
		//----------------------------------------------------------------
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTerrainTexture("greySand"));			
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, backgroundTexture, backgroundTexture, backgroundTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadBlendMap("blendMap2"));
		Terrain terrain11 = new Terrain(0,0,0,0, loader, texturePack, blendMap, "moon/tile11", 2365f, 40f);
		Terrain terrain12 = new Terrain(0,2365f,0,0, loader, texturePack, blendMap, "moon/tile11", 1000f, 30f);
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
		 startPostion = new Vector3f(600,150,0);
		}
		SpaceShip spaceShip = new SpaceShip();
		spaceShip.setMass(15000);
		spaceShip.getPropulsion().setPrimaryPropellant(300);
		spaceShip.setInertiaTensorMatrix(InertiaTensorMatrix);
		spaceShip.getPropulsion().setPrimaryISPMax(330);
		spaceShip.getPropulsion().setSecondaryMomentum(MRCS);
		spaceShip.getPropulsion().setPrimaryThrustMax(30000);
		Spacecraft spacecraft = new Spacecraft(spaceShip,staticModel, startPostion,0,0,0,1);	
		spaceElements.add(spacecraft);
		//----------------------------------------------------------------
		// 					  Light Setting
		//----------------------------------------------------------------
		Vector3f lightPostion = new Vector3f(600,350,0);
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
				if(spacecraft.getPosition().x<1000f) {
					spacecraft.fly(terrain11);
				} else {
					//spacecraft.fly(terrain12);
				}
			}
			camera.move();	// Do not touch!!!
			renderer.processEntity(spacecraft);
			renderer.render(light, camera);
			guirenderer.render(Logos);
			//----------------------------------------------------------------------------------------------
			// 			Overlay Display
			//----------------------------------------------------------------------------------------------
			if(isOverlayDisplay) {
				updateOverlayDisplayTextLeft(guiTextsLeft, spacecraft, animationTime);
			if(!isAnimate) {
				updateOverlayDisplayTextRight(guiTextsRight, spacecraft);
			}
			TextMaster.render();
			//----------------------------------------------------------------------------------------------
			}
			DisplayManager.updateDisplay(); 
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
	@SuppressWarnings("static-access")
	public static void updateOverlayDisplayTextRight(List<GUIText> guiTextsRight, Spacecraft spacecraft) {
		guiTextsRight.get(5).updateTextString("Roll [deg]:"+df2.format(spacecraft.getRotX()));
		guiTextsRight.get(6).updateTextString("Pitch [deg]:"+df2.format(spacecraft.getRotZ()));
		guiTextsRight.get(7).updateTextString("Yaw [deg]:"+df2.format(spacecraft.getRotY()));	
		
		double rotTreshold = 3;
		guiTextsRight.get(9).updateTextString("Roll Rate [deg/s]:" +df2.format(Math.toDegrees(spacecraft.getPQR()[0][0])));
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[0][0]))>rotTreshold) {
			guiTextsRight.get(9).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(9).setColour(0.5f, 0.5f, 0.5f);
		}
		guiTextsRight.get(10).updateTextString("Pitch Rate [deg/s]:"+df2.format(Math.toDegrees(spacecraft.getPQR()[1][0])));
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[1][0]))>rotTreshold) {
			guiTextsRight.get(10).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(10).setColour(0.5f, 0.5f, 0.5f);
		}
		guiTextsRight.get(11).updateTextString("Yaw Rate [deg/s]:"  +df2.format(Math.toDegrees(spacecraft.getPQR()[2][0])));	
		if(Math.abs(Math.toDegrees(spacecraft.getPQR()[2][0]))>rotTreshold) {
			guiTextsRight.get(11).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsRight.get(11).setColour(0.5f, 0.5f, 0.5f);
		}
	}
	
	@SuppressWarnings("static-access")
	public static void updateOverlayDisplayTextLeft(List<GUIText> guiTextsLeft, Spacecraft spacecraft, float animationTime) {
	    guiTextsLeft.get(0).updateTextString("Vel H [m/s]: "+df2.format(spacecraft.getCurrentHorizontalSpeed()));
		guiTextsLeft.get(1).updateTextString("Vel V [m/s]: "+df2.format(spacecraft.getCurrentVerticalSpeed()));
		if(spacecraft.getCurrentVerticalSpeed()<-10) {
			guiTextsLeft.get(1).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsLeft.get(1).setColour(0.5f, 0.8f, 0.5f);
		}
		guiTextsLeft.get(2).updateTextString("Vel T [m/s]: "+df2.format(spacecraft.getCurrentSpeed()));
		guiTextsLeft.get(4).updateTextString("Pos X [m]: "+df2.format(spacecraft.getPosition().x));
		guiTextsLeft.get(5).updateTextString("Pos Y [m]: "+df2.format(spacecraft.getPosition().y));
		if(spacecraft.getPosition().y<30) {
			guiTextsLeft.get(5).setColour(0.8f, 0.5f, 0.5f);
		} else {
			guiTextsLeft.get(5).setColour(0.5f, 0.8f, 0.5f);
		}
		guiTextsLeft.get(6).updateTextString("Pos Z [m]: "+df2.format(spacecraft.getPosition().z));
		guiTextsLeft.get(8).updateTextString("Time [s]: "+df2.format(animationTime));
		guiTextsLeft.get(8).setColour(0.5f, 0.5f, 0.8f);
		guiTextsLeft.get(10).updateTextString("Propellant [kg]: "+df2.format(spacecraft.getSCPropMass()));
		if(spacecraft.getisThrust()) {
			guiTextsLeft.get(14).setColour(1.0f, 0.647f, 0.5f);
		} else {
			guiTextsLeft.get(14).setColour(0.5f, 0.5f, 0.5f);
		}
	}
}
