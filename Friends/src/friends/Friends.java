package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		// breadth first search queue
		
		//p1 cannot equal p2
		if( p1.equals( p2 ) ) {
			return null;
		}
		
		Queue< Integer > bfsQ = new Queue< Integer >();
		
		//stack to store the shortest route to a friend
		Stack < Integer > shortest = new Stack< Integer >();
		
		// array that keeps track of the previous person along the route
		int[] previous = new int[ g.members.length ];
		
		boolean[] visited = new boolean[ g.members.length ];
		
		for( int i = 0; i < visited.length ; i++) {
			
			visited[ i ] = false;
			
		}
		
		//sets the index of p1 to have a previous of -1, a dummy value to indicate a stopping point when adding to the stack
		previous[ g.map.get( p1 ) ] = -1;
				
		//visits the first person and enqueues to begin breadth first
		bfsQ.enqueue( g.map.get( p1 ) );
		
		visited[ g.map.get( p1 ) ] = true;
		//begins breadth first
		
		while ( !bfsQ.isEmpty() ) {
			int w = bfsQ.dequeue();
			
			for( Friend nbr = g.members[ w ].first ; nbr != null ; nbr = nbr.next ) {
				//fills out previous array
				if( visited[ nbr.fnum ] == false ) {
					visited [ nbr.fnum ] = true;
					previous [ nbr.fnum ] = w;
					bfsQ.enqueue( nbr.fnum );
				}
				
			}
			
		}
		
		//used to traverse previous array and insert shortest path into the stack
		int key = g.map.get( p2 );
		if( previous[ key ] == 0 && visited[ key ] == false ) {
			return null;
		}
		while( key != -1) {
			shortest.push( key );
			key = previous[ key ];
		}
		
		//array list to be returned mapping out the ideal shortest path from one friend to another 
		ArrayList< String > answer = new ArrayList< String >();
		
		//takes items from the stack and adds them into the array list
		while ( !shortest.isEmpty() ) {
			answer.add( g.members[ shortest.pop() ].name );
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return answer;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		//array list that stores the cliques
		ArrayList< ArrayList< String > > cliques = new ArrayList< ArrayList< String > >();
		
		//boolean array to keep track of visited vertices in graph
		boolean[] visited = new boolean[ g.members.length ]; 
		
		//initializes all indexes to false in the visited array, as nothing has been visited yet
		for(int i = 0; i < visited.length ; i++) {
			visited[ i ] = false;
		}
		
		//checks to see if the school is even in the graph, else it returns null
		int counter = 0;
		for (int i = 0; i < g.members.length; i++) {
			if( g.members[ i ].student ) {
				if( g.members[ i ].school.equals( school ) ) {
					counter++;
				}
			}
		}
		if( counter == 0 ) {
			return null;
		}
		
		//queue used for BFS
		Queue< Integer > bfsQ = new Queue< Integer >();
		
		//starts BFS, for loop serves as a driver
		for( int i = 0 ; i < g.members.length ; i++) {
			if( visited[ i ] ) {
				continue;
			}
			
			//Array list to keep track of individual cliques in a school
			ArrayList< String > cliqueList = new ArrayList< String >();
			
			//visits the member
			visited[ i ] = true;
			
			//does BFS on cliques in a school
			if( g.members[ i ].student && g.members[ i ].school.equals( school ) ) {
				
				//adds student to the array, and enqueues 
				cliqueList.add( g.members[ i ].name );
				bfsQ.enqueue( i );
				
				//does BFS
				while( !bfsQ.isEmpty() ) {
					int w = bfsQ.dequeue();
					for( Friend nbr = g.members[ w ].first; nbr != null; nbr = nbr.next ) {
						if( visited[ nbr.fnum ] == false ) {
							if( g.members[ nbr.fnum ].student && g.members[ nbr.fnum ].school.equals( school ) ) {
								cliqueList.add( g.members[ nbr.fnum ].name );
								bfsQ.enqueue( nbr.fnum );
							}
							visited[ nbr.fnum ] = true;
						}
					}
					
					//adds clique when there are no more remaining
					if( bfsQ.isEmpty() ) {
						cliques.add( cliqueList );
					}
				}
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return cliques;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		//array to keep track of the dfs number and the back number as talked about in the writeup
		int[] dfsNum = new int [ g.members.length ];
		int[] backNum = new int[ g.members.length ]; 
		
		//both arrays are set to be 0 initally
		for ( int i = 0; i <  dfsNum.length ; i++ ) {
			dfsNum[ i ] = 0;
		}
		for ( int i = 0; i < backNum.length; i++) {
			backNum[ i ] = 0;
		}
		
		//visited array
		boolean[] visited = new boolean[ g.members.length ];
		for( int i = 0; i < visited.length ; i++ ) {
			visited[ i ] = false;
		}
		
		//returned array list
		ArrayList< String > connectors = new ArrayList< String >();
		
		//start DFS
		for( int v = 0; v < visited.length; v++) {
			if( visited[ v ] ) {
				continue;
			}
			
			int startVertex = g.map.get( g.members[ v ].name );
			boolean startConnect = true;
			ArrayList< Integer > islands = new ArrayList< Integer >();
			dfs( startVertex, startVertex, visited, g, connectors, dfsNum, backNum, 1, islands);
			
			//goes through the islands
//			for( int i = 0; i < islands.size(); i++ ) {
//				
//				//checks to see if the start is a vertex
//				if( backNum[ islands.get( i ) ] > islands.get( 0 ) ) {
//					startConnect = false;
//					break;
//				}
//			}
			
			//adds the start if it is a connector
//			if( startConnect ) {
//				connectors.add( g.members[ startVertex ].name );
//			}
			
		}
		
		for( int i = 0; i < connectors.size(); i++) {
			String item = connectors.get( i );
			for( int j = i + 1; j < connectors.size(); j++) {
				if( item.equals( connectors.get( j ) ) ) {
					connectors.remove( j );
				}
			}
		}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return connectors;
		
	}
	
	
	//DFS method
	private static void dfs ( int v, int startVertex, boolean[] visited, Graph g, ArrayList< String > connectors, 
			int[] dfsNum, int[] backNum, int iteration, ArrayList< Integer > islands ) {
		visited[ v ] = true;
		dfsNum[ v ] = iteration;
		backNum[ v ] = iteration;
//		islands.add( v );
		for( Friend e = g.members[v].first; e != null; e = e.next ) {
			
			//if a neighbor is already visited then back(v) is set to min of back v and dfsnum w
			if( visited[ e.fnum ] ) {
				backNum[ v ] =  Math.min( backNum[ v ], dfsNum[ e.fnum ] );
			}
			if( !visited[ e.fnum ] ) {
				iteration++;
				dfs( e.fnum, startVertex, visited, g, connectors, dfsNum, backNum, iteration, islands );
				if( dfsNum[ v ] > backNum[ e.fnum ] ) {
					backNum[ v ] = Math.min( backNum[ v ], backNum[ e.fnum ] );
				} else if( startVertex != v ) {
					connectors.add( g.members[ v ].name );
				}
			}
		}
		return;
	}
}

