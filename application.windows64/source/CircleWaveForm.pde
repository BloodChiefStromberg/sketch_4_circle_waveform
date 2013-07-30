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
  AudioPlayer getAudioPlayer()
  {
    return m_apPlayer;
  }
  
  int getQuality()
  {
    return m_iQuality;
  }
  
  int getX()
  {
     return m_iX;
  }
  
  int getY()
  {
    return m_iY;
  }
  
  float getRadius()
  {
    return m_fRadius;
  }
  
  ////////////////////////////////////////
  //Setters
  void setAudioPlayer(AudioPlayer apPlayer)
  {
    m_apPlayer = apPlayer;
  }
  
  void setQuality(int iQuality)
  {
    m_iQuality = iQuality;
  }
  
  void setX(int iX)
  {
    m_iX = iX;
  }
  
  void setY(int iY)
  {
    m_iY = iY;
  }
  
  void setRadius(float fRadius)
  {
    m_fRadius = fRadius;
  }
  
  ////////////////////////////////////////
  //Other methods
  PShape createSnapShot()
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
      
      float fXAdjust = (sin(TWO_PI * ((i * m_iQuality) / float(fMixArray.length))) * fAdjustedRadius);
      float fYAdjust = (cos(TWO_PI * ((i * m_iQuality) / float(fMixArray.length))) * fAdjustedRadius);
      
      psCWFSnapShot.vertex(m_iX + (fXAdjust * fCurrentLevel), m_iY + (fYAdjust * fCurrentLevel));
    }
    psCWFSnapShot.end(CLOSE);
    
    return psCWFSnapShot;
  }
}
  
    
