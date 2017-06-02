/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpDownloadUtility;

import java.io.*;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RootPaneContainer;
/**
 *
 * @author amit
 */
public class HttpDownloadUtility extends Thread 
{
    
   
   /* Two arraylist one for storing the filename and one for storing file class variable*/ 
    ArrayList<String> fileno=new ArrayList<String>();
    ArrayList<File> filelist=new ArrayList<File>();
    String fileURL,saveDir;
  
     JTable fileTable;
     int flag;
    public HttpDownloadUtility(String fileURL, String saveDir, JTable fileTable,int flag) throws Exception{
    this.fileTable=fileTable;
        this.flag=flag;
        this.fileURL=fileURL;
        this.saveDir=saveDir;
        
    }
    
    
    
    public void run() 
    {
        try{
            downloadFile();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
   
    
    public  void downloadFile()throws IOException, InterruptedException, SQLException 
        {
          
         new proxyAuthentication();
            
            /*New connection starts from here*/
	URL url = new URL(fileURL);
	HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      
     //   System.out.println("timeout="+httpConn.getConnectTimeout());
        
        //Checking resume capability
       String resumecapablity=httpConn.getHeaderField("Accept-Ranges");
       
       boolean isresumeable=false;
    //   System.out.println(resumecapablity);
        if((resumecapablity!=null)&&resumecapablity.equals("bytes"))
        {
            isresumeable=true;
        }
       try{httpConn.connect();}
       catch(Exception e)
       {
           JOptionPane.showMessageDialog(fileTable, "Error while connecting...\nCannot Download...");
           this.suspend();
       }
	int responseCode = httpConn.getResponseCode();
                  
	if (responseCode == HttpURLConnection.HTTP_OK)
               {
		String fileName = "";
		String disposition = httpConn.getHeaderField("Content-Disposition");
		String contentType = httpConn.getContentType();
		int contentLength = httpConn.getContentLength();

		if (disposition != null)
                   {
                    int index = disposition.indexOf("filename=");   //Checking filename in Content-Disposition header
                    if (index > 0) 
                       {
			fileName = disposition.substring(index + 10,disposition.length() - 1);
			}
                    
                    }
                else
                    {
                        fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,fileURL.length());
                    }
                /*Output details of the connection should be removed soon*/
                System.out.println("Content-Type = " + contentType);
		System.out.println("Content-Disposition = " + disposition);
		System.out.println("Content-Length = " + contentLength);
		System.out.println("fileName = " + fileName);
                     
                
                /*Connected and now need to show the info of the file to be downloaded*/
                
              // System.out.println(URLConnection.guessContentTypeFromName(fileName));
                   
             //      System.out.println("savedir hyyp"+saveDir);
                
                new Thread(new ShowDownloadInfo(contentLength,fileName,fileURL,saveDir,isresumeable,fileTable,flag)).start();
                
                
                /*Continue after confirming from user*/
                
                
              
		}
                else
                {
                    JOptionPane.showMessageDialog(null,"No file to download\nServer replied HTTP code:\n"+responseCode);
			//System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                            //A message should be shown in form of GUI 
                            //to be implemented
		}
		httpConn.disconnect();
        }
}
