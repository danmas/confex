
public class ELog {
	static {
		// System.load("G:\\WRK\\WRK_CONFEX\\test_rv_jni\\dll\\e_log.dll");
		
		System.loadLibrary("e_log");
		System.out.println("The external e_log.dll was loaded.");
	}

	private native String GetEventLog(String eventLog);

	public String GetLog(String log) {
		return GetEventLog(log);
	}

}