package automata;

/* TODO: Implement a grep-like tool. */

public class MyApplication {

    public static void main(String[] args) throws Exception {

        Character epsilon = EpsNFA.EPSILON;
        EpsNFA test = new EpsNFA();
        test.addAcceptingState(2);

        test.addTransition(test.initial, 1, 'a');
        test.addTransition(test.initial, 2, 'a');
        test.addTransition(test.initial, test.initial, 'b');
        test.addTransition(test.initial, 1, epsilon);

        test.addTransition(1, 1, 'b');
        test.addTransition(1, 2, epsilon);

        test.addTransition(2, 2, 'a');
        test.addTransition(2,2, 'b');

        System.out.println("-------Test 1-------\n\n-------e-NFA 1-------");
        test.printGV();

        System.out.println("----------Converted to DFA----------\n\n");
        Automaton<Integer, Character> dfa = test.toDFA();
        dfa.printGV();

        // Additional test
        EpsNFA test1 = new EpsNFA();
        test1.addAcceptingState(3);

        test1.addTransition(test1.initial, 1, epsilon);
        test1.addTransition(1, 2, epsilon);
        test1.addTransition(2, 3, epsilon);
        test1.addTransition(0, 13, epsilon);
        test1.addTransition(13, 12, 'a');
        test1.addTransition(12, 11, epsilon);
        test1.addTransition(11, 10, 'b');
        test1.addTransition(10, 9, epsilon);
        test1.addTransition(9,8, epsilon);
        test1.addTransition(9, 4, epsilon);
        test1.addTransition(8, 7,'a');
        test1.addTransition(7, 6, epsilon);
        test1.addTransition(6, 5, 'b');
        test1.addTransition(5, 8, epsilon);
        test1.addTransition(5, 4, epsilon);
        test1.addTransition(4, 3, epsilon);

        System.out.println("-------Test 2-------\n\n-------e-NFA 2-------");
        test1.printGV();

        System.out.println("----------Converted to DFA----------\n\n");
        Automaton<Integer, Character> dfa1 = test1.toDFA();
        dfa1.printGV();


        /*

        Pls dont edit below

         */


		// Example of using the regexp parser: "(aba)*c+a|cd"
		System.out.println( "Checkpoint 0");
		String toParse = "(a*|ba)*";
		System.out.println( toParse);
		System.out.println();
		System.out.println( "Checkpoint 1");
		EpsNFA epsNFA = automata.REParser.parse( toParse).accept( new PrettyPrinter());
		epsNFA.printGV();
		System.out.println( "Checkpoint 2");
		System.out.println();
		Automaton<Integer, Character> testDFA = epsNFA.toDFA();
		testDFA.printGV();
		System.out.println( "Checkpoint 3");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println( "Below is the 3.4 experiment:");
		System.out.println();

		System.out.println( "Checkpoint 4");
		new AutomataMinimizer( testDFA);
		testDFA.printGV();
		System.out.println( "Checkpoint 5");
        System.exit(0);

    }

}
