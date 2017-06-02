/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadStatus;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author amit
 */


public class timerThread extends Thread
{
    int start_time=0,curr_time=1;
    progressVariables pvObj;
    int contentLength;
    JLabel timeRemainingLabel,speedLabel;
    public timerThread(progressVariables pvObj,int contentLength,JLabel speedLabel,JLabel timeRemainingLabel)
    {
        this.pvObj=pvObj;
        this.contentLength=contentLength;
        this.speedLabel=speedLabel;
        this.timeRemainingLabel=timeRemainingLabel;
    }
    public int getStartTime()
    {
        return start_time;
    }
    public int getCurrTime()
    {
        return curr_time;
    }
    
    public void run()
    {
        while(true)
        {
            
            try
            {
                int pre=pvObj.globalProgressBar.getValue();
                Thread.sleep(1000);
                int after=pvObj.globalProgressBar.getValue();
                long speed=0;
                if(after!=pre)
                {
                    long y=contentLength/100;
                    speed=(y*(after-pre));
                }
                    
                double temp=(double)speed;
                int state=1;
                double prev=(double)speed;
                while(temp>=1024)
                {
                    prev=temp;
                    temp/=1024;
                    state++;
                }
                prev=temp;
                    
               DecimalFormat df = new DecimalFormat("#.00");
                if(state==1)
                    speedLabel.setText(df.format(prev)+" Bps");
                else if(state==2)
                    speedLabel.setText(df.format(prev)+" KBps");
                else if(state==3)
                    speedLabel.setText(df.format(prev)+" MBps");
                else if(state==4)
                    speedLabel.setText(df.format(prev)+" GBps");
            
                
                int remainPer=100-pvObj.globalProgressBar.getValue();
                double time=0;
                
                if(speed!=0)
                {
                    double x=contentLength/speed;
                    time=(x*remainPer/100);
                //    String str=" len: "+contentLength+" reper: "+remainPer+" sp: "+speed;
               //     timeRemainingLabel.setText(String.valueOf(time)+" "+String.valueOf(x));
                    
                    temp=(double)time;
                    state=1;
                    prev=(double)time;
                    while(temp>=60)
                    {
                        prev=temp;
                        temp/=60;
                        state++;
                    }
                    prev=temp;
      
                    df = new DecimalFormat("#.00");
                    if(state==1)
                        timeRemainingLabel.setText(df.format(prev)+"seconds");
                    else if(state==2)
                        timeRemainingLabel.setText(df.format(prev)+"minutes");
                    else
                        timeRemainingLabel.setText(df.format(prev)+" hours");
                            
  //                else if(state==4)
      //              speedLabel.setText(df.format(prev)+" GBps");
                }
                else
                {
                    timeRemainingLabel.setText("Unknown time");
                }
                    
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(timerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            curr_time++;
        }
    }
}
