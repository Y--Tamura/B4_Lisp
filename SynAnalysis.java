import java.util.ArrayList;


public class SynAnalysis {

	private String[] tokens;
	private ArrayList<String> functions;
	private ArrayList<String> valiables;
	private ArrayList<ConsCell> functionvalues;
	private ArrayList<String> valiablevalues;

	public SynAnalysis( String[] str , ArrayList<String> functions , ArrayList<String> valiables , ArrayList<ConsCell> functionvalues , ArrayList<String> valiablevalues ) {
		this.tokens = str;
		this.functions = functions;
		this.valiables = valiables;
		this.functionvalues = functionvalues;
		this.valiablevalues = valiablevalues;
	}

	public ConsCell returnCC() {
		ConsCell returns = new ConsCell( this.tokens , 0 ,this.functions , this.valiables , this.functionvalues , this.valiablevalues);
		return returns;
	}

}
