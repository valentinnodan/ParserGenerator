package result.arithmetic ; 
 
import java.util.*; 
import java.util.regex.*; 
 
public class arithmeticLexicalAnalizer { 
	private String inputString; 
	private int placeString; 
	public arithmeticToken currToken; 
	private Pattern space = Pattern.compile("[ |\n|\r|\t]+"); 
	private Matcher matcher; 
	private String currString; 
	HashMap<arithmeticToken, Pattern> tokensReg = new HashMap<>(); 
 
	public arithmeticLexicalAnalizer(String inputString) { 
		this.inputString = inputString; 
		placeString = 0; 
		matcher = Pattern.compile("").matcher(inputString); 
		tokensReg.put(arithmeticToken._END, Pattern.compile("$")); 
		tokensReg.put(arithmeticToken.PLUS , Pattern.compile("\\+")); 
		tokensReg.put(arithmeticToken.MINUS , Pattern.compile("\\-")); 
		tokensReg.put(arithmeticToken.MUL , Pattern.compile("\\*")); 
		tokensReg.put(arithmeticToken.DIV , Pattern.compile("\\/")); 
		tokensReg.put(arithmeticToken.NUM , Pattern.compile("[1-9]+[0-9]*")); 
		tokensReg.put(arithmeticToken.LBR , Pattern.compile("\\(")); 
		tokensReg.put(arithmeticToken.RBR , Pattern.compile("\\)")); 
		tokensReg.put(arithmeticToken.FACT , Pattern.compile("\\!")); 
	} 
	public arithmeticToken next() { 
		if (placeString == inputString.length()) { 
		currToken =  arithmeticToken._END; 
			return currToken; 
		} 
		matcher.usePattern(space); 
		matcher.region(placeString, inputString.length()); 
		if (matcher.lookingAt()){ 
			placeString += matcher.end() - matcher.start(); 
		} 
		for (arithmeticToken token: arithmeticToken.values()) { 
			matcher.usePattern(tokensReg.get(token)); 
			matcher.region(placeString, inputString.length()); 
			if (matcher.lookingAt()) { 
				currToken = token; 
				currString = inputString.substring(placeString, placeString + matcher.end() - matcher.start()); 
				placeString += matcher.end() - matcher.start(); 
				return currToken; 
			} 
		} 
		System.err.println("Did not expect to see" + inputString.charAt(placeString)); 
		return(null); 
	} 
	public String getCurrString(){ 
		return currString; 
	} 
} 
