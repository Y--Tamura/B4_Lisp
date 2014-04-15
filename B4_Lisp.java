import java.util.ArrayList;

public class B4_Lisp {

	public static void main(String[] args) {

//		String lisp = "(+ 10 20)";
//		String lisp = "(+ (* x 2)(- y 4) z)";
//		String lisp = "(+ (* 2 3)(- 5 4) 3)";
//		String lisp = "(defun func (x y)( + x y))";
//		String lisp = "(if (= (+ 3 3) 6) 1.0 2.0)";
		String lisp = "(setq abc 100)";

		// 字句解析
		LexAnalysis token_Lex = new LexAnalysis(lisp);
		String[] tokens_str = token_Lex.returnStr();

//		字句解析結果確認用
//		for(int i = 0; i <tokens_str.length ; i++ ){
//			System.out.println(tokens_str[i]);
//		}

		ArrayList<DefinitionCell> functions = new ArrayList<DefinitionCell>();
		ArrayList<DefinitionCell> valiables = new ArrayList<DefinitionCell>();

		// 構文解析
		SynAnalysis token_Syn = new SynAnalysis( tokens_str , functions , valiables);
		ConsCell syntact = token_Syn.returnCC();

//		構文解析結果確認用
		ConsCell.printConsCell(syntact);
		System.out.println("");

		// 評価
		Evaluation result = new Evaluation( syntact, functions , valiables );
		System.out.println( "Answer: " + result.returnResult( syntact ));

	}

}
