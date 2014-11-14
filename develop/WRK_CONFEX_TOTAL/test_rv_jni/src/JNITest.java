//package JNITest;

import java.io.*;

public class JNITest
{
    native void initializeApplication();
    native void terminateApplication();
    native String getHello();
    
    public void launchThread()
    {
        new Thread() {
            public void run()
            {
                System.out.println( getHello() );
            }
        }.start();
    }
    
    public static void main( String[] args )
    {
        //load dynamic library with native methods
        File lib = new File( new File(System.getProperty("user.dir") ), "libjnitest.dll");
        System.load( lib.getAbsolutePath() );
        
        //start benchmark
        System.out.println( "hello START");
        double timestamp = System.nanoTime();

        //evoke native call
        JNITest t = new JNITest();
        t.initializeApplication();
        
        
        System.out.println( t.getHello() );
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
//        t.launchThread();        
        
        
        t.terminateApplication();
        //stop benchmark
        timestamp = (System.nanoTime()-timestamp)/1e6;
        System.out.println( "hello STOP in " + timestamp + "[ms]");
    }
}
