package automata;

/* TODO: Implement a grep-like tool. */

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class MyApplication {

    public static void main(String[] args) throws Exception {



        /*Character epsilon = EpsNFA.EPSILON;
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
        dfa.printGV();*/

        // Additional test
        /*EpsNFA test1 = new EpsNFA();
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
        dfa1.printGV();*/


        /*

        Pls dont edit below

         */

		/*
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
		AutomataMinimizer minimizer = new AutomataMinimizer( testDFA);
		testDFA = minimizer.minimize();
		testDFA.printGV();
		System.out.println( "Checkpoint 5");

        System.out.println("Below is the experiment of 3.5....");*/

		int testCaseCount = 0;
        try
		{
			testCaseCount = new File( "src/testcases/").list().length;
		}
		catch ( NullPointerException e)
		{
			System.out.println( "Could not open src/testcases, file might be deleted!");
			System.exit(0);
		}

        for (int idx = 1; idx <= testCaseCount; idx++) {
            System.out.println("\n\n\n---------- Processing testcase" + idx + ".txt file ----------\n\n\n");
            ArrayList<String> fileContents = readFile("testcase" + idx + ".txt");


            String alphabetString = fileContents.get(0); // input alphabet
            PrettyPrinter.alphabet = alphabetString;

            fileContents.remove(0);
            String toBeParsed = fileContents.get(0); // regex
            fileContents.remove(0);

            System.out.println(toBeParsed);
            EpsNFA eNFA = automata.REParser.parse( toBeParsed).accept( new PrettyPrinter());
            Automaton<Integer, Character> shrinked = eNFA.toDFA();
            AutomataMinimizer min = new AutomataMinimizer( shrinked);
            shrinked = min.minimize();

            System.out.println("The strings in this file with a matching pattern are:\n\n");

            for (String line : fileContents) {
                if (shrinked.containsPattern(line))
                    System.out.println(line);
            }
        }
        
        System.exit(0);
    }

    // reads the input file with the given name
    public static ArrayList<String> readFile(String fileName) throws FileNotFoundException {

        Path current = Paths.get("src/testcases/");
        String prefix = current.toAbsolutePath().toString();
        System.out.println(prefix);

        Scanner fileReader = new Scanner(new File(prefix + "/" + fileName));
        ArrayList<String> contents = new ArrayList<String>();

        while (fileReader.hasNextLine()) {
            String testCase = fileReader.nextLine();
            contents.add(testCase);
        }

        return contents;
    }

}
