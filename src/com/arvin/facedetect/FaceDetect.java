/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arvin.facedetect;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import static java.awt.Frame.NORMAL;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.Timer;
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

public class FaceDetect extends javax.swing.JFrame {

    private thread thread = null;
    VideoCapture cap = null;
    Mat frame = new Mat();
    MatOfRect faces = new MatOfRect();
    MatOfRect eyes = new MatOfRect();
    MatOfByte mob = new MatOfByte();
    String fileExten = ".png";
    CascadeClassifier faceDetect = new CascadeClassifier("haarcascade_frontalface_alt.xml");
    int status = 1;
    int banyak_wajah = -1;
//    int indeks = 0;
//    int ketemu = 1;
//    int suara = 0;
//    int ulang = 1;
//    String nama;
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
//                        System.out.println(String.format("Total faces Detected: %d", faces.toArray().length));
                        String text = String.format("Total wajah terdeteksi : %d", faces.toArray().length);

                        Rect rect_Crop = null;
                        Imgproc.putText(matrix, text, new Point(15, 25
                        ), NORMAL, 0.7, new Scalar(255, 255, 255), 2);
//                         Imgproc.putText(matrix, "29/01/2018", new Point(15, 40
//                         ), Core.FONT_HERSHEY_COMPLEX_SMALL, 0.5, new Scalar(0, 255, 0));
                        for (Rect rect : faces.toArray()) {
                            //simpan gambar
//                            if (indeks == 1) {
//
//                                while (ketemu == 1) {
//                                    File file = new File(path + "/dataWajah/" + nama + "_"+ indeks + ".jpg");
//                                    Desktop desktop = Desktop.getDesktop();
//                                    if (!file.exists()) {
//                                        ketemu = 0;
//                                    }else{
//                                        indeks = indeks +1;
//                                    }
//                                }
//
//                                rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
//                                Mat image_roi = new Mat(matrix, rect_Crop);
//                                Imgcodecs.imwrite(path + "/dataWajah/" + nama + "_"+ indeks + ".jpg", image_roi);
//                                JOptionPane.showMessageDialog(null, "Data wajah anda telah disimpan di dalam folder :"+path + "/dataWajah/" + nama + "_"+ indeks + ".jpg");
//                                indeks = 0;
//                            }

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

                        if (banyak_wajah != faces.toArray().length) {
                            if (faces.toArray().length == 0) {
                                timer = new Timer();
                                timer.schedule(new tampilkanWaktuMundur(), 0, 1000);
                            } else if (faces.toArray().length > 0) {
                                timer.cancel();
                            }
                            banyak_wajah = faces.toArray().length;
                        }

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

    class tampilkanWaktuMundur extends TimerTask {

        int menit = Integer.parseInt(fieldWaktu.getText());
        int detik = menit * 1;

        @Override
        public void run() {
            if (detik > 0) {
//                fieldWaktu.setText(String.valueOf(detik));
                detik--;
            } else {
                timer.cancel();
                stop();
                start.setText("Start");
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process proc = runtime.exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0");
//                fieldWaktu.setText("Waktu Habis");
                } catch (IOException ex) {
                    Logger.getLogger(FaceDetect.class.getName()).log(Level.SEVERE, null, ex);
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
        fieldWaktu = new javax.swing.JTextField();
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
            .addGap(0, 487, Short.MAX_VALUE)
        );

        jLabel1.setText("Waktu (menit)");

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
                .addGap(196, 196, 196)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(start)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    Timer timer;
    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // TODO add your handling code here:
        if (fieldWaktu.getText().compareTo("") == 0) {
            JOptionPane.showMessageDialog(this, "Silahkan isi timer terlebih dahulu!");
        } else {
            VoiceManager vm = VoiceManager.getInstance();
            Voice voice = vm.getVoice("kevin16");
            voice.allocate();
//
//        
            if (start.getText().compareTo("Start") == 0) {
                start();
                start.setText("Stop");
                fieldWaktu.setEditable(false);
                voice.speak("Face Detection Start");
//            timer = new Timer();
//            timer.schedule(new tampilkanWaktuMundur(), 0, 1000);
            } else {
                fieldWaktu.setEditable(true);
                stop();
                start.setText("Start");
                voice.speak("Face Detection Stop");
//            timer.cancel(); 
            }
        }


    }//GEN-LAST:event_startActionPerformed

    private void start() {

        cap = new VideoCapture(1);
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
    private javax.swing.JTextField fieldWaktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton start;
    // End of variables declaration//GEN-END:variables
}
