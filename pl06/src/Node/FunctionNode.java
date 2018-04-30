package Node;
// 201402407 ���ؿ�

import java.util.HashMap;
import java.util.Map;

import parser.TokenType;

public class FunctionNode implements ValueNode { // KEYWORDS�� �ش��ϴ� Type���� Node.
		
		enum FunctionType {
			DEFINE	{ TokenType tokenType()	{return TokenType.DEFINE;} },
			LAMBDA	{ TokenType tokenType()	{return TokenType.LAMBDA;} },
			COND	{ TokenType tokenType()	{return TokenType.COND;} },
			NOT		{ TokenType tokenType()	{return TokenType.NOT;} },
			CDR		{ TokenType tokenType()	{return TokenType.CDR;} },
			CAR		{ TokenType tokenType()	{return TokenType.CAR;} },
			CONS	{ TokenType tokenType()	{return TokenType.CONS;} },
			EQ_Q	{ TokenType tokenType()	{return TokenType.EQ_Q;} },
			NULL_Q	{ TokenType tokenType()	{return TokenType.NULL_Q;} },
			ATOM_Q	{ TokenType tokenType()	{return TokenType.ATOM_Q;} };
			
			private static Map<TokenType, FunctionType> fromTokenType = 
					new HashMap<TokenType, FunctionType>();
			static {
				for (FunctionType fType : FunctionType.values()) { // ��� FunctionType�� enum ���ǰ��� �� HashMap�� �����Ǵ� TokenType ���� �Բ� �ֱ�.
					fromTokenType.put(fType.tokenType(), fType);
				}
			}
			static FunctionType getFunctionType(TokenType tType) { // TokenType enum�� ������ �׿� �����ϴ� FunctionType enum�� ����.
				return fromTokenType.get(tType);
			}
			abstract TokenType tokenType(); // �������� �߻� �Լ��� ��� ���� enum���� �Լ�ó�� ����ϰ� ��.
		}
	public FunctionType value;
	
	@Override
	public String toString() {
		return value.name();
	}
	
	public FunctionNode(TokenType tType) {
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;
	}
}

