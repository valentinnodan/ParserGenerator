package generator.symbols;


public class Code extends Symbol {
    public String codeString;
    public Code(){
        super();
    }
    @Override
    public String name() {
        return null;
    }
    public String getCode() {
        return codeString;
    }
}
