package ru.erv.drakongen.test;

public class Test1 {

	public Test1() {}
	
	/**
	 * Manages the worker's thread
	 */
	private final Runnable workerRunnable = new Runnable() {
		public void run() {
			/*
			while (! workerStopped) {
				synchronized(workerLock) {
					workerCancelled = false;
					workerStateDir = workerNextDir;
				}
				workerExecute();
				synchronized(workerLock) {
					try {
						if ((!workerCancelled) && (workerStateDir == workerNextDir)) workerLock.wait();
					} catch (InterruptedException e) {
					}
				}
				
			}*/
			workerThread = null;
			// wake up UI thread in case it is in a modal loop awaiting thread termination
			// (see workerStop())
			display.wake();
		}
	};
	
}
