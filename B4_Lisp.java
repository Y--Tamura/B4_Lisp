import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class B4_Lisp {

	public static void main(String[] args) throws IOException {

		String[] lisp;
		int flag = 0;
		int lispcount = 0;
		int lispsize = 1;

		ArrayList<String> functions = new ArrayList<String>();
		ArrayList<ConsCell> functionvalues = new ArrayList<ConsCell>();
		ArrayList<String> valiables = new ArrayList<String>();
		ArrayList<String> valiablevalues = new ArrayList<String>();
		long start = 0;
		long stop = 0;

		do{

			if( flag == 3 || args.length == 0 ){
				// 直接入力
				System.out.print(">");
				BufferedReader input = new BufferedReader( (new InputStreamReader( System.in )) );
				lisp = new String[lispsize];
				lisp[lispcount] = input.readLine();
			} else if( args.length == 1 ) {
				// ファイルから入力
				flag = 1;
				lispFileOpener lispf = new lispFileOpener( args[0] );
				lisp = lispf.lispReader();
				lispsize = lisp.length;
				System.out.println("Read:" + args[0]);
				args[0] = null;
			} else {
				lisp = null;
				System.out.println("Error: 読み込めるファイルは１つです。");
				System.exit(0);
			}


			do{

				if("exit".equals(lisp[lispcount])) {
					System.exit(0);
				}

				if( flag == 1 ){
					System.out.println(">" + lisp[lispcount]);
				}

				// 字句解析
				LexAnalysis token_Lex = new LexAnalysis(lisp[lispcount]);
				String[] tokens_str = token_Lex.returnStr();

				//字句解析結果確認用
//				for(int i = 0; i <tokens_str.length ; i++ ){
//					System.out.println(tokens_str[i]);
//				}

				// 構文解析
				SynAnalysis token_Syn = new SynAnalysis( tokens_str , functions , valiables, functionvalues , valiablevalues);
				ConsCell syntact;
				try{
					syntact = token_Syn.returnCC();
				} catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Error: 解析不能な構文");
					continue;
				}

				// 構文解析結果確認用
//				ConsCell.printConsCell(syntact);
//				System.out.println("");

				// 評価
				start = System.currentTimeMillis();
				Evaluation3 result = new Evaluation3( syntact, functions , valiables, functionvalues , valiablevalues );
				System.out.println( result.returnResult( syntact ));
				stop = System.currentTimeMillis();
				System.out.println("\ttime: " + (stop-start) + "[ms]\n" );

				if( flag == 1 ) lispcount++;

			}while( lispcount < lispsize && flag == 1);

			if( flag == 1 ){
				flag = 3;
				lispsize = 1;
				lispcount = 0;
			}

		} while ( true );

	}

}
