import java.util.ArrayList;
import java.util.Stack;


public class Evaluation {

	public String value = null;
	public ConsCell fixMeCell;
	public ArrayList<String> functions, valiables, valiablevalues;
	public ArrayList<ConsCell> functionvalues;
//	public Deque<Integer> indexs = new ArrayDeque<Integer>();


	public Evaluation(ConsCell cell) {
		this( cell, null , null , null , null );
	}

	public Evaluation(ConsCell cell, ArrayList<String> functions, ArrayList<String> valiables, ArrayList<ConsCell> functionvalues, ArrayList<String> valiablevalues ) {
		fixMeCell = cell;
		this.functions = functions;
		this.valiables = valiables;
		this.functionvalues = functionvalues;
		this.valiablevalues = valiablevalues;
	}

	public String returnResult( ConsCell cell){
		ConsCell temp;
		ConsCell checkCell = cell;
		ConsCell operateCell;
		int index;
//		long start, stop;

//		permutation_value( tokens , valiables , valiablevalues );

		do{

			temp = cell;
			operateCell = null;


//			permutation_value( tokens , valiables , valiablevalues );

			// 終端へ移動
			do{

				if( functions.indexOf( temp.value ) != -1 ){
					temp.type = 7;
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
			}else if( "setq".equals(operateCell.value) ){
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
			}else if( "defun".equals( operateCell.value ) ){
				// 関数定義
				this.functions.add( operateCell.cdr.value );
				this.functionvalues.add( operateCell.cdr.cdr );

//				ConsCell valiableCell = operateCell.cdr.cdr.car;
//				ConsCell searchCell = operateCell.cdr.cdr.cdr.car;
//				while( valiableCell != null ){
//					permutation( valiableCell.value , searchCell );
//					valiableCell = valiableCell.cdr;
//				}

				checkCell.car = null;
				checkCell.value = "defun " + operateCell.cdr.value;
				checkCell.type = 1;
			}else if( "if".equals( operateCell.value ) ){
				// if文
				ConsCell compCell = new ConsCell( operateCell.cdr.value );
				compCell.car = operateCell.cdr.car;
				compCell.cdr = null;
				compCell.type = operateCell.cdr.type;
				operateCell.cdr.value = returnResult( compCell );

				if( "t".equals( operateCell.cdr.value )) operateCell = operateCell.cdr.cdr;
				else if( "nil".equals( operateCell.cdr.value )) operateCell = operateCell.cdr.cdr.cdr;
				else{
					System.out.println("if文の記法が間違っているか、予期しないエラー。");
					return "error";
				}

				// トークンがブロック
				if( operateCell.car != null ){
					checkCell.car = operateCell.car;
				// トークンが要素
				}else {
					if( checkCell.cdr != cell ){
						checkCell.value = operateCell.value;
					}else{
						checkCell.cdr = operateCell;
						checkCell.cdr.cdr = null;
						checkCell.cdr.car = null;
					}
					checkCell.car = null;
				}

			}else if( (index = functions.lastIndexOf(operateCell.value)) != -1 ){
				// 定義された関数
// ディープコピー版(一応完成)
				ConsCell vCell = operateCell.cdr;
				ConsCell tokenCell = ConsCell.CC( functionvalues.get( index ) );

				ConsCell vvCell = tokenCell.car;
				while( vCell != null ){
					if( vCell.value != null ){
						valiablevalues.add( vCell.value );
						valiables.add( vvCell.value );
//						indexs.addFirst( valiables.lastIndexOf( vvCell.value ) );
						vCell = vCell.cdr;
						vvCell = vvCell.cdr;
					}else break;
				}

				permutation_value( tokenCell.cdr.car );


				if( checkCell.cdr != cell ){
					checkCell.value = returnResult( tokenCell.cdr );
				}else{
					checkCell.cdr = new ConsCell( returnResult( tokenCell.cdr ) );
					checkCell.cdr.cdr = null;
					checkCell.cdr.car = null;
				}


				checkCell.car = null;

//				while( indexs.size() != 0 && indexs.peekFirst() != null ){
//					valiablevalues.remove( indexs.peekFirst() );
//					valiables.remove( indexs.removeFirst() );
//				}

// 参照版
//				ConsCell vCell = operateCell.cdr;
//				ConsCell tokenCell =  functionvalues.get( index );
//
//				ConsCell vvCell = tokenCell.car;
//				while( vCell != null ){
//					if( vCell.value != null ){
//						valiablevalues.add( vCell.value );
//						valiables.add( vvCell.value );
//						indexs.addFirst( valiables.lastIndexOf( vvCell.value ) );
//						vCell = vCell.cdr;
//						vvCell = vvCell.cdr;
//					}else break;
//				}
//
////				if( checkCell.cdr != tokens ){
//					checkCell.value = returnResult( tokenCell.cdr );
////				}else{
////					checkCell.cdr = new ConsCell( returnResult( tokenCell.cdr ) );
////					checkCell.cdr.cdr = null;
////					checkCell.cdr.car = null;
////				}
//
//
//				checkCell.car = null;
//
//				while( indexs.size() != 0 && indexs.peekFirst() != null ){
//					valiablevalues.remove( indexs.peekFirst() );
//					valiables.remove( indexs.removeFirst() );
//				}


			}else{
				System.out.println("記法が間違っているか、予期しないエラー。(failure_in_operater-analysis.)");
				return "error";
			}

		}while( cell.cdr != null || cell.car != null );
//		}while( ( tokens.cdr != null && ( tokens.cdr.cdr != null || tokens.cdr.car != null ) ) || tokens.car != null );

		return cell.value;
	}


//	private void permutation( String value , ConsCell searchCell) {
//
//		if( searchCell.type == 0 && searchCell.value != null  ){
//			if( value.matches( searchCell.value ) ){
//				searchCell.type = 6;
//			}
//		}
//
//		if( searchCell.car != null ) permutation( value , searchCell.car );
//		if( searchCell.cdr != null ) permutation( value , searchCell.cdr );
//
//	}


	private void permutation_value( ConsCell Cell ) {


		if( Cell.type == 6 ){
			int index = this.valiables.lastIndexOf( Cell.value );
			if( index != -1 ){
				Cell.value = this.valiablevalues.get( index );
			}
		}

		if( Cell.car != null ) permutation_value( Cell.car );
		if( Cell.cdr != null ) permutation_value( Cell.cdr );

	}


	// 四則演算
	private String operate(String operater, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();
//		int index = -1;

		while( temp != null ){
//			if( temp.value != null && (index = valiables.lastIndexOf( temp.value )) != -1 ){
//				values.push( Double.valueOf( valiablevalues.get( index ) ));
//			}else
			if( temp.value != null ) values.push( Double.valueOf( temp.value ) );
			temp = temp.cdr;
		}

		if( values.empty() ){
			System.out.println("記法が間違っているか、予期しないエラー。(stack_empty)");
			return "error";
		}

		double num = 0.0 ;
		int size = values.size();

		if( "+".equals(operater) ) {
			num = 0.0;
			for( int i = 0; i < size; i++ ){
				num += values.pop();
			}
		} else if( "*".equals(operater) ){
			num = 1.0;
			for( int i = 0; i < size; i++ ){
				num *= values.pop();
			}
		} else if( "-".equals(operater) ){
			if( size != 2 ){
				System.out.println("減算は２項演算です");
				return "error";
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m - n;
			}
		} else if( "/".equals(operater) ){
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

	values.clear();
	return String.valueOf(num);

	}


	// 比較演算
	private String compare(String operater, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();
//		int index = -1;

		while( temp != null ){
//			if( temp.value != null && (index = valiables.lastIndexOf( temp.value )) != -1 ){
//				values.push( Double.valueOf( valiablevalues.get( index ) ));
//			}else
			if( temp.value != null ) values.push( Double.valueOf( temp.value ) );
			temp = temp.cdr;
		}

		String value ;
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

		values.clear();
		return value;
	}

}
