package VisualEngine.renderEngine;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 700;
	private static final int FPS_CAP = 120;
	
	public static void createDisplay(){		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			 //Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			 Display.setTitle("3D Visual Environment Mark1");
			// Display.setFullscreen(true); // Be careful, no escape ... 
			 Display.setResizable(true);
			 Display.setLocation(0, 0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//GL11.glViewport(0,0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP);
		Display.update();
		
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}
	
	public static void setDisplayParent(Canvas canvas) throws LWJGLException {
		Display.setParent(canvas);
	}

}
