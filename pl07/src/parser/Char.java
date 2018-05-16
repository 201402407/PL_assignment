	package parser;
//201402407 이해원
class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) { 
		return new Char(ch, getType(ch)); // 새로운 문자와 해당되는 문자의 종류를 넣은 Char 객체를 생성 후 리턴.
	}
	
	static Char end() { // MIN_VALUE와 그에 따른 타입인 END_OF_STREAM (아무곳도 못 가는 경우)를 리턴.
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
	
	private static CharacterType getType(char ch) { // 타입을 리턴하는 함수.
		int code = (int)ch; // 아스키코드로 변환.
		if ( (code >= (int)'A' && code <= (int)'Z')
			|| (code >= (int)'a' && code <= (int)'z')) {
			return CharacterType.LETTER; // = Alphabet.
		}
		
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT; // = Digital.
		}
		
		switch ( ch ) { // Alphabet이나 Digital이 아닌 문자에 대한 경우 switch문. 더 추가하자.
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
			case '?': // ?를 추가.(?)
				return CharacterType.SPECIAL_CHAR;
		}
		
		if ( Character.isWhitespace(ch) ) { // isWhitespace() : 해당 문자가 공백이면 true, 아니면 false 리턴.
			return CharacterType.WS; // WS = 공백.
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
