package generator.symbols;

import java.util.ArrayList;

public class LexicalSymbol extends Symbol {
    String name;
    String rule;
    public Code code = new Code();
    public LexicalSymbol(String name) {
        this.name = name;
    }
    @Override
    public String name() {
        return name;
    }
    public void addRule(String str){
        rule = str;
    }
    public String getRule(){
        return rule;
    }
}
