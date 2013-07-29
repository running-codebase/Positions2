package com.travis.awesome.positions2;

public class InterviewPracticeQuestions {


	public boolean IsPalindrome(String input)
	{
		boolean palindrome = true;
		int length = input.length();
		char[] input_array = input.toCharArray();
		
		for (int i = 0; i<length/2;i++)
		{
			if (input_array[i] != input_array[length-1-i])
			{
				palindrome = false;
			}
		}
		return palindrome;
	}
	
	
	public String RemoveChar(char input, String input_string)
	{
		char[] input_array = input_string.toCharArray();
		int length = input_array.length;
		
		for (int i = 0; i<length; i++)
		{
			if (input_array[i] == input)
			{
				for (int j = i; j<length-1; j++)
				{
					input_array[j] = input_array[j+1];
				}
				length--;
			}
		}
		return input_array.toString();
	}
	
	
	
	//Find the largest palindrome in a string
	
	//write the nth fibonacci number out. Use recursion
	
	public int Fibonacci(int n)//1,1,2,3,5,8,13,21,34
	{
		if (n == 1 || n == 0)
		{
			return n;
		}
		
		return Fibonacci(n-2) + Fibonacci(n-1);	
	}
	
	
	
}
