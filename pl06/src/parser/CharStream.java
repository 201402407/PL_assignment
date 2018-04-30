package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class CharStream {
	private final Reader reader;
	private Character cache;
	
	static CharStream from(File file) throws FileNotFoundException {
		return new CharStream(new FileReader(file)); // ��ü ���� �� �����ϴ� �Լ�. FileReader�� �̿��� ���� ����. �� �� �ٽ� Scanner�� ���ư�.
	}
	
	CharStream(Reader reader) {
		this.reader = reader;
		this.cache = null;
	}
	
	Char nextChar() {
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			
			return Char.of(ch);
		}
		else {
			try {
				int ch = reader.read(); // ���� ���ڸ� �д´�.
				if ( ch == -1 ) { // �ƹ� ������ �ش��� ���� �ʴ� ����. �״�� EOS.
					return Char.end();
				}
				else {
					return Char.of((char)ch); // of()�� �Ű������� ch�� �ְ� �� ����.
				}
			}
			catch ( IOException e ) {
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
