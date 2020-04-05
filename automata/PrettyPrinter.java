package automata;

import automata.resyntax.*;
import java.util.Iterator;
import static automata.EpsNFA.EPSILON;

public class PrettyPrinter implements RegExpVisitor {


    public EpsNFA visit(Closure closure) {
        System.out.print("(");
        EpsNFA eNFA = closure.r.accept(this);
        System.out.print("*)");

        // Right shift only once
        eNFA = eNFA.shiftStates( 1);

        // Clear the accepting state
        eNFA.getAcceptingStates().clear();

        // Get the initial and end state
        int initial = eNFA.getInitialState();
        int endState = eNFA.getMaxState();

        // Add the new initial and end state, creating the closure
        eNFA.addTransition( 0, initial, EPSILON);
        eNFA.addTransition( endState, endState + 1, EPSILON);
        eNFA.addTransition( 0, endState + 1, EPSILON);
        eNFA.addTransition( endState, initial, EPSILON);

        // Save the new initial and end state
        eNFA.addAcceptingState( endState + 1);
        eNFA.setInitialState( 0);

        return eNFA;
    }
    
    public EpsNFA visit(Concatenation concat) {
        System.out.print("(");
        EpsNFA eNFA_1 = concat.r1.accept(this);
        EpsNFA eNFA_2 = concat.r2.accept(this);
        System.out.print(")");

        // Erase the accepting state of eNFA1
        eNFA_1.getAcceptingStates().clear();

        // Get the maximum state of NFA 1
        int nfa1MaxState = eNFA_1.getMaxState();

        // Shift eNFA 2 for at least NFA 1's maximum state + 1 to disable collusion
        int nfa2StartState = nfa1MaxState + 1;
        eNFA_2 = eNFA_2.shiftStates( nfa2StartState);

        // transfer the states of NFA 2 to NFA 1
        transferTransitions( eNFA_1, eNFA_2);
        eNFA_1.addTransition( nfa1MaxState, nfa2StartState, EPSILON); // Actually concatenate
        eNFA_1.getAcceptingStates().addAll( eNFA_2.getAcceptingStates() ); // Transfer all the accepting states of NFA2.

        return eNFA_1;
    }

    // No idea what this does? if it does not match any character, discard it, return null? Well it matches everything, .
    public EpsNFA visit(Dot dot) {
        System.out.print(".");
        return null;
    }

    public EpsNFA visit(Litteral litteral) {
        System.out.print(litteral.c);

        EpsNFA base = new EpsNFA();
        base.addTransition( 0, 1, litteral.c);
        base.setInitialState( 0);
        base.addAcceptingState( 1);

        return base;
    }

    public EpsNFA visit(OneOrMore oneOrMore) {
        System.out.print("(");
        EpsNFA eNFA = oneOrMore.r.accept(this);
        System.out.print("+)");

        // Get the initial state
        int startState = eNFA.getInitialState();

        // Connect each accepting state to the initial state
        Iterator iterator = eNFA.getAcceptingStates().iterator();
        while ( iterator.hasNext() )
        {
            eNFA.addTransition( (Integer) iterator.next(), startState, EPSILON);
        }

        return eNFA;
    }

    public EpsNFA visit(Union union) {
        System.out.print("(");
        EpsNFA eNFA_1 = union.r1.accept(this);
        System.out.print("|");
        EpsNFA eNFA_2 = union.r2.accept(this);
        System.out.print(")");

        // Erase both accepting states
        eNFA_2.getAcceptingStates().clear();
        eNFA_1.getAcceptingStates().clear();

        // Shift NFA 1 right once and Shift NFA 2 to disable conflict with NFA 1
        eNFA_1 = eNFA_1.shiftStates( 1);
        eNFA_2 = eNFA_2.shiftStates( eNFA_1.getMaxState() + 1);

        // Get the initial and end states
        int initial_1 = eNFA_1.getInitialState();
        int initial_2 = eNFA_2.getInitialState();
        int endState_1 = eNFA_1.getMaxState();
        int endState_2 = eNFA_2.getMaxState();

        // Transfer all the states in NFA 2 to NFA 1
        transferTransitions( eNFA_1, eNFA_2);

        // Connect edges
        eNFA_1.addTransition( 0, initial_1, EPSILON);
        eNFA_1.addTransition( 0, initial_2, EPSILON);

        eNFA_1.addTransition( endState_1, endState_2 + 1, EPSILON);
        eNFA_1.addTransition( endState_2, endState_2 + 1, EPSILON);

        // Save the new initial and accepting state
        eNFA_1.setInitialState( 0);
        eNFA_1.addAcceptingState( endState_2 + 1);

        return eNFA_1;
    }

    public EpsNFA visit(ZeroOrOne zeroOrOne) {
        System.out.print("(");
        EpsNFA eNFA = zeroOrOne.r.accept(this);
        System.out.print("?)");

        // Right shift only once
        eNFA = eNFA.shiftStates( 1);

        // Clear the accepting state
        eNFA.getAcceptingStates().clear();

        // Get the initial and end state
        int initial = eNFA.getInitialState();
        int endState = eNFA.getMaxState();

        // Add the new initial and end state, creating the closure
        eNFA.addTransition( 0, initial, EPSILON);
        eNFA.addTransition( endState, endState + 1, EPSILON);
        eNFA.addTransition( 0, endState + 1, EPSILON);

        // Save the new initial and end state
        eNFA.addAcceptingState( endState + 1);
        eNFA.setInitialState( 0);

        return eNFA;
    }

    // Transfers all of the transitions in e2 to e1
    private void transferTransitions( EpsNFA e1, EpsNFA e2)
    {
        for ( Integer src : e2.getTransitions().keySet())
        {
            for ( Integer dst : e2.getTransitions().get( src).keySet() )
            {
                //System.out.println( " from " + src + " to " + dst + " via" + e2.getTransitions().get(src).get( dst));
                e1.addTransitions( src, dst, e2.getTransitions().get(src).get( dst) );
            }
        }
    }
}   
