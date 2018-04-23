package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import Node.CuteParser;
import Node.NodePrinter;

public class ScannerMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ScannerMain.class.getClassLoader();
        File file = new File(cloader.getResource("lexer/as05.txt").getFile()); // 파일 읽기
        CuteParser cuteParser = new CuteParser(file);
        NodePrinter.getPrinter(System.out).prettyPrint(cuteParser.parseExpr());
    }
    
    // use tokens as a Stream 
    private static void testTokenStream(File file) throws FileNotFoundException {
    	System.out.println(">>> 파일에서 문장을 읽어옵니다. ");
        Stream<Token> tokens = Scanner.stream(file);
        System.out.println(">>> 단어별 분석을 끝냈습니다. ");
        System.out.println(">>> 출력합니다. ");
        tokens.map(ScannerMain::toString).forEach(System.out::println); // map을 이용해서 Token들 정규표현 출력.
    }    
    
    private static String toString(Token token) {
        return String.format("%-3s: %s", token.type().name(), token.lexme());
    }
    
}
