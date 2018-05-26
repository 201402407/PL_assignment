package parser;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;


class TokenIterator implements Iterator<Token> {
	private final ScanContext context;
	private Optional<Token> nextToken;
	
	TokenIterator(ScanContext context) { // 문장을 매개변수로 삼는 생성자 함수. Scanner에서 넘어옴.
		this.context = context;
		nextToken = readToNextToken(context); // readToNextToken 함수에 매개변수로 context 넣는다.
	}

	@Override
	public boolean hasNext() { // next 존재 여부 확인
		return nextToken.isPresent();
	}

	@Override
	public Token next() { // next 리턴 후 next 가리킴.
		if ( !nextToken.isPresent() ) {
			throw new NoSuchElementException();
		}
		
		Token token = nextToken.get();
		nextToken = readToNextToken(context);
		
		return token;
	}

	private Optional<Token> readToNextToken(ScanContext context) { // 다음 토큰 자체를 읽는 함수. 생성자에서 넘어옴.
		State current = State.START; // START 상태를 current String 변수에 생성 및 선언.
		while ( true ) { // 반복문
			TransitionOutput output = current.transit(context);
			if ( output.nextState() == State.MATCHED ) {
				return output.token();
			}
			else if ( output.nextState() == State.FAILED ) {
				//System.out.println(current);
				throw new ScannerException();
			}
			else if ( output.nextState() == State.EOS ) {
				return Optional.empty();
			}
			
			current = output.nextState();
		}
	}
}
