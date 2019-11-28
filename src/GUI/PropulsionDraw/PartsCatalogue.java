package GUI.PropulsionDraw;

import java.util.ArrayList;
import java.util.List;


public class PartsCatalogue {
	
	List<CatalogueElement> list;
	
	private  String imageRocketEngine = "images/propulsionElements/rocketEngine.png";
	
	
	private  String imageSingleThruster = "images/propulsionElements/singleThruster.png";
	private  String imageDualThruster = "images/propulsionElements/dualThruster.png";
	private  String imageQuadroThruster = "images/propulsionElements/quadroThruster.png";
	
	private  String imageTankBasic = "images/propulsionElements/tankBasic.png";
	
	private  String imageFilter = "images/propulsionElements/filter.png";
	private  String imageSolenoidValve = "images/propulsionElements/solenoidValve.png";
	private  String imagePyroValve = "images/propulsionElements/pyroValve.png";
	private  String imageFillDrainValve = "images/propulsionElements/fillDrainValve.png";
	private  String imageReliefValve = "images/propulsionElements/reliefValve.png";
	private  String imageCheckValve = "images/propulsionElements/checkValve.png";
	private  String imagelatchValve = "images/propulsionElements/latchValve.png";
	
	private  String imageOrifice = "images/propulsionElements/orifice.png";
	private  String imageBurstDisk = "images/propulsionElements/burstDisk.png";

	private  String imagePressureTransducer = "images/propulsionElements/pressureTransducer.png";
	private  String imagePipeElement = "images/propulsionElements/pipeElement.png";
	private  String imageJoint = "images/propulsionElements/joint.png";
	
	public PartsCatalogue(ReadWrite readWrite) {
		list = new ArrayList<>();
		list.add(new CatalogueElement("MainEngine",null,imageRocketEngine));
		
		list.add(new CatalogueElement("AUX Thruster",null,imageSingleThruster));
		list.add(new CatalogueElement("DualThruster",null,imageDualThruster));
		list.add(new CatalogueElement("ThrusterPod",null,imageQuadroThruster));
		
		list.add(new CatalogueElement("Tank",null,imageTankBasic));
		
		list.add(new CatalogueElement("Filter",null,imageFilter));
		list.add(new CatalogueElement("SolenoidValve",null,imageSolenoidValve));
		list.add(new CatalogueElement("PyroValve",null,imagePyroValve));
		list.add(new CatalogueElement("DrainValve",null,imageFillDrainValve));
		list.add(new CatalogueElement("ReliefValve",null,imageReliefValve));
		list.add(new CatalogueElement("CheckValve",null,imageCheckValve));
		list.add(new CatalogueElement("LatchValve",null,imagelatchValve));
		
		list.add(new CatalogueElement("Orifice",null,imageOrifice));
		list.add(new CatalogueElement("BurstDisk",null,imageBurstDisk));
		list.add(new CatalogueElement("PressureTransducer",null,imagePressureTransducer));
		list.add(new CatalogueElement("PipeElement",null,imagePipeElement));
		list.add(new CatalogueElement(" ",null,imageJoint));
	}

	public List<CatalogueElement> getList() {
		return list;
	}
	
	

}
