package Experimental.NeuralNetwork;

public class Neuron {
	
	private double[] input;
	private double[] weight;
	
	public double output=0;
	
	public double error=0;
	public double delta=0;
	
	private double bias=0;
	
	private int numberOfInputs;
	
	public Neuron(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
		this.weight =  new double[numberOfInputs];
		this.input = new double[numberOfInputs];		
		
		for(int i=0;i<numberOfInputs;i++) {
			weight[i] = Math.random();
			input[i]  = Math.random();
		}
	}
	
	

	public double getInput(int indx) {
		return input[indx];
	}



	public void setInput(int indx, double input) {
		this.input[indx] = input;
	}



	public int getNumberOfInputs() {
		return numberOfInputs;
	}



	public void setNumberOfInputs(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
	}



	public double getBias() {
		return bias;
	}



	public void setBias(double bias) {
		this.bias = bias;
	}



	public double getWeight(int indx) {
		return weight[indx];
	}


	public void setWeight(int indx, double weight) {
		this.weight[indx] = weight;
	}



	public double getDelta() {
		return delta;
	}


	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}	

}
