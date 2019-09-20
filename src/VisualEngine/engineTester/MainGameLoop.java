package VisualEngine.engineTester;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import VisualEngine.animation.AnimationSet;
import VisualEngine.engineLauncher.worldAnimation;
//import VisualEngine.engineLauncher.worldGenerator;

public class MainGameLoop {
	
	public static String RES_File    			= "/animationTestFile2.txt"  ; 
	
	public static int indx_time=0;
	public static int indx_x=78;
	public static int indx_y=79;
	public static int indx_z=80;
	
	public static int indx_vel =6;
	public static int indx_fpa =7;
	public static int indx_azi =8;
	
	public static float init_x;
	public static float init_y;
	public static float init_z;
	
	public static float init_alt;
	

	public static void main(String[] args) {
		// worldGenerator.launchVisualEngine();
		List<AnimationSet> animationSets = READ_AnimationData();
		worldAnimation.launchVisualEngine(animationSets);		
	}
	
    public static List<AnimationSet>  READ_AnimationData() {
    	 List<AnimationSet> animationSets= new ArrayList<AnimationSet>();
   	 String dir = System.getProperty("user.dir");
   	 RES_File  = dir + RES_File  ;
	 FileInputStream fstream = null;
     DataInputStream in = new DataInputStream(fstream);
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
     String strLine;
    	// Scan for specifin information 
	     fstream = null;
		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	              try {
	              int indx=0;
							while ((strLine = br.readLine()) != null )   {
								Object[] tokens = strLine.split(" ");

							    if(indx==0){init_alt=Float.parseFloat((String) tokens[4]);}
							    indx++;
							    }
			       fstream.close();
			       in.close();
			       br.close();

	              } catch (NullPointerException | IOException eNPE) { 
	            	  System.out.println("Read raw data, Nullpointerexception");
					}catch(IllegalArgumentException eIAE) {
					  System.out.println("Read raw data, illegal argument error");
					}
	//----------------------------------------------------------------------------------------------
	      	    fstream = null;
	    		try{ fstream = new FileInputStream(RES_File);} catch(IOException eIO) { System.out.println(eIO);}
	               in = new DataInputStream(fstream);
	               br = new BufferedReader(new InputStreamReader(in));
	               strLine=null;
	    	              try {
	    							while ((strLine = br.readLine()) != null )   {
	    								Object[] tokens = strLine.split(" ");
	    							    AnimationSet animationSet = new AnimationSet();
	    							    animationSet.setTime(Float.parseFloat((String) tokens[indx_time]));
	    							    float velocity = Float.parseFloat((String) tokens[indx_vel]);
	    							    float fpa = Float.parseFloat((String) tokens[indx_fpa]);
	    							    float azi = Float.parseFloat((String) tokens[indx_azi]);
	    							    float v_v = (float) (velocity * Math.sin(fpa));
	    							    float v_h = (float) (velocity * Math.cos(fpa));
	    							    float rotY = Float.parseFloat((String) tokens[89]);
	    							    
	    							    //System.out.println(v_v+" | "+init_alt);
	    							    animationSet.setV_h(v_h);
	    							    animationSet.setV_v(v_v);
	    							    animationSet.setAzimuth(azi);
	    							    animationSet.setAlt_init(init_alt);
	    							    animationSet.setRotY(rotY);
	    							    animationSet.setAngularRateY(Float.parseFloat((String) tokens[83]));
	    							    animationSets.add(animationSet);	    							  }
	    			       fstream.close();
	    			       in.close();
	    			       br.close();

	    	              } catch (NullPointerException | IOException eNPE) { 
	    	            	  System.out.println("Read raw data, Nullpointerexception");
	    					}catch(IllegalArgumentException eIAE) {
	    					  System.out.println("Read raw data, illegal argument error");
	    					}
						return animationSets;
    }
}
