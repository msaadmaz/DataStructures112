package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() { }

	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/

		//initializes the root and the first child to the first word in the array

		TrieNode root = new TrieNode( null , new TrieNode(new Indexes(0 ,(short) 0 ,(short) ( allWords[0].length() - 1 )  ) , null , null ) , null ) ;

		TrieNode ptr = root.firstChild ;

		for(int i = 1; i < allWords.length; i++) {

			//initializes the root as the roots first child each time
			ptr = root.firstChild;

			//gets the previous word at the node and stores it in to a string variable
			String prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );

			//gets the current word and stores it in to a string variable
			String word = allWords[ i ];
			
			//handles case for different lettered words
			if( word.charAt( 0 ) != prevWord.charAt( 0 ) ) {
				if( ptr.sibling != null ) {
					int ccounter = 0;
					while( ptr.sibling != null ) {
						ptr = ptr.sibling;
						if( allWords[ ptr.substr.wordIndex ].charAt( 0 ) == word.charAt( 0 ) ) {
							ccounter++;
							break;
						}
					}
					prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );
					if( ccounter < 1 && ptr.sibling == null) {
						ptr.sibling = new TrieNode( new Indexes( i , (short) 0 , (short) (word.length() - 1) ), null, null );
						ptr = ptr.sibling;
						continue;
					}
				} else {
					ptr.sibling = new TrieNode( new Indexes( i , (short) 0 , (short) (word.length() - 1) ), null, null );
					ptr = ptr.sibling;
					continue;
				}
			}
			
			//finds the index to where the prefix ends in the word, or finds the last index to where the two words have a commonality (establishes a prefix)
			int index = 0;

			//makes the initial prefix if there is none
			if( ptr.firstChild == null ) {

				//a loop to set the index
				for(int j = 0; j < prevWord.length(); j++) {

					//increments index for every matched character
					if( prevWord.charAt( j ) == word.charAt( j ) ) {
						index++;
					}else {
						break;
					}
				}

				//subtracts 1 from the index, to make it more like an index
				index--;


				//makes the current node in to a prefix node
				ptr.substr = new Indexes( ptr.substr.wordIndex , (short) 0 , (short) index );

				//give the prefix a child for the remaining word
				ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) (index + 1), (short) (prevWord.length() - 1)), null, null );
				ptr = ptr.firstChild;

				//gives the first child a sibling for the remaining current word
				ptr.sibling = new TrieNode( new Indexes( i , (short) (index + 1), (short) (word.length() - 1) ), null, null);
			} else {

				//gets the length of the prefix
				int preLength = (ptr.substr.endIndex - ptr.substr.startIndex) + 1;
				
				if( preLength > word.length() ) {
					int o = 0;
					while ( word.charAt( o ) != prevWord.charAt( o ) && ptr.firstChild != null ) {
						ptr = ptr.firstChild;
						o++;
					}
					
				} else {
					
					//loops through prefixes to get ptr to the proper node, while the word contains the prefix, and there is a child
					while( word.substring( ptr.substr.startIndex , preLength  ).equals( prevWord ) && ptr.firstChild != null ) {
						prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );
						preLength = (ptr.substr.endIndex - ptr.substr.startIndex) + 1;
						ptr = ptr.firstChild;
					} 

				}
				
				//updates the previous word
				prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );
				
				if( word.length() >= ptr.substr.endIndex + 1) {
					if( word.substring( ptr.substr.startIndex , ptr.substr.endIndex + 1).equals( prevWord ) && ptr.firstChild != null ) {
						ptr = ptr.firstChild;
					}
				}
				
				prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );

				int commonLetters = 0;
				int l = 0;
				if( prevWord.length() > word.length() ) {
					
					for(int j = ptr.substr.startIndex; j < word.length() ; j++) {
						if( prevWord.charAt( l ) == word.charAt( j ) ) {
							commonLetters++;
							l++;
						}else {
							break;
						}
					}
					
				} else {
					
					//a loop to set the index
					for(int j = ptr.substr.startIndex; j < ptr.substr.endIndex + 1; j++) {
						//increments index for every matched character
						if( prevWord.charAt( l ) == word.charAt( j ) ) {
							commonLetters++;
							l++;
						}else {
							break;
						}
					}
					
				}

				//the index is now updated to only encompass the common letters
				index = ptr.substr.endIndex - commonLetters;

				//adds the remaining word to a sibling if there are no more common elements
				if( commonLetters == 0 && ptr.sibling != null ) {
					
					index = ptr.substr.startIndex;

					int counter = 0;

					TrieNode prev = ptr;

					//gets the ptr to the last sibling
					while( ptr != null ) {

						//resets the prev word
						prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );


						//a boolean to see if the first letter of the prefix matches the adjacent letter in the word
						boolean similarLetter = word.charAt( ptr.substr.startIndex ) == prevWord.charAt( 0 );

						//breaks if a similar Letter is found
						if( similarLetter ) {
							counter++;
							break;
						}

						prev = ptr;
						ptr = ptr.sibling;

					}

					if(prev.sibling == null) {

						//resets the prev word
						prevWord = allWords[ prev.substr.wordIndex ].substring( prev.substr.startIndex , prev.substr.endIndex + 1 );
					} else {

						//resets the prev word
						prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );
					}

					//adds the word straight after if there is not sibling prefix that matches
					if( prev.sibling == null && counter <=0 ) {
						prev.sibling = new TrieNode( new Indexes( i , (short) prev.substr.startIndex , (short) (word.length() - 1) ), null, null ); //changed from index to prev.substr.startIndex
					} else {

						int k = ptr.substr.startIndex;
						//a loop to set the index
						for(int j = 0; j < prevWord.length(); j++) {
							//increments index for every matched character
							if( prevWord.charAt( j ) == word.charAt( k ) ) {
								commonLetters++;
								k++;
							}else {
								break;
							}
						}

						if( commonLetters == 1) {
							ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ptr.substr.startIndex );
						} else {
							ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ( ptr.substr.startIndex + commonLetters - 1) );
						}

						String prefixWord = allWords[ ptr.substr.wordIndex ]; 

						if( ptr.firstChild == null) {

							//give the prefix a child for the remaining word
							ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) ( ptr.substr.endIndex + 1), (short) ( prefixWord.length() - 1) ), null, null );
							ptr = ptr.firstChild;

							if(ptr.sibling == null) {

								//gives the first child a sibling for the remaining current word
								ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

							} else {

								//gets you to the last sibling
								while(ptr.sibling != null ) {
									ptr = ptr.sibling;
								}

								//gives the first child a sibling for the remaining current word
								ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

							}

						} else if ( word.substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 ).equals( prevWord) ) {
							ptr = ptr.firstChild;

							//checks to see if the sibling is null, if it is then it will add a sibling normally, else it will get to the last sibling and add one there
							if(ptr.sibling == null) {

								//gives the first child a sibling for the remaining current word
								ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

							} else {

								//gets you to the last sibling
								while(ptr.sibling != null ) {
									ptr = ptr.sibling;
								}
								
								prevWord = allWords[ ptr.substr.wordIndex ].substring( ptr.substr.startIndex , ptr.substr.endIndex + 1 );
								
								if( word.charAt( ptr.substr.startIndex ) == prevWord.charAt( 0 ) ) {
									 k = ptr.substr.startIndex;
									 commonLetters = 0;
										for(int j = 0; j < prevWord.length(); j++) {
											if( prevWord.charAt( j ) == word.charAt( k ) ) {
												commonLetters++;
												k++;
											}else {
												break;
											}
										}
										if( commonLetters == 1) {
											ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ptr.substr.startIndex );
										} else {
											ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ( ptr.substr.startIndex + commonLetters - 1) );
										}
										prefixWord = allWords[ ptr.substr.wordIndex ]; 
										if( ptr.firstChild == null) {
											ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) ( ptr.substr.endIndex + 1), (short) ( prefixWord.length() - 1) ), null, null );
											ptr = ptr.firstChild;
											if(ptr.sibling == null) {
												ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
											} else {
												while(ptr.sibling != null ) {
													ptr = ptr.sibling;
												}
												ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
											}
										} else {
											ptr = ptr.firstChild;
											if(ptr.sibling == null) {
												ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
											} else {
												while(ptr.sibling != null ) {
													ptr = ptr.sibling;
												}
												ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
											}
											
										}
										
								} else {
									//gives the first child a sibling for the remaining current word
									ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
								}

								

							}
						} else {

							ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) ( ptr.substr.endIndex + 1 ), (short) ( ptr.firstChild.substr.startIndex -1 ) ), ptr.firstChild , null );
							ptr = ptr.firstChild;

							//checks to see if the sibling is null, if it is then it will add a sibling normally, else it will get to the last sibling and add one there
							if(ptr.sibling == null) {

								//gives the first child a sibling for the remaining current word
								ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

							} else {

								//gets you to the last sibling
								while(ptr.sibling != null ) {
									ptr = ptr.sibling;
								}

								//gives the first child a sibling for the remaining current word
								ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

							}

						}

					}


					continue;

				}

				Indexes comp = ptr.substr;
				
				//makes the current node in to a prefix node
				if( commonLetters == 1 ) {
					ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ptr.substr.startIndex );
				} else {
					ptr.substr = new Indexes( ptr.substr.wordIndex , (short) ptr.substr.startIndex , (short) ( ptr.substr.startIndex + commonLetters - 1) );
				}
				

				String prefixWord = allWords[ ptr.substr.wordIndex ]; 

				if( ptr.firstChild == null) {

					//give the prefix a child for the remaining word
					ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) ( ptr.substr.endIndex + 1 ), (short) ( prefixWord.length() - 1) ), null, null );
					ptr = ptr.firstChild;

					if(ptr.sibling == null) {

						//gives the first child a sibling for the remaining current word
						ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

					} else {

						//gets you to the last sibling
						while(ptr.sibling != null ) {
							ptr = ptr.sibling;
						}

						//gives the first child a sibling for the remaining current word
						ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

					}



				} else {

					//give the prefix a child for the remaining word
					if( comp.wordIndex == ptr.substr.wordIndex && comp.startIndex == ptr.substr.startIndex && comp.endIndex == ptr.substr.endIndex ) {
						ptr = ptr.firstChild;
						
						//gets you to the last sibling
						while(ptr.sibling != null ) {
							ptr = ptr.sibling;
						}

						//gives the first child a sibling for the remaining current word
						ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);
						continue;
					}

					ptr.firstChild = new TrieNode( new Indexes( ptr.substr.wordIndex, (short) ( ptr.substr.endIndex + 1 ), (short) ( ptr.firstChild.substr.startIndex -1 ) ), ptr.firstChild , null );

					ptr = ptr.firstChild;

					//checks to see if the sibling is null, if it is then it will add a sibling normally, else it will get to the last sibling and add one there
					if(ptr.sibling == null) {
						//gives the first child a sibling for the remaining current word
						ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

					} else {
						

						//gets you to the last sibling
						while(ptr.sibling != null ) {
							ptr = ptr.sibling;
						}

						//gives the first child a sibling for the remaining current word
						ptr.sibling = new TrieNode( new Indexes( i , (short) ( ptr.substr.startIndex ), (short) (word.length() - 1) ), null, null);

					}

				}



			}




		}



		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
			String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList< TrieNode > compList = new ArrayList< TrieNode >();
		TrieNode ptr = root;
		
		//base case
		if( root == null ) {
			
			return null; 
			
		}
		
		while(ptr != null) {
			
			if( ptr.substr == null ) {
				
				ptr = ptr.firstChild;
				
			}
			
			String prevWord = allWords[ ptr.substr.wordIndex ] , pprefix = prevWord.substring( 0, ptr.substr.endIndex + 1 ) ; //prevWord is the whole word, pprefix is the prefix of that word
			
			if( prevWord.startsWith( prefix , 0 ) || prefix.startsWith( pprefix , 0 ) ) {				
				if(ptr.firstChild == null) {				
					compList.add(ptr);
					ptr = ptr.sibling;								
				} else { 		
					compList.addAll ( completionList ( ptr.firstChild, allWords, prefix ) ); //will add every other leaf node if ptr.firstChild != null
					ptr = ptr.sibling;
				}
			} else {	
				ptr = ptr.sibling;
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		return compList;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
					.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}

		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
}
