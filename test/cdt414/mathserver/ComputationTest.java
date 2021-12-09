package cdt414.mathserver;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.junit.jupiter.api.*;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class)
public class ComputationTest {
	static Map<Integer, Long> fibo_memoize = new HashMap<Integer, Long>();
	@BeforeEach
	public void setUp() throws Exception {
	}
	
	public ComputationTest()
	{
		
	}
	
	@Test
	public void Test1()
	{
		Random random = new Random();
		for(int i = 0; i < 100; ++i)
		{
			int n = -(random.nextInt(100) + 1);
			Long actual = Computations.Fibo(n);
			assertEquals(null, actual);
		}
	}
	
	@Test
	public void Test2()
	{
		assertEquals(0, Computations.Fibo(0).intValue());
		fibo_memoize.put(0, 0L);
		assertEquals(1, Computations.Fibo(1).intValue());
		fibo_memoize.put(1, 1L);
	}
	
	@Test
	public void Test3()
	{
		for(int n = 2; n < 40;++n)
		{
			long expected = fibo_memoize.get(n-2).longValue() + fibo_memoize.get(n-1).longValue();
			assertEquals("For n >= 2, fibo = sum of two preceeding numbers", expected, Computations.Fibo(n).longValue());
			
			if(!fibo_memoize.containsKey(expected))
			{
				fibo_memoize.put(n, expected);
			}
		}
	}
	
	@Test
	public void Test4() throws InterruptedException
	{
		Computations comp = Computations.Get_Instance();
		
		int id = comp.Fibo_Async(45);
		// We did some measurements, and it should not take longer than this to finish the computation
		Thread.sleep(8000);
		Long expected = Computations.Request_result(id);
		assertNotEquals(null, expected);
		assertEquals(1134903170L,expected.longValue());
	}
	
	@Test
	public void Test6() throws InterruptedException
	{
		Computations comp = Computations.Get_Instance();
		int n = comp.MaxWorkerCount() + 1;
		int ids[] = new int[n];
		for(int i = 0; i < n; ++i)
		{
			ids[i] = comp.Fibo_Async(10);
		}
		
		for(int i = 0; i < n-1; ++i)
		{
			assertTrue(ids[i] >= 0);
		}	
		assertEquals(-1, ids[n-1]);
	}
	
	@Test
	public void Test5() throws InterruptedException
	{
		Computations comp = Computations.Get_Instance();
		int n = comp.MaxWorkerCount();
		int ids[] = new int[n];
		for(int i = 0; i < n; ++i)
		{
			ids[i] = comp.Fibo_Async(10);
		}

		Thread.sleep(10);
		
		for(int i = 0; i < n; ++i)
		{
			assertEquals(55, Computations.Request_result(ids[i]).longValue());
		}
	}
	
	@Test
	public void Test7()
	{
		// Border tests for pascal's triangle
		assertEquals(1L, Computations.Pascal(0, 0).intValue());
		for(int i = 1; i < 10; ++i)
		{
			assertEquals(1L, Computations.Pascal(i, 0).intValue());
			assertEquals(1L, Computations.Pascal(i, i).intValue());
		}		
	}
	
	@Test
	public void Test8()
	{
		Random rand = new Random();
		try
		{
			File f = new File("testdata/memoize.txt");
			Scanner reader = new Scanner(f);
			
			List<String> lines = new ArrayList<String>();
			while(reader.hasNextLine())
				lines.add(reader.nextLine());
			for(int i = 0; i < 10; ++i) 
			{
				int line_to_test = rand.nextInt(lines.size()-1) + 1;
				String lineItems[] = lines.get(line_to_test).split(",");
				int column_to_test = rand.nextInt(lineItems.length);
				Integer expected = Integer.parseInt(lineItems[column_to_test]);
				assertEquals(expected, Computations.Pascal(line_to_test, column_to_test));
			}
		}
		catch (Exception e)
		{
			fail("Test Failed");
		}
	}
}
