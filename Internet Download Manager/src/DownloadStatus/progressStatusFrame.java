/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadStatus;

/**
 *
 * @author amit
 */
import Database.DBconnect;
import HttpDownloadUtility.*;
import java.awt.Color;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class progressStatusFrame extends javax.swing.JFrame implements Runnable {

     progressVariables pvObj;
     DBconnect dbconnection;
    int chunks,frame_width=705,frame_height=500;
    int contentLength,fileid;
    boolean isresumable;
    String fileName,fileURL,saveDir;
    /**
     * Creates new form progressStatusFrame
     */
    public String getfilesize(int contentLength)
    {
         double temp=(double)contentLength;
       int state=1;
       double prev=(double)contentLength;
       while(temp>=1024)
       {
           prev=temp;
           temp/=1024;
           state++;
       }
       prev=temp;
      
 DecimalFormat df = new DecimalFormat("#.00");
       
    if(state==1)
        return df.format(prev)+" B";
       else if(state==2)
             return df.format(prev)+" KB";
       else if(state==3)
             return df.format(prev)+" MB";
       else if(state==4)
             return df.format(prev)+" GB";
         return null;
    }
     JTable fileTable;
    public progressStatusFrame(int chunks,progressVariables pvObj,int contentLength,String fileName,String fileURL,
            String saveDir,boolean isresumable, JTable fileTable) throws Exception {
         this.chunks=chunks;
         
        this.pvObj=pvObj;
        this.fileTable=fileTable;
        pvObj.progressBar=new JProgressBar[chunks+1];
        pvObj.jb=new JLabel[chunks+1][3];
        this.contentLength=contentLength;
     this.isresumable=isresumable;
        this.fileName=fileName;
        this.fileURL=fileURL;
        this.saveDir=saveDir;
        
       
        dbconnection=new DBconnect();
        initComponents();
          String res=getfilesize(contentLength);
  

    if(res!=null)
    
        jLabel6.setText(res);
        if(isresumable)
            jLabel12.setText("Yes");
        else
        {
            jButton1.setEnabled(false);
            jLabel12.setText("No");
        }
        
        
    generateprogressbar();
    if(!isresumable)
    jButton1.setEnabled(false);
    
    jLabel2.setText(fileURL+File.separator+fileName);
        this.setVisible(true);
    }
    
 public void generateprogressbar() throws Exception
    {
     //   pvObj=new progressVariables();
    //    pvObj.progressBar[]=new JprogressBar[chunks];
        int width= ((frame_width)/chunks);
        int height=24,x=3;
        int globalpr=width*chunks;
        
        
        for(int i=0;i<chunks;i++)
        {
            pvObj.progressBar[i]=new JProgressBar(0,100);
            pvObj.progressBar[i].setBounds(x, 340, width,height);
        //    pvObj.progressBar[i].setStringPainted(true);
            pvObj.progressBar[i].setValue(0);
            pvObj.progressBar[i].setBackground(Color.lightGray);
            add(pvObj.progressBar[i]);
            x+=width;
          
            JPanel jp=new JPanel();
            for(int j=0;j<3;j++)
            {
            pvObj.jb[i][j]=new JLabel();
            pvObj.jb[i][j].setText("nothing");
            jp.add(pvObj.jb[i][j]);
            
            }
            jp.setBounds(0,450,500,20);
            jp.setVisible(true);
            jList1.add(jp).setVisible(true);
            
        }
        pvObj.globalProgressBar=new JProgressBar(0,100);
        pvObj.globalProgressBar.setBounds(5, 250,globalpr,height);
        pvObj.globalProgressBar.setStringPainted(true);
        pvObj.globalProgressBar.setValue(0);
        pvObj.globalProgressBar.setBackground(Color.lightGray);
        this.add(pvObj.globalProgressBar);
      //  this.setSize(frame_width,frame_height);
        


//setLayout(null);
    }
    
    public void update() throws InterruptedException, SQLException
    {
        int percentage=0,mark=1;
        timerThread thObj=new timerThread(pvObj,contentLength,jLabel10,jLabel17);
        new Thread(thObj).start();
        pvObj.start_time=thObj.getStartTime();
        pvObj.curr_time=thObj.getCurrTime();
        while(mark>0)
        {
            mark=0;
            percentage=0;
            
        for(int i=0;i<chunks;i++)
        {
            percentage+=pvObj.setVal[i];
            if(pvObj.setVal[i]<100)
                mark=1;
            pvObj.progressBar[i].setValue(pvObj.setVal[i]);
        }
             percentage/=chunks;
            
             pvObj.globalProgressBar.setValue(percentage);
             Thread.sleep(500);
             
        }
        pvObj.globalProgressBar.setValue(100);

        int rcount=fileTable.getRowCount();
         if(rcount>0)
             rcount--;
        fileTable.setValueAt("Downloaded",rcount,4);
         String stamp=currentstamp();
         
         fileTable.setValueAt(stamp,rcount, 5);
         if(fileTable.getSelectedRow()<0)
              fileid=(int) fileTable.getValueAt(rcount, 0);
         else
         fileid=(int) fileTable.getValueAt(fileTable.getSelectedRow(), 0);
         int st=dbconnection.st.executeUpdate("Update filedetails set file_status=\"Downloaded\" where id="+fileid);
         
         System.out.println("merge_omplete progreestatufroam"+pvObj.merge_complete);
         jLabel4.setText("Please wait whle the window gets closed automatically");
         jLabel4.setForeground(Color.red);
         
         while(true)
         {
             if(pvObj.download_complete==1)
             {
                 jButton1.setEnabled(false);
                 jButton2.setEnabled(false);
             }
             if(pvObj.merge_complete==1)
             {
                 jLabel4.setText("Completed");
                 this.dispose();
                 JOptionPane.showMessageDialog(rootPane, "Your File "+fileName+" Downloaded Successfully to "+saveDir+"!!!");
                 break;
             }
             
             Thread.sleep(1000);
             
         }
         
         
      //   System.out.println("fileid="+fileid+" ");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(fileName);
        setBounds(new java.awt.Rectangle(400, 100, 720, 500));
        setResizable(false);

        jButton1.setText("Pause");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("URL");

        jLabel2.setText("Unknown");

        jLabel3.setText("Status");

        jLabel4.setText("Running");

        jLabel5.setText("File Size");

        jLabel6.setText("Unknown");

        jLabel7.setText("Downloaded");

        jLabel8.setText("Unknown");

        jLabel9.setText("Rate");

        jLabel10.setText("Unknown");

        jLabel11.setText("Resume Capability");

        jLabel12.setText("Unknown");

        jScrollPane1.setViewportView(jList1);

        jLabel13.setText("S.No.");
        jLabel13.setMaximumSize(new java.awt.Dimension(40, 19));
        jLabel13.setMinimumSize(new java.awt.Dimension(40, 19));
        jLabel13.setPreferredSize(new java.awt.Dimension(40, 19));

        jLabel14.setText("Downloaded");

        jLabel15.setText("Status");

        jLabel16.setText("Time Remaining");

        jLabel17.setText("Unknown");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("          Real-Time Download Statistics");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1))
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 34, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator4)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7))))
                    .addComponent(jSeparator3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)
                                .addComponent(jLabel15))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(196, 196, 196))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        pvObj.interrupt=1;
        
        
        
         
        
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public String currentstamp()
    {
        java.util.Date dt=new java.util.Date();
       SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return ft.format(dt).toString();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.out.println("Pause\n\n");
        String tex=jButton1.getText();
        int rcount=0;
        if(tex=="Pause")
        {
            jLabel4.setText("Paused");
            jButton1.setText("Resume");
       pvObj.interrupt=1;
       rcount=fileTable.getRowCount();
         if(rcount>0)
             rcount--;
        fileTable.setValueAt("Queues",rcount, 4);
        fileTable.setValueAt(currentstamp(),rcount, 5);
        
        
        }else
        {
            jLabel4.setText("Running");
            jButton1.setText("Pause");
            
            Thread t;
            try {
                t = new ChunkDivider(contentLength,fileName,fileURL,saveDir,pvObj,isresumable,fileTable,0);
                t.start();
            } catch (SQLException ex) {
                Logger.getLogger(progressStatusFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        
       pvObj.interrupt=0;
       rcount=fileTable.getRowCount();
         if(rcount>0)
             rcount--;
       fileTable.setValueAt("Running",rcount, 4);
       fileTable.setValueAt(currentstamp(),rcount, 5);
        
        }
        
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    @Override
    public void run() {
         try {   
             update();
         } catch (InterruptedException ex) {
             Logger.getLogger(progressStatusFrame.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(progressStatusFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables

}
