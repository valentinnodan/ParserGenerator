package generator.gen;

import generator.Grammar;
import generator.symbols.LexicalSymbol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TokensGenerator extends ClassGenerator {

    public TokensGenerator(String dir, Grammar g) throws IOException {
        super(dir, g, "Token");

    }

    @Override
    public void writeImports() {
    }

    @Override
    public void writeBody() {
        writeCode(0, "public enum", g.name + "Token", "{");
        writeCode(1, g.END, ",");
        for (int i = 0; i < g.lexicalSymbols.size(); i++) {
            if (!g.lexicalSymbols.get(i).name().equals(g.EPSILON)) {
                writeCode(1, g.lexicalSymbols.get(i).name().toUpperCase());
                if (i < g.lexicalSymbols.size() - 1) {
                    writeCode(1, ",");
                }
            }
        }
        writeCode(0, "}");
    }
}
