package cdt414.mathserver;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import cdt414.mathserver.MathMessage.MessageType;

public class MessageTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testParseInputError()
	{
		MathMessage mess = MathMessage.parseInput("aasbdionaosd");
		
		assertEquals("Empty message shall cause error", MessageType.Error, mess.type);
		assertEquals("Error shall print info and the input", 
				"ERROR - Cannot parse the following message:\naasbdionaosd",  mess.msg);
		assertEquals("Error parameter list shall be empty", 0, mess.args.length);
	}
	
	@Test
	public void testFibonacciMessage_ArgumentCountTest()
	{
		MathMessage mess = MathMessage.parseInput("Fibo()");
		assertEquals("Fibonacci without argument shall result in error",
				MessageType.Error, mess.type);

		mess = MathMessage.parseInput("Fibo(32,23)");
		assertEquals("Fibonacci with multiple arguments shall result in error",
				MessageType.Error, mess.type);
	}
	
	@Test
	public void parseMessages()
	{
		List<String> lines = new ArrayList<String>();
		List<MathMessage> results = new ArrayList<MathMessage>();
		try
		{
			File f = new File("testdata/example_messages.txt");
			Scanner reader = new Scanner(f);
			
			while(reader.hasNextLine()) {
				lines.add(reader.nextLine());
			}
			reader.close();
			
			for(int i = 0; i < lines.size(); ++i)
			{
				results.add(MathMessage.parseInput(lines.get(i)));
			}
			f = new File("testdata/results/message_results.txt");
			reader = new Scanner(f);
			for(int i = 0; i < lines.size(); ++i)
			{
				assertEquals(reader.nextLine(), results.get(i).toString());
			}
		}
		catch (Exception e)
		{
			fail("Test Failed");
		}
		
	}
	
}
