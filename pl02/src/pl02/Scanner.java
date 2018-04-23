package pl02;
// 201402407 이해원

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
			this.source = source == null ? "" : source; // null이면 ""을, null이 아니면 source를 this.source에 입력.
			st = new StringTokenizer(this.source, " ");
			initTM();
		}
		
		private void initTM() {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 128; j++) {
					switch(i) {
					case 0: // 맨 처음 들어오는 문자. 0부터 시작.
						if(j == 45) { // -(음수)기호
							this.transM[i][j] = 1;	
						}
						else if((j >= 48) && (j <= 57)) { // Digit 0-9
							this.transM[i][j] = 2;	
						}
						else if((j >= 65) && (j <= 90)) { // 아스키 코드 A-Z
							this.transM[i][j] = 3;	
						}
						else if((j >= 97) && (j <= 122)) { // 아스키 코드 a-z
							this.transM[i][j] = 3;	
						}
						else {
							this.transM[i][j] = -1;
						}
						break;
					case 1: // 이전 문자를 읽고 해당 조건에 부합하는 곳으로 간 곳의 경우의 수.
						if((j >= 48) && (j <= 57)) {
							this.transM[i][j] = 2;	
						}
						else {
							this.transM[i][j] = -1;
						}
						break;
					case 2: // 이전 문자를 읽고 해당 조건에 부합하는 곳으로 간 곳의 경우의 수.
						if((j >= 48) && (j <= 57)) { // 2인 곳에서는 Digit만 와야 종료 가능하다.
							this.transM[i][j] = 2;	
						}
						else { // Alphabet이 오면 -1로 전환되서 reject.
							this.transM[i][j] = -1;
						}
						break;
					case 3: // 이전 문자를 읽고 해당 조건에 부합하는 곳으로 간 곳의 경우의 수.
						if((j >= 48) && (j <= 57)) { // 만약 mDFA에서 3으로 들어왔는데 여기서 Digit이 되면 똑같이 3이어야 하므로 3으로 설정.
							this.transM[i][j] = 3;	
						}
						else if((j >= 65) && (j <= 90)) { // 아스키 코드 A-Z
							this.transM[i][j] = 3;	
						}
						else if((j >= 97) && (j <= 122)) { // 아스키 코드 a-z
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
			
			if(!st.hasMoreTokens())	return null; // 토큰이 더 있는지 검사.
			
			String temp = st.nextToken(); // 그 다음 토큰을 받음.
			Token result = null;
			for(int i = 0; i < temp.length(); i++) { // 문자열의 문자를 하나씩 가져와 상태 판별
				stateNew = transM[stateOld][temp.charAt(i)];
				if(stateNew == -1) { // 입력된 문자의 상태가 reject이므로 에러메세지 출력 후 return.
					System.out.print(String.format(">> acceptState error : %s\n", temp)); // 오류 메세지.
					return null; // null 리턴.
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
		
		public List<Token> tokenize() { // Token 리스트 반환. nextToken() 이용
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
			BufferedReader br = new BufferedReader(fr); // BufferedReader로 파일 읽기.
			String source = br.readLine(); // txt file 내용을 string에 저장.
			Scanner s = new Scanner(source);
			System.out.println("name : 201402407 이해원");
			System.out.println(">>> Input Token : " + s.source);
			List<Token> tokens = s.tokenize();
			for(int i = 0; i < tokens.size(); i++) {
				System.out.println(tokens.get(i).toString());
			}
		}catch(IOException e) {
				e.printStackTrace(); // 예외처리
			}
	}
}


