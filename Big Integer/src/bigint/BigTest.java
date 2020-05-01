package bigint;

import java.io.IOException;
import java.util.Scanner;

public class BigTest {

	static Scanner sc;
	
	public static void check() {
		  String realParse;
		  String realSum;
		  String realProduct;
		  String cParse;
		  String cSum;
		  String cProduct;
		  for (int i = -5000; i < 5000; i++) {
		   for (int j = 5000; j > -5000; j--) {
		    
		    realParse = Integer.toString(j);
		    cParse = BigInteger.parse(realParse).toString();
		    try {
		    	if (!realParse.equals(cParse)) {
				     System.out.println("Parse Error: " + j);
				     return;
				    }
		    }catch (IllegalArgumentException e){
		    	System.out.println("\t\tIncorrect Format");
		    }
		    
		    
		    realSum = Integer.toString(i+j);
		    cSum = BigInteger.add(BigInteger.parse(Integer.toString(i)), BigInteger.parse(Integer.toString(j))).toString();
		    if (!realSum.equals(cSum)) {
		     System.out.println("Add Error: "+ i + "   and   " + j);
		     return;
		    }
		    
		    realProduct = Integer.toString(i*j);
		    cProduct = BigInteger.multiply(BigInteger.parse(Integer.toString(i)), BigInteger.parse(Integer.toString(j))).toString();
		    if (!realProduct.equals(cProduct)) {
		     System.out.println("Multiply Error: "+ i + "   and   " + j);
		     return;
		    }
		   }
		   if (i == 5000/4) System.out.println("Quarter of the way there!");
		   if (i == 5000/2) System.out.println("50% done!!");
		   if (i == 5000/4*3) System.out.println("75 cents!!!");
		  }
		  System.out.println("Successful!");
		  
		 }

	public static void parse() 
	throws IOException {
		System.out.print("\tEnter integer => ");
		String integer = sc.nextLine();
		try {
			BigInteger bigInteger = BigInteger.parse(integer);
			System.out.println("\t\tValue = " + bigInteger);
		} catch (IllegalArgumentException e) {
			System.out.println("\t\tIncorrect Format");
		}
	}
	
	public static void add() 
	throws IOException {
		System.out.print("\tEnter first integer => ");
		String integer = sc.nextLine();
		BigInteger firstBigInteger = BigInteger.parse(integer);
		
		System.out.print("\tEnter second integer => ");
		integer = sc.nextLine();
		BigInteger secondBigInteger = BigInteger.parse(integer);
		
		BigInteger result = BigInteger.add(firstBigInteger,secondBigInteger);
		System.out.println("\t\tSum: " + result);
	}
	
	public static void multiply() 
	throws IOException {
		System.out.print("\tEnter first integer => ");
		String integer = sc.nextLine();
		BigInteger firstBigInteger = BigInteger.parse(integer);
		
		System.out.print("\tEnter second integer => ");
		integer = sc.nextLine();
		BigInteger secondBigInteger = BigInteger.parse(integer);
		
		BigInteger result = BigInteger.multiply(firstBigInteger,secondBigInteger);
		System.out.println("\t\tProduct: " + result);
		
	}
	
	public static void main(String[] args) 
	throws IOException {
		
		// TODO Auto-generated method stub
		sc = new Scanner(System.in);
		
		char choice;
		while ((choice = getChoice()) != 'q') {
			switch (choice) {
				case 'p' : parse(); break;
				case 'a' : add(); break;
				case 'm' : multiply(); break;
				case 'c' : check(); break;
				default: System.out.println("Incorrect choice"); 
			}
		}
	}

	private static char getChoice() {
		System.out.print("\n(p)arse, (a)dd, (m)ultiply, (c)heck, or (q)uit? => ");
		String in = sc.nextLine();
		char choice;
		if (in == null || in.length() == 0) {
			choice = ' ';
		} else {
			choice = in.toLowerCase().charAt(0);
		}
		return choice;
	}

}
