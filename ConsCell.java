import java.util.ArrayList;

//	type	0:未指定 1:数値 2:演算子 3:カッコ 4:t/nil 5:関数定義 6:関数内引数候補 7:定義された関数
//	car		枝分かれ
//	value	値
//	cdr		次

public class ConsCell {

	public int type = 0;
	public ConsCell car = null;
	public ConsCell cdr = null;
	public String value = null;
	private static int printcounter = 0;
	private static int copycounter = 0;

	public ConsCell( String str ){
		this.value = str;
	}


	public ConsCell( String[] str , int counter , ArrayList<String> functions , ArrayList<String>valiables , ArrayList<ConsCell> functionvalues , ArrayList<String> valiablevalues ){
		// nullか")"なら終了
		if( str[counter] == null || ")".equals(str[counter]) ) ;

		// "("なら新たな分岐を作成
		else if( "(".equals( str[counter] ) ){
			this.type = 3;
			car = new ConsCell( str , counter + 1 , functions , valiables , functionvalues , valiablevalues );

			int bracket_counter = 0;

			do{
				if( "(".equals( str[counter] ) ) bracket_counter++ ;
				else if( ")".equals( str[counter] ) ) bracket_counter-- ;
				counter++;
			} while( bracket_counter != 0 );

			if( counter < str.length ){
				if( ")".equals(str[counter]) && str.length < counter + 1 ) counter++;
				cdr = new ConsCell( str , counter , functions , valiables , functionvalues , valiablevalues );
			}

		// それ以外なら要素を格納
		} else {
			this.value = str[ counter ];

			// 関数名か否かの判定
			if( !functions.isEmpty() ){
				if( functions.indexOf( this.value ) != -1 ) this.type = 7;
			}

			// もとから実装している関数
			if( this.type == 0 && this.value.matches("setq") ){
				this.type = 2;
			}else if( this.type == 0 && this.value.matches("(if)|(defun)") ){
				this.type = 5;
			}

			// 変数名か否かの判定
			if( !valiables.isEmpty() ){
				if( valiables.indexOf( this.value ) != -1 ) this.type = 1;
			}

			// 演算子か否かの判定
			if( this.type == 0 && (this.value.matches("<|<=|>|>=|=") || this.value.matches("[+-/*]") ) ){
				this.type = 2;
			}


			// 数値か否かの判定
			if( this.type == 0 ){
				try{
					Double.parseDouble( str[counter] );
					this.type = 1;
				} catch(NumberFormatException e){
					this.type = 6;
				}
			}

			// 次のセルの作成
			counter++;
			if( !")".equals( str[ counter ]) ){
				this.cdr = new ConsCell( str , counter , functions , valiables , functionvalues , valiablevalues );
			}else this.cdr = null;
		}

	}


	public ConsCell( String[] str ){
		this( str, 0 , null , null , null , null );
	}

	private static ConsCell copyCC(ConsCell Cell ) {
		copycounter++;
		if(Cell.value == null && Cell.cdr == null && Cell.car == null) return null;
		else{
			ConsCell newCell = new ConsCell( Cell.value );
			newCell.type = Cell.type;
			newCell.value = Cell.value;

			if( Cell.car != null ) newCell.car = copyCC( Cell.car );
			if( Cell.cdr != null ) newCell.cdr = copyCC( Cell.cdr );

			return newCell;
		}

	}


	public static ConsCell CCf( ConsCell CC, ArrayList<String> a, ArrayList<String> b ){
		return copyCCf( CC, a, b );
	}


	private static ConsCell copyCCf(ConsCell Cell, ArrayList<String> k, ArrayList<String> v ) {
		copycounter++;
		if(Cell.value == null && Cell.cdr == null && Cell.car == null) return null;
		else{
			ConsCell newCell = new ConsCell( Cell.value );
			newCell.type = Cell.type;
			newCell.value = Cell.value;

			if( newCell.type == 6 ){
				int index = k.lastIndexOf( Cell.value );
				if( index != -1 ){
					newCell.value = v.get( index );
				}
			}

			if( Cell.car != null ) newCell.car = copyCCf( Cell.car, k, v );
			if( Cell.cdr != null ) newCell.cdr = copyCCf( Cell.cdr, k, v );

			return newCell;
		}

	}


	public static ConsCell CC( ConsCell CC ){
		return copyCC( CC );
	}

	public static void returnCopycount( ){
		if(copycounter != 0 ){
			System.out.println("  copy: " + copycounter);
			copycounter = 0;
		}
	}

	public static void printConsCell( ConsCell cc ){
		int temp_counter;

		if( cc.car != null ) {
			if( printcounter != 0 ) System.out.print( "( to " + printcounter + ") " );
			if( cc.cdr != null && cc.cdr.type != 0 ) System.out.print("-- ");
			else System.out.println("");
			temp_counter = printcounter;
			printcounter++;
			if( cc.cdr != null ) printConsCell( cc.cdr );
			if( temp_counter != 0 ) System.out.print("\n(" + temp_counter + ")-- ");
			printConsCell( cc.car );
		} else if( cc.value != null  && cc.type != 3 ){
			System.out.print( cc.value );
			if( cc.cdr != null ) {
				System.out.print(" -- ");
				printConsCell( cc.cdr );
			} else System.out.println("");
		}

	}



}
