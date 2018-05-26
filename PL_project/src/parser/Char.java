	package parser;
//201402407 ���ؿ�
class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) { 
		return new Char(ch, getType(ch)); // ���ο� ���ڿ� �ش�Ǵ� ������ ������ ���� Char ��ü�� ���� �� ����.
	}
	
	static Char end() { // MIN_VALUE�� �׿� ���� Ÿ���� END_OF_STREAM (�ƹ����� �� ���� ���)�� ����.
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	char value() {
		return this.value;
	}
	
	CharacterType type() {
		return this.type;
	}
	
	private static CharacterType getType(char ch) { // Ÿ���� �����ϴ� �Լ�.
		int code = (int)ch; // �ƽ�Ű�ڵ�� ��ȯ.
		if ( (code >= (int)'A' && code <= (int)'Z')
			|| (code >= (int)'a' && code <= (int)'z')) {
			return CharacterType.LETTER; // = Alphabet.
		}
		
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT; // = Digital.
		}
		
		switch ( ch ) { // Alphabet�̳� Digital�� �ƴ� ���ڿ� ���� ��� switch��. �� �߰�����.
			case '-':
			case '+':
			case '*':
			case '/':
			case '<':
			case '>':
			case '=':
			case '\'':
			case '(':
			case ')':
			case '#':
			case '?': // ?�� �߰�.(?)
				return CharacterType.SPECIAL_CHAR;
		}
		
		if ( Character.isWhitespace(ch) ) { // isWhitespace() : �ش� ���ڰ� �����̸� true, �ƴϸ� false ����.
			return CharacterType.WS; // WS = ����.
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
