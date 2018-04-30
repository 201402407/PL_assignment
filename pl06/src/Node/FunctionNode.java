package Node;
// 201402407 이해원

import java.util.HashMap;
import java.util.Map;

import parser.TokenType;

public class FunctionNode implements ValueNode { // KEYWORDS에 해당하는 Type들의 Node.
		
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
				for (FunctionType fType : FunctionType.values()) { // 모든 FunctionType의 enum 정의값을 위 HashMap에 대응되는 TokenType 값을 함께 넣기.
					fromTokenType.put(fType.tokenType(), fType);
				}
			}
			static FunctionType getFunctionType(TokenType tType) { // TokenType enum을 얻으면 그에 대응하는 FunctionType enum값 리턴.
				return fromTokenType.get(tType);
			}
			abstract TokenType tokenType(); // 마지막에 추상 함수를 적어서 위의 enum에서 함수처럼 사용하게 함.
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

