 package HttpDownloadUtility;


 import DownloadStatus.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amit
 */
public class Downloader extends Thread
{
    DownloadStatus.progressVariables pvObj;
    String fileURL,saveDir;
    int start,end,a;
    String fileName ;
    
    
    
    Downloader(String fileURL,String saveDir,int start,int end,int a,DownloadStatus.progressVariables pvObj,String fileName)
    {
         this.fileURL=fileURL;
         this.saveDir=saveDir;
         this.start=start;
         this.end=end;
         this.a=a;
         this.fileName=fileName;
         this.pvObj=pvObj;
    }
    
    public void run()
    {
        try{
        new proxyAuthentication();
    
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	
        
         String saveFilePath = saveDir + File.separator + fileName+a;
         int startonce=start;
         File of=new File(saveFilePath);
                       if(of.exists())
                           {
                               System.out.println("length="+of.length()+" en-start="+(end-start));
                             start+=of.length();
                                     
                           }
                         FileOutputStream outputStream= new FileOutputStream(of,true);;
                           
                           
                           int total_content=end-startonce;
                           
                           pvObj.setVal[a]=(((start-startonce)*100)/total_content);
        //Very tough to get aware of  the statement below
        //Re-discovered by Prashant
        httpConn.setRequestProperty("Range","bytes="+start+"-"+end);
        try{httpConn.connect();}
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error while Downloading");
        }
        int responseCode = httpConn.getResponseCode();  //Response code=206    
      
		if (responseCode/200==1)
                {
			
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();

                    
                    
			System.out.println("For thread :"+a+" Content-Type = " + contentType);
                        
	//		System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);
                        
                        
                        InputStream remoteStream = httpConn.getInputStream();
                        
                      
                        int down=end-start;
                        byte[] temp = new byte[down];
                        long downloded=0;
                        int percentage=0;
                        while(down>0&&pvObj.interrupt!=1)
                        {
                         //  System.out.println("fanss");
                        pvObj.setVal[a]=percentage;
                        int  bytes= remoteStream.read(temp, 0,down);
                        downloded+=bytes;
                       percentage=(int) ((downloded)*100/contentLength);
                        down-=bytes;
     //   System.out.print("chunksize="+down+'\n');
    
                    if(bytes==-1)
                        {
                        pvObj.setVal[a]=100;               
                        System.out.println("Downloaded last chunk : Terminating ");
                        break;
                        }
                        
   // System.out.println("bytes:-"+bytes);
   
                        outputStream.write(temp,0,bytes);
    
   // System.out.println("temp length="+temp.length);
  //  temp=null;
    
                     }//End of while loop
                     
                        outputStream.close();
                        
                   }
                if(pvObj.interrupt!=1)
                        pvObj.setVal[a]=100;
                        System.out.println(" thread Completed:"+a+"\n");
                        
         }//Try statement closing here
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
}
