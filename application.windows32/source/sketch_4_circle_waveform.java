import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class sketch_4_circle_waveform extends PApplet {




Minim minim;
AudioPlayer apTI;
CircleWaveForm cwfLiveYourLife;
ArrayList<PShape> alSnapShots = new ArrayList();
BeatDetect bdDetector = new BeatDetect();

// int iMouthScale; //Becacuse the level is between 0 and 1, but usually closer to 0,
                     //use this to scale the mouth up.
// int iQuality; //higher is less, but faster probably
// float fMaxRadius;

public void setup()
{
  frameRate(25);
  size(500, 500, P2D);
  minim = new Minim(this);
  apTI = minim.loadFile("live your life.mp3");
  cwfLiveYourLife = new CircleWaveForm(apTI, width/2, height/2);
  
  //iMouthScale = 6;
  //iQuality = 1;
  //fMaxRadius = 150;
 
  background(0);
} 

public void draw()
{
  //clear the screen
  background(0);
  
  //Detect some beats
  bdDetector.detect(apTI.mix);

  //Draw all the saved snapshots
  for (PShape psSnapShot : alSnapShots)
  {
    if (bdDetector.isOnset())
    {
      scale(1);
    }
    shape(psSnapShot);
  }  
  
  //Draw the mouse
  cwfLiveYourLife.setX(mouseX);
  cwfLiveYourLife.setY(mouseY);
  PShape psCWFSnapShot = cwfLiveYourLife.createSnapShot();
  shape(psCWFSnapShot);
  
  //check if the mouse is pressed. if it is, save the PShape, and draw it every frame
  if (mousePressed)
  {
    alSnapShots.add(psCWFSnapShot);
  }
  
  if (alSnapShots.size() > 150)
  {
    alSnapShots.remove(0);
  }
  
//  background(0);
//  float[] fMixArray = apTI.mix.toArray();
//  float fCurrentLevel = apTI.mix.level();
//  float fAmplitude;
//  
//  beginShape();
//  for (int i = 0; i < fMixArray.length / iQuality; i++)
//  {
//    fAmplitude = fMixArray[i] * (fMaxRadius / 2);
//    
//    //Remember: fAmplitude could be negative or positive. So since fMaxRadius is the max
//    //radius, we don't want to subtract or add the whole radius. So what I did is start
//    //with a circle half the size of fMaxRadius, then add fAmplitude, which can be either
//    //-fMaxRadius/2 or fMaxRadius/2.
//    
//    float fRadius = (fMaxRadius / 2) + fAmplitude;
//    float fX = (sin(TWO_PI * ((i * iQuality) / float(fMixArray.length))) * fRadius);
//    float fY = (cos(TWO_PI * ((i * iQuality) / float(fMixArray.length))) * fRadius);
//    fY = fY * (fCurrentLevel * iMouthScale);
//    vertex((width / 2) + fX, (height / 2) + fY);
//  }
//  endShape();
}

public void stop()
{
    apTI.close();
    minim.stop();
    super.stop();
}
public class CircleWaveForm
{
  private AudioPlayer m_apPlayer;
  private int m_iQuality; //lower is better quality, but maybe slower (more vertices)
  
  //Describe the center of the object
  private int m_iX;
  private int m_iY;
  private float m_fRadius;
  
  //Constructor
  CircleWaveForm(AudioPlayer apPlayer, int iX, int iY)
  {
    m_apPlayer = apPlayer;
    m_apPlayer.play();
    m_iQuality = 20;
    m_iX = iX;
    m_iY = iY;
    m_fRadius = 50;
  }
  
  ////////////////////////////////////////
  //Getters
  public AudioPlayer getAudioPlayer()
  {
    return m_apPlayer;
  }
  
  public int getQuality()
  {
    return m_iQuality;
  }
  
  public int getX()
  {
     return m_iX;
  }
  
  public int getY()
  {
    return m_iY;
  }
  
  public float getRadius()
  {
    return m_fRadius;
  }
  
  ////////////////////////////////////////
  //Setters
  public void setAudioPlayer(AudioPlayer apPlayer)
  {
    m_apPlayer = apPlayer;
  }
  
  public void setQuality(int iQuality)
  {
    m_iQuality = iQuality;
  }
  
  public void setX(int iX)
  {
    m_iX = iX;
  }
  
  public void setY(int iY)
  {
    m_iY = iY;
  }
  
  public void setRadius(float fRadius)
  {
    m_fRadius = fRadius;
  }
  
  ////////////////////////////////////////
  //Other methods
  public PShape createSnapShot()
  {
    //Make some vars
    float[] fMixArray = m_apPlayer.mix.toArray();
    float fCurrentLevel = m_apPlayer.mix.level();
    float fAdjustedAmplitude;
   
   //Create the shape
    PShape psCWFSnapShot = createShape();
    
    //Create ColorRamp and fill the shape
    colorMode(HSB);
    int iColorValue = frameCount % 510;
    if (iColorValue > 255)
    {
      iColorValue = 510 - iColorValue;
    }
    psCWFSnapShot.fill(iColorValue, iColorValue, iColorValue); 
    
    
    for (int i = 0; i < fMixArray.length / m_iQuality; i++)
    {
      //fAdjustedAmplitude will be max of m_fRadius, min of -m_fRadius
      fAdjustedAmplitude = fMixArray[i * m_iQuality] *  m_fRadius;
      float fAdjustedRadius = m_fRadius + fAdjustedAmplitude;
      
      float fXAdjust = (sin(TWO_PI * ((i * m_iQuality) / PApplet.parseFloat(fMixArray.length))) * fAdjustedRadius);
      float fYAdjust = (cos(TWO_PI * ((i * m_iQuality) / PApplet.parseFloat(fMixArray.length))) * fAdjustedRadius);
      
      psCWFSnapShot.vertex(m_iX + (fXAdjust * fCurrentLevel), m_iY + (fYAdjust * fCurrentLevel));
    }
    psCWFSnapShot.end(CLOSE);
    
    return psCWFSnapShot;
  }
}
  
    
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "sketch_4_circle_waveform" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
