// 201402407 이해원
package interpreter;

import Node.*;
import parser.TokenType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CuteInterpreter {
	private static Map<String, Node> DefineTable = new HashMap<String, Node>();
	
	private boolean insertTable(Node id, ListNode value) {
		if(id == null || value == null) {
			return false;
		}
		
		String Id = ((IdNode)runExpr(id)).getIdNode(); // 인자 id가 무조건 IdNode이거나, QuoteNode의 NodeInside도 IdNode라고 가정.
		Node Value = runExpr(value.car());
		
		for(String key : DefineTable.keySet()) {
			if(id.equals(key)) 
				DefineTable.remove(key); // 같은 문자가 존재한다면, 제거.
		}
		DefineTable.put(Id, Value); // 집어넣기.
		return true;
	}
	
	public Node lookupTable(String id) { // HashMap에서 데이터 꺼내오기.
		if(DefineTable.get(id) == null) {
			return new IdNode(id);
		}
		else {
		return DefineTable.get(id);
		}
	}
	
	public ListNode ListNode_operand(Node operand) {
		if(runExpr(operand) instanceof ListNode)
			return (ListNode)operand;
		return null;
	}
	
	private void errorLog(String err) {
		System.out.println(err);
	}
	
	public Node runExpr(Node rootExpr) { // Node 리턴.
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr); // ListNode인 경우에만 다르게 리턴한다. runList() 실행.
		else if (rootExpr instanceof QuoteNode) // runExpr에 들어온 rootExpr가 QuoteNode인 경우.
			return ((QuoteNode)rootExpr).nodeInside(); // nodeInside를 return한다.
		else
			errorLog("run Expr error");
		return null;
	}
	
	private Node runList(ListNode list) { // ListNode인 경우.
		if(list.equals(ListNode.EMPTYLIST)) // ListNode가 EMPTYLIST인 경우.
			return list;
		if(list.car() instanceof FunctionNode) { // car()는 head. head가 FunctionNode인 경우.
			return runFunction((FunctionNode)list.car(), list.cdr()); // car() 문자열 연산을 수행하기 위해 runFunction() 실행.
		}
		if(list.car() instanceof BinaryOpNode) { // head가 BinaryOpNode인 경우.
			return runBinary(list); // Binary 연산을 수행하기 위해 runBinary() 실행. parameter를 ListNode list전체로 받는다.
		}
		if(list.car() instanceof QuoteNode) { // runExpr에 들어온 rootExpr가 ListNode이고, 이 노드의 car()이 QuoteNode인 경우.
			return runQuote(list); // ListNode 안의 QuoteNode의 nodeInside 리턴. 
		}
		return list; // 이외의 노드는 그냥 리턴.
	}
	
	
	private Node runFunction(FunctionNode operator, ListNode operand) { // runList에서 받은 인자 ListNode의 car()(head)가 FunctionNode인 경우.
		// operator는 인자로 받은 car()의 car(), operand는 ListNode 타입의 cdr()이다.
		Node op_car = operand.car();
		Node op_cdr = operand.cdr().car();
		
		if(op_car instanceof IdNode) {
			op_car = lookupTable(((IdNode)op_car).getIdNode());
		}
		if(op_cdr instanceof IdNode) {
			op_cdr = lookupTable(((IdNode)op_cdr).getIdNode());
		}
		
		switch (operator.getFunctionType()) { // 여러 동작 구현.
			case CAR: // List의 맨 처음 원소 리턴.
					return ((ListNode)runExpr(op_car)).car();
			case CDR: // List의 맨 처음 원소를 제외한 나머지 list 리턴.
					return new QuoteNode(((ListNode)runExpr(op_car)).cdr()); // PDF에 cdr의 출력값에는 ' 가 붙어있다.
			case CONS: // 한 개의 원소와 한 개의 리스트를 붙여서 새로운 리스트 만듬. (head + tail)
				return new QuoteNode(ListNode.cons(runExpr(op_car), (ListNode)runExpr(operand.cdr())));	
			case NOT: // BooleanNode에 !(not) 걸어버리기!
				if(((BooleanNode)runExpr(op_car)).getBoolean()) {
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case COND: // 조건문. 하나만 T면 그 값을 리턴. 대신 둘 다 True면 앞의 값인 car() 리턴.
				if(runExpr(((ListNode)(op_car)).car()) instanceof BooleanNode) { // 만약 car()의 앞이 Boolean이면.
					if(((BooleanNode)runExpr(((ListNode)(op_car)).car())).getBoolean()) { // car().car()이 #T이면.
						if(((ListNode)runExpr(op_car)).cdr().car() instanceof QuoteNode) { // ( cond ( #T ' ( + 2 3 ) ) ) 에 대한 예외처리.
							return runExpr((ListNode)runExpr(((ListNode)(op_car)).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)(op_car)).cdr())).car()); // '가 없는 경우.
					}
				}
				if(runExpr(((ListNode)runExpr(op_cdr)).car()) instanceof BooleanNode) { // 만약 cdr()의 앞이 Boolean이면.
					if(((BooleanNode)runExpr(((ListNode)runExpr(op_cdr)).car())).getBoolean()) { // car()이 #T이면.
						if((((ListNode)runExpr(op_cdr)).cdr()).car() instanceof QuoteNode) { // ' 에 대한 예외처리.
							return runExpr(runExpr(((ListNode)runExpr(op_cdr)).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)runExpr(op_cdr)).cdr())).car()); // '가 아닌 listnode 예외 처리.
					}
				}
				
				if(((BooleanNode)runExpr(((ListNode)runExpr(op_car)).car())).getBoolean()) { // 연산을 통해 car()이 True가 나오면.
					return runExpr(((ListNode)(op_car)).cdr());
				}
				if(((BooleanNode)runExpr(op_cdr)).getBoolean()) { // 연산을 통해 cdr()이 True가 나오면.
					return runExpr((operand.cdr()).cdr());
				}
				
				return BooleanNode.FALSE_NODE; // 둘 다 False가 나오면.
			case ATOM_Q: // ATOM(list이면 false, list가 아니면 true, null이면 true(이건 선택 - 완료.) )
				if(runExpr(op_car) instanceof ListNode) {
					if(runExpr(((ListNode)runExpr(op_car)).car()) == null // 해당 list의 car()이 null이고 
							&& runExpr(((ListNode)runExpr(op_car)).cdr()) == null) { // 해당 list의 cdr()도 null이면
								return BooleanNode.TRUE_NODE; // null list이면 true.
							}
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case NULL_Q: // list가 null인지 검사.
				if(runExpr(((ListNode)runExpr(op_car)).car()) == null // 해당 list의 car()이 null이고 
				&& runExpr(((ListNode)runExpr(op_car)).cdr()) == null) { // 해당 list의 cdr()도 null이면
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE; 	
			case EQ_Q: // 두 값이 같은 지 검사.
				if(runExpr(op_car) instanceof ListNode){ //만약 operand의 quoted가 listNode면
					ListNode eq1 =(ListNode)(runExpr(op_car)); // eq1의 초기값을 operand의 quoted값으로
					if(runExpr(op_cdr) instanceof ListNode) {
						ListNode eq2 =(ListNode)(runExpr(op_cdr));
					}
					
					while(eq1.cdr()!=null || eq2.cdr()!=null){ // eq1나 eq2의 cdr이 null일 때까지 반복
					if(((IdNode)eq1.car()).getIdNode().equals(((IdNode)eq2.car()).getIdNode())){ // 만약 eq1의 car의 string값이 eq2의 car의 string값과 같다면
						eq1 = eq1.cdr(); // eq1을 eq1의 cdr로
						eq2 = eq2.cdr(); // eq2를 eq2의 cdr로
					}
					else{ // 그렇지 않으면 같지 않으므로 falsenode리턴
						return BooleanNode.FALSE_NODE;
					}	
					}
					return BooleanNode.TRUE_NODE; // while문을 빠져나오면 false가 아니므로 truenode리턴
				}else{ // 만약 operand의 quoted가 listNode가 아니면
				if(((IdNode)(runQuote(operand))).getIdNode().equals(((IdNode)(runQuote(operand.cdr()))).getIdNode())){ //operand의 quoted의 string값과 operand의 cdr의 quoted값의 string값을 비교
					return BooleanNode.TRUE_NODE; // 같으므로 truenode리턴
				}else{ // 다르면
					return BooleanNode.FALSE_NODE; // 같지않으므로 falsenode리턴
				}	
			}
			case DEFINE:
				if(insertTable(operand.car(), operand.cdr())) {
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE;
			default:
				errorLog("runFunction error");
				break;
		}
		return null;
	}
	
	private Node runBinary(ListNode list) { // runList에서 받은 인자 ListNode의 car()(head)가 BinaryOpNode인 경우.
		BinaryOpNode operator = (BinaryOpNode)list.car(); // 위의 runList에 list.car()이 BinaryOpNode임을 검사했으므로.
		ListNode operand = list.cdr();
		Node op_car = operand.car();
		Node op_cdr = operand.cdr().car();
		
		if(op_car instanceof IdNode) {
			op_car = lookupTable(((IdNode)op_car).getIdNode());
		}
		if(op_cdr instanceof IdNode) {
			op_cdr = lookupTable(((IdNode)op_cdr).getIdNode());
		}
		Integer a = ((IntNode)runExpr(op_car)).getIntValue();
		Integer b = ((IntNode)runExpr(op_cdr)).getIntValue();
		
		switch (operator.getBinType()) { // 바이너리 연산 동작 구현
			case PLUS: // 덧셈. +
				return new IntNode(String.valueOf(a + b)); // operand 더하기.
			case MINUS: // 뺄셈. -
				return new IntNode(String.valueOf(a - b)); // operand 빼기.
			case DIV: // 나눗셈. /
				return new IntNode(String.valueOf(a / b)); // operand 나누기.
			case TIMES: // 곱셈. *
				return new IntNode(String.valueOf(a * b)); // operand의 곱하기.
			case LT: // <
				if(a < b) 
					return BooleanNode.TRUE_NODE; // public static 변수 이름이 TRUE_NODE인데 이는 true인 BooleanNode 객체를 생성한다.
				return BooleanNode.FALSE_NODE; // < 가 아니면.
			case GT: // >
				if(a > b) 
					return BooleanNode.TRUE_NODE; // 두 operand 중 car()가 더 크면 TRUE
				return BooleanNode.FALSE_NODE; // > 가 아니면.				
			case EQ: // = ( 두 값을 int로 변환 )
				if(a.equals(b)) 
					return BooleanNode.TRUE_NODE; // 두 operand가 같으면 TRUE
				return BooleanNode.FALSE_NODE; // = 가 아니면.				
			default:
				errorLog("runBinary error");
				break;
		}
		return null;
	}
	
	private Node runQuote(ListNode node) { // QuoteNode랑 ListNode랑 무슨 관계? QuoteNode = ListNode ?
		return ((QuoteNode)node.car()).nodeInside(); // 
	}
	
	public static void main(String[] args) 	{
		PrintStream ps = System.out;
		File tmp = new File("test");
		while(true) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(tmp));
				Scanner sc = new Scanner(System.in); // scanner 객체 생성.
				
				ps.print("> ");
				String str = sc.nextLine(); // 사용자가 원하는 문장 입력받아 파일에 기록할 준비.
				if(str.equals("quit")) {
					pw.close();
					sc.close();
					tmp.delete();
					break;
				}
				pw.print(str);
				pw.flush(); // 파일에 기록. 여기까지가 입력 첫 줄.
				
				CuteParser cuteParser = new CuteParser(tmp); // 계산 출력을 위한 준비.
				Node parseTree = cuteParser.parseExpr(); // 문자열 노드로.
				CuteInterpreter i = new CuteInterpreter();
				Node resultNode = i.runExpr(parseTree);
				ps.print("... ");
				NodePrinter.getPrinter(System.out).prettyPrint(resultNode);

				ps.println();				
				pw.close(); // PrintWriter 닫기. 메모리 절약.
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
