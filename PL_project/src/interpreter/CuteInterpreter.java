// 201402407 ���ؿ�
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
		
		String Id = ((IdNode)runExpr(id)).getIdNode(); // ���� id�� ������ IdNode�̰ų�, QuoteNode�� NodeInside�� IdNode��� ����.
		Node Value = runExpr(value.car());
		
		for(String key : DefineTable.keySet()) {
			if(id.equals(key)) 
				DefineTable.remove(key); // ���� ���ڰ� �����Ѵٸ�, ����.
		}
		DefineTable.put(Id, Value); // ����ֱ�.
		return true;
	}
	
	public Node lookupTable(String id) { // HashMap���� ������ ��������.
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
		Node op_car = operand.car();
		Node op_cdr = operand.cdr().car();
		
		if(op_car instanceof IdNode) {
			op_car = lookupTable(((IdNode)op_car).getIdNode());
		}
		if(op_cdr instanceof IdNode) {
			op_cdr = lookupTable(((IdNode)op_cdr).getIdNode());
		}
		
		switch (operator.getFunctionType()) { // ���� ���� ����.
			case CAR: // List�� �� ó�� ���� ����.
					return ((ListNode)runExpr(op_car)).car();
			case CDR: // List�� �� ó�� ���Ҹ� ������ ������ list ����.
					return new QuoteNode(((ListNode)runExpr(op_car)).cdr()); // PDF�� cdr�� ��°����� ' �� �پ��ִ�.
			case CONS: // �� ���� ���ҿ� �� ���� ����Ʈ�� �ٿ��� ���ο� ����Ʈ ����. (head + tail)
				return new QuoteNode(ListNode.cons(runExpr(op_car), (ListNode)runExpr(operand.cdr())));	
			case NOT: // BooleanNode�� !(not) �ɾ������!
				if(((BooleanNode)runExpr(op_car)).getBoolean()) {
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case COND: // ���ǹ�. �ϳ��� T�� �� ���� ����. ��� �� �� True�� ���� ���� car() ����.
				if(runExpr(((ListNode)(op_car)).car()) instanceof BooleanNode) { // ���� car()�� ���� Boolean�̸�.
					if(((BooleanNode)runExpr(((ListNode)(op_car)).car())).getBoolean()) { // car().car()�� #T�̸�.
						if(((ListNode)runExpr(op_car)).cdr().car() instanceof QuoteNode) { // ( cond ( #T ' ( + 2 3 ) ) ) �� ���� ����ó��.
							return runExpr((ListNode)runExpr(((ListNode)(op_car)).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)(op_car)).cdr())).car()); // '�� ���� ���.
					}
				}
				if(runExpr(((ListNode)runExpr(op_cdr)).car()) instanceof BooleanNode) { // ���� cdr()�� ���� Boolean�̸�.
					if(((BooleanNode)runExpr(((ListNode)runExpr(op_cdr)).car())).getBoolean()) { // car()�� #T�̸�.
						if((((ListNode)runExpr(op_cdr)).cdr()).car() instanceof QuoteNode) { // ' �� ���� ����ó��.
							return runExpr(runExpr(((ListNode)runExpr(op_cdr)).cdr()));
						}
						return runExpr(((ListNode)runExpr(((ListNode)runExpr(op_cdr)).cdr())).car()); // '�� �ƴ� listnode ���� ó��.
					}
				}
				
				if(((BooleanNode)runExpr(((ListNode)runExpr(op_car)).car())).getBoolean()) { // ������ ���� car()�� True�� ������.
					return runExpr(((ListNode)(op_car)).cdr());
				}
				if(((BooleanNode)runExpr(op_cdr)).getBoolean()) { // ������ ���� cdr()�� True�� ������.
					return runExpr((operand.cdr()).cdr());
				}
				
				return BooleanNode.FALSE_NODE; // �� �� False�� ������.
			case ATOM_Q: // ATOM(list�̸� false, list�� �ƴϸ� true, null�̸� true(�̰� ���� - �Ϸ�.) )
				if(runExpr(op_car) instanceof ListNode) {
					if(runExpr(((ListNode)runExpr(op_car)).car()) == null // �ش� list�� car()�� null�̰� 
							&& runExpr(((ListNode)runExpr(op_car)).cdr()) == null) { // �ش� list�� cdr()�� null�̸�
								return BooleanNode.TRUE_NODE; // null list�̸� true.
							}
					return BooleanNode.FALSE_NODE;
				}
				return BooleanNode.TRUE_NODE;
			case NULL_Q: // list�� null���� �˻�.
				if(runExpr(((ListNode)runExpr(op_car)).car()) == null // �ش� list�� car()�� null�̰� 
				&& runExpr(((ListNode)runExpr(op_car)).cdr()) == null) { // �ش� list�� cdr()�� null�̸�
					return BooleanNode.TRUE_NODE;
				}
				return BooleanNode.FALSE_NODE; 	
			case EQ_Q: // �� ���� ���� �� �˻�.
				if(runExpr(op_car) instanceof ListNode){ //���� operand�� quoted�� listNode��
					ListNode eq1 =(ListNode)(runExpr(op_car)); // eq1�� �ʱⰪ�� operand�� quoted������
					if(runExpr(op_cdr) instanceof ListNode) {
						ListNode eq2 =(ListNode)(runExpr(op_cdr));
					}
					
					while(eq1.cdr()!=null || eq2.cdr()!=null){ // eq1�� eq2�� cdr�� null�� ������ �ݺ�
					if(((IdNode)eq1.car()).getIdNode().equals(((IdNode)eq2.car()).getIdNode())){ // ���� eq1�� car�� string���� eq2�� car�� string���� ���ٸ�
						eq1 = eq1.cdr(); // eq1�� eq1�� cdr��
						eq2 = eq2.cdr(); // eq2�� eq2�� cdr��
					}
					else{ // �׷��� ������ ���� �����Ƿ� falsenode����
						return BooleanNode.FALSE_NODE;
					}	
					}
					return BooleanNode.TRUE_NODE; // while���� ���������� false�� �ƴϹǷ� truenode����
				}else{ // ���� operand�� quoted�� listNode�� �ƴϸ�
				if(((IdNode)(runQuote(operand))).getIdNode().equals(((IdNode)(runQuote(operand.cdr()))).getIdNode())){ //operand�� quoted�� string���� operand�� cdr�� quoted���� string���� ��
					return BooleanNode.TRUE_NODE; // �����Ƿ� truenode����
				}else{ // �ٸ���
					return BooleanNode.FALSE_NODE; // ���������Ƿ� falsenode����
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
	
	private Node runBinary(ListNode list) { // runList���� ���� ���� ListNode�� car()(head)�� BinaryOpNode�� ���.
		BinaryOpNode operator = (BinaryOpNode)list.car(); // ���� runList�� list.car()�� BinaryOpNode���� �˻������Ƿ�.
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
		
		switch (operator.getBinType()) { // ���̳ʸ� ���� ���� ����
			case PLUS: // ����. +
				return new IntNode(String.valueOf(a + b)); // operand ���ϱ�.
			case MINUS: // ����. -
				return new IntNode(String.valueOf(a - b)); // operand ����.
			case DIV: // ������. /
				return new IntNode(String.valueOf(a / b)); // operand ������.
			case TIMES: // ����. *
				return new IntNode(String.valueOf(a * b)); // operand�� ���ϱ�.
			case LT: // <
				if(a < b) 
					return BooleanNode.TRUE_NODE; // public static ���� �̸��� TRUE_NODE�ε� �̴� true�� BooleanNode ��ü�� �����Ѵ�.
				return BooleanNode.FALSE_NODE; // < �� �ƴϸ�.
			case GT: // >
				if(a > b) 
					return BooleanNode.TRUE_NODE; // �� operand �� car()�� �� ũ�� TRUE
				return BooleanNode.FALSE_NODE; // > �� �ƴϸ�.				
			case EQ: // = ( �� ���� int�� ��ȯ )
				if(a.equals(b)) 
					return BooleanNode.TRUE_NODE; // �� operand�� ������ TRUE
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
