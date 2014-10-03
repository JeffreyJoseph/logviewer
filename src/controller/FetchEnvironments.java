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

import bean.LoggerBean;
import bean.ServerDetailsBean;

/**
 * Servlet implementation class FetchEnvironments
 */

@WebServlet("/FetchEnvironments")
public class FetchEnvironments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchEnvironments() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    
	    String envpath = ReadProperties.getValue("xmlfilepath");
		
		XMLReader xmlReader = new XMLReader();
		PrintWriter out = response.getWriter();
	    
		ArrayList<LoggerBean> list = xmlReader.fetchStages(envpath, 1);
		Iterator<LoggerBean> itrLB = list.iterator();
		LoggerBean tempLoggerBean = null;
		ServerDetailsBean tempSDBean = null;
		response.setContentType("text/html");

		out.println("<ul class=\"collapsibleList\" id=\"env\">");

		while(itrLB.hasNext()) {
			 tempLoggerBean = itrLB.next();
			 out.println("<li>" + tempLoggerBean.getStageName() + "</li>\n");
		     ArrayList<ServerDetailsBean> serverList = tempLoggerBean.getManagedServerDetails();
		     Iterator<ServerDetailsBean> itrSDB = serverList.iterator();
		     
		     while(itrSDB.hasNext()){
		     	tempSDBean = itrSDB.next();
		     	out.println("<input type=\"radio\" name =\"server\" id =\"server\" value=" + tempSDBean.getManagedServerName() + " onclick=\"fetchLoglist(this.value)\" >" + tempSDBean.getManagedServerName() + "</input><br>\n");
		     
			 }
		}
		
		out.println("</ul><br>");
		//out.println("<input type=\"button\" value=\"Get Log list\" onclick=\"fetchLoglist()\"/>");
		
		//request.setAttribute("envlist", outputBuf.toString());
		
    	//request.getRequestDispatcher("/index.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
