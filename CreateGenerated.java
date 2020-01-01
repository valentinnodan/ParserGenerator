import generator.Grammar;
import generator.gen.LexicalAnalizerGenerator;
import generator.gen.SyntaxAnalizerGenerator;
import generator.gen.TokensGenerator;
import generator.inputGrammar.grammarInputLexer;
import generator.inputGrammar.grammarInputParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;

public class CreateGenerated {
    public static void main(String[] args) throws IOException {
        grammarInputLexer lexer = new grammarInputLexer(CharStreams.fromFileName("/home/valentinnodan/Documents/Mt_Lab_4/src/" + args[0] +".txt"));
        TokenStream tokens = new CommonTokenStream(lexer);
        grammarInputParser parser = new grammarInputParser(tokens);
        Grammar grammar = parser.inputGrammar().g;
        grammar.makeCalculations();
        TokensGenerator tokensGen = new TokensGenerator("/home/valentinnodan/Documents/Mt_Lab_4/src/", grammar);
        LexicalAnalizerGenerator lexGen = new LexicalAnalizerGenerator("/home/valentinnodan/Documents/Mt_Lab_4/src/", grammar);
        SyntaxAnalizerGenerator synGen = new SyntaxAnalizerGenerator("/home/valentinnodan/Documents/Mt_Lab_4/src/", grammar);
        tokensGen.generateClass();
        lexGen.generateClass();
        synGen.generateClass();
    }
}
