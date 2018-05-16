package parser;
//201402407 ���ؿ�
import java.util.HashMap;
import java.util.Map;

public class Token {
	private final TokenType type;
	private final String lexme;
	
	static Token ofName(String lexme) { // ��ū ���ڵ��� ���� �а��� ��������.
		TokenType type = KEYWORDS.get(lexme);
		if(type != null) { // �ش� �ܾ KEYWORDS�� �����ϴ� ���.
			return new Token(type, lexme); // �״�� Ÿ���� ������ ��ū ����.
		}
		else if(lexme.endsWith("?")) { // ?�� ������ token(�ܾ�)�ΰ� ?
			if(lexme.substring(0, lexme.length() - 1).contains("?")) { // ���� ?�� �� ���� �ϳ��� �ִ� ���� �ƴ�, �߰����� �ִ� ����� ����ó��.
				throw new ScannerException("invalid ID=" + lexme); // ���� ó��.
			}
			return new Token(TokenType.QUESTION, lexme); // QUESTION Tokentype�� ����.
		}
		else if(lexme.contains("?")) { // ���� �߰��� �ִ� ? �� ���� ���� ó��.
			throw new ScannerException("invalid ID=" + lexme);
		}
		else {
			return new Token(TokenType.ID, lexme); // ID type�� ��ū ����.
		}
	}
	
	Token(TokenType type, String lexme) {
		this.type = type;
		this.lexme = lexme;
	}
	
	public TokenType type() {
		return this.type;
	}
	
	public String lexme() {
		return this.lexme;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", type, lexme);
	}
	
	private static final Map<String,TokenType> KEYWORDS = new HashMap<>();
	static {
		KEYWORDS.put("define", TokenType.DEFINE);
		KEYWORDS.put("lambda", TokenType.LAMBDA);
		KEYWORDS.put("cond", TokenType.COND);
		KEYWORDS.put("quote", TokenType.QUOTE);
		KEYWORDS.put("not", TokenType.NOT);
		KEYWORDS.put("cdr", TokenType.CDR);
		KEYWORDS.put("car", TokenType.CAR);
		KEYWORDS.put("cons", TokenType.CONS);
		KEYWORDS.put("eq?", TokenType.EQ_Q);
		KEYWORDS.put("null?", TokenType.NULL_Q);
		KEYWORDS.put("atom?", TokenType.ATOM_Q);
	}
}
