//	type	0:未指定 1:数値 2:演算子 3:カッコ
//	car		枝分かれ
//	value	値
//	cdr		次

public class ConsCell {

	public int type = 0;
	public ConsCell car = null;
	public ConsCell cdr = null;
	public String value = null;
	private static int printcounter = 0;


	public ConsCell( String[] str , int counter , String[] functions , String[]valiables){
		// nullか")"なら終了
		if( str[counter] == null || ")".equals(str[counter]) ) ;

		// "("なら新たな分岐を作成
		else if( "(".equals( str[counter] ) ){
			this.type = 3;
			car = new ConsCell( str , counter + 1 , functions , valiables );

			int bracket_counter = 0;

			do{
				if( "(".equals( str[counter] ) ) bracket_counter++ ;
				else if( ")".equals( str[counter] ) ) bracket_counter-- ;
				counter++;
			} while( bracket_counter != 0 );

			if( counter < str.length ){
				if( ")".equals(str[counter]) && str.length < counter + 1 ) counter++;
				cdr = new ConsCell( str , counter , functions , valiables );
			}

		// それ以外なら要素を格納
		} else {
			this.value = str[ counter ];

			// 関数名か否かの判定
			int scanningcounter = 0;
			while( functions != null && functions[scanningcounter]!= null && functions.length != 0 ){
				if( functions[scanningcounter].equals( this.value ) ) type = 2;
				scanningcounter++;
			}

			// 変数名か否かの判定
			scanningcounter = 0;
			while( valiables != null && valiables[scanningcounter] != null && valiables.length != 0 ){
				if( valiables[scanningcounter].equals( this.value ) ) type = 1;
				scanningcounter++;
			}

			// 数値か否かの判定
			if( this.type == 0 ){
				try{
					Double.parseDouble( str[counter] );
					this.type = 1;
				} catch(NumberFormatException e){
					this.type = 2;
				}
			}

			// 次のセルの作成
			counter++;
			if( !")".equals( str[ counter ]) ){
				this.cdr = new ConsCell( str , counter , functions , valiables );
			}
		}

	}


	public ConsCell( String[] str ){
		this( str, 0 , null , null );
	}

	public static void printConsCell( ConsCell cc ){
		int temp_counter;

		if( cc.car != null ) {
			if( printcounter != 0 ) System.out.print( "( to " + printcounter + ") " );
			temp_counter = printcounter;
			printcounter++;
			if( cc.cdr != null ) printConsCell( cc.cdr );
			if( temp_counter != 0 ) System.out.print("\n(" + temp_counter + ")-- ");
			printConsCell(cc.car);
		} else if( cc.value != null  && cc.type != 3 ){
			System.out.print(cc.value);
			if( cc.cdr != null ) {
				System.out.print(" -- ");
				printConsCell(cc.cdr);
			} else System.out.println("");
		}

	}

}
