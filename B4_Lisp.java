
public class B4_Lisp {

	public static void main(String[] args) {

//		String lisp = "(+ 10 20)";
		String lisp = "(+ (* 10 20)(- y 4))";

		// 字句解析
		LexAnalysis token_Lex = new LexAnalysis(lisp);
		String[] tokens_str = token_Lex.returnStr();

//		字句解析結果確認用
//		for(int i = 0; i <tokens_str.length ; i++ ){
//			System.out.println(tokens_str[i]);
//		}

		String[] functions = null;
		String[] valiables = null;

		// 構文解析
		SynAnalysis token_Syn = new SynAnalysis( tokens_str , functions , valiables);
		ConsCell syntact = token_Syn.returnCC();
		ConsCell.printConsCell(syntact);

		System.out.println("This program was finished.");
	}

}
