/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

import Database.DBconnect;
import HttpDownloadUtility.HttpDownloadUtility;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author amit
 */
public class schedulerThread extends Thread {
  
    DBconnect dbconnection;
     String start_h,start_m,start_am_pm;
    String stop_h,stop_m,stop_am_pm;
    String once_d,once_m,once_day_no,once_y;
    String daily_day="x",once_daily,queue_no;
    
    JTable fileTable;String saveDir;
    int q=0;
    public schedulerThread(String saveDir,JTable fileTable) throws SQLException {
        dbconnection=new DBconnect();
        this.saveDir=saveDir;
        this.fileTable=fileTable;
    getvaluefromdb();           //get value from db after some time interval
    }
    void getvaluefromdb() throws SQLException
    {
        dbconnection.rs=dbconnection.st.executeQuery("Select value_item from scheduler");
        if(dbconnection.rs.next())
        {
            start_h=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            start_m=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            start_am_pm=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            once_d=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            once_m=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            once_day_no=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            once_y=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            daily_day=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            once_daily=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            stop_h=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            stop_h=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            stop_am_pm=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            queue_no=dbconnection.rs.getString("value_item");
            dbconnection.rs.next();
            if(!queue_no.equals(""))
            q=(int)Integer.parseInt(queue_no);
            
        }
    }
    String getmonth_to_int(String month)
    {
       // System.out.println("month="+month);
        if(month.equalsIgnoreCase("MONDAY")) return "2";
       else if(month.equalsIgnoreCase("TUESDAY")) return "3";
        
       else if(month.equalsIgnoreCase("WEDNESDAY")) return "4";
       else if(month.equalsIgnoreCase("THURSDAY")) return "5";
       else if(month.equalsIgnoreCase("FRIDAY")) return "6";
       else if(month.equalsIgnoreCase("SATURDAY")) return "7";  
       else if(month.equalsIgnoreCase("SUNDAY")) return "1";
        
        return "-1";
    }
    public void run()
    {
        
        while(true)
        {
            
            
        Calendar cal=Calendar.getInstance();
        String h=String.valueOf(cal.get(Calendar.HOUR));
       String m=String.valueOf(cal.get(Calendar.MINUTE));
       
       String am_pm="";
       if(cal.get(Calendar.AM_PM)==Calendar.PM&&(start_am_pm.equals("0")))
           am_pm+="0";
       else if(cal.get(Calendar.AM_PM)==Calendar.AM&&(start_am_pm.equals("1")))
           am_pm+="1";
           
       
        
        String day_num=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month=String.valueOf(cal.get(Calendar.MONTH));
        String year=String.valueOf(cal.get(Calendar.YEAR));
        String day=String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
       
        
            System.out.println("min="+m+"h="+h+"am_pm="+am_pm +"start_am_pm="+start_am_pm);
        
        Boolean go=false;
        if(start_h.equals(h)&&(start_m.equals(m))&&(start_am_pm.equals(am_pm)))
        {
            System.out.println("entered in hour once dialy="+once_daily);
            
            if(once_daily.equals("1"))
            {
                System.out.println("nside daily_day: day_num="+day_num+"month="+month+"day="+day+"year="+year);
                
                if(once_day_no.equals(day_num)&&(once_m.equals(month))&&(once_y.equals(year)&&(once_d.equals(day))))
                {
                        go=true;
                }
            }
            else 
            {
                String str;
                
                for(int i=1;i<daily_day.length();i++)
                {
                    str="";
                    System.out.println("i="+i);
                    while(daily_day.charAt(i)!='x'&&i<daily_day.length())
                    {str+=daily_day.charAt(i);
                    i++;
                    }
                    
                    System.out.println("String str="+str+"day="+day+"gt="+getmonth_to_int(str));
                    if(getmonth_to_int(str).equals(day))
                    {
                        
                        System.out.println("go");
                        go=true;break;
                    }
                }
                
            }
        }
            if(go)
                {
                     int x=JOptionPane.showConfirmDialog(fileTable,"Scheduler invoked:Do you want to start downloading now?");
                if(x==0)
                {
            try {
                
                dbconnection.rs=dbconnection.st.executeQuery("Select url,path from filedetails where file_status=\"Queues\"");
                System.out.println("q="+q);
                boolean ispresenttodownloadsomethingfromqueue=false;
                for(int i=0;i<q&&(i<5);i++)
                {
                    
                    if(dbconnection.rs.next())
                    {
                        ispresenttodownloadsomethingfromqueue=true;
                        System.out.println(dbconnection.rs.getString("url"));
                        
                          System.out.println(dbconnection.rs.getString("path"));
                        new HttpDownloadUtility(dbconnection.rs.getString("url"), 
                                dbconnection.rs.getString("path"), fileTable,1).start();
                        
                    }
                    
                }
                if(!ispresenttodownloadsomethingfromqueue)
                    JOptionPane.showMessageDialog(fileTable, "Scheduler Says:No file to download present in Queues!!!");
                
            } catch (SQLException ex) {
                Logger.getLogger(schedulerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(schedulerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(schedulerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
        
            try {
                Thread.sleep(950);
                getvaluefromdb();           //added at last day
            } catch (InterruptedException ex) {
                Logger.getLogger(schedulerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(schedulerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
}
