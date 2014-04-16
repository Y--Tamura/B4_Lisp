import java.util.ArrayList;
import java.util.Stack;


public class Evaluation {

	public String value = null;
	public ConsCell token_CC;
	public ArrayList<String> functions, valiables, valiablevalues;
	public ArrayList<ConsCell> functionvalues;

	public Evaluation(ConsCell tokens) {
		this( tokens, null , null , null , null );
	}

	public Evaluation(ConsCell tokens, ArrayList<String> functions , ArrayList<String> valiables , ArrayList<ConsCell> functionvalues , ArrayList<String> valiablevalues ) {
		token_CC = tokens;
		this.functions = functions;
		this.valiables = valiables;
		this.functionvalues = functionvalues;
		this.valiablevalues = valiablevalues;
	}

	public String returnResult( ConsCell tokens ){

		ConsCell temp ;
		ConsCell checkCell = tokens;
		ConsCell operateCell;


		do{

			temp = tokens;
			operateCell = null;

			// 終端へ移動
			do{
				if( temp.type == 2 ) operateCell = temp;
				else if ( temp.type == 5 ){
					operateCell = temp;
					break;
				}

				if( temp.car != null ){
					checkCell = temp;
					temp = temp.car;
				} else if( temp.cdr != null ){
					temp = temp.cdr;
				}

			} while ( temp.car != null || temp.cdr != null );

			if( operateCell == null ) {
				System.out.println("演算子がありません");
				return "error";
			}

			if( operateCell.value.matches("[+-/*]") ){
				// 四則演算
				checkCell.value = operate( operateCell.value , operateCell.cdr );
				checkCell.car = null;
				checkCell.type = 1;
			}else if( operateCell.value.matches("<|<=|>|>=|=")){
				// 比較演算
				checkCell.value = compare( operateCell.value , operateCell.cdr );
				checkCell.car = null;
				checkCell.type = 4;
			}else if( operateCell.value.matches("setq") ){
				// 変数定義
				if( operateCell.cdr.cdr != null ){
					checkCell.value = operateCell.cdr.cdr.value;
					this.valiables.add( operateCell.cdr.value );
					this.valiablevalues.add( operateCell.cdr.cdr.value );
				} else {
					checkCell.value = null;
					this.valiables.add( operateCell.cdr.value );
					this.valiablevalues.add( null );
				}
				checkCell.car = null;
				checkCell.type = 1;
			}else if( operateCell.value.matches("defun") ){
				// 関数定義
				this.functions.add( operateCell.cdr.value );
				this.functionvalues.add( operateCell.cdr.cdr );

				// 引数処理
				if( operateCell.cdr.cdr.type != 3 ){
					this.valiables.add( operateCell.cdr.cdr.value );
					this.valiablevalues.add( null );
				} else {
					ConsCell valiableCell = operateCell.cdr.cdr.car;
					while( valiableCell != null ){
						this.valiables.add( valiableCell.value );
						this.valiablevalues.add( null );
						valiableCell = valiableCell.cdr;
					}
				}

				checkCell.car = null;
				checkCell.type = 1;
			}else if( operateCell.value.matches("if") ){
				// if文
				if( operateCell.cdr.value == "t" ){
					operateCell.cdr.cdr.cdr = null;
					checkCell.value = operateCell.cdr.cdr.value;
					checkCell.car = null;
					checkCell.type = 0;
				} else if( operateCell.cdr.value == "nil" ){
					checkCell.value = operateCell.cdr.cdr.cdr.value;
					checkCell.car = null;
					checkCell.type = 0;
				} else{
					System.out.println("if文の記法が間違っているか、予期しないエラー。");
					return "error";
				}
			}else if( functions.lastIndexOf(operateCell.value) != -1 ){
				// 定義された関数

				if( operateCell.cdr.type != 3 ){
					valiablevalues.set( valiables.lastIndexOf( functionvalues.get( this.functions.lastIndexOf( operateCell.value ) ).value ) , operateCell.cdr.value );
				} else {
					ConsCell valiableCell = functionvalues.get( this.functions.lastIndexOf( operateCell.value ) ).car;
					ConsCell o_valiableCell = operateCell.cdr.car;
					while( valiableCell != null ){
						valiablevalues.set( valiables.lastIndexOf( valiableCell.value ) , o_valiableCell.value );
						valiableCell = valiableCell.cdr;
						o_valiableCell = o_valiableCell.cdr;
					}
				}

				operateCell = functionvalues.get( functions.lastIndexOf( operateCell.value ) ).cdr ;

			}else{
				System.out.println("記法が間違っているか、予期しないエラー。(failure_in_operater-analysis.)");
				return "error";
			}

		}while( tokens.cdr != null || tokens.car != null );

		return tokens.value;
	}


	// 四則演算
	private String operate(String operater, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();

		while( temp != null && temp.type == 1 ){
			values.push( Double.valueOf( temp.value ) );
			temp = temp.cdr;
		}

		if( values.empty() ){
			System.out.println("記法が間違っているか、予期しないエラー。(stack_empty)");
			return "error";
		}

		double num = 0.0 ;
		int size = values.size();

		if( operater.equals("+") ) {
			num = 0.0;
			for( int i = 0; i < size; i++ ){
				num += values.pop();
			}
		} else if( operater.equals("*") ){
			num = 1.0;
			for( int i = 0; i < size; i++ ){
				num *= values.pop();
			}
		} else if( operater.equals("-") ){
			if( size != 2 ){
				System.out.println("減算は２項演算です");
				return "error";
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m - n;
			}
		} else if( operater.equals("/") ){
			if( size != 2 ){
				System.out.println("除算は２項演算です");
				return "error";
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m / n;
			}
		} else {
				System.out.println("記法が間違っているか、予期しないエラー。");
				return "error";
		}

	return String.valueOf(num);

	}


	// 比較演算
	private String compare(String operater, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();

		while( temp != null && temp.type == 1 ){
			values.push( Double.valueOf( temp.value ) );
			temp = temp.cdr;
		}

		String value = null ;
		int size = values.size();

		if( values.empty() || size != 2 ){
			System.out.println("比較は２項演算です。");
			return "error";
		}

		double n = values.pop();
		double m = values.pop();

		if( "<".equals(operater) ){
			if( m < n ) value = "t";
			else value = "nil";
		} else if( ">".equals(operater) ){
			if( m > n ) value = "t";
			else value = "nil";
		} else if( "<=".equals(operater) ){
			if( m <= n ) value = "t";
			else value = "nil";
		} else if( ">=".equals(operater) ){
			if( m >= n ) value = "t";
			else value = "nil";
		} else if( "=".equals(operater) ){
			if( m == n ) value = "t";
			else value = "nil";
		} else {
			System.out.println("比較の記述が不正");
			return "error";
		}

		return value;
	}

}
