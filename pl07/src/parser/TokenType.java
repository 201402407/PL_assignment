package parser;
//201402407 이해원

public enum TokenType {
	INT,  ID, QUESTION,  TRUE, 
	FALSE, NOT,  PLUS, MINUS, TIMES,
	DIV,  LT, GT, EQ, APOSTROPHE, 
	L_PAREN, R_PAREN,
	DEFINE, LAMBDA, COND, QUOTE, CAR, CDR, CONS,  ATOM_Q, NULL_Q, EQ_Q; 
	
	//private final int finalState;
	
	static TokenType fromSpecialCharactor(char ch) {
		switch ( ch ) {  //정규 표현식을 참고하여 ch와 매칭되는 keyword를 반환하는 case문 작성  
			case '+':		return PLUS;
			case '-':		return MINUS;
			case '*':		return TIMES;
			case '/':		return DIV;
			case '<':		return LT;
			case '>':		return GT;
			case '=':		return EQ;
			case '\'':		return APOSTROPHE;
			case '(':		return L_PAREN;
			case ')':		return R_PAREN;
			
			default: 
				throw new IllegalArgumentException("unregistered char: " + ch);  }
			}
	}

