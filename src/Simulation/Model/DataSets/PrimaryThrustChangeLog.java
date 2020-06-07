package Simulation.Model.DataSets;

public class PrimaryThrustChangeLog {
	
	private boolean isChange=false;
	private double TimeStamp;
	private double CMD_OLD;
	private double CMD_NEW;
	
	private double propTime=0;
	
	public PrimaryThrustChangeLog() {

	}

	




	public double getPropTime() {
		return propTime;
	}






	public void setPropTime(double propTime) {
		this.propTime = propTime;
	}






	public boolean isChange() {
		return isChange;
	}


	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}



	public double getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(double timeStamp) {
		TimeStamp = timeStamp;
	}

	public double getCMD_OLD() {
		return CMD_OLD;
	}

	public void setCMD_OLD(double cMD_OLD) {
		CMD_OLD = cMD_OLD;
	}

	public double getCMD_NEW() {
		return CMD_NEW;
	}

	public void setCMD_NEW(double cMD_NEW) {
		CMD_NEW = cMD_NEW;
	}

}
