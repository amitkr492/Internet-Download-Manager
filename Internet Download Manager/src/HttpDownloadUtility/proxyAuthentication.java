/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpDownloadUtility;

import Database.DBconnect;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author amit
 */
public class proxyAuthentication 
{
    DBconnect dbconnection;
    public String proxyAddr,port,username,password;
     Authenticator authenticator;
     public int defaultVal=0;
    public proxyAuthentication() throws SQLException 
    { 
        dbconnection=new DBconnect();
        getdetails();           //Get the proxy configuration from database 
        if(proxyAddr!=null)     //if the proxy is not set then dont authenticate 
        authenticate();
        
        
    }
    
    public void authenticate()
    {
        this.authenticator = new Authenticator() 
        {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password.toCharArray());
                // return (new PasswordAuthentication("edcguest","edcguest".toCharArray()));
            }
        };
        
        
        Authenticator.setDefault(authenticator);
	Properties systemProperties = System.getProperties();
        
        
	systemProperties.setProperty("http.proxyHost",proxyAddr);
	systemProperties.setProperty("http.proxyPort",port);
	systemProperties.setProperty("https.proxyHost",proxyAddr);
	systemProperties.setProperty("https.proxyPort",port);
    }
    
    public void getdetails() throws SQLException
    {
        
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from add_details where key_item=\"proxy_server\"");
        if(dbconnection.rs.next())
            proxyAddr=dbconnection.rs.getString("value_item");
        
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from add_details where key_item=\"proxy_port\"");
        if(dbconnection.rs.next())
            port=dbconnection.rs.getString("value_item");
        
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from add_details where key_item=\"proxy_uname\"");
        if(dbconnection.rs.next())
            username=dbconnection.rs.getString("value_item");
        
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from add_details where key_item=\"proxy_pass\"");
        if(dbconnection.rs.next())
            password=dbconnection.rs.getString("value_item");
        
    }
}