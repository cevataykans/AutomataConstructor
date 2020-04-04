package automata;

import automata.resyntax.*;

public class PrettyPrinter implements RegExpVisitor {
    
    int i = 1;

    public EpsNFA visit(Closure closure) {
        System.out.print("(ClosureStart ");
        closure.r.accept(this);
        System.out.print(" *ClosureEnd)");
        return null;
    }
    
    public EpsNFA visit(Concatenation concat) {
        System.out.print("(ConcetStart ");
        concat.r1.accept(this);
        concat.r2.accept(this);
        System.out.print(" ConcetEnd)");
        return null;
    }

    public EpsNFA visit(Dot dot) {
        System.out.print(".");
        return null;
    }

    public EpsNFA visit(Litteral litteral) {
        System.out.print(litteral.c);
        System.out.print(i++);
        return null;
    }

    public EpsNFA visit(OneOrMore oneOrMore) {
        System.out.print("(OneOrMoreStart ");
        oneOrMore.r.accept(this);
        System.out.print(" +OneorMoreEnd)");
        return null;
    }

    public EpsNFA visit(Union union) {
        System.out.print("(UnionStart ");
        union.r1.accept(this);
        System.out.print(" |UnionMiddle ");
        union.r2.accept(this);
        System.out.print(" UnionEnd)");
        return null;
    }

    public EpsNFA visit(ZeroOrOne zeroOrOne) {
        System.out.print("(ZeroOrOneStart ");
        zeroOrOne.r.accept(this);
        System.out.print(" ?ZeroOrOneEnd)");
        return null;
    }
    
}   
