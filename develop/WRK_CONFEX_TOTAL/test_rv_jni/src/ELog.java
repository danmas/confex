public class ELog {
  static {
    // System.loadLibrary("e_log");
    System.load("G:\\WRK\\WRK_CONFEX\\test_rv_jni\\dll\\e_log.dll"); 
  }
  
  private native String GetEventLog(String eventLog);
  
  public String GetLog(String log) {
    return GetEventLog(log);
  }
  
}