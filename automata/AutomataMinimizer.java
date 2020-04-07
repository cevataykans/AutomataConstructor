package automata;

import java.util.HashSet;
import java.util.*;

public class AutomataMinimizer
{
	private int[][] symbolTable;
	private Automaton<Integer, Character> other;
	private boolean[] visited; // ( 0 - not visited ) ( 1 - distinguishable ) ( -1 - equivalent )

	public AutomataMinimizer( Automaton other)
	{
		this.other = other;

		this.visited = new boolean[ other.getStates().size() ];
		this.constructSymbolTable();
		this.printTable();
	}


	private void constructSymbolTable()
	{
		symbolTable = new int[ other.getStates().size() ][ other.getStates().size() ];

		// Get the accepting states and set up basis
		Set<Integer> acceptingStates = other.getAcceptingStates();
		this.setupInitialTable( acceptingStates);

		// Back track tp find all states to distinguish them
		Set< Integer> preSuccessors = getPreSuccessors( acceptingStates, true);
		while ( preSuccessors.size() > 0)
		{
			// For each state in the presuccessor set, compare them with every other state
			for ( Integer state : preSuccessors)
			{
				for ( Integer otherState : other.getStates() )
				{
					if ( !state.equals( otherState) )
					{
						compareStates( state, otherState); //************************************************
					}
				}
			}
			preSuccessors = getPreSuccessors( preSuccessors, true); // I used to pre successor to get the pre successors!
		}
	}

	/**
	 * Returns every state that has a connection to the destination state "dest"
	 * @param keyDest destination state that is being searched
	 * @return the set of all states that leads to the destination state regarding any input
	 */
	private Set<Integer> getPreSuccessors( Integer keyDest, boolean save ) //+
	{
		Set<Integer> preSuccessors = new HashSet<>();

		// For each src and its destination check if the destination is the key destination we are looking for
		for ( Integer src : other.getTransitions().keySet() )
		{
			if ( !visited[ src])
			{
				for (Integer dst : other.getTransitions().get(src).keySet()) {
					// If it is, add the src to the pre-successor set, return back to the first loop.
					if (dst.equals(keyDest)) {
						preSuccessors.add(src);

						if (save) {
							visited[src] = true;
						}
						break;
					}
				}
			}
		}
		return preSuccessors;
	}

	private Set<Integer> getPreSuccessors( Set<Integer> states, boolean save) //+
	{
		Set<Integer> preSuccessors = new HashSet<>();
		for ( Integer keyDest : states)
		{
			preSuccessors.addAll( getPreSuccessors( keyDest, save) );
		}
		return preSuccessors;
	}

	// Sets up the basis table where each accepting state is differentiated from non accepting states
	private void setupInitialTable( Set<Integer> acceptingStates)
	{
		// Differentiate every accepting state with non accepting states
		for ( Integer acceptingState : acceptingStates)
		{
			// If this state is disconnected, erase it, return back to loop for the next state
			if ( getPreSuccessors( acceptingState, false).size() == 0 )
			{
				other.trans.remove( acceptingState);
				other.accepting.remove( acceptingState);
			}
			else {
				// First go over the accepting state row
				for (int y = 0; y < acceptingState; y++) {
					if (!acceptingStates.contains(y)) {
						symbolTable[acceptingState][y] = 1;
					}
					else
					{
						compareStates( acceptingState, y ); //************************************************
					}
				}

				// Secondly, go over the accepting state column
				for (int x = acceptingState + 1; x < symbolTable.length; x++) {
					if (!acceptingStates.contains(x)) {
						symbolTable[x][acceptingState] = 1;
					}
					else
					{
						compareStates( x, acceptingState); //************************************************
					}
				}
			}
			visited[ acceptingState] = true;
		}
	}


	private void compareStates( Integer src, Integer compareTo)
	{

	}


	private void printTable() //+
	{
		for ( int x = 0; x < symbolTable.length; x++)
		{
			System.out.print( x + " -> ");
			for ( int y = 0; y < symbolTable.length; y++)
			{
				if ( symbolTable[ x][ y] == 0)
				{
					System.out.print( " (" + y + "->" + "0) ");
				}
				else if ( symbolTable[ x][ y] == 1)
				{
					System.out.print( " (" + y + "->" + "1) ");
				}
				else if ( symbolTable[ x][ y] == -1)
				{
					System.out.print( " (" + y + "->" + "--1) ");
				}
				else
				{
					System.out.println( x + ":" + y);
					return;
				}

			}
			System.out.println( " -");
		}
	}

}
