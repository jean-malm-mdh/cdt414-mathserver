package cdt414.mathserver;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathMessage {
	private static final String trim_number = "\\s*([-+]?\\d+)\\s*";
	private static final String unary = "(Fibo|Dice|Collatz)[(]" + trim_number + "[)]";
	private static final String binary = "(GCD|Add|Sub|Mul|Div|Pascal)[(]" + 
											trim_number + "[,]" + trim_number + "[)]";
	private static Pattern regex_msg_unary = Pattern.compile(unary);
	private static Pattern regex_msg_binary = Pattern.compile(binary);
	
	public enum MessageType
	{
		// Functional messages
		// Unary messages
		Fibo,
		Dice,
		Collatz,
		
		// Binary messages
		GCD,
		Pascal,
		Add,
		Mul,
		Sub,
		Div,
		
		// Status
		Result,
		Error
	}
	public MessageType type;
	public int[] args;
	public String msg;
	static MathMessage parseUnary(MatchResult m)
	{
		MessageType t = MessageType.valueOf(m.group(1));
		int args[] = {Integer.parseInt(m.group(2))};
		MathMessage res = new MathMessage(t, m.group(0), args);
	
		return res;
	}
	static MathMessage parseBinary(MatchResult m)
	{		
		MessageType t = MessageType.valueOf(m.group(1));
		int args[] = {Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))};
		MathMessage res = new MathMessage(t, m.group(0), args);
	
		return res;
	}
	public static MathMessage parseInput(String input)
	{
		Matcher m = regex_msg_unary.matcher(input);
		if(!m.matches())
		{
			m = regex_msg_binary.matcher(input);
			if(!m.matches()) 
				return MathMessage.MakeError(input);
			else
			{
				return parseBinary(m.toMatchResult());
			}
		}
		else
		{
			return parseUnary(m.toMatchResult());
		}
	}
	
	public MathMessage(MessageType type, String msg, int... args)
	{
		this.type = type;
		this.args = args;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		String arg_list = "";
		int i = 0;
		for(i = 0; i < this.args.length - 1; ++i)
		{
			arg_list += this.args[i] + ", ";
		}
		arg_list += this.args[i];
		return String.format("%s: %s {%s}", this.msg, type.name(), arg_list);
	}
	
	public static MathMessage MakeError(String msg)
	{
		return new MathMessage(MessageType.Error, "ERROR - Cannot parse the following message:\n" + msg);
	}
}
