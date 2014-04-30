import java.util.ArrayList;
import java.util.Stack;


public class Evaluation3 {

	public String value = null;
	public ConsCell fixMeCell;
	public ArrayList<String> functions, valiables, valiablevalues;
	public ArrayList<ConsCell> functionvalues;
	private ArrayList<String> keys, vals;
//	public Deque<Integer> indexs = new ArrayDeque<Integer>();


	public Evaluation3(ConsCell cell) {
		this( cell, null , null , null , null );
	}

	public Evaluation3(ConsCell cell, ArrayList<String> functions, ArrayList<String> valiables, ArrayList<ConsCell> functionvalues, ArrayList<String> valiablevalues ) {
		ArrayList<String> k = new ArrayList<String>();
		ArrayList<String> v = new ArrayList<String>();
		fixMeCell = cell;
		this.functions = functions;
		this.valiables = valiables;
		this.functionvalues = functionvalues;
		this.valiablevalues = valiablevalues;
		this.keys = k;
		this.vals = v;
	}

	public String returnResult( ConsCell cell){
		ConsCell operateCell = cell;
		int index;

		operateCell = cell;
		if( cell.car != null ){
			return returnResult(cell.car);
		}else if( operateCell.value == null ){
			return "nil";
		}else if( operateCell.value.matches("[+-/*]") ){
			// 四則演算
			return operate( operateCell.value , operateCell.cdr );
		}else if( operateCell.value.matches("<|<=|>|>=|=")){
			// 比較演算
			return compare( operateCell.value , operateCell.cdr );
		}else if( "setq".equals(operateCell.value) ){
			// 変数定義
			this.valiables.add( operateCell.cdr.value );
			this.valiablevalues.add( operateCell.cdr.cdr.value );
			return "set " + operateCell.value;
		}else if( "defun".equals( operateCell.value ) ){
			// 関数定義
			this.functions.add( operateCell.cdr.value );
			this.functionvalues.add( operateCell.cdr.cdr );
			return "defun " + operateCell.cdr.value;
		}else if( "if".equals( operateCell.value ) ){
			// if文
			String bool = returnResult(operateCell.cdr);
			if( "t".equals( bool )) return returnResult( operateCell.cdr.cdr );
			else if( "nil".equals( bool )) return returnResult ( operateCell.cdr.cdr.cdr );
			else{
				System.out.println("if文の記法が間違っているか、予期しないエラー。");
				return "error";
			}
		}else if( (index = valiables.lastIndexOf(operateCell.value)) != -1 ){
			// 定義された変数
			return valiablevalues.get(index);
		}else if( (index = functions.lastIndexOf(operateCell.value)) != -1 ){
			// 定義された関数
			ConsCell vCell = operateCell.cdr;
//			ConsCell tokenCell = ConsCell.CC( functionvalues.get( index ) );	// ディープコピー
			ConsCell tokenCell = functionvalues.get( index );					// 参照

			ConsCell vvCell = tokenCell.car;
			while( vCell != null && vvCell != null ){
//				if( vCell.value != null ){
					this.vals.add( returnResult(vCell) );
					this.keys.add( vvCell.value );
					vCell = vCell.cdr;
					vvCell = vvCell.cdr;
//				}else break;
			}

			tokenCell = ConsCell.CCf( functionvalues.get( index ), this.keys, this.vals );

//			permutation_value( tokenCell.cdr );
			this.keys.clear();
			this.vals.clear();
			String result = returnResult( tokenCell.cdr );

			tokenCell = null;

			return result;

		}else{
			return cell.value;
		}

	}


	private void permutation_value( ConsCell Cell ) {

		if( Cell.value != null ){
			int index = this.keys.lastIndexOf( Cell.value );
			if( index != -1 ){
				Cell.value = this.vals.get( index );
			}
		}

		if( Cell.car != null ) permutation_value( Cell.car );
		if( Cell.cdr != null ) permutation_value( Cell.cdr );

	}

	// 四則演算
	private String operate(String operator, ConsCell operateCC) {

		ConsCell temp = operateCC;
		Stack<Double> values = new Stack<Double>();
		double value = 0.0;

		while( temp != null ){
			try{
				value = Double.valueOf( returnResult(temp) );
				values.push(value);
				temp = temp.cdr;
			}catch(NumberFormatException e){
				break;
			}
		}

		if( values.empty() ){
			System.out.println("記法が間違っているか、予期しないエラー。(stack_empty)");
			return "error";
		}

		double num = 0.0 ;
		int size = values.size();

		if( "+".equals(operator) ) {
			num = 0.0;
			for( int i = 0; i < size; i++ ){
				num += values.pop();
			}
		} else if( "*".equals(operator) ){
			num = 1.0;
			for( int i = 0; i < size; i++ ){
				num *= values.pop();
			}
		} else if( "-".equals(operator) ){
			if( size != 2 ){
				System.out.println("減算は２項演算です");
				return "error";
			} else {
				double n = values.pop();
				double m = values.pop();
				num = m - n;
			}
		} else if( "/".equals(operator) ){
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
		double dValue = 0.0;

		while( temp != null ){
			try{
				dValue = Double.valueOf( returnResult(temp) );
				values.push(dValue);
				temp = temp.cdr;
			}catch(NumberFormatException e){
				break;
			}
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
