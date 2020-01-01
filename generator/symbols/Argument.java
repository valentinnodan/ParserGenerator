package generator.symbols;

public class Argument extends Symbol {
    public String type;
    public String name;

    public Argument(){
        super();
    }
    @Override
    public String name() {
        return name;
    }
    public String type() {
        return type;
    }
}
