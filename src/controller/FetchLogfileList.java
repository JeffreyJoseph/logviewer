package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.FilesBean;
import bean.LoggerBean;
import bean.ServerDetailsBean;

/**
 * Servlet implementation class FetchLogList
 */
@WebServlet("/FetchLogfileList")
public class FetchLogfileList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FetchLogfileList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String managedServerName = request.getParameter("server");
		String prevdayLog = request.getParameter("prevday");
		String prevdate = request.getParameter("prevdate");
	    
	    String envpath = ReadProperties.getValue("xmlfilepath");
	    
		XMLReader xmlReader = new XMLReader();
		ArrayList<LoggerBean> list = xmlReader.fetchStages(envpath,2);
		Iterator<LoggerBean> itrLB = list.iterator();
		
		LoggerBean tempLoggerBean = null;
		ServerDetailsBean tempSDBean = null;
		FilesBean tempFBean = null;
		String file = null;
		String path = null;
		
		PrintWriter out = response.getWriter();		
		response.setContentType("text/html");
		
		out.println("<ul class=\"collapsibleList\" id=\"env\">");
		
		while(itrLB.hasNext()) {
			 tempLoggerBean = itrLB.next();
			 out.println("<li>" + tempLoggerBean.getStageName() + "</li>\n");
		     ArrayList<ServerDetailsBean> serverList = tempLoggerBean.getManagedServerDetails();
		     Iterator<ServerDetailsBean> itrSDB = serverList.iterator();
		     
		     //out.println("<ul class=\"select\" id = \"selectedserver\" class=\"collapsibleList\" >");
		     while(itrSDB.hasNext()){
		     	tempSDBean = itrSDB.next();

		     	if(tempSDBean.getManagedServerName().equalsIgnoreCase(managedServerName)){
		     		out.println("<input type=\"radio\" name =\"server\" id =\"server\" value=" + tempSDBean.getManagedServerName() + " onclick=\"fetchLoglist(this.value)\" checked=\"checked\" >" + tempSDBean.getManagedServerName() + "</input><br>\n");
		     		out.println("<ul>");
		     		
		     		ArrayList<FilesBean> logFileNamePath = tempSDBean.getLogFileNamePath();
		     		ArrayList<FilesBean> output = Utilities.checkLogfiles(logFileNamePath, prevdayLog, prevdate);
		     		Iterator<FilesBean> itrFiles = output.iterator();
		    		while(itrFiles.hasNext()){
		    			tempFBean = itrFiles.next();
		    			file = tempFBean.getFileNames();
		    			path = tempFBean.getPath();
		    			
		    			out.println("<input type=\"radio\" name =\"file\" id=\"file\" value=" + path + "/" + file + " onclick=fetchLogfileContent(this.value) >" + file + "</input><br>\n");
		    			
		    		}
		    		out.println("</ul>");
		     	}
		     	else{
		     		out.println("<input type=\"radio\" name =\"server\" id =\"server\" value=" + tempSDBean.getManagedServerName() + " onclick=\"fetchLoglist(this.value)\" >" + tempSDBean.getManagedServerName() + "</input><br>\n");
		     	}
		     
			 }
		}
		
		out.println("</ul><br>");

		//End of environments file read
		
		//request.setAttribute("result", outputBuf.toString());
        //request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
