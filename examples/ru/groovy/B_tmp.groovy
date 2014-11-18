
/*
*  Class B depends from class A 
*/
class B_tmp {

	void run() {
		println "B_tmp.test_run() started and call A_tmp().test_run()"
		new A_tmp().test_run() 
	}
	
}
