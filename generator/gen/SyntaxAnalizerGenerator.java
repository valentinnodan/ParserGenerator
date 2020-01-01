package generator.gen;

import generator.Grammar;
import generator.Rule;
import generator.symbols.Argument;
import generator.symbols.LexicalSymbol;
import generator.symbols.Symbol;
import generator.symbols.SyntaxSymbol;

import java.io.IOException;
import java.util.ArrayList;

public class SyntaxAnalizerGenerator extends ClassGenerator {
    String tokensName;

    public SyntaxAnalizerGenerator(String dir, Grammar g) throws IOException {
        super(dir, g, "SyntaxAnalizer");
        tokensName = g.name + "Token";
    }

    @Override
    public void writeImports() {
        writeCode(0, "import java.util.*;");
        writeCode(0, "import java.io.IOException;");
    }

    @Override
    public void writeBody() {
        writeCode(0, "public class", getGeneratedClassName(), "{");
        writeInnerClass();
        writeInnerClasses();
        writeMembers();
        writeConstructor();
        writeMethods();
        writeCode(0, "}");
    }

    private void writeInnerClass() {
        writeCode(1, "public class Node {");
        writeCode(2, "public String symbol;");
        writeCode(2, "public ArrayList<Node> children;", ENTER);
        writeCode(2, "public Node(String name) {");
        writeCode(3, "symbol = name;");
        writeCode(3, "children = new ArrayList<>();");
        writeCode(2, "}");
        writeCode(2, "public void addChild(Node child) {");
        writeCode(3, "children.add(child);");
        writeCode(2, "}");
        writeCode(2, "public void visualize(int tab, ArrayList<Boolean> padding) {");
        writeCode(3, "if (tab != 0) {");
        writeCode(4, "System.out.print(\"____\");");
        writeCode(3, "}");
        writeCode(3, "System.out.println(this.symbol);");
        writeCode(3, "if (!(children.size() == 0)) {");
        writeCode(4, "for (int i = 0; i < children.size(); i++) {");
        writeCode(5, "ArrayList<Boolean> currentList = new ArrayList<>();");
        writeCode(5, "currentList.addAll(padding);");
        writeCode(5, "for (int j = 0; j < tab; j++) {");
        writeCode(6, "if (currentList.get(j)) {");
        writeCode(7, "System.out.print(\"|   \");");
        writeCode(6, "} else {");
        writeCode(7, "System.out.print(\"    \");");
        writeCode(6, "}");
        writeCode(5, "}");
        writeCode(5, "currentList.add(i != children.size() - 1);");
        writeCode(5, "System.out.print(\"|\");");
        writeCode(5, "children.get(i).visualize(tab + 1, currentList);");
        writeCode(4, "}");
        writeCode(3, "}");
        writeCode(2, "}");
        writeCode(1, "}");
    }

    private void writeInnerClasses() {
        for (SyntaxSymbol s : g.syntaxSymbolsReal) {
            writeCode(1, "public class", s.name().toUpperCase(), "extends Node {");
            writeCode(2, "public", s.name().toUpperCase(), "() {");
            writeCode(3, "super(", "\"" + s.name().toUpperCase() + "\"", ");");
            writeCode(2, "}");
            for (Argument a : s.retArguments) {
                writeCode(2, "public", a.type, a.name, ";");
            }
            writeCode(1, "}", ENTER);
        }
    }

    private void writeMembers() {
        writeCode(1, "private", g.name + "LexicalAnalizer", "lexicalAnalizer;", ENTER);
    }

    private void writeConstructor() {
        writeCode(1, "public", getGeneratedClassName(), "(" + g.name + "LexicalAnalizer lexicalAnalizer)", "{");
        writeCode(2, "this.lexicalAnalizer = lexicalAnalizer;");
        writeCode(2, "lexicalAnalizer.next();");
        writeCode(1, "}");
    }

    private void writeMethods() {
        for (SyntaxSymbol s : g.syntaxSymbolsReal) {
            writeMethod(s);
        }
    }

    private void writeMethod(SyntaxSymbol s) {
        String getArgs = "";
        if (s.getArguments.size() > 0){
            for (Argument a: s.getArguments){
                getArgs = getArgs + a.type + " " + a.name + ", ";
            }
            getArgs = getArgs.substring(0, getArgs.length() - 2);
        }
        writeCode(1, "public", s.name().toUpperCase(), s.name() + "("+getArgs+") throws IOException", "{");
        writeCode(2, s.name().toUpperCase(), "res = new", s.name().toUpperCase() + "();");
        if (s.initCode.codeString != null) {
            writeCode(2, s.initCode.codeString.substring(1, s.initCode.codeString.length() - 1));
        }
        writeCode(2, "switch (lexicalAnalizer.currToken){");
        for (int i = 0; i < s.getRules().size(); i++) {
            for (String t : g.firstAp.get(s.name()).get(i)) {
                writeCode(3, "case", t, ":");
            }
            writeCode(4, "{");
            for (Symbol symbol : s.getRules().get(i).getRuleItems()) {
                if (symbol instanceof SyntaxSymbol) {
                    String getArgsAppl = "";
                    if (((SyntaxSymbol) symbol).applied.size() > 0) {
                        for (String str : ((SyntaxSymbol) symbol).applied) {
                            getArgsAppl = getArgsAppl + str + ", ";
                        }
                        getArgsAppl = getArgsAppl.substring(0, getArgsAppl.length() - 2);
                    }
                    writeCode(4, symbol.name().toUpperCase(), symbol.name(), "=", symbol.name() + "("+getArgsAppl+");");
                    if (((SyntaxSymbol) symbol).insideCode.codeString != null) {
                        writeCode(4, ((SyntaxSymbol) symbol).insideCode.codeString.substring(1, ((SyntaxSymbol) symbol).insideCode.codeString.length() - 1));
                    }
                    writeCode(4, "res.addChild(" + symbol.name() + ");");
                }
                if (symbol instanceof LexicalSymbol) {
                    if (!symbol.name().toUpperCase().equals(g.EPSILON)) {
                        writeCode(4, "assert(lexicalAnalizer.currToken == " + tokensName + "." + symbol.name().toUpperCase() + ");");
                        writeCode(4, "res.addChild(new Node(\"" + symbol.name().toUpperCase() + "\"));");
                        if (((LexicalSymbol) symbol).code.codeString != null){
                            writeCode(4, ((LexicalSymbol) symbol).code.codeString.substring(1, ((LexicalSymbol) symbol).code.codeString.length() - 1));
                        }
                        writeCode(4, "this.lexicalAnalizer.next();");
                    }
                }
            }
            if (s.getRules().get(i).code.codeString != null){
                writeCode(4, s.getRules().get(i).code.codeString.substring(1, s.getRules().get(i).code.codeString.length() - 1));
            }
            writeCode(4, "return res;");
            writeCode(4, "}");
        }
        writeCode(3, "default:");
        writeCode(4, "throw new IOException(\"Did not expect to get \" + lexicalAnalizer.currToken + \" expression\");");
        writeCode(2, "}");
        writeCode(1, "}");
    }
}
