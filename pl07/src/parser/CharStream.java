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
		return new CharStream(new FileReader(file)); // 객체 생성 후 리턴하는 함수. FileReader를 이용해 문장 읽음. 그 후 다시 Scanner로 돌아감.
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
				int ch = reader.read(); // 단일 문자를 읽는다.
				if ( ch == -1 ) { // 아무 종류도 해당이 되지 않는 문자. 그대로 EOS.
					return Char.end();
				}
				else {
					return Char.of((char)ch); // of()에 매개변수로 ch를 넣고 값 리턴.
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
