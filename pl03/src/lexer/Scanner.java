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
    public static Iterator<Token> scan(File file) throws FileNotFoundException { // Iterator�� �̿��ؼ� ��ū�� ����.
        ScanContext context = new ScanContext(file); // ScanContext Ŭ������ file ����ִ´�.
        return new TokenIterator(context); // TokenIterator ��ü ���� �� ����.
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(File file) throws FileNotFoundException { // stream ������� ��ū�� ����.
        Iterator<Token> tokens = scan(file); // �� Iterator��� ��ū�� ���ϰ��� tokens�� �ִ´�.
        return StreamSupport.stream( // ORDERED : ������� ����. tokens�� �ִ´�.
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false); 
    } // spliterator�� �̿��� �����۾��� Ưȭ�� ó����� ���.
}