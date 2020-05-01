package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		/* IMPLEMENT THIS METHOD */
		BigInteger newInt=new BigInteger();
		DigitNode ptr=newInt.front,node=null;
		integer=integer.trim();
		while(integer.length()>=1) {
			if(integer.charAt(0)=='-') {
				newInt.negative=true;
				integer=integer.substring(1);
				if(integer.length()==1&&integer.charAt(0)=='0') {
					newInt.negative=false;
					return newInt;
				}
			}
			if(integer.charAt(0)=='+') {
				integer=integer.substring(1);
			}
			if(integer.charAt(0)=='0') {
				integer=integer.substring(1);
			}else {
				break;
			}
		}
		for(int i=integer.length()-1;i>=0;i--) {
			if(integer.charAt(i)==' ') {
				throw new IllegalArgumentException();
			}
			if(Character.isDigit(integer.charAt(i))==false) {
				throw new IllegalArgumentException();
			}
			if(newInt.front==null) {
				newInt.front=new DigitNode(Integer.parseInt(integer.substring(i,i+1)),null);
				ptr=newInt.front;
				newInt.numDigits++;
			}else {
				node=new DigitNode(Integer.parseInt(integer.substring(i,i+1)),null);
				ptr.next=node;
				ptr=ptr.next;
				newInt.numDigits++;
			}
		}
		// following line is a placeholder for compilation
		
		System.out.println(newInt.numDigits);
		return newInt;
	}

	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger sum=new BigInteger(),first1=new BigInteger(),second2=new BigInteger();
		String str1=first.toString();
		String str2=second.toString();
		if(first.negative==true&&second.negative==true) {
			sum.negative=true;
		}
		int tSum,diff;
		if(second.numDigits>first.numDigits) {
			first1=parse(str2);
			second2=parse(str1);
		}else {
			first1=parse(str1);
			second2=parse(str2);
		}
		DigitNode ptr1=first1.front,ptr2=second2.front,ptr=sum.front;
		if(first.negative==true^second.negative==true) {
			if(first.front==null) {
				return sum=second;
			}else if(second.front==null) {
				return sum=first;
			}
			if(first.numDigits==second.numDigits) {
				boolean greaterThan=false;
				while(ptr1!=null) {
					if(ptr1.digit>ptr2.digit) {
						greaterThan=true;
					}else if(ptr1.digit==ptr2.digit) {
						ptr1=ptr1.next;
						ptr2=ptr2.next;
						continue;
					}else {
						greaterThan=false;
					}
					ptr1=ptr1.next;
					ptr2=ptr2.next;
				}
				if(greaterThan==false) {
					BigInteger temp=first1;
					first1=second2;
					second2=temp;	
				}
			}
			ptr1=first1.front;
			ptr2=second2.front;
			if(first1.negative==true) {
				sum.negative=true;
			}
			while(ptr2!=null) {
				diff=ptr1.digit-ptr2.digit;
				if(sum.front==null) {
					if(diff<0) {
						if(ptr1.next!=null) { 
							if(ptr1.next.digit==0) {
								ptr1.digit+=10;
								DigitNode ptr3=first1.front.next;
								while(ptr3.digit==0) {
									ptr3.digit=9;
									ptr3=ptr3.next;
								}
								ptr3.digit-=1;
							}else {
								ptr1.digit+=10;
								ptr1.next.digit-=1;
							}
						}else {
							sum.front=new DigitNode(Math.abs(diff),null);
							sum.numDigits++;
							if(ptr2.digit>ptr1.digit) {
								sum.negative=false;
							}
							ptr=sum.front;
							ptr1=ptr1.next;
							ptr2=ptr2.next;
							continue;
						}
						diff=ptr1.digit-ptr2.digit;
						sum.front=new DigitNode(diff,null);
						sum.numDigits++;
						ptr=sum.front;
					}else {
						sum.front=new DigitNode(diff,null);
						sum.numDigits++;
						ptr=sum.front;
					}
				}else {
					if(diff<0) {
						if(ptr1.next.digit==0) {
							ptr1.digit+=10;
							DigitNode ptr3=ptr1.next;
							while(ptr3.digit==0) {
								ptr3.digit=9;
								ptr3=ptr3.next;
							}
							ptr3.digit-=1;
						}else {
							ptr1.digit+=10;
							ptr1.next.digit-=1;
						}
						diff=ptr1.digit-ptr2.digit;
						ptr.next=new DigitNode(diff,null);
						sum.numDigits++;
						ptr=ptr.next;
					}else {
						ptr.next=new DigitNode(diff,null);
						sum.numDigits++;
						ptr=ptr.next;
					}
				}
				ptr1=ptr1.next;
				ptr2=ptr2.next;
			}
			if(ptr1!=null) {
				while(ptr1!=null) {
					if(ptr1.digit!=0) {
						ptr.next=new DigitNode(ptr1.digit,null);
						sum.numDigits++;
						ptr1=ptr1.next;
						ptr=ptr.next;
					}else {
						if(ptr1.digit==0&&ptr1.next!=null) {
							ptr.next=new DigitNode(ptr1.digit,null);
							sum.numDigits++;
							ptr1=ptr1.next;
							ptr=ptr.next;
						}else {
							ptr1=ptr1.next;
						}
					}
				}
			}
		}else {
			if(first1.front==null) {
				return second2;
			}else if(second2.front==null) {
				return first1;
			}
			if(second2.numDigits>first1.numDigits) {
				BigInteger temp=second2;
				second2=first1;
				first1=temp;
				ptr1=first1.front;
				ptr2=second2.front;
			}
			while(ptr2!=null) {
				tSum=ptr1.digit+ptr2.digit;
				if(sum.front==null) {
					 if(Math.abs(tSum)>9) {
						 sum.front=new DigitNode(tSum%10,null);
						 sum.numDigits++;
						 ptr=sum.front;
						ptr.next=new DigitNode(1,null);
						sum.numDigits++;
					 }else {
						 sum.front=new DigitNode(tSum,null);
						 sum.numDigits++;
						 ptr=sum.front;
					 }
				}else {
					if(ptr.next!=null) {
						if(Math.abs(tSum)>9) {
							 ptr.next.digit+=tSum;
							 int temp=ptr.next.digit/10;
							 ptr.next.digit=ptr.next.digit%10;
							 ptr=ptr.next;
								 ptr.next=new DigitNode(temp,null);
								 sum.numDigits++;
						 }else {
							 ptr.next.digit+=tSum;
							 ptr=ptr.next;
							 if(ptr.digit>9) {
								 int temp=ptr.digit/10;
								 ptr.digit=ptr.digit%10;
									 ptr.next=new DigitNode(temp,null);
									 sum.numDigits++;
							 }		 
						 }
					}else {
						if(tSum>9) {
							int temp=tSum/10;
							ptr.next=new DigitNode(tSum%10,null);
							ptr=ptr.next;
							ptr.next=new DigitNode(temp,null);
							sum.numDigits++;
						}else {
							ptr.next=new DigitNode(tSum,null);
							sum.numDigits++;
							ptr=ptr.next;
						}
					}
				}
				ptr1=ptr1.next;
				ptr2=ptr2.next;
			}
			DigitNode ptr3=ptr;
			ptr=ptr.next;
			if(ptr1!=null) {
				while(ptr1!=null) {
						if(ptr!=null) {
							tSum=ptr.digit+ptr1.digit;
							if(Math.abs(tSum)>9) {
								 int temp=tSum/10;
								 ptr.digit=tSum%10;
									 ptr.next=new DigitNode(temp,null);
									 sum.numDigits++;
							 }else {
								 ptr.digit=tSum;
							 }
						}else {
							ptr=new DigitNode(ptr1.digit,null);
							ptr3.next=ptr;
							ptr3=ptr3.next;
						}
					ptr1=ptr1.next;
					ptr=ptr.next;
					if(ptr3.next!=null) {
						ptr3=ptr3.next;
					}
				}
			}
		}
		sum=parse(sum.toString());
		
		System.out.println("numDigits: " + sum.numDigits);
		return sum;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		BigInteger product= new BigInteger();
		if(first.front==null||second.front==null) {
			return product;
		}
		BigInteger first1=new BigInteger(),second2=new BigInteger();
		String str1=first.toString(),str2=second.toString();
		if(first.numDigits>second.numDigits||first.numDigits==second.numDigits) {
			first1=parse(str1);
			second2=parse(str2);
		}else {
			first1=parse(str2);
			second2=parse(str1);
		}
		DigitNode ptr1=first1.front,ptr2=second2.front,ptr=null;
		BigInteger product2=new BigInteger();
		int p=0,r=0,c=0;
		while(ptr2!=null) {
			for(int i=c;i>0;i--) {
				if(product2.front==null) {
					product2.front=new DigitNode(0,null);
					ptr=product2.front;
				}else {
					ptr.next=new DigitNode(0,null);
					ptr=ptr.next;
				}
			}
			while(ptr1!=null) {
				p=(ptr1.digit*ptr2.digit)+r;
				if(product2.front==null) {
					if(p>=10) {
						r=p/10;
						product2.front=new DigitNode(p%10,null);
						ptr=product2.front;
					}else {
						r=p/10;
						product2.front=new DigitNode(p,null);
						ptr=product2.front;
					}
				}else {
					if(p>=10) {
						r=p/10;
						ptr.next=new DigitNode(p%10,null);
						ptr=ptr.next;
					}else {
						r=p/10;
						ptr.next=new DigitNode(p,null);
						ptr=ptr.next;
					}
				}
				if(r!=0&&ptr1.next==null) {
					ptr.next=new DigitNode(r,null);
					ptr=ptr.next;
					r=0;
				}
				ptr1=ptr1.next;
			}
			product=add(product,product2);
			product2.front=null;
			ptr1=first1.front;
			ptr2=ptr2.next;
			r=0;
			c++;
			ptr=product2.front;
		}
		if(first.negative==true^second.negative==true) {
			product.negative=true;
		}else if(first.negative==true&&second.negative==true) {
			product.negative=false;
		}else {
			product.negative=false;
		}
		product=parse(product.toString());
		return product;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
