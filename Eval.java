import java.util.Stack;


public class Eval {

	public String value = null;
	public ConsCell token_CC;

	public Eval(ConsCell tokens) {
		token_CC = tokens;
	}

	public String returnResult( ConsCell tokens ){

		String temp = null;

		if( this.token_CC.car != null ) temp = returnResult( this.token_CC.car );

		if( this.token_CC.value != null ){
			if( this.token_CC.type == 2 ){
				if( this.token_CC.value.matches( "[+-*/]" ) operate( this.token_CC.value , this.token_CC.cdr );
			}

		}

		return this.value;
	}

	private void operate(String operater, ConsCell cdr) {

		ConsCell temp = cdr;
		Stack<Double> values = new Stack<Double>();

		while( temp.cdr != null ){
			values.push( Double.valueOf( cdr.value ) );
			temp = temp.cdr;
		}

		if( values.empty() ){
			System.out.println("記法が間違っているか、予期しないエラー。");
			System.exit(1);
		}

		double num ;

		if( operater.equals("+") ) {
			num = 0;
			for( int i = 0; i < values.size(); i++ ){
				num += values.pop();
			}
		} else if( operater.equals("*") ){
			num = 1;
			for( int i = 0; i < values.size(); i++ ){
				num *= values.pop();
			}
		} else if( operater.equals("-") ){

		} else if( operater.equals("/") ){

		} else {
			try{
				num = (Double.valueOf( token ));
			}catch(NumberFormatException e){
				System.out.println("記法が間違っているか、予期しないエラー。");
				System.exit(1);
			}
		}
	}

}
