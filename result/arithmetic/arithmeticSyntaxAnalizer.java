package result.arithmetic ; 
 
import java.util.*; 
import java.io.IOException; 
public class arithmeticSyntaxAnalizer { 
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
		public int v ; 
	} 
 
	public class DIVTRANS extends Node { 
		public DIVTRANS () { 
			super( "DIVTRANS" ); 
		} 
		public int v ; 
	} 
 
	public class F extends Node { 
		public F () { 
			super( "F" ); 
		} 
		public int v ; 
	} 
 
	public class EA extends Node { 
		public EA () { 
			super( "EA" ); 
		} 
		public int v ; 
	} 
 
	public class FACTOPA extends Node { 
		public FACTOPA () { 
			super( "FACTOPA" ); 
		} 
		public int v ; 
	} 
 
	public class FACTTRANS extends Node { 
		public FACTTRANS () { 
			super( "FACTTRANS" ); 
		} 
		public int v ; 
	} 
 
	public class TA extends Node { 
		public TA () { 
			super( "TA" ); 
		} 
		public int v ; 
	} 
 
	public class MULTRANS extends Node { 
		public MULTRANS () { 
			super( "MULTRANS" ); 
		} 
		public int v ; 
	} 
 
	public class PLUSTRANS extends Node { 
		public PLUSTRANS () { 
			super( "PLUSTRANS" ); 
		} 
		public int v ; 
	} 
 
	public class T extends Node { 
		public T () { 
			super( "T" ); 
		} 
		public int v ; 
	} 
 
	public class MINUSTRANS extends Node { 
		public MINUSTRANS () { 
			super( "MINUSTRANS" ); 
		} 
		public int v ; 
	} 
 
	public class FACTOP extends Node { 
		public FACTOP () { 
			super( "FACTOP" ); 
		} 
	} 
 
	private arithmeticLexicalAnalizer lexicalAnalizer; 
 
	public arithmeticSyntaxAnalizer (arithmeticLexicalAnalizer lexicalAnalizer) { 
		this.lexicalAnalizer = lexicalAnalizer; 
		lexicalAnalizer.next(); 
	} 
	public E e() throws IOException { 
		E res = new E(); 
		switch (lexicalAnalizer.currToken){ 
			case NUM : 
			case LBR : 
				{ 
				T t = t(); 
				int u = t.v; 
				res.addChild(t); 
				EA ea = ea(u); 
				res.v = ea.v; 
				res.addChild(ea); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public DIVTRANS divTrans(int op1, int op2) throws IOException { 
		DIVTRANS res = new DIVTRANS(); 
		switch (lexicalAnalizer.currToken){ 
			case DIV : 
			case _END : 
			case RBR : 
			case MUL : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = java.lang.Math.round(op1 / op2); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public F f() throws IOException { 
		F res = new F(); 
		switch (lexicalAnalizer.currToken){ 
			case NUM : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.NUM); 
				res.addChild(new Node("NUM")); 
				int u = Integer.parseInt(lexicalAnalizer.getCurrString()); 
				this.lexicalAnalizer.next(); 
				FACTOPA factOpA = factOpA(u); 
				res.v = factOpA.v; 
				res.addChild(factOpA); 
				return res; 
				} 
			case LBR : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.LBR); 
				res.addChild(new Node("LBR")); 
				this.lexicalAnalizer.next(); 
				E e = e(); 
				int u = e.v; 
				res.addChild(e); 
				assert(lexicalAnalizer.currToken == arithmeticToken.RBR); 
				res.addChild(new Node("RBR")); 
				this.lexicalAnalizer.next(); 
				FACTOPA factOpA = factOpA(u); 
				res.v = factOpA.v; 
				res.addChild(factOpA); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public EA ea(int a) throws IOException { 
		EA res = new EA(); 
		switch (lexicalAnalizer.currToken){ 
			case _END : 
			case RBR : 
				{ 
				res.v = a; 
				return res; 
				} 
			case PLUS : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.PLUS); 
				res.addChild(new Node("PLUS")); 
				this.lexicalAnalizer.next(); 
				T t = t(); 
				int u = t.v; 
				res.addChild(t); 
				PLUSTRANS plusTrans = plusTrans(a, u); 
				int acc = plusTrans.v; 
				res.addChild(plusTrans); 
				EA ea = ea(acc); 
				res.v = ea.v; 
				res.addChild(ea); 
				return res; 
				} 
			case MINUS : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.MINUS); 
				res.addChild(new Node("MINUS")); 
				this.lexicalAnalizer.next(); 
				T t = t(); 
				int u = t.v; 
				res.addChild(t); 
				MINUSTRANS minusTrans = minusTrans(a, u); 
				int acc = minusTrans.v; 
				res.addChild(minusTrans); 
				EA ea = ea(acc); 
				
    res.v = ea.v;
     
				res.addChild(ea); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public FACTOPA factOpA(int a) throws IOException { 
		FACTOPA res = new FACTOPA(); 
		switch (lexicalAnalizer.currToken){ 
			case FACT : 
				{ 
				FACTOP factOp = factOp(); 
				res.addChild(factOp); 
				FACTTRANS factTrans = factTrans(a); 
				int u = factTrans.v; 
				res.addChild(factTrans); 
				FACTOPA factOpA = factOpA(u); 
				res.v = factOpA.v; 
				res.addChild(factOpA); 
				return res; 
				} 
			case DIV : 
			case _END : 
			case RBR : 
			case MUL : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = a; 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public FACTTRANS factTrans(int a) throws IOException { 
		FACTTRANS res = new FACTTRANS(); 
		switch (lexicalAnalizer.currToken){ 
			case DIV : 
			case _END : 
			case RBR : 
			case MUL : 
			case FACT : 
			case PLUS : 
			case MINUS : 
				{ 
				 int acc = 1;
    for (int i = 1; i <= a; i++) {
        acc *= i;
    }
    res.v = acc;
     
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public TA ta(int a) throws IOException { 
		TA res = new TA(); 
		switch (lexicalAnalizer.currToken){ 
			case _END : 
			case RBR : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = a; 
				return res; 
				} 
			case MUL : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.MUL); 
				res.addChild(new Node("MUL")); 
				this.lexicalAnalizer.next(); 
				F f = f(); 
				int u = f.v; 
				res.addChild(f); 
				MULTRANS mulTrans = mulTrans(a, u); 
				int acc = mulTrans.v; 
				res.addChild(mulTrans); 
				TA ta = ta(acc); 
				res.v = ta.v; 
				res.addChild(ta); 
				return res; 
				} 
			case DIV : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.DIV); 
				res.addChild(new Node("DIV")); 
				this.lexicalAnalizer.next(); 
				F f = f(); 
				int u = f.v; 
				res.addChild(f); 
				DIVTRANS divTrans = divTrans(a, u); 
				int acc = divTrans.v; 
				res.addChild(divTrans); 
				TA ta = ta(acc); 
				res.v = ta.v; 
				res.addChild(ta); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public MULTRANS mulTrans(int op1, int op2) throws IOException { 
		MULTRANS res = new MULTRANS(); 
		switch (lexicalAnalizer.currToken){ 
			case DIV : 
			case _END : 
			case RBR : 
			case MUL : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = op1 * op2; 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public PLUSTRANS plusTrans(int op1, int op2) throws IOException { 
		PLUSTRANS res = new PLUSTRANS(); 
		switch (lexicalAnalizer.currToken){ 
			case _END : 
			case RBR : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = op1 + op2; 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public T t() throws IOException { 
		T res = new T(); 
		switch (lexicalAnalizer.currToken){ 
			case NUM : 
			case LBR : 
				{ 
				F f = f(); 
				int u = f.v; 
				res.addChild(f); 
				TA ta = ta(u); 
				res.v = ta.v; 
				res.addChild(ta); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public MINUSTRANS minusTrans(int op1, int op2) throws IOException { 
		MINUSTRANS res = new MINUSTRANS(); 
		switch (lexicalAnalizer.currToken){ 
			case _END : 
			case RBR : 
			case PLUS : 
			case MINUS : 
				{ 
				res.v = op1 - op2; 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
	public FACTOP factOp() throws IOException { 
		FACTOP res = new FACTOP(); 
		switch (lexicalAnalizer.currToken){ 
			case FACT : 
				{ 
				assert(lexicalAnalizer.currToken == arithmeticToken.FACT); 
				res.addChild(new Node("FACT")); 
				this.lexicalAnalizer.next(); 
				return res; 
				} 
			default: 
				throw new IOException("Did not expect to get " + lexicalAnalizer.currToken + " expression"); 
		} 
	} 
} 
