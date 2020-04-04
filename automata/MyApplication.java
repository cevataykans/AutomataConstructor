package automata;

/* TODO: Implement a grep-like tool. */

public class MyApplication {

    public static void main(String[] args) throws Exception {

        // Example of using the regexp parser:
		String toParse = "(aba)*c+a|cd";
		System.out.println( toParse);

        automata.REParser.parse( toParse).accept( new PrettyPrinter());
        System.out.println();

        System.exit(0);

    }

}
