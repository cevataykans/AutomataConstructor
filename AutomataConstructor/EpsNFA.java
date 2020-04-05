package automata;

import java.sql.Statement;
import java.util.*;

public class EpsNFA extends Automaton<Integer, Character> {

    public final static Character EPSILON = '\u03B5';


    public EpsNFA() {
        initial = 0;
    }


    public Integer getMaxState() {
        return Collections.max(getStates());
    }


    public EpsNFA shiftStates(int delta) {
        EpsNFA newNfa = new EpsNFA();

        for (int src : trans.keySet())
            for (int dst : trans.get(src).keySet())
                newNfa.addTransitions(src + delta, dst + delta, trans.get(src).get(dst));

        for (int acc : accepting)
            newNfa.addAcceptingState(acc + delta);

        newNfa.setInitialState(initial + delta);

        return newNfa;
    }


    public Set<Integer> epsClosure(Integer q) {
        List<Integer> toVisit = new ArrayList<Integer>();
        toVisit.add(q);

        Set<Integer> closure = new HashSet<Integer>();

        while (!toVisit.isEmpty()) {
            Integer p = toVisit.remove(0);
            closure.add(p);

            if (trans.containsKey(p))
                for (Integer dst : trans.get(p).keySet())
                    if (trans.get(p).get(dst).contains(EPSILON))
                        if (!closure.contains(dst))
                            toVisit.add(dst);
        }

        return closure;
    }

    public Automaton<Integer, Character> toDFA() {
        // Set-up the new DFA
        Automaton<Integer, Character> finalDFA = new Automaton<Integer, Character>();
        finalDFA.setInitialState(0);

        // Get the initial set of states for the DFA & get the accepting state of e-NFA
        Set<Integer> initial = epsClosure(this.initial);
        int eNfaAccepting = this.getFirstAcceptingState();

        // Get the input symbols of e-NFA & exclude EPSILON from the set of symbols
        Set<Character> symbols = getSymbols();
        symbols.remove(EPSILON);

        // Map the set of states to integers for the creation of DFA
        int dfaStateCount = 0;
        Map<Set<Integer>, Integer> setToIntMapping = new HashMap<Set<Integer>, Integer>();

        // Add the eClosure of the initial state to the list of sets to be processed
        ArrayList<Set<Integer>> toVisit = new ArrayList<Set<Integer>>();
        toVisit.add(initial);

        while (!toVisit.isEmpty()) {
            Set<Integer> currentSet = toVisit.remove(0);

        /* If the currentSet contains the accepting state of the
            e-NFA, add it as an accepting state to the finalDFA*/
            if (currentSet.contains(eNfaAccepting))
                finalDFA.addAcceptingState(dfaStateCount);

            // Map the current set to an index
            setToIntMapping.put(currentSet, dfaStateCount++);

            for (Character input : symbols) {
                HashSet<Integer> transition = new HashSet<Integer>(); // Transitions for the current symbol

                // Process for each state in the set
                for (Integer state : currentSet) {
                    Map<Integer, Set<Character>> curTransitions = trans.get(state);

                /* If a transition from the state exists, compute the states
                   that can be reached from it directly with the current input symbol*/
                    if (curTransitions != null) {
                        ArrayList<Integer> epsilonVisits = new ArrayList<Integer>();

                        // The direct transitions from the current state with symbol
                        for (Integer dst : curTransitions.keySet()) {
                            if (curTransitions.get(dst).contains(input)) {
                                transition.add(dst);
                                epsilonVisits.add(dst);
                            }
                        }

                    /*Process the states that can be reached with
                       e-transitions from the states found in the previous step*/
                        while (!epsilonVisits.isEmpty()) {
                            Integer epsCheck = epsilonVisits.remove(0);
                            Map<Integer, Set<Character>> epsCheckTransition = trans.get(epsCheck);

                            // If any transitions from the current state exist, process them
                            if (epsCheckTransition != null) {
                                for (Integer dst : epsCheckTransition.keySet()) {
                                    if (epsCheckTransition.get(dst).contains(EPSILON)) {
                                        transition.add(dst);
                                        epsilonVisits.add(dst);
                                    }
                                }
                            }
                        }
                    }
                }

            /* If the new transition set is not empty, add its
                transition from the set being processed*/
                if (!transition.isEmpty()) {
                    int dst = setToIntMapping.containsKey(transition) ? setToIntMapping.get(transition) : dfaStateCount;
                    Integer src = setToIntMapping.get(currentSet);
                    finalDFA.addTransition(src, dst, input);

                    /* If the new transition set hasn't been processed
                        yet, add it to the list*/
                    if (dst == dfaStateCount)
                        toVisit.add(transition);
                }
            }
        }
        return finalDFA;
    }
}
