package lexer;
//201402407 ���ؿ�
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
	START { // ù ���� ���� enum
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar(); // CharStream Ŭ������ nextChar()�� ���� Char Ŭ���� ��ü �����ؼ� ch�� ����.
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
					if(v == '#') { // #�� ���� ����� ���� ó��.
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
				case LETTER: // Alphabet�� ���� ���. 
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_ID; // ���� ��쵵 ID�� ������ �Ѵ�. (�ݺ�)
				case SPECIAL_CHAR:
					if(v == '?') {	// ID �߰��� ? �� ���� ���. ?�� �������� ������ ��쵵 ������ �켱 ?���� �ְ� �ѹ� �� ACCEPT_ID�� ������.
						 context.append(v);
						 return GOTO_ACCEPT_ID;
					}
					else { // '?' ���� ��ȣ�� ���´ٸ� �翬�� FAILED�� �ǹǷ�
						return GOTO_FAILED;
					}
				case WS:
				case END_OF_STREAM: // EOS�� ���� ó��. ���̻��� ���ڰ� ���� ��.
					Token temp = Token.ofName(context.getLexime()); // KEYWORDS�� ���� ���� ó��.
					if( temp != null) { // null�� �ƴ϶� ���� QUESTION�̵� ID�̵� TokenType�� �����ٴ� ���̹Ƿ� MATCH�� �����ϴ�.
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
					throw new AssertionError(); // ���� ó��.
			}
		}
	},
	SIGN { // '+' �� '-' ���� ��ȣ�� ���. GOTO_MATCHED ���.
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();	
			char strtemp;
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT: // ���ڸ� INT�� ����.
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
					} // ��ȣ �ϳ��ۿ� ���� context�̹Ƿ� getLexime()�� �̿�, String���� char�� ��ȯ�ؼ� �Ű������� �̿��ߴ�.
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
					throw new AssertionError(); // ���� ó��.
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
