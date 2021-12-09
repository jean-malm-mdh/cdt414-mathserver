//

package cdt414.mathserver;
import java.util.Random;

public class Computations {
	final static int workerCount = 20;
	
	public int MaxWorkerCount() {
		return workerCount;
	}
	static Long workerResults[] = new Long[workerCount];
	
	// Because mutexes are expensive
	static boolean workerLockIsTaken[] = new boolean[workerCount];
	public static Computations INSTANCE = null;
	
	private void startup()
	{
		for(int i = 0; i < workerCount; ++i)
		{
			workerResults[i] = null;
			workerLockIsTaken[i] = false;
		}
	}
	private Computations()
	{
		startup();
	}
	
	// Using Singleton pattern to make sure only one instance of Computations is running on a server
	public static Computations Get_Instance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new Computations();
		}
		return INSTANCE;
	}
	
	
	//Returns the nth fibonacci number
	public static Long Fibo(int n)
	{
		if (n < 0) return null;
		if (n == 0) return 0L;
		if (n == 1) return 1L;
		return Fibo(n-1) + Fibo(n-2);
	}
	
	//Starts a parallel process to compute nth fibonacci number
	//Returns ID, to be used for data retrieval
	public int Fibo_Async(int n)
	{
		int id = -1;
		// Grab the first free worker
		for(int i = 0; i < workerCount; ++i)
		{
			if (!workerLockIsTaken[i])
			{
				workerLockIsTaken[i] = true;
				id = i;
				break;
			}
		}
		if(-1 == id) 
			return -1;
		
		final int _id = id;
		
		Thread worker = new Thread(() -> {
			Long res = Fibo(n);
			workerResults[_id] = res;
		});
		worker.start();
		
		return _id;
	}
	public static Long Request_result(int id)
	{
		if(workerLockIsTaken[id])
		{
			// There should be some value here
			Long res = workerResults[id];
			workerLockIsTaken[id] = false;
			return res;
		}
		return null;
	}
	
	//Returns if the given number is a prime or not
	public static boolean IsPrime(int n)
	{
		final Integer precomputed_primes[] = new Integer[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 
				61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 
				163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 
				269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 
				383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 
				503, 509, 521, 523, 541};
		final Integer max_precomputed_prime = precomputed_primes[precomputed_primes.length-1];
		
		if (n < 3) return n > 1;
		if (n % 2 == 0) return false;
		for(int i = 0; i < precomputed_primes.length; ++i)
		{
			if(precomputed_primes[i] == n)
				return true;
			if(precomputed_primes[i] > n)	//Gone past precomputed without finding the number
				return false;
		}
		int i = 5;
	    while (i * i <= n) {
	        if (n % i == 0 || n % (i + 2) == 0)
	            return false;
	        i += 6;
	    }
	    return true;
	}
	
	//Return the greatest common denominator between a and b
	public static int GCD(int a, int b)
	{
		
		if (a == 0) return b;
		if (b == 0) return a;
		
		int q = a / b;
		int r = a - b * q;
		
		return GCD(b,r);
	}
	
	/*By applying the following rules
	  	If the number is even, divide it by two.
    	If the number is odd, triple it and add one.
      Returns how many steps are required to reach the value 1
    */
	public static int Collatz_Count(int n, int count)
	{
		if (n == 1) return count;
		int n_new;
		if (n % 2 == 0)
			n_new = n / 2;
		else
			n_new = n * 3 + 1;
		
		return Collatz_Count(n_new, count+1);
	}
	
	public static int Add(int a, int b)
	{
		return a + b;
	}
	public static int Sub(int a, int b)
	{
		return a - b;
	}
	public static int Mult(int a, int b)
	{
		return a * b;
	}
	public static Integer Div(int a, int b)
	{
		if(0 == b)
			return null;
		return a / b;
	}
	
	static int Pascal_loop(int n, int k)
	{
		if (0 == n || 0 == k)
			return 1;
		
		if (k == n)
			return 1;
		
		return Pascal_loop(n-1, k-1) + Pascal_loop(n-1, k);
	}
	
	public static Integer Pascal(int n, int k)
	{
		if (n < 0) {
			return null;
		}
		if(0 > k || k > n)
		{
			return null;
		}
		
		return Pascal_loop(n, k);
	}
	
	public static int Dice(int n)
	{
		Random rand = new Random();
		return rand.nextInt(n) + 1;
	}
}
