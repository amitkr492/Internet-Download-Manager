/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.*;

/**
 *
 * @author amit
 */
public class DBconnect {
               
    public Connection con=null;
    public Statement st=null;
    public ResultSet rs=null;
    
    public DBconnect() throws SQLException
    {	
		String url="jdbc:mysql://localhost:3306/dmanager";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection(url,"root","");	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		st=con.createStatement();  
                System.out.println("Connection Established\n");
    }
    
}
