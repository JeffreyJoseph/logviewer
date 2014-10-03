package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import bean.FilesBean;

public class Utilities {
	
	public static ArrayList<FilesBean> checkLogfiles(ArrayList<FilesBean> files, String prevday, String prevdate){
		ArrayList<FilesBean> output = new ArrayList<FilesBean>();
		Iterator<FilesBean> itrFiles = files.iterator();
		FilesBean tempFBean = null;	
		FilesBean outputFBean = null;	
		String file = null;
		String path = null;
		String prevdateTrim = prevdate.substring(2);
		//System.out.println("prevdateTrim "+prevdateTrim);
		
		/*DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String prev = dateFormat.format(date.getTime()-(24*3600000)).toString();*/
		
		while(itrFiles.hasNext()){
			tempFBean = itrFiles.next();
			file = tempFBean.getFileNames();
			path = tempFBean.getPath();
			
			if(new File( path, file ).exists() && (prevday.equals("false"))){
				outputFBean = new FilesBean();
				outputFBean.setFileNames(file);
				outputFBean.setPath(path);
				output.add(outputFBean);
			}
			else if(new File( path, file+"."+prevdateTrim+".log").exists() && (prevday.equals("true"))){
				outputFBean = new FilesBean();
				outputFBean.setFileNames(file+"."+prevdateTrim+".log");
				outputFBean.setPath(path);
				output.add(outputFBean);
			}
			else if(new File( path, file+"."+prevdate).exists() && (prevday.equals("true"))){
				outputFBean = new FilesBean();
				outputFBean.setFileNames(file+"."+prevdate);
				outputFBean.setPath(path);
				output.add(outputFBean);
			}
		}
		
		/*Iterator<FilesBean> itr = output.iterator();
		while(itr.hasNext()){
			tempFBean = itr.next();
			file = tempFBean.getFileNames();
			path = tempFBean.getPath();
			System.out.println("FILE "+file);
			System.out.println("PATH "+path);
			
		}*/
		
		return output;
	}
	
	public static int averageLineLength(String filename) throws IOException{
		FileInputStream fis = null;
		BufferedReader reader = null;
		int loop = 0, sum = 0, average = 0;
		
		try{
			fis = new FileInputStream(filename);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String newLine = reader.readLine();			
			while(newLine!=null && loop<500){
				//System.out.println("Line length = "+newLine.getBytes().length);
				sum += newLine.getBytes().length;
				newLine = reader.readLine();
				loop++;
			}
			average = sum/loop;
			return average;
		}
		catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}
	    catch(IOException ioe){
	    	ioe.printStackTrace();
	    }
		finally{
			if(reader!=null)
				reader.close();
		}
		return average;
	}
	
}
