import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class B4_Lisp {

	public static void main(String[] args) throws IOException {

		String lisp;
		int argcounter = 0;

		ArrayList<String> functions = new ArrayList<String>();
		ArrayList<ConsCell> functionvalues = new ArrayList<ConsCell>();
		ArrayList<String> valiables = new ArrayList<String>();
		ArrayList<String> valiablevalues = new ArrayList<String>();
		long start = 0;
		long stop = 0;

		do{

			if( args.length == 0 ){
				System.out.print(" Input:");
				BufferedReader input = new BufferedReader( (new InputStreamReader( System.in )) );
				lisp = input.readLine();
			} else {
				if( argcounter >= args.length ) lisp = null;
				lisp = args[ argcounter ];
				argcounter++;
			}

			if("exit".equals(lisp)) break;

			// 字句解析
			LexAnalysis token_Lex = new LexAnalysis(lisp);
			String[] tokens_str = token_Lex.returnStr();

			//字句解析結果確認用
//			for(int i = 0; i <tokens_str.length ; i++ ){
//			System.out.println(tokens_str[i]);
//			}

			// 変数置換
//			int counter = 0;
//			while( counter < valiables.size() ){
//				for( int i = 0; i < tokens_str.length ; i++ ){
//					if( tokens_str[i].matches( valiables.get(counter) ) ) tokens_str[i] = valiablevalues.get(counter) ;
//				}
//				counter++;
//			}

			// 構文解析
			SynAnalysis token_Syn = new SynAnalysis( tokens_str , functions , valiables, functionvalues , valiablevalues);
			ConsCell syntact;
			try{
			syntact = token_Syn.returnCC();
			} catch(ArrayIndexOutOfBoundsException e){
				System.out.println("解析不能な構文");
				continue;
			}

			// 構文解析結果確認用
//			ConsCell.printConsCell(syntact);
//			System.out.println("");

			// 評価
			start = System.currentTimeMillis();
			Evaluation result = new Evaluation( syntact, functions , valiables, functionvalues , valiablevalues );
			System.out.println( "Output: " + result.rR( syntact ));
			stop = System.currentTimeMillis();
			System.out.println("  time: " + (stop-start) + "[ms]" );
			ConsCell.returnCopycount();
			result.returnPerCount();


		} while ( lisp != null );

	}

}
