package parser;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;


class TokenIterator implements Iterator<Token> {
	private final ScanContext context;
	private Optional<Token> nextToken;
	
	TokenIterator(ScanContext context) { // ������ �Ű������� ��� ������ �Լ�. Scanner���� �Ѿ��.
		this.context = context;
		nextToken = readToNextToken(context); // readToNextToken �Լ��� �Ű������� context �ִ´�.
	}

	@Override
	public boolean hasNext() { // next ���� ���� Ȯ��
		return nextToken.isPresent();
	}

	@Override
	public Token next() { // next ���� �� next ����Ŵ.
		if ( !nextToken.isPresent() ) {
			throw new NoSuchElementException();
		}
		
		Token token = nextToken.get();
		nextToken = readToNextToken(context);
		
		return token;
	}

	private Optional<Token> readToNextToken(ScanContext context) { // ���� ��ū ��ü�� �д� �Լ�. �����ڿ��� �Ѿ��.
		State current = State.START; // START ���¸� current String ������ ���� �� ����.
		while ( true ) { // �ݺ���
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
