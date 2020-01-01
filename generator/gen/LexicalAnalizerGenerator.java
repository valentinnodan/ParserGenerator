package generator.gen;

import generator.Grammar;
import generator.symbols.LexicalSymbol;

import java.io.IOException;


public class LexicalAnalizerGenerator extends ClassGenerator {
    String tokensName;

    public LexicalAnalizerGenerator(String dir, Grammar g) throws IOException {
        super(dir, g, "LexicalAnalizer");
        tokensName = g.name + "Token";
    }

    @Override
    public void writeImports() {
        writeCode(0, "import java.util.*;");
        writeCode(0, "import java.util.regex.*;", ENTER);
    }

    @Override
    public void writeBody() {
        writeCode(0, "public class", getGeneratedClassName(), "{");
        writeMembers();
        writeConstructor();
        writeGetNext();
        writeGetCurrString();
        writeCode(0, "}");
    }

    private void writeMembers() {
        writeCode(1, "private String inputString;");
        writeCode(1, "private int placeString;");
        writeCode(1, "public", tokensName, "currToken;");
        writeCode(1, "private Pattern space = Pattern.compile(\"[ |\\n|\\r|\\t]+\");");
        writeCode(1, "private Matcher matcher;");
        writeCode(1, "private String currString;");
        writeCode(1, "HashMap<" + tokensName + ", Pattern> tokensReg = new HashMap<>();", ENTER);
    }

    private void writeConstructor() {
        writeCode(1, "public", getGeneratedClassName() + "(String inputString) {");
        writeCode(2, "this.inputString = inputString;");
        writeCode(2, "placeString = 0;");
        writeCode(2, "matcher = Pattern.compile(\"\").matcher(inputString);");
        writeCode(2, "tokensReg.put(" + tokensName + "._END, Pattern.compile(\"$\"));");
        for (LexicalSymbol ls : g.lexicalSymbols) {
            if (!ls.name().equals(g.EPSILON)) {
                writeCode(2, "tokensReg.put(" + tokensName + "." + ls.name().toUpperCase(), ", Pattern.compile(" + ls.getRule() + "));");
            }
        }
        writeCode(1, "}");
    }

    private void writeGetNext() {
        writeCode(1, "public", tokensName, "next() {");
        writeCode(2, "if (placeString == inputString.length()) {");
        writeCode(2, "currToken = ", tokensName+"._END;");
        writeCode(3, "return currToken;");
        writeCode(2, "}");
        //skip spaces
        writeCode(2, "matcher.usePattern(space);");
        writeCode(2, "matcher.region(placeString, inputString.length());");
        writeCode(2, "if (matcher.lookingAt()){");
        writeCode(3, "placeString += matcher.end() - matcher.start();");
        writeCode(2, "}");
        //end of skipping spaces
        writeCode(2, "for (" + tokensName, "token:", tokensName + ".values()) {");
        writeCode(3, "matcher.usePattern(tokensReg.get(token));");
        writeCode(3, "matcher.region(placeString, inputString.length());");
        writeCode(3, "if (matcher.lookingAt()) {");
        writeCode(4, "currToken = token;");
        writeCode(4, "currString = inputString.substring(placeString, placeString + matcher.end() - matcher.start());");
        writeCode(4, "placeString += matcher.end() - matcher.start();");
        writeCode(4, "return currToken;");
        writeCode(3, "}");
        writeCode(2, "}");
        writeCode(2, "System.err.println(\"Did not expect to see\" + inputString.charAt(placeString));");
        writeCode(2, "return(null);");
        writeCode(1, "}");
    }
    private void writeGetCurrString(){
        writeCode(1, "public String getCurrString(){");
        writeCode(2, "return currString;");
        writeCode(1, "}");
    }


}
