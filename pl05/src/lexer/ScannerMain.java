package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import Node.CuteParser;
import Node.NodePrinter;

public class ScannerMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ScannerMain.class.getClassLoader();
        File file = new File(cloader.getResource("lexer/as05.txt").getFile()); // ���� �б�
        CuteParser cuteParser = new CuteParser(file);
        NodePrinter.getPrinter(System.out).prettyPrint(cuteParser.parseExpr());
    }
    
    // use tokens as a Stream 
    private static void testTokenStream(File file) throws FileNotFoundException {
    	System.out.println(">>> ���Ͽ��� ������ �о�ɴϴ�. ");
        Stream<Token> tokens = Scanner.stream(file);
        System.out.println(">>> �ܾ �м��� ���½��ϴ�. ");
        System.out.println(">>> ����մϴ�. ");
        tokens.map(ScannerMain::toString).forEach(System.out::println); // map�� �̿��ؼ� Token�� ����ǥ�� ���.
    }    
    
    private static String toString(Token token) {
        return String.format("%-3s: %s", token.type().name(), token.lexme());
    }
    
}
