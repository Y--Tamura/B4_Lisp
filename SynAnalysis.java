import java.util.ArrayList;


public class SynAnalysis {

	private String[] tokens;
	private ArrayList<DefinitionCell> functions;
	private ArrayList<DefinitionCell> valiables;

	public SynAnalysis( String[] str , ArrayList<DefinitionCell> functions , ArrayList<DefinitionCell> valiables) {
		this.tokens = str;
		this.functions = functions;
		this.valiables = valiables;
	}

	public ConsCell returnCC() {
		ConsCell returns = new ConsCell( this.tokens , 0 ,this.functions , this.valiables);
		return returns;
	}

}
