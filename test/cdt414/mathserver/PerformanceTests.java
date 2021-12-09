package cdt414.mathserver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

class PerformanceTests {

	@RepeatedTest(value = 10)
	void Test_SystemShallHandle_10000_Fibo20_Computations_Per_Sec() throws InterruptedException {
		Computations computer = Computations.Get_Instance();
		long start = System.currentTimeMillis();
		for(int i = 0; i < 10000; ++i)
		{
			long n = Computations.Fibo(20);			
		}
		long stop = System.currentTimeMillis();
		assertTrue((stop-start) < 1050L, "Executions shall not take more than ~1 second");
		
		Thread.sleep(1000);
	}
}
