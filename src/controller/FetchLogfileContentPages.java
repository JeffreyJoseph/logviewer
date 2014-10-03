package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FetchLogfileContentPages
 */
@WebServlet("/FetchLogfileContentPages")
public class FetchLogfileContentPages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchLogfileContentPages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String filename = request.getParameter("file");
		int pageNumber = Integer.parseInt(request.getParameter("pageno").toString());
		int totalPages = Integer.parseInt(request.getParameter("total").toString());
		//System.out.println("pageno="+pageNumber+"\ttotal="+totalPages);
		
		PrintWriter out = response.getWriter();
		
		RandomAccessFile raf = null;
		List<String> lines = new ArrayList<String>();
		
		int averageLineLength = Utilities.averageLineLength(filename);
		final int chunkSize = 2600 * averageLineLength;
		
		
		response.setContentType("text/html");
		
		try{
			
			File file = new File(filename);
			raf = new RandomAccessFile(file, "r");
			
			long readLen = chunkSize;
			long end = raf.length();
			long seek = (pageNumber-1)*chunkSize;
			System.out.println("Seek = "+seek);
			if(pageNumber == totalPages)
				readLen = end - seek;
			byte[] buf = new byte[chunkSize];			
			
			raf.seek(seek);
			readLen = raf.read(buf, 0, (int)readLen);
			
			int unparsedSize = (int)readLen;
		    int index = unparsedSize - 1;
		    
		    while (index >= 0) {
		        if (buf[index] == '\n') {
		            int startOfLine = index + 1;
		            int len = (unparsedSize - startOfLine);
		            if (len > 0) {
		                lines.add(new String(buf, startOfLine, len));
		            }
		            unparsedSize = index + 1;
		        }
		        --index;
		    }
		    
		    //System.out.println("Lines read = "+lines.size());
		    //System.out.println(">>>> LOG FILE <<<<<");
			for (int i = lines.size() - 1; i >= 0; --i)
				out.println(lines.get(i)+"<br>");
			
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if(raf != null) raf.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
