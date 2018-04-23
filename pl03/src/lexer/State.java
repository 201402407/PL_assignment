package lexer;
//201402407 이해원
import static lexer.TokenType.ID;
import static lexer.TokenType.INT;
import static lexer.TokenType.TRUE;
import static lexer.TokenType.FALSE;
import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_BOOL;
import static lexer.TransitionOutput.GOTO_START;


enum State {
	START { // 첫 시작 상태 enum
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar(); // CharStream 클래스의 nextChar()을 통해 Char 클래스 객체 리턴해서 ch에 넣음.
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER: 
					context.append(v);
					return GOTO_ACCEPT_ID;
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					context.append(v);
					if(v == '#') { // #이 나올 경우의 상태 처리.
						return GOTO_BOOL;
					}
					else {
					return GOTO_SIGN;
					}
				case WS:
					return GOTO_START;
				case END_OF_STREAM:
					return GOTO_EOS;
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_ID { 
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER: // Alphabet일 때의 경우. 
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_ID; // 다음 경우도 ID로 가도록 한다. (반복)
				case SPECIAL_CHAR:
					if(v == '?') {	// ID 중간에 ? 가 나온 경우. ?가 마지막에 들어오는 경우도 있으니 우선 ?까지 넣고 한번 더 ACCEPT_ID로 가보자.
						 context.append(v);
						 return GOTO_ACCEPT_ID;
					}
					else { // '?' 외의 기호가 들어온다면 당연히 FAILED가 되므로
						return GOTO_FAILED;
					}
				case WS:
				case END_OF_STREAM: // EOS에 대한 처리. 더이상의 문자가 없을 때.
					Token temp = Token.ofName(context.getLexime()); // KEYWORDS에 대한 예외 처리.
					if( temp != null) { // null이 아니란 뜻은 QUESTION이든 ID이든 TokenType을 가졌다는 뜻이므로 MATCH가 가능하다.
						return GOTO_MATCHED(temp);
					}
					else {
						return GOTO_FAILED;
					}
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(ch.value());
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					return GOTO_MATCHED(INT, context.getLexime());
				default:
					throw new AssertionError(); // 예외 처리.
			}
		}
	},
	SIGN { // '+' 나 '-' 등의 기호인 경우. GOTO_MATCHED 사용.
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();	
			char strtemp;
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT: // 숫자면 INT로 간다.
					strtemp = context.getLexime().charAt(0);
					if((strtemp == '+') || (strtemp == '-')) {
						context.append(strtemp);	
						context.append(v);
						return GOTO_ACCEPT_INT;
					}
					else {
						return GOTO_FAILED;
					}
				case WS:
					//return GOTO_FAILED;
				case END_OF_STREAM:
					strtemp = context.getLexime().charAt(0);
					if(TokenType.fromSpecialCharactor(strtemp) != null) {	
						return GOTO_MATCHED(TokenType.fromSpecialCharactor(strtemp), String.valueOf(strtemp));
					} // 기호 하나밖에 없는 context이므로 getLexime()을 이용, String에서 char로 변환해서 매개변수로 이용했다.
					else {
						return GOTO_FAILED;
					}
					
				default:
					throw new AssertionError();
			}
		}
	},
	BOOL {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			String strtemp = context.getLexime();
			switch ( ch.type() ) {
				case LETTER:
					if(strtemp.equals("#")) {
						if((v == 'T') || (v == 'F')) {
							context.append(strtemp.charAt(0));
							context.append(v);
							return GOTO_BOOL;
						}
					}
					//return GOTO_FAILED;
				case DIGIT:
					return GOTO_FAILED;
				case SPECIAL_CHAR:
					if(strtemp.equals("#")) {
						context.append(v);
						return GOTO_BOOL;
					}
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					if(strtemp.equals("#T")) {
						return GOTO_MATCHED(TRUE, strtemp);
					}
					else if(strtemp.equals("#F")) {
						return GOTO_MATCHED(FALSE, strtemp);
					}
					return GOTO_FAILED;
				default:
					throw new AssertionError(); // 예외 처리.
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED{
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};
	
	abstract TransitionOutput transit(ScanContext context);
}
