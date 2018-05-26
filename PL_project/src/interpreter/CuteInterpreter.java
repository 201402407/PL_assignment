// 201402407 ���ؿ�
package interpreter;

import Node.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

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
		else if (rootExpr instanceof QuoteNode) // runExpr�� ���� rootExpr�� QuoteNode�� ���.
			return ((QuoteNode)rootExpr).nodeInside(); // nodeInside�� return�Ѵ�.
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
		if(list.car() instanceof QuoteNode) { // runExpr�� ���� rootExpr�� ListNode�̰�, �� ����� car()�� QuoteNode�� ���.
			return runQuote(list); // ListNode ���� QuoteNode�� nodeInside ����. 
		}
		return list; // �̿��� ���� �׳� ����.
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) { // runList���� ���� ���� ListNode�� car()(head)�� FunctionNode�� ���.
		// operator�� ���ڷ� ���� car()�� car(), operand�� ListNode Ÿ���� cdr()�̴�.
		switch (operator.getFunctionType()) { // ���� ���� ����.
			case CAR: // List�� �� ó�� ���� ����.
					return ((ListNode)runExpr(operand.car())).car();
			case CDR: // List�� �� ó�� ���Ҹ� ������ ������ list ����.
					return new QuoteNode(((ListNode)runExpr(operand.car())).cdr()); // PDF�� cdr�� ��°����� ' �� �پ��ִ�.
			case CONS: // �� ���� ���ҿ� �� ���� ����Ʈ�� �ٿ��� ���ο� ����Ʈ ����. (head + tail)
				return new QuoteNode(ListNode.cons(runExpr(operand.car()), (ListNode)runExpr(operand.cdr())));	
			case NOT: // BooleanNode�� !(not) �ɾ������!
				if(((BooleanNode)runExpr(operand.car())).getBoolean()) {
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case COND: // ���ǹ�. �ϳ��� T�� �� ���� ����. ��� �� �� True�� ���� ���� car() ����.
				if(runExpr(((ListNode)(operand.car())).car()) instanceof BooleanNode) { // ���� car()�� ���� Boolean�̸�.
					if(((BooleanNode)runExpr(((ListNode)(operand.car())).car())).getBoolean()) { // car().car()�� #T�̸�.
						if(((ListNode)runExpr(operand.car())).cdr().car() instanceof QuoteNode) { // ( cond ( #T ' ( + 2 3 ) ) ) �� ���� ����ó��.
							return runExpr((ListNode)runExpr(((ListNode)(operand.car())).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)(operand.car())).cdr())).car()); // '�� ���� ���.
					}
				}
				if(runExpr(((ListNode)runExpr(operand.cdr().car())).car()) instanceof BooleanNode) { // ���� cdr()�� ���� Boolean�̸�.
					if(((BooleanNode)runExpr(((ListNode)runExpr(operand.cdr().car())).car())).getBoolean()) { // car()�� #T�̸�.
						if((((ListNode)runExpr(operand.cdr().car())).cdr()).car() instanceof QuoteNode) { // ' �� ���� ����ó��.
							return runExpr(runExpr(((ListNode)runExpr(operand.cdr().car())).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)runExpr(operand.cdr().car())).cdr())).car()); // '�� �ƴ� listnode ���� ó��.
					}
				}
				
				if(((BooleanNode)runExpr(((ListNode)runExpr(operand.car())).car())).getBoolean()) { // ������ ���� car()�� True�� ������.
					return runExpr(((ListNode)(operand.car())).cdr());
				}
				if(((BooleanNode)runExpr(((ListNode)runExpr(operand.cdr())).car())).getBoolean()) { // ������ ���� cdr()�� True�� ������.
					return runExpr((operand.cdr()).cdr());
				}
				
				return BooleanNode.FALSE_NODE; // �� �� False�� ������.
			case ATOM_Q: // ATOM(list�̸� false, list�� �ƴϸ� true, null�̸� true(�̰� ���� - �Ϸ�.) )
				if(runExpr(operand.car()) instanceof ListNode) {
					if(runExpr(((ListNode)runExpr(operand.car())).car()) == null // �ش� list�� car()�� null�̰� 
							&& runExpr(((ListNode)runExpr(operand.car())).cdr()) == null) { // �ش� list�� cdr()�� null�̸�
								return BooleanNode.TRUE_NODE; // null list�̸� true.
							}
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case NULL_Q: // list�� null���� �˻�.
				if(runExpr(((ListNode)runExpr(operand.car())).car()) == null // �ش� list�� car()�� null�̰� 
				&& runExpr(((ListNode)runExpr(operand.car())).cdr()) == null) { // �ش� list�� cdr()�� null�̸�
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE; 	
			case EQ_Q: // �� ���� ���� �� �˻�.
				String temp = "";
				if(runExpr(operand.car()) instanceof ListNode && runExpr(operand.cdr().car()) instanceof ListNode) { // ListNode�� ���.
					String a = NodeToString(operand.car(), temp);
					temp = "";
					String b = NodeToString(operand.cdr().car(), temp);
					if(a.equals(b)) {
						return BooleanNode.TRUE_NODE;
					}
		
				}
				else { // ListNode�� �ƴ� ���.
					if(String.valueOf(runExpr(operand.car())).equals(String.valueOf(runExpr(operand.cdr())))) { // ���� String���� ��ȯ �� ��.
	 					return BooleanNode.TRUE_NODE;
	 				}
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
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						+ ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue())); // operand ���ϱ�.
			case MINUS: // ����. -
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						- ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue())); // operand�� ����.
			case DIV: // ������. /
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						/ ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue())); // operand�� ������.
			case TIMES: // ����. *
				return new IntNode(String.valueOf(((IntNode)runExpr(operand.car())).getIntValue()
						* ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue())); // operand�� ���ϱ�.
			case LT: // <
				if(((IntNode)runExpr(operand.car())).getIntValue() < ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue()) {
					return BooleanNode.TRUE_NODE; // public static ���� �̸��� TRUE_NODE�ε� �̴� true�� BooleanNode ��ü�� �����Ѵ�.
				}
				return BooleanNode.FALSE_NODE; // < �� �ƴϸ�.
			case GT: // >
				if(((IntNode)runExpr(operand.car())).getIntValue() > ((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue()) {
					return BooleanNode.TRUE_NODE; // �� operand �� car()�� �� ũ�� TRUE
				}
				return BooleanNode.FALSE_NODE; // > �� �ƴϸ�.				
			case EQ: // = ( �� ���� int�� ��ȯ )
				if(((IntNode)runExpr(operand.car())).getIntValue().equals(((IntNode)((ListNode)runExpr(operand.cdr())).car()).getIntValue())) {
					return BooleanNode.TRUE_NODE; // �� operand�� ������ TRUE
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
	
	private String NodeToString(Node car, String a) {
		if(car instanceof ListNode) { // listNode.car() ���� ����.
			if(car.equals(((ListNode)car).EMPTYLIST)) 
				return " ( ) ";
			if(car.equals(((ListNode)car).ENDLIST)) 
				return " ) ";
			a += " ( ";
			if(((ListNode)car).car() instanceof ListNode) { // listNode.car() ���� ����.
				a += NodeToString(((ListNode)car).car(), a); // ListNode�� ��ȯ �� �޼ҵ� ����.
			}
			else {
				a += NodeToString(((ListNode)car).car(), a);
			}
			
			if(((ListNode)car).cdr() instanceof ListNode) { // listNode.cdr() ���� ����.
				a += NodeToString(((ListNode)car).cdr(), a);
			}
			else {
				a += NodeToString(((ListNode)car).cdr().car(), a);
			}
			a += " ) ";
			return a;
		}
		if(car instanceof IntNode) 
			return String.valueOf(((IntNode)car).getIntValue());
		if(car instanceof BooleanNode) 
			return String.valueOf(((BooleanNode)car).getBoolean());
		if(car instanceof IdNode) 
			return ((IdNode)car).getIdNode();
		if(car instanceof QuoteNode) 
			return " ' " + NodeToString(((QuoteNode)car).nodeInside(), a);
		a += " ";
		return a;
	}
	
	public static void main(String[] args) 	{
		PrintStream ps = System.out;
		File tmp = new File("test");
		
		while(true) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(tmp));
				Scanner sc = new Scanner(System.in); // scanner ��ü ����.

				
				ps.print("> ");
				String str = sc.nextLine(); // ����ڰ� ���ϴ� ���� �Է¹޾� ���Ͽ� ����� �غ�.
				if(str.equals("quit")) {
					pw.close();
					sc.close();
					tmp.delete();
					break;
				}
				pw.print(str);
				pw.flush(); // ���Ͽ� ���. ��������� �Է� ù ��.
				
				CuteParser cuteParser = new CuteParser(tmp); // ��� ����� ���� �غ�.
				Node parseTree = cuteParser.parseExpr(); // ���ڿ� ����.
				CuteInterpreter i = new CuteInterpreter();
				Node resultNode = i.runExpr(parseTree);
				ps.print("... ");
				NodePrinter.getPrinter(System.out).prettyPrint(resultNode);

				ps.println();
								
				pw.close(); // PrintWriter �ݱ�. �޸� ����.
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
