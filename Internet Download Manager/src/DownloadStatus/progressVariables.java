/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadStatus;

import javax.swing.JProgressBar;

/**
 *
 * @author amit
 */


public class progressVariables 
{
    public JProgressBar progressBar[];
  public  JProgressBar globalProgressBar;
       public int setVal[];
       public int percentage;
        public int start_time,curr_time,curr_speed;
       public int timeRemaining;
       public int speed;
       public int interrupt,merge_complete,download_complete;
       public javax.swing.JLabel jb[][];

    public progressVariables() {
    interrupt=0;
    merge_complete=0;
        }
       
}
