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
 * Servlet implementation class FetchLogfileContent
 */
@WebServlet("/FetchLogfileContent")
public class FetchLogfileContent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchLogfileContent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String readFile = request.getParameter("file");
		int linesToRead = 2000;							/* Default number of lines read */
	    
		RandomAccessFile raf = null;
	    PrintWriter out = response.getWriter();
		List<String> lines = new ArrayList<String>();
	    
	    //outputBuf.append("<html><body>");
	    
	    try{
			response.setContentType("text/html");
			
			File file = new File(readFile);
			raf = new RandomAccessFile(file,"r");
				
			final int chunkSize = 1024 * 32;
			long startPoint = 0;
			long end = 0;
			int totalPages = 0;
			end = raf.length();
			boolean readMore = true;
			
			if (end <= 0) {								/* When log file is present but empty */
				out.println("<!--pages:"+ totalPages +";-->");
				out.println("The log file is empty");
			}
			else {
				int avgLineLength = Utilities.averageLineLength(readFile);
				int onePage = avgLineLength * 2600;
				totalPages = (int)(end/onePage);
				if (totalPages<1) totalPages = 1;
				else if (totalPages>=1 && (end%onePage)!=0) totalPages++;
				
				out.println("<!--pages:"+ totalPages +";-->");
				
				while (readMore) {
				    byte[] buf = new byte[chunkSize];
				    
				    // Read a chunk from the end of the file
				    startPoint = end - chunkSize;
				    long readLen = chunkSize;
				    //System.out.println("startPoint = "+startPoint+"\treadLen = "+readLen);
				    if (startPoint < 0) {
				        readLen = chunkSize + startPoint;
				        startPoint = 0;
				    }
				    
				    raf.seek(startPoint);
				    readLen = raf.read(buf, 0, (int)readLen);
				    if (readLen <= 0) 
				        break;			    
					
				    // Parse newlines and add them to an array
				    int unparsedSize = (int)readLen;
				    int index = unparsedSize - 1;
				    //System.out.println("unparsedSize = "+unparsedSize+"\tindex = "+index);
				    while (index >= 0) {
				        if (buf[index] == '\n') {
				            int startOfLine = index + 1;
				            int len = (unparsedSize - startOfLine);
				            if (len > 0) {
				                lines.add(new String(buf, startOfLine, len));
				            }
				            unparsedSize = index + 1;
				            //System.out.println("index 2 = "+index+"\tunparsedSize 2 = "+unparsedSize);
				        }
				        index -= 1;
				    }
				    //System.out.println("unparsedSize 3 = "+unparsedSize);
					
				    // Move end point back by the number of lines we parsed
				    // Note: We have not parsed the first line in the chunked
				    // content because could be a partial line
				    end = end - (chunkSize - unparsedSize);
				    //System.out.println("End 2 = "+end);
					
				    readMore = (lines.size() < linesToRead) && (startPoint != 0);
				}
					
				// Only print the requested number of lines
				if (linesToRead > lines.size())
				    linesToRead = lines.size();
				//System.out.println("Lines size = "+lines.size());
				for (int i = linesToRead - 1; i >= 0; --i)
					out.println(lines.get(i)+"<br>");	
				
				//outputBuf.append("</body></html>");
					
				//request.setAttribute("resultdata", outputBuf.toString());
				
			    //request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
	    }
	    catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    catch(IOException ioexception){
	    	throw new ServletException(ioexception.getMessage());
	    }
	    finally {
	         if(raf != null)
	        	 raf.close();
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		// TODO Auto-generated method stub
	}

}
