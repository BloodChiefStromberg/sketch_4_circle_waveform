import ddf.minim.*;
import ddf.minim.analysis.*;

Minim minim;
AudioPlayer apTI;
CircleWaveForm cwfLiveYourLife;
ArrayList<PShape> alSnapShots = new ArrayList();
BeatDetect bdDetector = new BeatDetect();

// int iMouthScale; //Becacuse the level is between 0 and 1, but usually closer to 0,
                     //use this to scale the mouth up.
// int iQuality; //higher is less, but faster probably
// float fMaxRadius;

void setup()
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

void draw()
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

void stop()
{
    apTI.close();
    minim.stop();
    super.stop();
}
