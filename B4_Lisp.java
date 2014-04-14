
public class B4_Lisp {

	public static void main(String[] args) {

//		String lisp = "(+ 10 20)";
//		String lisp = "(+ (* x 2)(- y 4) z)";
		String lisp = "(+ (* 2 3)(- 5 4) 3)";
//		String lisp = "(defun func (x y)( + x y))";

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

		// 評価
		Evaluation result = new Evaluation( syntact );
		System.out.println(result.returnResult( syntact ));

	}

}
