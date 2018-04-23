package pl01;
// 201402407 이해원
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	
	public static void main(String args[]) throws FileNotFoundException{
		RecusionLinkedList list = new RecusionLinkedList();
		FileReader fr;
		try {
			fr = new FileReader("hw01.txt");
			BufferedReader br = new BufferedReader(fr); // BufferedReader로 파일 읽기
			String inputString = br.readLine();
			for(int i = 0; i < inputString.length(); i++) {
				list.add(inputString.charAt(i));
			}
		}catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println(list.toString()); // 과제(4) toString 메소드 구현 확인. hw01.txt 내용 및 리스트 확인.
			
			list.add('!'); // 과제(1) LinkLast 메소드 구현 확인.
			System.out.println(list.toString());
			
			System.out.println(list.get(5)); // 과제(2) node 메소드 구현 확인. 인덱스는 0부터 시작.
			
			System.out.println(list.size()); // 과제(3) length 메소드 구현 확인.
			
			list.reverse(); // 과제(5) reverse 메소드 실행.
			System.out.println(list.toString()); // 과제(5) reverse 메소드 구현 확인.
		}
	}

