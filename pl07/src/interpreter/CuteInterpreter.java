// 201402407 이해원
package interpreter;

import parser.Token;
import parser.TokenType;
import Node.*;
import parser.ScannerMain;
import java.io.File;

public class CuteInterpreter {
	
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
	//	else if (rootExpr instanceof QuoteNode) // QuoteNode인 경우에 추가 ?
		//	return runExpr(runQuote((ListNode)rootExpr)); // nodeInside를 runExpr한다.
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
	//	if(list.car() instanceof QuoteNode) { // QuoteNode인 경우. ?
	//		return runQuote(list);
	//	}
		return list; // 이외의 노드는 그냥 리턴.
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) { // runList에서 받은 인자 ListNode의 car()(head)가 FunctionNode인 경우.
		// operator는 인자로 받은 car()의 car(), operand는 ListNode 타입의 cdr()이다.
		switch (operator.getFunctionType()) { // 여러 동작 구현.
			case CAR: // List의 맨 처음 원소 리턴.
				if(operand.car() instanceof QuoteNode) { // 앞에 ' 가 있는 경우.
					return ((ListNode)runQuote(operand)).car();
				}
				return ((ListNode)operand.car()).car(); // 앞에 ' 가 없는 경우.
			case CDR: // List의 맨 처음 원소를 제외한 나머지 list 리턴.
				if(operand.car() instanceof QuoteNode) { // 앞에 ' 가 있는 경우.
					return new QuoteNode(((ListNode)runQuote(operand)).cdr()); // PDF에 cdr의 출력값에는 ' 가 붙어있다.
				}
				return new QuoteNode(((ListNode)operand.car()).cdr()); // 앞에 ' 가 없는 경우.
			case CONS: // 한 개의 원소와 한 개의 리스트를 붙여서 새로운 리스트 만듬. (head + tail)
				return new QuoteNode(ListNode.cons(runExpr(operand.car()), (ListNode)runExpr(operand.cdr())));
			case NOT: // BooleanNode에 !(not) 걸어버리기!
				if(((BooleanNode)runExpr(operand.cdr())).getBoolean()) {
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case COND: // 조건문.
				
			case ATOM_Q: // ATOM(list이면 false, list가 아니면 true, null이면 true(이건 선택) )
				if(runExpr(operand.car()) instanceof ListNode) {
					if(runExpr(operand.car()).equals(null)) {
						return BooleanNode.TRUE_NODE;
					}
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case NULL_Q: // list가 null인지 검사.
				if(((ListNode)runExpr(operand.car())).equals(ListNode.EMPTYLIST)) {
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE;
			case EQ_Q: // 두 값이 같은 지 검사.
				if(runExpr(operand.car()).equals(runExpr(operand.cdr()))) {
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
		switch (operator.getBinType()) { // 바이너리 연산 동작 구현
			case PLUS: // 덧셈. +
			//	NodePrinter.getPrinter(System.out).prettyPrint(operand);
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						+ ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()의 car()과 cdr() 더하기.
			case MINUS: // 뺄셈. -
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						- ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()의 car()과 cdr() 빼기.
			case DIV: // 나눗셈. /
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						/ ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()의 car()과 cdr() 나누기.
			case TIMES: // 곱셈. *
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						* ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()의 car()과 cdr() 곱하기.
			case LT: // <
				if(((IntNode)runExpr(operand.car())).getIntValue() < ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static 변수 이름이 TRUE_NODE인데 이는 true인 BooleanNode 객체를 생성한다.
				}
				return BooleanNode.FALSE_NODE; // < 가 아니면.
			case GT: // >
				if(((IntNode)runExpr(operand.car())).getIntValue() > ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static 변수 이름이 TRUE_NODE인데 이는 true인 BooleanNode 객체를 생성한다.
				}
				return BooleanNode.FALSE_NODE; // > 가 아니면.				
			case EQ: // =
				if(((IntNode)runExpr(operand.car())).getIntValue() == ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static 변수 이름이 TRUE_NODE인데 이는 true인 BooleanNode 객체를 생성한다.
				}
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
		ClassLoader cloader = CuteInterpreter.class.getClassLoader();
		File file = new File(cloader.getResource("interpreter/as07.txt").getFile());
		CuteParser cuteParser = new CuteParser(file);
		Node parseTree = cuteParser.parseExpr(); // 문자열 노드로.
		CuteInterpreter i = new CuteInterpreter();
		Node resultNode = i.runExpr(parseTree);
		NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
		System.out.println();
		System.out.println("201402407 이해원");
	}
}
