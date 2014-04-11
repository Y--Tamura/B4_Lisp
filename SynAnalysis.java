
public class SynAnalysis {

	private String[] tokens;
	private String[] functions;
	private String[] valiables;

	public SynAnalysis( String[] str , String[] functions , String[] valiables) {
		this.tokens = str;
		this.functions = functions;
		this.valiables = valiables;
	}

	public ConsCell returnCC() {
		ConsCell returns = new ConsCell( this.tokens , 0 ,this.functions , this.valiables);
		return returns;
	}

}
