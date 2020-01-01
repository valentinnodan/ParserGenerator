package result.varDef ; 
 
import java.util.*; 
import java.util.regex.*; 
 
public class varDefLexicalAnalizer { 
	private String inputString; 
	private int placeString; 
	public varDefToken currToken; 
	private Pattern space = Pattern.compile("[ |\n|\r|\t]+"); 
	private Matcher matcher; 
	private String currString; 
	HashMap<varDefToken, Pattern> tokensReg = new HashMap<>(); 
 
	public varDefLexicalAnalizer(String inputString) { 
		this.inputString = inputString; 
		placeString = 0; 
		matcher = Pattern.compile("").matcher(inputString); 
		tokensReg.put(varDefToken._END, Pattern.compile("$")); 
		tokensReg.put(varDefToken.TYPE , Pattern.compile("(int\\s)|(char\\s)|(float\\s)|(bool\\s)")); 
		tokensReg.put(varDefToken.EOF , Pattern.compile("(\\;[\\s]*$)")); 
		tokensReg.put(varDefToken.COMMA , Pattern.compile("\\,")); 
		tokensReg.put(varDefToken.POINTER , Pattern.compile("\\*")); 
		tokensReg.put(varDefToken.VAR , Pattern.compile("[a-z]")); 
	} 
	public varDefToken next() { 
		if (placeString == inputString.length()) { 
		currToken =  varDefToken._END; 
			return currToken; 
		} 
		matcher.usePattern(space); 
		matcher.region(placeString, inputString.length()); 
		if (matcher.lookingAt()){ 
			placeString += matcher.end() - matcher.start(); 
		} 
		for (varDefToken token: varDefToken.values()) { 
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
