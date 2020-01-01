package generator.symbols;

import generator.Rule;

import java.util.ArrayList;

public class SyntaxSymbol extends Symbol {
    String name;
    ArrayList<Rule> rules = new ArrayList<>();
    public ArrayList<Argument> getArguments = new ArrayList<>();
    public ArrayList<Argument> retArguments = new ArrayList<>();
    public ArrayList<String> applied = new ArrayList<>();
    public Code initCode = new Code();
    public Code insideCode = new Code();

    public SyntaxSymbol(String name){
        this.name = name;
    }
    @Override
    public String name() {
        return name;
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }
}
