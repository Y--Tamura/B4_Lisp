import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class B4_Lisp {

	public static void main(String[] args) throws IOException {

		String[] lisp;
		String[] paths = args;
		int flag = 0;
		int lispcount = 0;
		int lispsize = 1;

		ArrayList<String> functions = new ArrayList<String>();
		ArrayList<ConsCell> functionvalues = new ArrayList<ConsCell>();
		ArrayList<String> valiables = new ArrayList<String>();
		ArrayList<String> valiablevalues = new ArrayList<String>();
		DecimalFormat df = new DecimalFormat("########0.#########;-########0.#########");
//		long start = 0;
//		long stop = 0;

		System.out.println("\nWelcome to the Lisp Interpreter.\n");
		System.out.println("Type \"(exit)\" and press enter to quit.");
		System.out.println("Type \"(file)\" and press enter to read file(s).\n");


		do{

			if( flag == 3 || paths.length == 0 ){
				// 直接入力
				System.out.print(">");
				BufferedReader input = new BufferedReader( (new InputStreamReader( System.in )) );
				lisp = new String[lispsize];
				lisp[lispcount] = input.readLine();
			} else {
				// ファイルから入力
				flag = 1;
				int i = 0;
				try{
					lispFileOpener lispf = new lispFileOpener( paths );
					lisp = lispf.lispReader();
					lispsize = lisp.length;
				}catch(NullPointerException e){
					lisp = new String[1];
					lisp[0] = "(file)";
					lispsize = 1;
					flag = 0;
				}
				while(i<paths.length){
					System.out.println("Read:" + paths[i]);
					paths[i] = null;
					i++;
				}
				System.out.println("");
			}

			do{

				if("(exit)".equals(lisp[lispcount])) {
					// "exit"→終了
//					long exittime = System.currentTimeMillis();
//					System.out.print("\nClosing the application...\n");
//					while( System.currentTimeMillis() - exittime < 1000 ){
//					}
					System.out.println("Bye.\n");
					System.exit(0);
				}

				if("(file)".equals(lisp[lispcount])) {
					// "file"→ファイルから入力
					System.out.print("\nfilepath(s)? >");
					BufferedReader input = new BufferedReader( (new InputStreamReader( System.in )) );
					paths = input.readLine().split(" ");
					System.out.println("");
					flag = 0;
					if("(exit)".equals(paths[0])){
						flag = 3;
					}
					break;
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
//				start = System.currentTimeMillis();
				Evaluation3 result = new Evaluation3( syntact, functions , valiables, functionvalues , valiablevalues );
				String answer = result.returnResult( syntact );
				try{
					double d = Double.parseDouble( answer );
					System.out.println( df.format(d) + "\n");
				} catch(NumberFormatException e){
					System.out.println( answer+ "\n");;
				}
//				stop = System.currentTimeMillis();
//				System.out.println("\ttime: " + (stop-start) + "[ms]\n" );

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
