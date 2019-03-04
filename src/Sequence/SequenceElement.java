package Sequence;

public class SequenceElement{
	private int 	sequence_ID;
	private int 	trigger_end_type;
	private double 	trigger_end_value;
	private int 	sequence_type;
	private int 	sequence_controller_ID; 
	private double  ctrl_target_vel ;
	private double  ctrl_target_alt; 
	private int 	ctrl_target_curve;
	private double  tzero; 
	
	public SequenceElement(int sequence_ID, int trigger_end_type, double trigger_end_value, int sequence_type, int sequence_controller_ID, double ctrl_target_vel, double ctrl_target_alt, int ctrl_target_curve) {
		this.sequence_ID 			= sequence_ID;
		this.trigger_end_type 		= trigger_end_type;
		this.trigger_end_value 		= trigger_end_value;
		this.sequence_type 			= sequence_type;
		this.sequence_controller_ID = sequence_controller_ID; 
		this.ctrl_target_vel 		= ctrl_target_vel;
		this.ctrl_target_alt 		= ctrl_target_alt;
		this.ctrl_target_curve 		= ctrl_target_curve; 

	}
	
	public void Update(int sequence_ID, int trigger_end_type, double trigger_end_value, int sequence_type, int sequence_controller_ID, double ctrl_target_vel, double ctrl_target_alt, int ctrl_target_curve) {
		this.sequence_ID 			= sequence_ID;
		this.trigger_end_type 		= trigger_end_type;
		this.trigger_end_value 		= trigger_end_value;
		this.sequence_type 			= sequence_type;
		this.sequence_controller_ID = sequence_controller_ID; 
		this.ctrl_target_vel 		= ctrl_target_vel;
		this.ctrl_target_alt 		= ctrl_target_alt;
		this.ctrl_target_curve		= ctrl_target_curve;
	}
	
	public int  get_sequence_ID() {
		return sequence_ID;
	}
	public int get_trigger_end_type() {
		return trigger_end_type;
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
	public double get_ctrl_target_vel() {
		return ctrl_target_vel;
	}
	public double get_ctrl_target_alt() {
		return ctrl_target_alt;
	}
	public int get_ctrl_target_curve() {
		return ctrl_target_curve;
	}
}