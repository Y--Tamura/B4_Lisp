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


			permutation_value( tokens , valiables , valiablevalues );

			// 終端へ移動

			do{

				if( !functions.isEmpty() ){
					if( functions.indexOf( temp.value ) != -1 ) temp.type = 7;
				}

				if( temp.type == 2 || temp.type == 7 ) operateCell = temp;
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
					this.valiablevalues.add( "nil" );
				}
				checkCell.car = null;
				checkCell.type = 1;
			}else if( operateCell.value.matches("defun") ){
				// 関数定義
				this.functions.add( operateCell.cdr.value );
				this.functionvalues.add( operateCell.cdr.cdr );

				ConsCell valiableCell = operateCell.cdr.cdr.car;
				ConsCell searchCell = operateCell.cdr.cdr.cdr.car;
				while( valiableCell != null ){
					permutation( valiableCell.value , searchCell );
					valiableCell = valiableCell.cdr;
				}

				checkCell.car = null;
				checkCell.value = "defun " + operateCell.cdr.value;
				checkCell.type = 1;
			}else if( operateCell.value.matches("if") ){
				// if文
				ConsCell compCell = new ConsCell( operateCell.cdr.value );
				compCell.car = operateCell.cdr.car;
				compCell.cdr = null;
				compCell.type = operateCell.cdr.type;
				operateCell.cdr.value = returnResult( compCell );

				if( operateCell.cdr.value == "t" ){
					if( operateCell.cdr.cdr.car != null ) checkCell.car = operateCell.cdr.cdr.car;
					else checkCell.value = operateCell.cdr.cdr.value;
				} else if( operateCell.cdr.value == "nil" ){
					if( operateCell.cdr.cdr.car != null ) checkCell.car = operateCell.cdr.cdr.cdr.car;
					else checkCell.value = operateCell.cdr.cdr.cdr.value;
				} else{
					System.out.println("if文の記法が間違っているか、予期しないエラー。");
					return "error";
				}
			}else if( functions.lastIndexOf(operateCell.value) != -1 ){
				// 定義された関数

				ConsCell valiableCell = operateCell.cdr;
				ConsCell valiablevCell = functionvalues.get( functions.lastIndexOf( operateCell.value ) ).car;
				while( valiableCell != null ){
					if( valiableCell.value != null ){
						valiablevalues.add( valiableCell.value );
						valiables.add( valiablevCell.value );
						valiableCell = valiableCell.cdr;
						valiablevCell = valiablevCell.cdr;
					}else break;
				}

//				tokens.car = functionvalues.get( functions.lastIndexOf( operateCell.value ) ).cdr.car ;
				tokens.car = ConsCell.copyCC( functionvalues.get( functions.lastIndexOf( operateCell.value ) ).cdr.car );

//				permutation_value( tokens.car , valiables , valiablevalues );

			}else{
				System.out.println("記法が間違っているか、予期しないエラー。(failure_in_operater-analysis.)");
				return "error";
			}

		}while( tokens.cdr != null || tokens.car != null );

		return tokens.value;
	}


	private void permutation( String value , ConsCell searchCell) {

		if( searchCell.value != null && searchCell.type == 0 ){
			if( value.matches( searchCell.value ) ){
				searchCell.type = 6;
			}
		}

		if( searchCell.car != null ) permutation( value , searchCell.car );
		if( searchCell.cdr != null ) permutation( value , searchCell.cdr );

	}


	private void permutation_value(ConsCell Cell, ArrayList<String> vs, ArrayList<String> vvs) {

		if( vs.lastIndexOf( Cell.value ) != -1 ){
			Cell.value = vvs.get( vs.lastIndexOf( Cell.value ) );
		}

		if( Cell.car != null ) permutation_value( Cell.car , vs , vvs );
		if( Cell.cdr != null ) permutation_value( Cell.cdr , vs , vvs );

	}



	// 四則演算
	private String operate(String operater, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();

		while( temp != null ){
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

		while( temp != null ){
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
