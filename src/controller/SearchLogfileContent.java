package controller;

import java.io.BufferedReader;
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
 * Servlet implementation class SearchLogfileContent
 */
@WebServlet("/SearchLogfileContent")
public class SearchLogfileContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchLogfileContent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String readFile = request.getParameter("file");
		String searchText = request.getParameter("text").trim();
		int pageNumber = Integer.parseInt(request.getParameter("pageno").toString());
		//System.out.println("Search text:"+searchText+";");
		
	    PrintWriter out = response.getWriter();
	    response.setContentType("text/html");
	    
		BufferedReader reader = null;
		RandomAccessFile raf = null;
		List<String> lines = new ArrayList<String>();
		boolean readMore = true;
		
		int chunkSize = 0;
		long startPoint = 0;
		long end = 0;
		int linesToRead = 3000;
		
		if(searchText.isEmpty())
			out.println("Please enter the Search text and then click Search<br>");
		else{
			/*try{
				FileInputStream fis = new FileInputStream(readFile);
				reader = new BufferedReader(new InputStreamReader(fis));
				
				String newLine = reader.readLine();
				int index = 0;
				while(newLine!=null && index<=8000){
					if(newLine.toLowerCase().indexOf(searchText.toLowerCase())!=-1){
						out.println(newLine+"<br>");
						index++;
					}						
					newLine = reader.readLine();					
				}*/
				try{
					File file = new File(readFile);
					raf = new RandomAccessFile(file,"r");
					int totalPages = 0;
					end = raf.length();
					
					int avgLineLength = Utilities.averageLineLength(readFile);
					int onePage = avgLineLength * 16000;
					totalPages = (int)(end/onePage);
					
					if(pageNumber==-1)
						chunkSize = 1024 * 32;
					else
						chunkSize = avgLineLength * 16000;
					
					if (totalPages<1) totalPages = 1;
					else if (totalPages>=1 && (end%onePage)!=0) totalPages++;
					
					out.println("<!--pages:"+ totalPages +";-->");
					
					while (readMore) {
					    byte[] buf = new byte[chunkSize];
					    
					    // Read a chunk from the end of the file
					    if(pageNumber==-1)
					    	startPoint = end - chunkSize;
					    else
					    	startPoint = (pageNumber-1)*chunkSize;
					    
					    long readLen = chunkSize;
					    //System.out.println("startPoint = "+startPoint+"\treadLen = "+readLen);
					    if (startPoint < 0) {
					        readLen = chunkSize + startPoint;
					        startPoint = 0;
					    }
					    
					    raf.seek(startPoint);
					    readLen = raf.read(buf, 0, (int)readLen);
					    //if (readLen <= 0) 
					        //break;			    
						
					    // Parse newlines and add them to an array
					    int unparsedSize = (int)readLen;
					    int index = unparsedSize - 1;
					    while (index >= 0) {
					        if (buf[index] == '\n') {
					            int startOfLine = index + 1;
					            int len = (unparsedSize - startOfLine);
					            String str = new String(buf, startOfLine, len);
					            if ((len > 0) && (str.toLowerCase().indexOf(searchText.toLowerCase())!=-1)) {
					                lines.add(str);
					            }
					            unparsedSize = index + 1;
					            //System.out.println("index 2 = "+index+"\tunparsedSize 2 = "+unparsedSize);
					        }
					        index -= 1;
					    }
					    end = end - (chunkSize - unparsedSize);
						
					    readMore = (lines.size() < linesToRead) && (startPoint != 0);
					    
					    if(pageNumber!=-1) break;
					}
						
					// Only print the requested number of lines
					if (linesToRead > lines.size())
					    linesToRead = lines.size();
					for (int i = linesToRead - 1; i >= 0; --i)
						out.println(lines.get(i)+"<br>");
			}
		    catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		    catch(IOException ioexception){
		    	throw new ServletException(ioexception.getMessage());
		    }
		    finally {
		         if(reader != null)
		        	 reader.close();
		    }
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
