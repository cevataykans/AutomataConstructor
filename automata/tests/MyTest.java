package automata.tests;

import automata.AutomataMinimizer;
import automata.Automaton;
import automata.EpsNFA;
import automata.PrettyPrinter;
import automata.resyntax.RegExp;
import org.junit.Test;
import org.jetbrains.jetCheck.*;

import java.util.regex.*;

import static automata.MyApplication.*;


public class MyTest {

    @Test
    public void test1() {
        String regex = "abab";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abc"));
        //PrettyPrinter.alphabet = findAlphabet( "abc");
        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test2() {
        String regex = "ab|cde";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcdef"));
        //PrettyPrinter.alphabet = findAlphabet( "abcdef");
        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test3() {
        String regex = "a*bc*(ab)+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcd"));
        //PrettyPrinter.alphabet = findAlphabet( "abcd");
        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test4() {
        String regex = "a+.+b?a+.+b+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 40), Generator.charsFrom("abcde"));
        //PrettyPrinter.alphabet = findAlphabet( "abcde");
        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    @Test
    public void test5() {
        String regex = "abcd*|cba(abc)+|(abc)+(bdb)(abc)?|a.b.c.d|(((abc)+)+)+";
        Generator<String> gen = Generator.stringsOf(IntDistribution.uniform(0, 80), Generator.charsFrom("abcde"));
        //PrettyPrinter.alphabet = findAlphabet( "abcde");
        PropertyChecker.forAll(gen, s -> Pattern.compile(regex).matcher(s).find() == mySearch(regex, s));
    }

    public static boolean mySearch( String regex, String s)
    {
        PrettyPrinter.alphabet = findAlphabet( s);
        try
        {
            EpsNFA eNFA = automata.REParser.parse( regex).accept( new PrettyPrinter());
            Automaton<Integer, Character> shrinked = eNFA.toDFA();
            AutomataMinimizer min = new AutomataMinimizer( shrinked);
            shrinked = min.minimize();

            shrinked.printGV();

            return shrinked.containsPattern( s);
        }
        catch ( Exception e)
        {
            System.out.println( e);
            return false;
        }
    }

    private static String findAlphabet( String regex)
    {
        StringBuilder alphabet = new StringBuilder();
        for ( int i = 0; i < regex.length(); i++)
        {
            if ( regex.charAt( i) <= 'z' && regex.charAt( i) >= 'a')
            {
                alphabet.append( regex.charAt( i) );
            }
        }
        alphabet.append( " ");
        return alphabet.toString();
    }

}
