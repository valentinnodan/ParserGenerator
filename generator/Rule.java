package generator;

import generator.symbols.Code;
import generator.symbols.Symbol;

import java.util.ArrayList;

public class Rule {
    ArrayList<Symbol> ruleItems = new ArrayList<>();
    public Code code = new Code();

    public void addItem(Symbol item){
        ruleItems.add(item);
    }
    public ArrayList<Symbol> getRuleItems(){
        return ruleItems;
    }
}
