package generator.gen;

import generator.Grammar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ClassGenerator {
    Grammar g;
    String whatToGen;
    String packageName;
    PrintWriter output;
    String ENTER = System.lineSeparator();

    public ClassGenerator(String dir, Grammar g, String whatToGen) throws IOException {
        packageName = "result." + g.name;
        Path pathDir = Paths.get(dir);
        pathDir = pathDir.resolve("result").resolve(g.name);
        this.g = g;
        this.whatToGen = whatToGen;
        Files.createDirectories(pathDir);
        String fileName = g.name + whatToGen + ".java";
        output = new PrintWriter(new File(pathDir.toString(), fileName));
    }
    public void generateClass() {
        writeCode(0, "package", packageName, ";", ENTER);
        writeImports();
        writeBody();
        output.close();
    }

    public abstract void writeImports();

    public abstract void writeBody();

    public void writeCode(int padding, String... codeItems) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++){
            sb.append('\t');
        }
        for (String item: codeItems){
            sb.append(item).append(' ');
        }
        sb.append(ENTER);
        output.write(sb.toString());
    }
    public String getGeneratedClassName(){
        return g.name + whatToGen;
    }

}
