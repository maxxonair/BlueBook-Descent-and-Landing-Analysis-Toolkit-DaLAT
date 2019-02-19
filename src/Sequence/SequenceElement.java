package Sequence;

public class SequenceElement{
	private int 	sequence_ID;
	private int 	trigger_start_type;
	private int 	trigger_end_type;
	private double 	trigger_start_value;
	private double 	trigger_end_value;
	private int 	sequence_type;
	private int 	sequence_controller_ID; 
	
	public SequenceElement(int sequence_ID, int trigger_start_type, int trigger_end_type, double trigger_start_value, double trigger_end_value, int sequence_type, int sequence_controller_ID) {
		this.sequence_ID 			= sequence_ID;
		this.trigger_start_type 	= trigger_start_type;
		this.trigger_end_type 		= trigger_end_type;
		this.trigger_start_value 	= trigger_start_value;
		this.trigger_end_value 		= trigger_end_value;
		this.sequence_type 			= sequence_type;
		this.sequence_controller_ID = sequence_controller_ID; 
	}
	
	public void Update(int sequence_ID, int trigger_start_type, int trigger_end_type, double trigger_start_value, double trigger_end_value, int sequence_type, int sequence_controller_ID) {
		this.sequence_ID 			= sequence_ID;
		this.trigger_start_type 	= trigger_start_type;
		this.trigger_end_type 		= trigger_end_type;
		this.trigger_start_value 	= trigger_start_value;
		this.trigger_end_value 		= trigger_end_value;
		this.sequence_type 			= sequence_type;
		this.sequence_controller_ID = sequence_controller_ID; 
	}
	
	public int  get_sequence_ID() {
		return sequence_ID;
	}
	public int get_trigger_start_type() {
		return trigger_start_type;
	}
	public int get_trigger_end_type() {
		return trigger_end_type;
	}
	public double get_trigger_start_value() {
		return trigger_start_value;
	}
	public double get_trigger_end_value() {
		return trigger_end_value;
	}
	public int get_sequence_type() {
		return sequence_type;
	}
	public int get_sequence_controller_ID() {
		return sequence_controller_ID; 
	}	
}