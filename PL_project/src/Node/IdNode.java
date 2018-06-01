package Node;
// 201402407 ÀÌÇØ¿ø 

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Node.FunctionNode.FunctionType;
import parser.TokenType;

public class IdNode implements ValueNode {
	
	String idString;
	
	public IdNode(String text) {
		idString = text;
	}
	
	@Override
	public String toString() {
		return "ID: " + idString;
	}
	
	public String getIdNode() {
		return idString;
	}
}
