/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpDownloadUtility;

import java.util.ArrayList;

/**
 *
 * @author amit
 */
public class getCategory {
    
    String fileName;
    public getCategory(String fileName)
    {
        this.fileName=fileName;
    }
    public String getfiletype()
    {
        
        String extension;
        extension=fileName.substring(fileName.lastIndexOf(".")+1);
        if(extension.equals("zip")||extension.equals("rar"))
            return "Compressed";
        else if(extension.equals("pdf")||extension.equals("txt")||extension.equals("docx")||extension.equals("doc")
                ||extension.equals("ppt")||extension.equals("pptx"))
            return "Documents";
        else if(extension.equals("mp3")||extension.equals("wav"))
            return "Music";
        else if(extension.equals("exe")||extension.equals("msi"))
            return "Programs";
        else if(extension.equals("mp4")||extension.equals("mpg")||extension.equals("mpeg")||extension.equals("avi")||extension.equals("flv")||
                extension.equals("wmv")||extension.equals("mov")||extension.equals("mkv"))
            return "Video";
        else
            return "General";
    }
    
    public ArrayList<String> getextension(String cat)
    {
        cat=fileName;
        ArrayList<String> ext=new ArrayList<String>();
        if(cat=="Compressed")
        {
            ext.add("zip");ext.add("rar");
        }
        else if(cat=="Documents")
        {
            ext.add("pdf");ext.add("txt");ext.add("docx");ext.add("doc");ext.add("pptx");ext.add("ppt");
        }
        else if(cat=="Music")
        {
            ext.add("mp3");ext.add("wav");
        }
        else if(cat=="Programs")
        {
            ext.add("msi");ext.add("exe");
        }
        else if(cat=="Video")
        {
            ext.add("mp4");ext.add("mpg");ext.add("mpeg");ext.add("mov");ext.add("mkv");ext.add("avi");ext.add("flv");
            ext.add("wmv");
        }
        else
            ext.add("File Types not listed any category");
        return ext;
    }
}
