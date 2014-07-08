/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rbm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author loke
 */
public class Rbm {
    final int imgHxW=4; /* image input width x height */
    final int VIS=imgHxW*imgHxW;
    final int HID=5;
    final double rate=0.035;
    final double error=0.001;
    int Training;
    int iter=10;
    boolean print=false; //set to print console output
    
    double[] ph,pv,inp,h,v;
    double[][] W;
    private data[] train;

public Rbm(int iter){
    inp=new double[VIS];
    v=new double[VIS];
    h=new double[HID];
    ph=new double[HID];
    pv=new double[VIS];
    W=new double[VIS][HID];
    this.iter=iter;
    Random ran=new Random();
    ran.setSeed(System.currentTimeMillis());
    for(int i=0;i<VIS;i++)
        for(int j=0;j<HID;j++)
            W[i][j]=ran.nextDouble();
}
public void trainIter(int r){
    for(int i=0;i<iter;i++){
        if(r==1)trainCD1(); //run 1 cycle of data
        else trainCD2(); //
    }
}
public void trainCD1(){
    double sum=0;
    double err=0,temp=0;
    Random ran=new Random();
    ran.setSeed(System.currentTimeMillis());

    for(int k=0;k<Training;k++){
       //clamp input
        for (int n = 0; n < VIS; n++) {
         inp[n]= train[k].val[n];
        }
              
        for(int j=0;j<HID;j++){
            sum=0;
            for(int i=0;i<VIS;i++){
                sum+=inp[i]*W[i][j];
            }
            ph[j]=1.0/(1.0+Math.exp(-sum));
            h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
        }

        for(int i=0;i<VIS;i++){
            sum=0;
            for(int j=0;j<HID;j++){
                sum+=h[j]*W[i][j];
            }
            pv[i]=1.0/(1.0+Math.exp(-sum));
            v[i]=(ran.nextDouble()<pv[i]?1.0:0.0);
        }

        for(int j=0;j<HID;j++){
            sum=0;
            for(int i=0;i<VIS;i++){
                sum+=v[i]*W[i][j];
            }
            ph[j]=1.0/(1.0+Math.exp(-sum));
            //h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
        }   

        //update weights
         for(int i=0;i<VIS;i++){
            for(int j=0;j<HID;j++){
                W[i][j]+=rate*(h[j]*inp[i]-ph[j]*v[i]);
            }
        }   
        if(print){
            printVisible();
            printHidden();
            printWeights();
        }
        temp=0;
        for(int n=0;n<VIS;n++){
            temp+=Math.pow(inp[n]-v[n],2);
        }
        err+=Math.sqrt(temp);
      }
      if(print)
      System.out.println(" Err="+err);
    }
public void trainCD2(){
    double sum=0;
    double err=0,temp=0;
    Random ran=new Random();
    ran.setSeed(System.currentTimeMillis());

    for(int k=0;k<Training;k++){
       //clamp input
        for (int n = 0; n < VIS; n++) {
         inp[n]= train[k].val[n];
        }
              
        for(int j=0;j<HID;j++){
            sum=0;
            for(int i=0;i<VIS;i++){
                sum+=inp[i]*W[i][j];
            }
            ph[j]=1.0/(1.0+Math.exp(-sum));
            h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
        }

        for(int i=0;i<VIS;i++){
            sum=0;
            for(int j=0;j<HID;j++){
                sum+=h[j]*W[i][j];
            }
            pv[i]=1.0/(1.0+Math.exp(-sum));
            v[i]=(ran.nextDouble()<pv[i]?1.0:0.0);
        }

        for(int j=0;j<HID;j++){
            sum=0;
            for(int i=0;i<VIS;i++){
                sum+=v[i]*W[i][j];
            }
            ph[j]=1.0/(1.0+Math.exp(-sum));
            h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
        }   
        ///end CD1, start CD2
        for(int i=0;i<VIS;i++){
            sum=0;
            for(int j=0;j<HID;j++){
                sum+=h[j]*W[i][j];
            }
            pv[i]=1.0/(1.0+Math.exp(-sum));
            v[i]=(ran.nextDouble()<pv[i]?1.0:0.0);
        }

        for(int j=0;j<HID;j++){
            sum=0;
            for(int i=0;i<VIS;i++){
                sum+=v[i]*W[i][j];
            }
            ph[j]=1.0/(1.0+Math.exp(-sum));
            //h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
        }   

        //update weights
         for(int i=0;i<VIS;i++){
            for(int j=0;j<HID;j++){
                W[i][j]+=rate*(h[j]*inp[i]-ph[j]*v[i]);
            }
        }   
        if(print){
            printVisible();
            printHidden();
            printWeights();
        }
        temp=0;
        for(int n=0;n<VIS;n++){
            temp+=Math.pow(inp[n]-v[n],2);
        }
        err+=Math.sqrt(temp);
      }
      

      if(print)
      System.out.println(" err="+err);


    }

    public void readTestInput() {
        //read in training size X size bitmap values into train[]
        Training=4;
        train = new data[Training];
        int[] d0 = {0, 1, 0,0, 
                    0, 1, 0,0, 
                    0, 1, 0,0, 
                    0, 1, 0,0};
        int[] d1 = {1, 0, 0,0, 
                    1, 0, 0,0, 
                    1, 0, 0,0, 
                    1, 0, 0,0};
        int[] d2 = {0, 0, 1,0,
                    0, 0, 1,0, 
                    0, 0, 1,0, 
                    0, 0, 1,0};
        int[] d3 = {0, 0, 0,0,
                    1, 1, 1,1, 
                    0, 0, 0,0,
                    0, 0, 0,0};

        train[0] = new data(d0);
        train[1] = new data(d1);
        train[2] = new data(d2);
        train[3] = new data(d3);
    }
    public void printWeights() {
    System.out.println("****WEIGHT****");
        for (int j = 0; j < HID; j++) {
            for (int i = 0; i < VIS; i++) {
                System.out.print(" " + W[i][j] + " ");
                if (i % 3 == 2) {
                    System.out.println("");
                }
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
    }

    public void printVisible() {
        System.out.println("****VISIBLE****");
        for (int i = 0; i < VIS; i++) {
            System.out.print(" " + v[i]);
            if (i % 3 == 2) {
                System.out.println("");
            }
        }
        System.out.println("****************");
    }

    public void printHidden() {
        System.out.println("****HIDDEN******");
        for (int i = 0; i < HID; i++) {
            System.out.println(" " + h[i]);
        }
        System.out.println("****************");
    }
    /** note hard-coded stuffs here **/
    /** don't use for HID not equal 4 coz it is hard coded*/
    public void testHiddenReconstruction(){

        double sum;
        Random ran=new Random();
        ran.setSeed(System.currentTimeMillis());
        //test all/some patterns -- hard coded
        double hh[][]={{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,1,0,0},{0,0,1,1},{0,1,1,1},
        {1,0,0,0},{1,0,0,1},{1,0,1,0},{1,1,0,0},{1,0,1,1},{1,1,1,1}};
        BufferedImage img[]=new BufferedImage[hh.length];
        BufferedImage img2[]=new BufferedImage[hh.length];
       
        for(int k=0;k<hh.length;k++){
            for(int i=0;i<VIS;i++){
                sum=0;
                for(int j=0;j<HID;j++){
                    sum+=hh[k][j]*W[i][j];
                }
                pv[i]=1.0/(1.0+Math.exp(-sum));
                v[i]=(ran.nextDouble()<pv[i]?1.0:0.0);
            }
            img[k]=getImageFromArray(v,imgHxW,imgHxW);
            img2[k]=getImageFromArray(hh[k],1,HID);

        }
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        for(int k=0;k<hh.length;k++){
            JPanel panel=new JPanel();
            panel.setBackground(Color.yellow);
            frame.getContentPane().add(panel);
            panel.add(new JLabel(new ImageIcon(img2[k])));
            panel.add(new JLabel(new ImageIcon(img[k])));
        }
        frame.pack();
        frame.setVisible(true);    
        frame.setTitle("Reconstruction Hidden-Visible");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
    }
    public BufferedImage getImageFromArray(int[] pixels, int width, int height) {
       int[] pix=new int[pixels.length];
       for(int i=0;i<pixels.length;i++){
               pix[i]=(pixels[i]==1?0xffff:0);
       }
       return convert(pix,width,height);
    }
    public BufferedImage getImageFromArray(double[] pixels, int width, int height) {
       int[] pix=new int[pixels.length];
       for(int i=0;i<pixels.length;i++){
               pix[i]=(pixels[i]==1?0xffff:0);
       }
       return convert(pix,width,height);    
    }
    public BufferedImage convert(int[] pixels, int width, int height){
         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
         WritableRaster raster = (WritableRaster) image.getData();
         raster.setPixels(0,0,width,height,pixels);
         image.setData(raster);
         int size=21;
         BufferedImage resized = new BufferedImage(width*size, height*size, image.getType());
         Graphics2D g = resized.createGraphics();
         //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         g.drawImage(image, 0, 0, width*size, height*size, 0, 0, image.getWidth(), image.getHeight(), null);
         g.dispose();
         return resized;
    }
    
    public void testInputReconstruction(boolean perturb){

        double sum;
        Random ran=new Random();
        ran.setSeed(System.currentTimeMillis());
        //test input patterns 
        int perb=0;
        
        BufferedImage img1[]=new BufferedImage[train.length];
        BufferedImage img2[]=new BufferedImage[train.length];
        BufferedImage img3[]=new BufferedImage[train.length];
        if(perturb){ //perturb one same position
            perb=ran.nextInt(VIS);
        }
        for(int k=0;k<train.length;k++){
            for (int n = 0; n < VIS; n++) {
             inp[n]= train[k].val[n];
            }
            if(perturb)
                inp[perb]=(inp[perb]==0?1:0);
            
            for(int j=0;j<HID;j++){
                sum=0;
                for(int i=0;i<VIS;i++){
                    sum+=inp[i]*W[i][j];
                }
                ph[j]=1.0/(1.0+Math.exp(-sum));
                h[j]=(ran.nextDouble()<ph[j]?1.0:0.0);
            }

            for(int i=0;i<VIS;i++){
                sum=0;
                for(int j=0;j<HID;j++){
                    sum+=h[j]*W[i][j];
                }
                pv[i]=1.0/(1.0+Math.exp(-sum));
                v[i]=(ran.nextDouble()<pv[i]?1.0:0.0);
            }
            img1[k]=getImageFromArray(inp,imgHxW,imgHxW);
            img2[k]=getImageFromArray(h,1,HID);
            img3[k]=getImageFromArray(v,imgHxW,imgHxW);
            
        }
        JFrame frame = new JFrame();
        
        frame.getContentPane().setLayout(new FlowLayout());
        for(int k=0;k<train.length;k++){
            JPanel panel=new JPanel();
            frame.getContentPane().add(panel);
            panel.setLayout(new FlowLayout());
            panel.setBackground(Color.yellow);
            panel.add(new JLabel(new ImageIcon(img1[k])));
            panel.add(new JLabel(new ImageIcon(img2[k])));
            panel.add(new JLabel(new ImageIcon(img3[k])));
        }
        frame.pack();
        frame.setVisible(true);    
        frame.setTitle("Reconstruction Input-Hidden-Visible");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
    }
    public void displayTestInput(){
        BufferedImage img[]=new BufferedImage[Training];
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        for(int k=0;k<Training;k++){
            img[k]=getImageFromArray(train[k].val,imgHxW,imgHxW);
            frame.getContentPane().add(new JLabel(new ImageIcon(img[k])));
        }
        frame.pack();
        frame.setVisible(true);    
        frame.setTitle("Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private class data {

        public int val[];
        public int pix[];
        public data() {
            val = new int[VIS];
        }
        public data(int[] in) {
            val = in;

        }

    }
    
    public static void main(String[] args){
        Rbm r=new Rbm(8000);
        r.readTestInput();
        r.trainIter(1); //CD1 or CD2 
        r.displayTestInput();
       // r.testHiddenReconstruction();
        r.testInputReconstruction(true); //true -- perturb one position
    }    
}