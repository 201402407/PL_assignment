package pl02;
// 201402407 ���ؿ�

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {
	
		private int transM[][];
		private String source;
		private StringTokenizer st;
		public Scanner(String source) {
			this.transM = new int[4][128];
			this.source = source == null ? "" : source; // null�̸� ""��, null�� �ƴϸ� source�� this.source�� �Է�.
			st = new StringTokenizer(this.source, " ");
			initTM();
		}
		
		private void initTM() {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 128; j++) {
					switch(i) {
					case 0: // �� ó�� ������ ����. 0���� ����.
						if(j == 45) { // -(����)��ȣ
							this.transM[i][j] = 1;	
						}
						else if((j >= 48) && (j <= 57)) { // Digit 0-9
							this.transM[i][j] = 2;	
						}
						else if((j >= 65) && (j <= 90)) { // �ƽ�Ű �ڵ� A-Z
							this.transM[i][j] = 3;	
						}
						else if((j >= 97) && (j <= 122)) { // �ƽ�Ű �ڵ� a-z
							this.transM[i][j] = 3;	
						}
						else {
							this.transM[i][j] = -1;
						}
						break;
					case 1: // ���� ���ڸ� �а� �ش� ���ǿ� �����ϴ� ������ �� ���� ����� ��.
						if((j >= 48) && (j <= 57)) {
							this.transM[i][j] = 2;	
						}
						else {
							this.transM[i][j] = -1;
						}
						break;
					case 2: // ���� ���ڸ� �а� �ش� ���ǿ� �����ϴ� ������ �� ���� ����� ��.
						if((j >= 48) && (j <= 57)) { // 2�� �������� Digit�� �;� ���� �����ϴ�.
							this.transM[i][j] = 2;	
						}
						else { // Alphabet�� ���� -1�� ��ȯ�Ǽ� reject.
							this.transM[i][j] = -1;
						}
						break;
					case 3: // ���� ���ڸ� �а� �ش� ���ǿ� �����ϴ� ������ �� ���� ����� ��.
						if((j >= 48) && (j <= 57)) { // ���� mDFA���� 3���� ���Դµ� ���⼭ Digit�� �Ǹ� �Ȱ��� 3�̾�� �ϹǷ� 3���� ����.
							this.transM[i][j] = 3;	
						}
						else if((j >= 65) && (j <= 90)) { // �ƽ�Ű �ڵ� A-Z
							this.transM[i][j] = 3;	
						}
						else if((j >= 97) && (j <= 122)) { // �ƽ�Ű �ڵ� a-z
							this.transM[i][j] = 3;	
						}
						else {
							this.transM[i][j] = -1;
						}
						break;
					}
				}
			}
		}
		
		private Token nextToken() {
			int stateOld = 0, stateNew;
			
			if(!st.hasMoreTokens())	return null; // ��ū�� �� �ִ��� �˻�.
			
			String temp = st.nextToken(); // �� ���� ��ū�� ����.
			Token result = null;
			for(int i = 0; i < temp.length(); i++) { // ���ڿ��� ���ڸ� �ϳ��� ������ ���� �Ǻ�
				stateNew = transM[stateOld][temp.charAt(i)];
				if(stateNew == -1) { // �Էµ� ������ ���°� reject�̹Ƿ� �����޼��� ��� �� return.
					System.out.print(String.format(">> acceptState error : %s\n", temp)); // ���� �޼���.
					return null; // null ����.
				}
				stateOld = stateNew;
			}
			for(TokenType t : TokenType.values()) {
				if(t.finalState == stateOld) {
					result = new Token(t, temp);
					break;
				}
			}
			return result;
		}
		
		public List<Token> tokenize() { // Token ����Ʈ ��ȯ. nextToken() �̿�
			List<Token> TokenList = new ArrayList<Token>();
			while(st.hasMoreTokens()) {
				Token Tokentemp = this.nextToken();
				if(Tokentemp != null) {
				TokenList.add(Tokentemp);
				}
			}
			return TokenList;
		}
		
	public static class Token {
		public final TokenType type;
		public final String lexme;
		
		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}
		
		@Override
		public String toString() {
			return String.format("[%s : %s]", type.toString(), lexme);
		}
	}
	
	public enum TokenType {
		ID(3), INT(2);
		
		private final int finalState;
		
		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		FileReader fr;
		try {
			fr = new FileReader("as02.txt");
			BufferedReader br = new BufferedReader(fr); // BufferedReader�� ���� �б�.
			String source = br.readLine(); // txt file ������ string�� ����.
			Scanner s = new Scanner(source);
			System.out.println("name : 201402407 ���ؿ�");
			System.out.println(">>> Input Token : " + s.source);
			List<Token> tokens = s.tokenize();
			for(int i = 0; i < tokens.size(); i++) {
				System.out.println(tokens.get(i).toString());
			}
		}catch(IOException e) {
				e.printStackTrace(); // ����ó��
			}
	}
}


