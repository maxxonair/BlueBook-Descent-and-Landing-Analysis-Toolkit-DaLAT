package Experimental.NeuralNetwork;

public class NeuronLayer {

	private int numberOfNeurons;
	private int numberOfInputs;
	private Neuron[] neuron ;
	
	public NeuronLayer(int numberOfNeurons, int numberOfInputs) {
		this.numberOfNeurons = numberOfNeurons;
		this.numberOfInputs = numberOfInputs;
		
		 this.neuron = new Neuron[numberOfNeurons];
		for(int i=0;i<neuron.length;i++) {
			neuron[i] = new Neuron(numberOfInputs);
		}
	}

	public int getNumberOfNeurons() {
		return numberOfNeurons;
	}

	public int getNumberOfInputs() {
		return numberOfInputs;
	}

	public Neuron getNeuron(int indx) {
		return neuron[indx];
	}
		
}
