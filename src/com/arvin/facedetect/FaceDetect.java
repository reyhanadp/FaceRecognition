/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arvin.facedetect;

import java.awt.Color;
import java.awt.Desktop;
import static java.awt.Frame.NORMAL;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author Arvind Mahto
 * @Email mahto.arvin@gmail.com
 */
public class FaceDetect extends javax.swing.JFrame {

    private thread thread = null;
    VideoCapture cap = null;
    Mat frame = new Mat();
    MatOfRect faces = new MatOfRect();
    MatOfRect eyes = new MatOfRect();
    MatOfByte mob = new MatOfByte();
    String fileExten = ".png";
    CascadeClassifier faceDetect = new CascadeClassifier("haarcascade_frontalface_alt.xml");
    CascadeClassifier eyeDetect = new CascadeClassifier("haarcascade_eye.xml");
    int indeks = 0;
    int ketemu = 1;
    String nama;

    // VideoCapture cap;
    // public BufferedImage buff;
    Mat matrix = new Mat();
    Mat cropImage = new Mat();

    class thread implements Runnable {
        
        protected volatile boolean runnable = true;

        @Override
        public void run() {
            String path = null;
            try {
                path = new File(".").getCanonicalPath();
            } catch (IOException ex) {
                Logger.getLogger(FaceDetect.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (runnable) {
                if (cap.isOpened()) {
                    try {
                        cap.retrieve(matrix);
                        Graphics g = jPanel1.getGraphics();
                        faceDetect.detectMultiScale(matrix, faces);
                        eyeDetect.detectMultiScale(matrix, eyes);
//                        System.out.println(String.format("Total faces Detected: %d", faces.toArray().length));
                        String text = String.format("Total wajah terdeteksi : %d", faces.toArray().length);

                        Rect rect_Crop = null;
                        Imgproc.putText(matrix, text, new Point(15, 25
                        ), NORMAL, 0.7, new Scalar(255, 255, 255), 2);
//                         Imgproc.putText(matrix, "29/01/2018", new Point(15, 40
//                         ), Core.FONT_HERSHEY_COMPLEX_SMALL, 0.5, new Scalar(0, 255, 0));
                        for (Rect rect : faces.toArray()) {
                            //simpan gambar
                            if (indeks == 1) {

                                while (ketemu == 1) {
                                    File file = new File(path + "/dataWajah/" + nama + "_"+ indeks + ".jpg");
                                    Desktop desktop = Desktop.getDesktop();
                                    if (!file.exists()) {
                                        ketemu = 0;
                                    }else{
                                        indeks = indeks +1;
                                    }
                                }

                                rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
                                Mat image_roi = new Mat(matrix, rect_Crop);
                                Imgcodecs.imwrite(path + "/dataWajah/" + nama + "_"+ indeks + ".jpg", image_roi);
                                JOptionPane.showMessageDialog(null, "Data wajah anda telah disimpan di dalam folder :"+path + "/dataWajah/" + nama + "_"+ indeks + ".jpg");
                                indeks = 0;
                            }

                            Imgproc.rectangle(matrix,
                                    new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                                    new Scalar(0, 255, 0), 5);

//                            Imgcodecs.imwrite(path + "/reyhan.jpg", matrix);
                            Imgproc.putText(matrix, String.format("%d x %d", rect.width, rect.height), new Point(rect.x + 30, rect.y + rect.height + 20
                            ), NORMAL, 0.5, new Scalar(0, 255, 0));
                            // if(rect.x)
                            Imgproc.putText(matrix, "Wajah terdeteksi", new Point(rect.x, rect.y - 10
                            ), NORMAL, 0.5, new Scalar(0, 255, 0));
                            String plus_minus = "\u00B1";
                            Imgproc.putText(matrix, String.format("%d kaki jarak dari kamera", (350 / ((rect.height)))), new Point(rect.x, rect.y + rect.height + 40
                            ), NORMAL, 0.5, new Scalar(0, 255, 0));
                            
                            
                            
                        }

//                        Mat image_roi = new Mat(matrix,rect_Crop);
//                        Imgcodecs.imwrite(path+"/reyhan.jpg",image_roi);
//                          works but not good!
//                            BufferedImage out; 
//                            byte[] data = new byte[320*240 * (int)matrix.elemSize()];
//                            int type;
//                            matrix.get(0, 0,data);
//                            
//                            if(matrix.channels() == 1){
//                                type = BufferedImage.TYPE_BYTE_GRAY;
//                            }
//                            else{
//                                type = BufferedImage.TYPE_3BYTE_BGR;
//                            }
//                            out = new BufferedImage(320,240,type);
//                            
//                            out.getRaster().setDataElements(0,0,320,240,data);
// BufferedImage buff = null ;
//                        if(matrix!=null){
//                            int cols = matrix.cols();
//                            int rows = matrix.rows();
//                            int elemSize = (int) matrix.elemSize();
//                            byte[] data = new byte[cols*rows*elemSize];
//                            int type = 0;
//                            matrix.get(0,0,data);
//                            switch(matrix.channels()){
//                                case 1:
//                                    type = BufferedImage.TYPE_BYTE_GRAY;
//                                    break;
//                                case 3:
//                                    type = BufferedImage.TYPE_3BYTE_BGR;
//                                    //bgr to rgb
//                                    byte b;
//                                    for(int i = 0;i<data.length;i+=3)
//                                    {
//                                        b= data[i];
//                                        data[i]=data[i+2];
//                                        data[i+2]=b;
//                                    }
//                                    break;
//                                default:
//                                    ;
//                            }
//                            
//                            //Reuse 
//                            
//                            if(buff==null || buff.getWidth()!=cols || buff.getHeight()!=rows || buff.getType()!=type){
//                                buff = new BufferedImage(cols,rows,type);
//                            }
//                            buff.getRaster().setDataElements(0, 0, data);
//                                
//                        }
//                        else{
//                            buff = null;
//                          
//                        }
                        Imgcodecs.imencode(fileExten, matrix, mob);
                        byte[] byteArray = mob.toArray();
                        BufferedImage buff = null;

                        InputStream in;
                        in = new ByteArrayInputStream(byteArray);
                        buff = ImageIO.read(in);
//
                        g.drawImage(buff, 0, 0, null);

                    } catch (IOException ex) {
                        Logger.getLogger(FaceDetect.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }

    }

    /**
     * Creates new form NewJFrame
     */
    public FaceDetect() {
        
        initComponents();
        setLocationRelativeTo(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fieldNama = new javax.swing.JTextField();
        simpan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        start = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jPanel1.setName("FaceDetect "); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        simpan.setText("Simpan");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama");

        start.setText("Start");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(275, 275, 275))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(start)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(simpan)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        // TODO add your handling code here:
        this.indeks = 1;
        this.nama = fieldNama.getText();
        this.ketemu = 1;
    }//GEN-LAST:event_simpanActionPerformed

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // TODO add your handling code here:
        if(start.getText().compareTo("Start")==0){
            start();
            start.setText("Stop");
            simpan.setEnabled(true);
        }else{
            stop();
            simpan.setEnabled(false);
            start.setText("Start");
        }
        
    }//GEN-LAST:event_startActionPerformed
    
    private void start() {

        cap = new VideoCapture(0);
        thread = new thread();
        Thread t = new Thread(thread);
        t.setDaemon(true);
        thread.runnable = true;
        t.start();

    }
    
    private void stop() {

        thread.runnable = false;
        cap.release();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Library loaded");
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FaceDetect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FaceDetect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FaceDetect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FaceDetect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FaceDetect frame = new FaceDetect();
                frame.setTitle("FaceDetect");

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                /*frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        thread
                    }
                    
                });*/
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldNama;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton simpan;
    private javax.swing.JButton start;
    // End of variables declaration//GEN-END:variables
}
