// 201402407 ���ؿ�
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
	
	public Node runExpr(Node rootExpr) { // Node ����.
		if (rootExpr == null)
			return null;
		
		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr); // ListNode�� ��쿡�� �ٸ��� �����Ѵ�. runList() ����.
	//	else if (rootExpr instanceof QuoteNode) // QuoteNode�� ��쿡 �߰� ?
		//	return runExpr(runQuote((ListNode)rootExpr)); // nodeInside�� runExpr�Ѵ�.
		else
			errorLog("run Expr error");
		return null;
	}
	
	private Node runList(ListNode list) { // ListNode�� ���.
		if(list.equals(ListNode.EMPTYLIST)) // ListNode�� EMPTYLIST�� ���.
			return list;
		if(list.car() instanceof FunctionNode) { // car()�� head. head�� FunctionNode�� ���.
			return runFunction((FunctionNode)list.car(), list.cdr()); // car() ���ڿ� ������ �����ϱ� ���� runFunction() ����.
		}
		if(list.car() instanceof BinaryOpNode) { // head�� BinaryOpNode�� ���.
			return runBinary(list); // Binary ������ �����ϱ� ���� runBinary() ����. parameter�� ListNode list��ü�� �޴´�.
		}
	//	if(list.car() instanceof QuoteNode) { // QuoteNode�� ���. ?
	//		return runQuote(list);
	//	}
		return list; // �̿��� ���� �׳� ����.
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) { // runList���� ���� ���� ListNode�� car()(head)�� FunctionNode�� ���.
		// operator�� ���ڷ� ���� car()�� car(), operand�� ListNode Ÿ���� cdr()�̴�.
		switch (operator.getFunctionType()) { // ���� ���� ����.
			case CAR: // List�� �� ó�� ���� ����.
				if(operand.car() instanceof QuoteNode) { // �տ� ' �� �ִ� ���.
					return ((ListNode)runQuote(operand)).car();
				}
				return ((ListNode)operand.car()).car(); // �տ� ' �� ���� ���.
			case CDR: // List�� �� ó�� ���Ҹ� ������ ������ list ����.
				if(operand.car() instanceof QuoteNode) { // �տ� ' �� �ִ� ���.
					return new QuoteNode(((ListNode)runQuote(operand)).cdr()); // PDF�� cdr�� ��°����� ' �� �پ��ִ�.
				}
				return new QuoteNode(((ListNode)operand.car()).cdr()); // �տ� ' �� ���� ���.
			case CONS: // �� ���� ���ҿ� �� ���� ����Ʈ�� �ٿ��� ���ο� ����Ʈ ����. (head + tail)
				return new QuoteNode(ListNode.cons(runExpr(operand.car()), (ListNode)runExpr(operand.cdr())));
			case NOT: // BooleanNode�� !(not) �ɾ������!
				if(((BooleanNode)runExpr(operand.cdr())).getBoolean()) {
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case COND: // ���ǹ�.
				
			case ATOM_Q: // ATOM(list�̸� false, list�� �ƴϸ� true, null�̸� true(�̰� ����) )
				if(runExpr(operand.car()) instanceof ListNode) {
					if(runExpr(operand.car()).equals(null)) {
						return BooleanNode.TRUE_NODE;
					}
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case NULL_Q: // list�� null���� �˻�.
				if(((ListNode)runExpr(operand.car())).equals(ListNode.EMPTYLIST)) {
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE;
			case EQ_Q: // �� ���� ���� �� �˻�.
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
	
	private Node runBinary(ListNode list) { // runList���� ���� ���� ListNode�� car()(head)�� BinaryOpNode�� ���.
		BinaryOpNode operator = (BinaryOpNode)list.car(); // ���� runList�� list.car()�� BinaryOpNode���� �˻������Ƿ�.
		ListNode operand = list.cdr();
		switch (operator.getBinType()) { // ���̳ʸ� ���� ���� ����
			case PLUS: // ����. +
			//	NodePrinter.getPrinter(System.out).prettyPrint(operand);
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						+ ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()�� car()�� cdr() ���ϱ�.
			case MINUS: // ����. -
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						- ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()�� car()�� cdr() ����.
			case DIV: // ������. /
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						/ ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()�� car()�� cdr() ������.
			case TIMES: // ����. *
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						* ((IntNode)runExpr(operand.cdr())).getIntValue())); // list.cdr()�� car()�� cdr() ���ϱ�.
			case LT: // <
				if(((IntNode)runExpr(operand.car())).getIntValue() < ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static ���� �̸��� TRUE_NODE�ε� �̴� true�� BooleanNode ��ü�� �����Ѵ�.
				}
				return BooleanNode.FALSE_NODE; // < �� �ƴϸ�.
			case GT: // >
				if(((IntNode)runExpr(operand.car())).getIntValue() > ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static ���� �̸��� TRUE_NODE�ε� �̴� true�� BooleanNode ��ü�� �����Ѵ�.
				}
				return BooleanNode.FALSE_NODE; // > �� �ƴϸ�.				
			case EQ: // =
				if(((IntNode)runExpr(operand.car())).getIntValue() == ((IntNode)runExpr(operand.cdr())).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static ���� �̸��� TRUE_NODE�ε� �̴� true�� BooleanNode ��ü�� �����Ѵ�.
				}
				return BooleanNode.FALSE_NODE; // = �� �ƴϸ�.				
			default:
				errorLog("runBinary error");
				break;
		}
		return null;
	}
	
	private Node runQuote(ListNode node) { // QuoteNode�� ListNode�� ���� ����? QuoteNode = ListNode ?
		return ((QuoteNode)node.car()).nodeInside(); // 
	}
	
	public static void main(String[] args) 	{ 
		ClassLoader cloader = CuteInterpreter.class.getClassLoader();
		File file = new File(cloader.getResource("interpreter/as07.txt").getFile());
		CuteParser cuteParser = new CuteParser(file);
		Node parseTree = cuteParser.parseExpr(); // ���ڿ� ����.
		CuteInterpreter i = new CuteInterpreter();
		Node resultNode = i.runExpr(parseTree);
		NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
		System.out.println();
		System.out.println("201402407 ���ؿ�");
	}
}
