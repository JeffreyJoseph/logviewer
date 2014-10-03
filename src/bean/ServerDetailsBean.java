package bean;

import java.util.ArrayList;

public class ServerDetailsBean {
	private String managedServerName;
	private ArrayList<FilesBean> logFileNamePath;
	
	public String getManagedServerName() {
		return managedServerName;
	}
	public void setManagedServerName(String managedServerName) {
		this.managedServerName = managedServerName;
	}
	public ArrayList<FilesBean> getLogFileNamePath() {
		return logFileNamePath;
	}
	public void setLogFileNamePath(ArrayList<FilesBean> logFileNamePath) {
		this.logFileNamePath = logFileNamePath;
	}
	
}
