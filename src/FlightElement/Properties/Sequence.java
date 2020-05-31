package FlightElement.Properties;

import java.util.ArrayList;
import java.util.List;

import FlightElement.GNCModel.SequenceContent;
import FlightElement.GNCModel.SequenceElement;

public class Sequence {
	
	private  List<SequenceElement> sequenceData 					 = new ArrayList<SequenceElement>(); 
	
	List<SequenceContent> sequenceSet = new ArrayList<SequenceContent>();
	
	public Sequence() {
		
	}

	public List<SequenceContent> getSequenceSet() {
		return sequenceSet;
	}

	public void setSequenceSet(List<SequenceContent> SequenceSet) {
		sequenceSet = SequenceSet;
	}

	public List<SequenceElement> getSequenceData() {
		return sequenceData;
	}

	public void setSequenceData(List<SequenceElement> sequenceData) {
		this.sequenceData = sequenceData;
	}
	

}
