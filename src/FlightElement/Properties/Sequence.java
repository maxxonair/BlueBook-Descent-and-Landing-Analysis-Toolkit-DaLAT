package FlightElement.Properties;

import java.util.ArrayList;
import java.util.List;

import FlightElement.GNCModel.SequenceElement;

public class Sequence {
	
	private  List<SequenceElement> SEQUENCE_DATA_main 					 = new ArrayList<SequenceElement>(); 
	
	public Sequence() {
		
	}

	public List<SequenceElement> getSEQUENCE_DATA_main() {
		return SEQUENCE_DATA_main;
	}

	public void setSEQUENCE_DATA_main(List<SequenceElement> sEQUENCE_DATA_main) {
		SEQUENCE_DATA_main = sEQUENCE_DATA_main;
	}
	
	

}
