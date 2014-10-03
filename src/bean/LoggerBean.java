package bean;

import java.util.ArrayList;

public class LoggerBean {
	private String stageName;
	private ArrayList<ServerDetailsBean> managedServerDetails;
	
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public ArrayList<ServerDetailsBean> getManagedServerDetails() {
		return managedServerDetails;
	}
	public void setManagedServerDetails(ArrayList<ServerDetailsBean> managedServerDetails) {
		this.managedServerDetails = managedServerDetails;
	}
	
	
}
