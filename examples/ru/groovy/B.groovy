
/*
*  Class B depends from class A 
*/
class B {

	void run() {
		println "B.test_run() started and call A().test_run()"
		new A().test_run() 
	}
	
}
