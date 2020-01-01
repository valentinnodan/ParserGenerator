package generator;

import generator.symbols.LexicalSymbol;
import generator.symbols.Symbol;
import generator.symbols.SyntaxSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Grammar {
    public String name = "";
    public ArrayList<LexicalSymbol> lexicalSymbols = new ArrayList<>();
    public SyntaxSymbol start;
    public SyntaxSymbol startReal;
    public HashSet<String> symbols = new HashSet<>();
    public HashSet<SyntaxSymbol> syntaxSymbols = new HashSet<>();
    public HashSet<SyntaxSymbol> syntaxSymbolsReal = new HashSet<>();
    public HashMap<String, HashSet<String>> first = new HashMap<>();
    public HashMap<String, ArrayList<HashSet<String>>> firstAp = new HashMap<>();
    public HashMap<String, HashSet<String>> follow = new HashMap<>();
    public String EPSILON = "EPSILON";
    public String END = "_END";

    public void initialize() {
//        lexicalSymbols.add(END);
        for (SyntaxSymbol symb : syntaxSymbols) {
            HashSet<String> firstEmpty = new HashSet<>();
            HashSet<String> followEmpty = new HashSet<>();
            first.put(symb.name(), firstEmpty);
            follow.put(symb.name(), followEmpty);
        }
    }

    public boolean isEqual(HashMap<String, HashSet<String>> a, HashMap<String, HashSet<String>> b) {
        if (a == null) {
            return false;
        }
        for (SyntaxSymbol symbol : syntaxSymbols) {
            if (!a.get(symbol.name()).containsAll(b.get(symbol.name()))) {
                return false;
            }
        }
        return true;
    }

    public HashSet<String> getFirst(Rule a) {
        ArrayList<Symbol> ruleItems = a.getRuleItems();
        HashSet<String> curr = new HashSet<>();
        if (ruleItems.size() == 1 &&
                (ruleItems.get(0) instanceof LexicalSymbol &&
                        (ruleItems.get(0)).name().equals(EPSILON))) {
            curr.add(EPSILON);
            return curr;
        }
        if (ruleItems.get(0) instanceof LexicalSymbol) {
            curr.add(ruleItems.get(0).name().toUpperCase());
            return curr;
        }
        curr.addAll(first.get(ruleItems.get(0).name()));
        if (curr.contains(EPSILON)) {
            curr.remove(EPSILON);
            if (ruleItems.size() > 1) {
                Rule currRule = new Rule();
                for (int i = 1; i < ruleItems.size(); i++) {
                    currRule.addItem(ruleItems.get(i));
                }
                curr.addAll(getFirst(currRule));
            }
        }
        return curr;
    }

    private HashMap<String, HashSet<String>> copy(HashMap<String, HashSet<String>> a) {
        HashMap<String, HashSet<String>> b = new HashMap<>();
        for (String s : a.keySet()) {
            HashSet<String> curr = new HashSet<>(a.get(s));
            b.put(s, curr);
        }
        return b;
    }

    public void findFirst() {
        HashMap<String, HashSet<String>> firstA = null;
        while (!isEqual(firstA, first)) {
            firstA = copy(first);
            for (SyntaxSymbol symbol : syntaxSymbols) {
                ArrayList<Rule> rules = symbol.getRules();
                for (Rule rule : rules) {
                    HashSet<String> ansSet = first.get(symbol.name());
                    ansSet.addAll(getFirst(rule));
                    first.put(symbol.name(), ansSet);
                }
            }
        }
    }

    public void findFollow() {
        follow.get(start.name()).add(END);
        HashMap<String, HashSet<String>> followA = null;
        while (!isEqual(followA, follow)) {
            followA = copy(follow);
            for (SyntaxSymbol symbol : syntaxSymbols) {
                ArrayList<Rule> rules = symbol.getRules();
                for (Rule rule : rules) {
                    ArrayList<Symbol> ruleItems = rule.getRuleItems();
                    for (int i = 0; i < ruleItems.size() - 1; i++) {
                        if (ruleItems.get(i) instanceof SyntaxSymbol) {
                            HashSet<String> ansSet = new HashSet<>(follow.get(ruleItems.get(i).name()));
                            if (ruleItems.get(i + 1) instanceof LexicalSymbol) {
                                ansSet.add((ruleItems.get(i + 1)).name().toUpperCase());
                            } else {
                                HashSet<String> currFst = first.get(ruleItems.get(i + 1).name());
                                ansSet.addAll(currFst);
                                if (currFst.contains(EPSILON)) {
                                    ansSet.remove(EPSILON);
                                    HashSet<String> curr = follow.get(symbol.name());
                                    ansSet.addAll(curr);
                                }
                            }
                            follow.put(ruleItems.get(i).name(), ansSet);
                        }
                    }
                    int t = ruleItems.size() - 1;
                    if (ruleItems.get(t) instanceof SyntaxSymbol) {
                        HashSet<String> ansSet = new HashSet<>(follow.get(ruleItems.get(t).name()));
//                        ansSet.add(END);
                        HashSet<String> curr = follow.get(symbol.name());
                        ansSet.addAll(curr);
                        follow.put(ruleItems.get(t).name(), ansSet);
                    }
                }
            }
        }
    }

    public HashSet<String> findFirstForRule(Rule rule, SyntaxSymbol a){
        HashSet<String> ansSet = new HashSet<>();
        Symbol currSymbol = rule.getRuleItems().get(0);
        int ind = 0;
        while (currSymbol instanceof SyntaxSymbol && first.get(currSymbol.name()).contains(EPSILON)){
                ansSet.addAll(first.get(currSymbol.name()));
                ansSet.remove(EPSILON);
                currSymbol = rule.getRuleItems().get(ind++);
                if (ind == rule.getRuleItems().size()){
                    break;
                }
        }
        if (ind == rule.getRuleItems().size()){
            ansSet.addAll(follow.get(a.name()));
        }
        if (ind < rule.getRuleItems().size()){
            if (currSymbol instanceof SyntaxSymbol){
                ansSet.addAll(first.get(currSymbol.name()));
            } else {
                if (currSymbol.name().equals(EPSILON)){
                    ansSet.addAll(follow.get(a.name()));
                } else {
                    ansSet.add(currSymbol.name().toUpperCase());
                }
            }
        }
        return ansSet;
    }
    public void findFirstAp() {
        for (SyntaxSymbol s : syntaxSymbols) {
            ArrayList<HashSet<String>> currAns = new ArrayList<>();
            for (Rule rule: s.getRules()){
                currAns.add(findFirstForRule(rule, s));
            }
            firstAp.put(s.name(), currAns);
        }
    }

    public void makeCalculations() {
        initialize();
        findFirst();
        findFollow();
        findFirstAp();
    }
}
