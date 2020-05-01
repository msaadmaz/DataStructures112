package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	String nDelims ="0123456789 \t*+-/()]"; //new delims w/o opening bracket and with numbers
    	StringTokenizer exprToke = new StringTokenizer(expr.trim() , nDelims);
    	int arraysi = 0, varsi = 0; //index counters for array lists
    	while( exprToke.hasMoreTokens() ) { //loops through the tokens
    		String toke = exprToke.nextToken();
    		if( toke.contains("[") ) { //checks to see if it is an array
    			if( toke.charAt( toke.length() - 1) == '[') { //checks to see if the last char is a [, covers multiple arrays w/o variable index 
    				StringTokenizer ttoke = new StringTokenizer( toke.trim() , delims); //tokenizes toke w/o [
        			while( ttoke.hasMoreElements() ) { //stores tokens in to arrays array list
        				String toke2 = ttoke.nextToken();
        				Array arr = new Array( toke2 );
        				arrays.add( arraysi , arr );
        				arraysi++;
        			}
    			}else {
    				StringTokenizer ttoke = new StringTokenizer( toke.trim() , delims); //tokenizes toke w/o [
    				int numTtoke = ttoke.countTokens();
    				for(int i = numTtoke ; i > 0; i--) { //loops through tokens, taking in to account variable index
    					if( i == 1) { //will store variable index into variable array
    						String toke2 = ttoke.nextToken();
    						Variable var = new Variable( toke2 );
    						if(vars.contains(var)) {
    							continue;
    						}
    						vars.add( varsi , var);
    						varsi++;
    					}else { //stores arrays into array list
    						String toke2 = ttoke.nextToken();
            				Array arr = new Array( toke2 );
            				if(arrays.contains(arr)) {
    							continue;
    						}
            				arrays.add( arraysi , arr );
            				arraysi++;
    					}
    				}
    			}
    			
    		}else { //will store variable into variable array list
    			Variable var = new Variable( toke );
    			if(vars.contains(var)) {
					continue;
				}
    			vars.add( varsi , var );
    			varsi++;
    		}
    	}
    	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    
    private static boolean isVariable(String expr) {
    	if( expr.contains( "(" ) || expr.contains( "[" ) ) {
    		return false;
    	}
    	String nDelims = " \t*+-/()]";
    	StringTokenizer toke = new StringTokenizer(expr.trim() , nDelims);
    	if( !toke.hasMoreElements() ) {
    		return false;
    	}
    	if( toke.countTokens() > 1 ) {
    		return false;
    	}else {
    		String token = toke.nextToken();
    		if( token.matches( "-?\\d+(\\.\\d+)?") ) { 
        		return false;
        	}
    		if(token.contains("[")) {
    			return false;
    		}else {
    			return true;
    		}
    	}
    }
    
    private static Stack<Float> reverseStackF(Stack<Float> stk){
    	Stack<Float> temp = new Stack<Float>();
    	while( !stk.isEmpty() ) {
    		temp.push( stk.pop() );
    	}
    	return temp;
    }
    
    private static Stack<String> reverseStackO(Stack<String> stk){
    	Stack<String> temp = new Stack<String>();
    	while( !stk.isEmpty() ) {
    		temp.push( stk.pop() );
    	}
    	return temp;
    }
    
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	Stack<Float> stk = new Stack<Float>(); //stack that stores the values of variables and numbers
    	Stack<String> stkOperator = new Stack<String>(); //stack that stores the operators (+-/*)
    	if( expr.matches( "-?\\d+(\\.\\d+)?") ) { //base case checks for a single number and returns that number
    		stk.push( Float.parseFloat(expr) );
    		return stk.pop();
    	}
    	if( isVariable(expr) ) { //checks for single variable and returns the value of that variable 
    		Variable var = new Variable( expr );
    		int i = vars.indexOf( var );
    		stk.push( ( float ) vars.get( i ).value );
    		return stk.pop();
    	}
    	if(!expr.contains("(") && !expr.contains("[")) {
    		StringTokenizer operands = new StringTokenizer( expr.trim() , delims );
    		int numOperands = operands.countTokens();
    		String nDelims = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\t.";
    		StringTokenizer operators = new StringTokenizer ( expr.trim() , nDelims );
    		int opc = 0;
    		int nnc = 0;
    		while( operands.hasMoreElements() ) { //pushes variable values and numbers into operands stack
    			String stOperands = operands.nextToken();
    			if( stOperands.matches("-?\\d+(\\.\\d+)?") ) {
    				if( expr.charAt(0) == '-' && nnc%2 == 0) {
    					stk.push( Float.parseFloat( stOperands ) * (-1) );
    					nnc++;
    					continue;
    				}else {
        				stk.push( Float.parseFloat( stOperands ) );
    				}
    			}else {
    				Variable var = new Variable( stOperands );
    	    		int i = vars.indexOf( var );
    	    		stk.push( ( float ) vars.get( i ).value );
    			}
    		}
    		int numOp = operands.countTokens();
    		while( operators.hasMoreElements() ) { //pushes operators into operators stack
    			String stOperators = operators.nextToken();
    			if( expr.charAt( 0 ) == '-' && numOp == operands.countTokens()) {
    				numOp--;
    				continue;
    			}
    			if( stOperators.equals( "+-" ) ) {
    				stOperators = "-";
    			}else if( stOperators.equals( "--" ) ) {
    				stOperators = "+";
    			}else if(stOperators.equals( "/-" )) {
    				stOperators = "/";
    				opc++;
    			}else if( stOperators.equals( "*-") ) {
    				stOperators = "*";
    				opc++;
    			}
    			stkOperator.push( stOperators );
    		}
    		stk = reverseStackF( stk ); //reverses order of operands so that first operand is first to pop
    		stkOperator = reverseStackO( stkOperator ); //reverses order of operands so that first operator is first to pop
    		int counter = numOperands;
    		while( !stk.isEmpty() ) { //loop that performs operations
    			if(counter <= 1) {
    				break;
    			}
    			float num1 = stk.pop();
    			counter--;
    			float num2 = stk.pop();
    			counter--;
    			String optr = stkOperator.pop();   			
    			if( !stkOperator.isEmpty() ) { //starts pemdas
    				if(( stkOperator.peek().equals( "*" ) || stkOperator.peek().equals( "/") ) && (optr.equals( "-" ) || optr.equals( "+" )) ) { //multply and divide take greater precedance over + and -
    					if( stkOperator.peek().equals( "*" ) ) { //the if will switch around the values in the stack in order to prioritize the values that need *
    						float temp = num1;
        					num1 = num2;
        					num2 = stk.pop();
        					counter--;
        					String tempO = optr;
        					optr = stkOperator.pop();
        					stkOperator.push( tempO );
        					if( opc%2 != 0 ) {
        						stk.push( (num1*num2)*(-1) );
        						opc--;
        						counter++;
        					}else {
            					stk.push( num1*num2 );
            					counter++;
        					}
        					stk.push( temp );
        					counter++;
    					}else { //the if will switch around the values in the stack in order to prioritize the values that need /
    						float temp = num1;
        					num1 = num2;
        					num2 = stk.pop();
        					counter--;
        					String tempO = optr;
        					optr = stkOperator.pop();
        					stkOperator.push( tempO );
        					if( opc%2 != 0) {
        						stk.push( (num1/num2)*(-1) );
        						opc--;
            					counter++;
        					}else {
        						stk.push( num1/num2 );
            					counter++;
        					}
        					stk.push( temp );
        					counter++;
    					}
    				}else { //just does normal operations
    					if( optr.equals( "+" ) ) { //adds numbers and pushes into stack
        					stk.push( num1 + num2 );
        					counter++;
        				}else if( optr.equals( "-" ) ) {
        					stk.push( num1 - num2 );
        					counter++;
        				}else if( optr.equals( "*" ) ) {
        					if( opc%2 != 0 ) {
        						stk.push( (num1*num2)*(-1) );
        						opc--;
        						counter++;
        					}else {
            					stk.push( num1*num2 );
            					counter++;
        					}
        				}else {
        					if( opc%2 != 0 ) {
        						stk.push( (num1/num2)*(-1) );
        						opc--;
            					counter++;
        					}else {
        						stk.push( num1/num2 );
            					counter++;
        					}
        				}
    				}
    			}else { //if the operator stack has nothing left, it executes the last operation
    				if( optr.equals( "+" ) ) { //adds numbers and pushes into stack
    					stk.push( num1 + num2 );
    					counter++;
    				}else if( optr.equals( "-" ) ) { // subtracts numbers and pushes into stack
    					stk.push( num1 - num2 );
    					counter++;
    				}else if( optr.equals( "*" ) ) {
    					if( opc%2 != 0 ) {
    						stk.push( (num1*num2)*(-1) );
    						opc--;
    						counter++;
    					}else {
        					stk.push( num1*num2 );
        					counter++;
    					}
    				}else {
    					if( opc%2 != 0 ) {
    						stk.push( (num1/num2)*(-1) );
    						opc--;
        					counter++;
    					}else {
    						stk.push( num1/num2 );
        					counter++;
    					}
    				}
    			}
    		}
    		return stk.pop();
    	}
    	if( expr.contains( "(" ) && !expr.contains( "[" ) ) { //checks to see if there are parenthesis in the expression
    		if( !expr.contains("(") ) { //base case for recursion of solving parenthesis
    			return evaluate( expr , vars , arrays );
    		}else if( expr.charAt(0) == '(' && (expr.indexOf( ')', 0) == expr.length()-1 )) { //another condition that solves single pair parenthesis expressions ex.) (8+3)
    			return evaluate( expr.substring( 1, expr.length()-1) , vars , arrays );
    		}else { //last recursive block that will solve any amount of parenthesis by breaking them down and subbing them back into the expression
    			int i = expr.lastIndexOf( '(' );
    			int j = expr.indexOf( ')', i);
    			float res = evaluate( expr.substring( i+1 , j ), vars, arrays );
    			expr = expr.replace( expr.substring( i, j+1 ), Float.toString( res ) );
    			return evaluate (expr, vars, arrays );
    		}
    	}
    	if( expr.contains( "[" ) ) {
    		if( !expr.contains( "[" ) ) {
    			return evaluate( expr, vars, arrays );
    		}else {
    			int subi = expr.lastIndexOf( "[" ); //gets the index of the last [ indicating the most nested array
    			int subj = expr.indexOf( "]" , subi); //gest the index of the first ] from [
    			int namei = 0; //integer used to find the first index if the name
    			for(int i = subi-1; i>=0; i--) { // a loop that gets the name of the most nested array
    				namei = i;
    				if( Character.isDigit( expr.charAt( i ) ) ){
    					namei += 1;
    					break;
    				}else if( expr.charAt( i ) == '-' || expr.charAt( i ) == '+' || expr.charAt( i ) == '*' || expr.charAt( i ) == '/' || expr.charAt( i ) == '[' || expr.charAt( i ) == '(') {
    					namei += 1;
    					break;
    				}
    			}
    			String name = expr.substring( namei , subi); //sets the name of the array into a string
    			Array arr = new Array(name); //makes an Array object in order to find the array and its values
    			int i = arrays.indexOf( arr ); //gets the index of that array in the array list
    			float res = evaluate( expr.substring( subi+1, subj), vars, arrays ); //evaluates sub expression inside of array
    			String temp = Float.toString(res); //parses the float answer into  string
    			StringTokenizer temp3 = new StringTokenizer( temp , "." ); //gets rid of decimal so 2.0 becomes 2 and 0, I only want the 2
    			String temp4 = temp3.nextToken(); //a string equal to the token, with the example above it would be 2
    			int arrIndex = Integer.parseInt(temp4); //is the index of the array, which was received from solving the sub expression
    			String sub = Integer.toString( arrIndex ); //makes a substitution string to place back in to the string
    			expr = expr.replace( expr.substring( subi+1, subj ) , sub ); //substitutes subexpression inside array with evaluated expression ex) A[1+2] = A[3]
    			int[] array = arrays.get(i).values; //makes an int array for the array that was just found
    			int value = array[arrIndex]; //gets the value of the array at that index
    			String sub2 = Integer.toString( value ); //makes a second substitution back into the expression
    			subj = expr.indexOf( "]" , subi );
    			expr = expr.replace( expr.substring( namei , subj+1 ), sub2);
    			return evaluate( expr, vars, arrays);
    		}
    	}
    	
    	return 0;
    }
}
