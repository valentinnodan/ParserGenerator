package result.varDef ; 
 
import java.util.*; 
import java.io.IOException; 
public class varDefSyntaxAnalizer { 
	public class Node { 
		public String symbol; 
		public ArrayList<Node> children; 
 
		public Node(String name) { 
			symbol = name; 
			children = new ArrayList<>(); 
		} 
		public void addChild(Node child) { 
			children.add(child); 
		} 
		public void visualize(int tab, ArrayList<Boolean> padding) { 
			if (tab != 0) { 
				System.out.print("____"); 
			} 
			System.out.println(this.symbol); 
			if (!(children.size() == 0)) { 
				for (int i = 0; i < children.size(); i++) { 
					ArrayList<Boolean> currentList = new ArrayList<>(); 
					currentList.addAll(padding); 
					for (int j = 0; j < tab; j++) { 
						if (currentList.get(j)) { 
							System.out.print("|   "); 
						} else { 
							System.out.print("    "); 
						} 
					} 
					currentList.add(i != children.size() - 1); 
					System.out.print("|"); 
					children.get(i).visualize(tab + 1, currentList); 
				} 
			} 
		} 
	} 
	public class E extends Node { 
		public E () { 
			super( "E" ); 
		} 
	} 
 
	public class STR extends Node { 
		public STR () { 
			super( "STR" ); 
		} 
	} 
 
	public class VARA extends Node { 
		public VARA () { 
			super( "VARA" ); 
		} 
	} 
 
	public class STRA extends Node { 
		public STRA () { 
			super( "STRA" ); 
		} 
	} 
 
	private varDefLexicalAnalizer lexicalAnalizer; 
 
	public varDefSyntaxAnalizer (varDefLexicalAnalizer lexicalAnalizer) { 
		this.lexicalAnalizer = lexicalAnalizer; 
		lexicalAnalizer.next(); 
	} 
	public E e() throws IOException { 
		E res = new E(); 
		switch (lexicalAnalizer.currToken){ 
			case TYPE : 
				{ 
				assert(lexicalAnalizer.currToken == varDefToken.TYPE); 
				res.addChild(new Node("TYPE")); 
				this.lexicalAnalizer.next(); 
				STR str = str(); 
				res.addChild(str); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public STR str() throws IOException { 
		STR res = new STR(); 
		switch (lexicalAnalizer.currToken){ 
			case VAR : 
			case POINTER : 
				{ 
				VARA varA = varA(); 
				res.addChild(varA); 
				STRA strA = strA(); 
				res.addChild(strA); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public VARA varA() throws IOException { 
		VARA res = new VARA(); 
		switch (lexicalAnalizer.currToken){ 
			case POINTER : 
				{ 
				assert(lexicalAnalizer.currToken == varDefToken.POINTER); 
				res.addChild(new Node("POINTER")); 
				this.lexicalAnalizer.next(); 
				VARA varA = varA(); 
				res.addChild(varA); 
				return res; 
				} 
			case VAR : 
				{ 
				assert(lexicalAnalizer.currToken == varDefToken.VAR); 
				res.addChild(new Node("VAR")); 
				this.lexicalAnalizer.next(); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public STRA strA() throws IOException { 
		STRA res = new STRA(); 
		switch (lexicalAnalizer.currToken){ 
			case EOF : 
				{ 
				assert(lexicalAnalizer.currToken == varDefToken.EOF); 
				res.addChild(new Node("EOF")); 
				this.lexicalAnalizer.next(); 
				return res; 
				} 
			case COMMA : 
				{ 
				assert(lexicalAnalizer.currToken == varDefToken.COMMA); 
				res.addChild(new Node("COMMA")); 
				this.lexicalAnalizer.next(); 
				STR str = str(); 
				res.addChild(str); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
} 
