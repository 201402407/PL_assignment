package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(File file) throws FileNotFoundException { // Iterator를 이용해서 토큰들 리턴.
        ScanContext context = new ScanContext(file); // ScanContext 클래스에 file 집어넣는다.
        return new TokenIterator(context); // TokenIterator 객체 생성 후 리턴.
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(File file) throws FileNotFoundException { // stream 방식으로 토큰들 리턴.
        Iterator<Token> tokens = scan(file); // 위 Iterator사용 토큰들 리턴값을 tokens에 넣는다.
        return StreamSupport.stream( // ORDERED : 순서대로 분할. tokens에 넣는다.
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false); 
    } // spliterator를 이용해 병렬작업에 특화된 처리기법 사용.
}