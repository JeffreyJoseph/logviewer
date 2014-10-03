package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bean.FilesBean;
import bean.LoggerBean;
import bean.ServerDetailsBean;

public class XMLReader {

	Document doc;
	
	public ArrayList<LoggerBean> fetchStages(String fileName, int depth){
		ArrayList<LoggerBean> resultList = new ArrayList<LoggerBean>();
		
		String xmlfile = fileName;

		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		try {
			doc = db.parse(xmlfile);
		} 
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NodeList nodeList = doc.getDocumentElement().getChildNodes(); //Get <environment> nodes
		
		for(int i = 0;i<nodeList.getLength();i++){  //Start of Outer Loop
			LoggerBean loggerBean = new LoggerBean();
			ArrayList<ServerDetailsBean> serverList = new ArrayList<ServerDetailsBean>(); 
			Node node = nodeList.item(i);
			if(node instanceof Element){
				NodeList envDetails = node.getChildNodes();
				
				for(int j = 0;j<envDetails.getLength();j++){  //Start of Inner Loop - get <envname>, <server> nodes
					
					Node nameNode = envDetails.item(j);
					if(nameNode instanceof Element){
						String content = nameNode.getLastChild().getTextContent().trim();
												
						if(nameNode.getNodeName().equals("envname"))
							loggerBean.setStageName(content);
						
						else if(nameNode.getNodeName().equals("server")){
							ServerDetailsBean serverDetailsBean = new ServerDetailsBean();
							serverDetailsBean = getServerDetails(nameNode, depth);
							serverList.add(serverDetailsBean);
						}
						
					}
				}  //End of Inner Loop
				
				loggerBean.setManagedServerDetails(serverList);
				resultList.add(loggerBean);
			}
		}  //End of Outer Loop
			
		return resultList;
	}
	

	public ServerDetailsBean getServerDetails(Node node, int depth){
		ServerDetailsBean sdBean = new ServerDetailsBean();
		FilesBean tempFilesBean = new FilesBean();
		NodeList serverDetails = node.getChildNodes();
		ArrayList<FilesBean> logFileNamePath = new ArrayList<FilesBean>();
		
		for(int k = 0;k<serverDetails.getLength();k++){		//Get Server name <name> and Files <files> nodes 
			
			Node serverNode = serverDetails.item(k);
			if(serverNode instanceof Element){
				String content = serverNode.getLastChild().getTextContent().trim();
				
				if(serverNode.getNodeName().equals("name"))
					sdBean.setManagedServerName(content);
					
				else if(serverNode.getNodeName().equals("files") && depth==2){
					tempFilesBean = getLogFilesAndPaths(serverNode);
					
					StringTokenizer fileslist = new StringTokenizer(tempFilesBean.getFileNames(), ";");
					while(fileslist.hasMoreElements()){
						FilesBean fb = new FilesBean();
						fb.setFileNames(fileslist.nextElement().toString());
						fb.setPath(tempFilesBean.getPath());
						logFileNamePath.add(fb);
					}
				}
			}
			
		}
		sdBean.setLogFileNamePath(logFileNamePath);
		return sdBean;
	}
	
	public FilesBean getLogFilesAndPaths(Node node){
		FilesBean filesBean = new FilesBean();
		NodeList fileNamesPath = node.getChildNodes();
		
		for(int l = 0;l<fileNamesPath.getLength();l++){		//Get filename <filename> and path <path> nodes
			
			Node filesNode = fileNamesPath.item(l);
			if(filesNode instanceof Element){
				String content = filesNode.getLastChild().getTextContent().trim();
				
				if(filesNode.getNodeName().equals("filename"))
					filesBean.setFileNames(content);
				
				else if(filesNode.getNodeName().equals("path"))
					filesBean.setPath(content);
			}
		}		

		return filesBean;
	}
}
