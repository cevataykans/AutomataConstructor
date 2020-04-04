package automata;

/* TODO: Implement a grep-like tool. */

public class MyApplication {

    public static void main(String[] args) throws Exception {

        // Example of using the regexp parser: "(aba)*c+a|cd"
		String toParse = "(aba)*c+a|cd";
		System.out.println( toParse);

        EpsNFA epsNFA = automata.REParser.parse( toParse).accept( new PrettyPrinter());
        System.out.println();
		System.out.println( epsNFA);
		epsNFA.printGV();
		System.out.println( epsNFA.getInitialState() );

        System.exit(0);

    }

}
