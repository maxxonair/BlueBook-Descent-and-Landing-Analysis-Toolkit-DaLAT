package VisualEngine.renderEngine;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.apple.eawt.Application;

public class DisplayManager {
	
	private static final int WIDTH = 1300;
	private static final int HEIGHT = 750;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime; 
	private static float deltaTime;
	
    public static String ICON_File   	 		= "/images/moon.png";
	
	
	public static void createDisplay(){	
		
   	 String dir = System.getProperty("user.dir");
   	 ICON_File = dir + ICON_File; 
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			 Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			 Display.create(new PixelFormat(), attribs);
			 Display.setTitle("3D Visual Environment Mark1");
			// Display.setFullscreen(true); // Be careful, no escape ... 
			 Display.setResizable(true);
			 Display.setLocation(0, 0);
	         try {
	             Application application = Application.getApplication();
	             Image image = Toolkit.getDefaultToolkit().getImage(ICON_File);
	             application.setDockIconImage(image);
	             } catch(Exception e) {
	            	 System.err.println("Taskbar icon could not be created");
	             }
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		lastFrameTime = getCurrentTime();
		//GL11.glViewport(0,0, WIDTH, HEIGHT);
	}
	
	public static float getFrameTimeSeconds() {
		return deltaTime;
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		deltaTime = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = getCurrentTime();
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}
	
	public static void setDisplayParent(Canvas canvas) throws LWJGLException {
		Display.setParent(canvas);
	}
	
	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}

}
