import java.util.Stack;


public class Evaluation {

	public String value = null;
	public ConsCell token_CC;

	public Evaluation(ConsCell tokens) {
		token_CC = tokens;
	}

	public String returnResult( ConsCell tokens ){

		ConsCell temp = tokens;
		ConsCell checkCell = tokens;
		ConsCell operateCell = null;

		do{

			// 終端へ移動
			do{
				if( temp.type == 2 ) operateCell = temp;
				if( temp.car != null ){
					checkCell = temp;
					temp = temp.car;
				} else if( temp.cdr != null ){
					temp = temp.cdr;
				}
			} while ( temp.car != null || temp.cdr != null );

			if( operateCell == null ) {
				System.out.println("演算子がありません");
				System.exit(1);
			}

			if( operateCell.value.matches("[+-/*]") ){
				// 四則演算
				checkCell.value = operate( checkCell.value , checkCell.cdr );
				checkCell.car = null;
			}else if( operateCell.value.matches("[<>=(<=)(>=)]")){
				// 比較演算

			}else if(false){
				// 変数定義
			}else if(false){
				// 関数定義
			}else if(false){
				// if文
			}

		}while( tokens.cdr != null || tokens.car != null );


		return tokens.value;
	}

	// 四則演算
	private String operate(String operater, ConsCell cdr) {

		ConsCell temp = cdr;

		temp = cdr;
		Stack<Double> values = new Stack<Double>();

		while( temp != null && temp.type == 1 ){
			values.push( Double.valueOf( temp.value ) );
			temp = temp.cdr;
		}

		if( values.empty() ){
			System.out.println("記法が間違っているか、予期しないエラー。(stack_empty)");
			System.exit(1);
		}

		double num = 0.0 ;

		if( operater.equals("+") ) {
			num = 0.0;
			for( int i = 0; i <= values.size(); i++ ){
				num += values.pop();
			}
		} else if( operater.equals("*") ){
			num = 1.0;
			for( int i = 0; i <= values.size(); i++ ){
				num *= values.pop();
			}
		} else if( operater.equals("-") ){
			if( values.size() != 2 ){
				System.out.println("減算は２項演算です");
				System.exit(1);
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m - n;
			}
		} else if( operater.equals("/") ){
			if( values.size() != 2 ){
				System.out.println("除算は２項演算です");
				System.exit(1);
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m / n;
			}
		} else {
				System.out.println("記法が間違っているか、予期しないエラー。");
				System.exit(1);
		}

	return String.valueOf(num);

	}

}
