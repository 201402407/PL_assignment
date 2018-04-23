package pl01;
//201402407 ���ؿ�
public class Recusion {
	
	public static void main(String args[]) {
		System.out.println(fibonacci(10));
		System.out.println(recursiveAnt(10));
		System.out.println("201402407 ���ؿ� !");
	}

	public static int fibonacci(int n) {
		if(n == 0) {
			return 0;
		}
		if(n == 1) {
			return 1;
		}
		return fibonacci(n-1) + fibonacci(n-2);
	}
	
	public static String recursiveAnt(int n) {// ä���� ���, recuison ���
		if(n == 1) {
			return "1";
		}
		return makeResult(recursiveAnt(n-1));
	}
	
	public static String makeResult(String previous) {  // ä���� ���, �ݺ��� �ִ� 1ȸ ��� ����
		String result = "";
		if(previous.length() == 1) {
			return "11";
		}
		int count = 1;
		for(int i = 1; i < previous.length(); i++) {
			if(previous.charAt(i - 1) == previous.charAt(i)) {
				count++;
			}
			else {
				result += previous.charAt(i - 1);
				result += (Integer.toString(count));
				count = 1;
			}
		}
		result += previous.charAt(previous.length() - 1);
		result += (Integer.toString(count));
		return result;
	}
		 
}
