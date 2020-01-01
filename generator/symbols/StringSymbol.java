package generator.symbols;

public class StringSymbol extends LexicalSymbol {
    String name;

    public StringSymbol(String name, int num) {
        super("STRING" + num);
        this.name = name;
    }

    @Override
    public String name() {
        return super.name();
    }

    public String content() {
        return name;
    }
    public String getRule() {
        return name;
    }
}
