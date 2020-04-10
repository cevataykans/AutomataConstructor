package automata;

import java.util.HashSet;
import java.util.*;

public class AutomataMinimizer
{
	private int[][] symbolTable; // ( 0 - not visited ) ( 1 - distinguishable ) ( -1 - equivalent )
	private Automaton<Integer, Character> other;
	private boolean[] visited;

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
		this.setupDiagonal();

		// Back track tp find all states to distinguish them
		Set< Integer> preSuccessors = getPreSuccessors( acceptingStates, true);
		while ( preSuccessors.size() > 0)
		{
			// For each state in the presuccessor set, compare them with every other state
			for ( Integer state : preSuccessors)
			{
				System.out.println( "States are: " + state);

				for ( int i = 0; i < state; i++)
				{
					//symbolTable[ state][ i] = 5;
					compareStates( state, i);
				}

				for ( int i = state + 1; i < symbolTable.length; i++)
				{
					//symbolTable[ i][ state] = 5;
					compareStates( i, state);
				}

			}
				/*for ( Integer otherState : other.getStates() ) //other.getStates()
				{
					if ( !state.equals( otherState) )
					{
						//compareStates( state, otherState); //************************************************
						System.out.println( "Comparing state: " + state + " to other state: " + otherState);
					}
				}
			}*/
			System.out.println( );
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
				/*other.trans.remove( acceptingState);
				other.accepting.remove( acceptingState);
				System.out.println( "Removed:" + acceptingState);*/
			}
			else
			{
				// First go over the accepting state row
				for ( int y = 0; y < acceptingState; y++) {

					if ( !acceptingStates.contains(y))
					{
						symbolTable[ acceptingState][ y] = 1;
					}
					else
					{
						compareStates( acceptingState, y);
					}
				}

				// Secondly, go over the accepting state column
				for ( int x = acceptingState + 1; x < symbolTable.length; x++) {

					if ( !acceptingStates.contains(x) )
					{
						symbolTable[ x][ acceptingState] = 1;
					}
					else
					{
						compareStates( x, acceptingState);
					}
				}
			}
			visited[ acceptingState] = true;
		}
	}

	// You cannot differantiate a state from itself!
	private void setupDiagonal()
	{
		for ( int i = 0; i < symbolTable.length; i++)
		{
			symbolTable[ i][ i] = -1;
		}
	}


	private void compareStates( Integer src, Integer compareTo)
	{
		if ( symbolTable[ src][ compareTo] == 0)
		{
			Set<Character> symbols = this.other.getSymbols();
			for ( Character ch : symbols )
			{
				// Get the successors
				Set<Integer> srcSuccessor = this.other.getSuccessors(src, ch);
				Set<Integer> compareToSuccessor = this.other.getSuccessors(compareTo, ch);
				if ( srcSuccessor.size() > 2 || compareToSuccessor.size() > 2)
				{
					System.out.println( "There is a successor problem");
				}

				// If one does not have a successor and the other does, surely they are differentiable.
				// This is because one translates into trap state, other does not.
				if ( (srcSuccessor.size() == 0 && compareToSuccessor.size() != 0) || (srcSuccessor.size() != 0 && compareToSuccessor.size() == 0 ) )
				{
					symbolTable[ src][ compareTo] = 1;
					return;
				}

				if ( srcSuccessor.size() != 0 )
				{
					for ( Integer srcDest : srcSuccessor )
					{
						for ( Integer compareDest : compareToSuccessor )
						{

							if ( srcDest > compareDest && symbolTable[ srcDest][ compareDest] == 1)
							{
								symbolTable[ src][ compareTo] = 1;
								return;
							}
							else if ( srcDest < compareDest && symbolTable[ compareDest][ srcDest] == 1 )
							{
								symbolTable[ src][ compareTo] = 1;
								return;
							}
						}
					}
				}
			}
			// All the distinction tests have failed, mark it as negative
			System.out.println( "Could not distinguish " + src + " and " + compareTo);
			symbolTable[ src][ compareTo] = -1;
		}
	}


	private void printTable() //+
	{
		for ( int x = 0; x < symbolTable.length; x++)
		{
			System.out.print( x + "-> ");
			for ( int y = 0; y < symbolTable.length; y++)
			{
				System.out.print( symbolTable[ x][ y] + " \t, " );

			}
			System.out.println( " END ROW");
		}
	}

}
