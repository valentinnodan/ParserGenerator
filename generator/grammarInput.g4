grammar grammarInput;

@header {
//    package generator.inputGrammar;
    import generator.Grammar;
    import generator.Rule;
    import generator.symbols.*;

}

inputGrammar returns [generator.Grammar g]
    @init {
        $g = new generator.Grammar();
    } :
    'grammar' SynSymbName {$g.name = $SynSymbName.text;} ';'
    s=syntaxRule[$g]{$g.start = $s.symb;
                     $g.startReal = $s.realSymb;}
    (syntaxRule[$g])* (lexicalRule[$g])+;

syntaxRule [generator.Grammar g] returns [SyntaxSymbol symb, SyntaxSymbol realSymb] locals [SyntaxSymbol thisRule, SyntaxSymbol thisRuleReal]
    @init {
        $symb = null;
        $thisRule = null;
    }:
    syn=SynSymbName {$thisRule = new generator.symbols.SyntaxSymbol($syn.text);
                     $thisRuleReal = new generator.symbols.SyntaxSymbol($syn.text);
                     if ($g.symbols.contains($syn.text)) {
                         for (SyntaxSymbol s: $g.syntaxSymbols){
                             if (s.name().equals($thisRule.name())){
                             $thisRule = s;
                             }
                         }
                         for (SyntaxSymbol s: $g.syntaxSymbolsReal){
                                if (s.name().equals($thisRuleReal.name())){
                                    $thisRuleReal = s;
                                    }
                         }
                     } else {
                      $g.syntaxSymbols.add($thisRule);
                      $g.symbols.add($thisRule.name());
                     };
                     $g.syntaxSymbolsReal.add($thisRuleReal);
                     $symb = $thisRule;}
     (getsBlock {$thisRule.getArguments = $getsBlock.arguments;
                 $thisRuleReal.getArguments = $getsBlock.arguments;})?
     (returnsBlock {$thisRule.retArguments = $returnsBlock.arguments;
                    $thisRuleReal.retArguments = $returnsBlock.arguments;})?
     (initBlock {$thisRule.initCode = $initBlock.initCode;
                 $thisRule.initCode = $initBlock.initCode;})?
      ':'
      rul=syntaxRightPart[$g] {$thisRule.addRule($rul.syms);
                               $thisRuleReal.addRule($rul.real);}
                               (code=Code{$rul.real.code.codeString = $code.text;})?
      ('|' ruleA=syntaxRightPart[$g] {$thisRule.addRule($ruleA.syms);
                                      $thisRuleReal.addRule($ruleA.real);}
                                      (codeA=Code {$ruleA.real.code.codeString = $codeA.text;})?)* ';';

syntaxRightPart [generator.Grammar g] returns [Rule syms, Rule real] locals [Symbol thisRule, LexicalSymbol lexS]
    @init{
        $syms = new Rule();
        $thisRule = null;
        $real = new Rule();
    } :
    ((a=LexSymbName {if (!$g.symbols.contains($a.text)) {
                            LexicalSymbol s = new LexicalSymbol($a.text);
                            $syms.addItem(s);
                            $g.symbols.add($a.text);
                            $g.lexicalSymbols.add(s);
                        } else {
                            for (LexicalSymbol s: $g.lexicalSymbols){
                                 if (s.name().equals($a.text)){
                                       $thisRule = s;
                                 }
                            }
                            $syms.addItem($thisRule);
                        }
                        $lexS = new LexicalSymbol($a.text);
                        $real.addItem($lexS);
                   } (codeLex=Code {$lexS.code.codeString = $codeLex.text;})?)|
    b=SynSymbName
    '[' argNames {
                    if (!$g.symbols.contains($b.text)) {
                         SyntaxSymbol s = new SyntaxSymbol($b.text);
                         $syms.addItem(s);
                         $g.symbols.add($b.text);
                         $g.syntaxSymbols.add(s);
                     } else {
                         for (SyntaxSymbol s: $g.syntaxSymbols){
                             if (s.name().equals($b.text)){
                                  $thisRule = s;
                             }
                         }
                         $syms.addItem($thisRule);
                     }
                         SyntaxSymbol current = new SyntaxSymbol($b.text);
                         current.applied = $argNames.names;
                         $real.addItem(current);
                  }
     ']' (code=Code {current.insideCode.codeString = $code.text;})?|
     c=SynSymbName {
                   if (!$g.symbols.contains($c.text)) {
                                                 SyntaxSymbol s = new SyntaxSymbol($c.text);
                                                 $syms.addItem(s);
                                                 $g.symbols.add($c.text);
                                                 $g.syntaxSymbols.add(s);
                                            } else {
                                               for (SyntaxSymbol s: $g.syntaxSymbols){
                                                 if (s.name().equals($c.text)){
                                                    $thisRule = s;
                                                 }
                                                }
                                               $syms.addItem($thisRule);
                                            }
                                                SyntaxSymbol toReal = new SyntaxSymbol($c.text);
                                               $real.addItem(toReal);
                   }
                   (codeA=Code {toReal.insideCode.codeString = $codeA.text;})?
    )+;

lexicalRule [generator.Grammar g]:
    lex=LexSymbName { LexicalSymbol thisRule = new generator.symbols.LexicalSymbol($lex.text);
    if ($g.symbols.contains($lex.text)) {
        for (LexicalSymbol s: $g.lexicalSymbols){
            if (s.name().equals($lex.text)){
            thisRule = s;
            }
        }
    } else {
     $g.lexicalSymbols.add(thisRule);
     $g.symbols.add(thisRule.name());
    }
   }
    ':'
    (str=String) {thisRule.addRule($str.text);}
     ';';

returnsBlock returns [ArrayList<generator.symbols.Argument> arguments]
@init {
    $arguments = new ArrayList<>();
}:
    'returns [' args {$arguments = $args.arguments;}']';

initBlock returns [generator.symbols.Code initCode]
@init{
    $initCode = new Code();
}:
    '@init ' Code {$initCode.codeString = $Code.text;};

getsBlock returns [ArrayList<generator.symbols.Argument> arguments]
@init{
    $arguments = new ArrayList<>();
}:
    '[' args {$arguments = $args.arguments;}']';

args returns [ArrayList<generator.symbols.Argument> arguments]
@init {
    $arguments = new ArrayList<>();
}:
    a=arg {$arguments.add($a.argument);}
    (',' b=arg {$arguments.add($b.argument);})*;

arg returns [generator.symbols.Argument argument]
@init {
    $argument = new Argument();
}:
    type SynSymbName {$argument.type = $type.t;
                      $argument.name = $SynSymbName.text;
    };

argNames returns [ArrayList<String> names]
@init {
    $names = new ArrayList<>();
} :
    fst=SynSymbName {$names.add($fst.text);}
    (',' other=SynSymbName {$names.add($other.text);})* ;

type returns [String t]
@init {
    $t = "";
}:
    LexSymbName {$t = $LexSymbName.text;} |
    SynSymbName {$t = $SynSymbName.text;};

SynSymbName: ('a'..'z')('a'..'z' | 'A' .. 'Z' | '0' .. '9')*;
LexSymbName: ('A'..'Z')('a'..'z' | 'A' .. 'Z' | '0' .. '9')*;
Code: '{' (CodeString | Code)* '}';
Attr: '[' (CodeString)* '}';
String: '"' (~('"'))* '"';
WS: (' '|'\n'|'\t')+ -> skip;
CodeString: (~('{'|'}'))+?;
