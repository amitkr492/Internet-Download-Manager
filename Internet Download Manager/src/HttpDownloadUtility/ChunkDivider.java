/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpDownloadUtility;


import Database.DBconnect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import DownloadStatus.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
/**
 *
 * @author amit
 */
public class ChunkDivider extends Thread
{
     public DownloadStatus.progressVariables pvObj;
     
     int contentLength;
   ArrayList<String> fileno=new ArrayList<String>();
    ArrayList<File> filelist=new ArrayList<File>();
    String fileURL,saveDir,tempDir;
    DBconnect dbconnection;
    String fileName; 
    public int chunks;
    boolean isresumeable;
    int resume=0;
     JTable fileTable;
     int idd;
    public ChunkDivider(int contentLength,String fileName,String fileURL,String saveDir,DownloadStatus.progressVariables pvObj,
            boolean isresumable, JTable fileTable,int idd) throws SQLException
    {
        this.idd=idd;
        this.fileTable=fileTable;
          this.contentLength=contentLength;
        this.fileName=fileName;
        this.fileURL=fileURL;
        this.saveDir=saveDir;
        this.isresumeable=isresumable;
        dbconnection=new DBconnect();
        gettempdir();
        if(pvObj!=null)         //If already allocated  for ongoing download 
                            //Possible only in the case of resume 
        {
       this.pvObj=pvObj;
        resume=1;
        }else
        {
            resume=0;
            System.out.println("allocated");
        this.pvObj=new  DownloadStatus.progressVariables();
       
        
    }}
    
    public void gettempdir() throws SQLException
    {
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from add_details where key_item=\"tempDir\"");
        if(dbconnection.rs.next())
        {
            tempDir=dbconnection.rs.getString("value_item");
        }
    }
    public void run()
    {
       
         try {
         //    System.out.println("saveDir "+saveDir+"\n\n\n");
             Downloadnow();
         } catch (InterruptedException ex) {
             Logger.getLogger(ChunkDivider.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(ChunkDivider.class.getName()).log(Level.SEVERE, null, ex);
         } catch (Exception ex) {
             Logger.getLogger(ChunkDivider.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
     public void Downloadnow() throws InterruptedException, IOException, Exception
    {
     
                
                int bytesize=contentLength/1024;    //size of file in byte
                int new_chunk=1;                    //new chunk size intially 1 
                int mbsize=bytesize/1024;           //size of file in MB
                double logvalue=(Math.log(contentLength)/Math.log(2));  //logvalue for splitting strategy
                             
                                
                     if(logvalue>=21)
                        {
                         new_chunk=((int)logvalue-20);
                        }
              System.out.println("new_chunk="+new_chunk);
                                
              System.out.print("mbisze:"+mbsize);
        	// opens input stream from the HTTP connection
		
            //     InputStream remoteStream = httpConn.getInputStream();
                        
                 
			int CHUNK_SIZE;         //size of each chunk
                        chunks=new_chunk*2;     // no. of chunks is multiplied by 2
                       
                        CHUNK_SIZE = (int) Math.ceil((float)mbsize / (float)chunks);    
                        if(CHUNK_SIZE==0||(!isresumeable))       //if filesize is less than 1 MB
                        chunks=1;
                        System.out.println("resue"+resume);
                        if(resume==0)
                         pvObj.setVal=new int[chunks+1];
                        if(resume!=1)
                       new Thread(new progressStatusFrame(chunks,pvObj,contentLength,fileName,fileURL,saveDir,isresumeable,fileTable)).start();       //Calling progress bar from here
                        
                    
                        
                        System.out.print("chunk:-"+chunks);
			
                        
                int down=CHUNK_SIZE*1024*1024;      //size to be downloaded at once
                byte[] temp = new byte[down];       //temp array act as a buffer
              
                int st=0;                       //start position of segment
                Thread t[]=new Thread[50];      //Thread for downloading at max 32
               int percentage=0,val=0; 
               for(int a = 0; a <chunks; a++)
                    {
               /*         val=0;
                        for(int i=0;i<chunks;i++)
                        {
                            val+=pvObj.progressBar[i].getValue();
                        }
                      
                        percentage=(int)(val*100/chunks);
                        pvObj.globalProgressBar.setValue(percentage);
                */
                       String saveFilePath = tempDir + File.separator + fileName+a;    //each temp file is given diff name
                        fileno.add(saveFilePath);       
                        filelist.add(new File(saveFilePath));
                         //FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                        
                        System.out.print("From main class: Thread "+a+"\n");
                        
                        if(a!=chunks-1)     //if last chunk then download whole remaining file size
                            {
                            t[a]= new Downloader(fileURL,tempDir,down*a,down*(a+1),a,pvObj,fileName);
                            t[a].start();
                            } 
                         else
                            {
                                 t[a]=new Downloader(fileURL,tempDir,down*a,contentLength,a,pvObj,fileName);
                                 t[a].start();
        
                            }
                        
                    }
                   
               
                    
               //Joining all the threads here before merging further
                for(int a=0;a<chunks;a++)
                    {
                     //  if(!this.isInterrupted())
                     //  System.out.println("watiing for"+a+"\n");
                    t[a].join();
                       
                    }


		System.out.println("File downloaded");
                       pvObj.download_complete=1; 
                   
                        
//merging
                if(pvObj.interrupt!=1)
                {
                
            String dest=saveDir+ File.separator + fileName ;
            //Destination for final merged file

//new merge try

                    System.out.println("destination="+dest+"\n\n");
                    
                    
                File ofile=new File(dest);      //Name of final file
                if(ofile.exists())
                   ofile.delete();
                FileInputStream fis;            
                FileOutputStream fos = null;
                byte filebytes[];
                int bytesRead=0;

                try{
                    int i=0;
                    fos=new FileOutputStream(ofile,true);   //New File stream with append mode true
                    for(File fil: filelist) //foreach temporary file 
                        {
                         fis=new FileInputStream(fil);
                         filebytes = new byte[(int) fil.length()];
                         bytesRead = fis.read(filebytes, 0,(int)  fil.length());
                         fos.write(filebytes);
                         fos.flush();

	                filebytes = null;

	                fis.close();

	                fis = null;
                        Path path=Paths.get(fileno.get(i));
                        Files.delete(path);
                        i++;
                        }
                    
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                        fos.close();
                        fos=null;

                            fileno.clear();
                            filelist.clear();
                    System.out.println("Merge was executed successfully.!");
                    pvObj.merge_complete=1;
                //    JOptionPane.showMessageDialog(fileTable, "Completed merge");
                           

    }
   
    }
    
    
}
