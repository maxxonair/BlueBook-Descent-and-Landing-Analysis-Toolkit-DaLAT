package Experimental.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Network {
	
	private List<NeuronLayer> neuronLayer = new ArrayList<NeuronLayer>();
	
	public Network() {
				
	}

	public int getNumberOfLayers() {
		return neuronLayer.size();
	}


	public NeuronLayer getLayer(int indx) {
		return neuronLayer.get(indx);
	}

	public void addLayer(NeuronLayer neuronLayer) {
		this.neuronLayer.add(neuronLayer);
	}	
	

}
