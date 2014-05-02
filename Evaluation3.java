import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


public class Evaluation3 {

	public String value = null;
	public ConsCell fixMeCell;
	public ArrayList<String> functions, valiables, valiablevalues;
	public ArrayList<ConsCell> functionvalues;
//	private ArrayList<String> keys, vals;
//	private int counter = 0;
//	public Deque<Integer> indexs = new ArrayDeque<Integer>();


	public Evaluation3(ConsCell cell) {
		this( cell, null , null , null , null );
	}

	public Evaluation3(ConsCell cell, ArrayList<String> functions, ArrayList<String> valiables, ArrayList<ConsCell> functionvalues, ArrayList<String> valiablevalues ) {
//		ArrayList<String> k = new ArrayList<String>();
//		ArrayList<String> v = new ArrayList<String>();
		fixMeCell = cell;
		this.functions = functions;
		this.valiables = valiables;
		this.functionvalues = functionvalues;
		this.valiablevalues = valiablevalues;
//		this.keys = k;
//		this.vals = v;
	}

	public String returnResult( ConsCell cell){
		ConsCell operateCell = cell;
		int index;

		operateCell = cell;
		if( cell.car != null ){
			return returnResult(cell.car);
		}else if( operateCell.value == null ){
			return "nil";
		}else if( "+-*/".indexOf(operateCell.value) != -1 ){
			// 四則演算
			return operate( operateCell.value , operateCell.cdr );
		}else if( "<=>=".indexOf(operateCell.value) != -1 ){
			// 比較演算
			return compare( operateCell.value , operateCell.cdr );
		}else if( "if".equals( operateCell.value ) ){
			// if文
			String bool = returnResult(operateCell.cdr);
			if( "t".equals( bool )) return returnResult( operateCell.cdr.cdr );
			else if( "nil".equals( bool )) return returnResult ( operateCell.cdr.cdr.cdr );
			else{
				System.out.println("if文の記法が間違っているか、予期しないエラー。");
				return "error";
			}
		}else if( (index = functions.lastIndexOf(operateCell.value)) != -1 ){
			// 定義された関数
			ConsCell vCell = operateCell.cdr;
			ConsCell tokenCell = functionvalues.get( index );


			//ディープコピー
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> vals = new ArrayList<String>();
			ConsCell vvCell = tokenCell.car;
			while( vvCell != null && vvCell.value != null ){
				vals.add( returnResult(vCell) );
				keys.add( vvCell.value );
				vCell = vCell.cdr;
				vvCell = vvCell.cdr;
			}


			tokenCell = ConsCell.CCf( functionvalues.get( index ), keys, vals );

			String result = returnResult( tokenCell.cdr );

			keys.clear();
			vals.clear();
			tokenCell = null;

			// 参照
//			int count = 0;
//			ConsCell vvCell = tokenCell.car;
//
//			while( vCell != null && vvCell != null ){
//				this.valiablevalues.add( returnResult(vCell) );
//				this.valiables.add( vvCell.value );
//				count++;
//				vCell = vCell.cdr;
//				vvCell = vvCell.cdr;
//			}
//
//			String result = returnResult( tokenCell.cdr );
//
//			int size = this.valiables.size();
//			for(int i = 0; i < count; i++){
//				size--;
//				this.valiablevalues.remove( size );
//				this.valiables.remove( size );
//			}

			return result;

		}else if( (index = valiables.lastIndexOf(operateCell.value)) != -1 ){
			// 定義された変数
			return valiablevalues.get(index);
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
		}else{
			return cell.value;
		}

	}


//	private void permutation_value( ConsCell Cell ) {
//
//		if( Cell.value != null ){
//			int index = this.valiables.lastIndexOf( Cell.value );
//			if( index != -1 ){
//				Cell.value = this.valiablevalues.get( index );
//			}
//		}
//
//		if( Cell.car != null ) permutation_value( Cell.car );
//		if( Cell.cdr != null ) permutation_value( Cell.cdr );
//
//	}

	// 四則演算
	private String operate(String operator, ConsCell operateCC) {

		Deque<Double> values = new ArrayDeque<Double>();
		ConsCell temp = operateCC;
		double value = 0.0;

		while( temp != null ){
			try{
				value = Double.valueOf( returnResult(temp) );
				values.addLast(value);
				temp = temp.cdr;
			}catch(NumberFormatException e){
				break;
			}
		}

		if( values.size() == 0 ){
			System.out.println("記法が間違っているか、予期しないエラー。(queue_empty)");
			return "error";
		}

		double num = values.removeFirst();
		int size = values.size();

		if( "+".equals(operator) ) {
			for( int i = 0; i < size; i++ ){
				num += values.removeFirst();
			}
		} else if( "*".equals(operator) ){
			for( int i = 0; i < size; i++ ){
				num *= values.removeFirst();
			}
		} else if( "-".equals(operator) ){
			for( int i = 0; i < size; i++ ){
				num -= values.removeFirst();
			}
		} else if( "/".equals(operator) ){
			for( int i = 0; i < size; i++ ){
				num /= values.removeFirst();
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
//		System.out.println("m=" + returnResult(operateCC) + " n=" + returnResult(operateCC.cdr));
		double m = Double.valueOf( returnResult(operateCC) );
		double n = Double.valueOf( returnResult(operateCC.cdr) );
		String value = null;
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
