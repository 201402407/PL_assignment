package pl01;
// 201402407 ���ؿ�
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
			BufferedReader br = new BufferedReader(fr); // BufferedReader�� ���� �б�
			String inputString = br.readLine();
			for(int i = 0; i < inputString.length(); i++) {
				list.add(inputString.charAt(i));
			}
		}catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println(list.toString()); // ����(4) toString �޼ҵ� ���� Ȯ��. hw01.txt ���� �� ����Ʈ Ȯ��.
			
			list.add('!'); // ����(1) LinkLast �޼ҵ� ���� Ȯ��.
			System.out.println(list.toString());
			
			System.out.println(list.get(5)); // ����(2) node �޼ҵ� ���� Ȯ��. �ε����� 0���� ����.
			
			System.out.println(list.size()); // ����(3) length �޼ҵ� ���� Ȯ��.
			
			list.reverse(); // ����(5) reverse �޼ҵ� ����.
			System.out.println(list.toString()); // ����(5) reverse �޼ҵ� ���� Ȯ��.
		}
	}

