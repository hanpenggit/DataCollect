package com.sinodata.kettle;

public class AppMain
{
  private static Integer lock = Integer.valueOf(1);

  public static void main(String[] args)
  {
    /*String[] unitNames = args[0].split(";");
    for (int i = 0; i < unitNames.length; ++i)
    {
      ThreadRun tr = new ThreadRun(unitNames[i], lock);
      new Thread(tr).start();
    }*/
  }
}