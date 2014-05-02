import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;


public class lispFileOpener {

	private FileReader lispFile;

	public lispFileOpener(String filepath){

		try{
				File file = new File(filepath);
				FileReader fr = new FileReader(file);
				this.lispFile = fr;
			}catch(FileNotFoundException e){
				System.out.println(e);
			}

	}

	public String[] lispReader(){
		int c = 0;
		char ch = 0;
		int bracketCount = 0;
		String temp = "";
		Deque<String> tokens = new ArrayDeque<String>();

		try{
			while( (c = this.lispFile.read()) != -1 ){
				ch = (char)c;
				if(ch == '(') bracketCount++;
				else if(ch == ')') bracketCount--;

				if( ch != '\n' && ch != '\r'){
					temp += ch;

					if(bracketCount == 0){
						tokens.addLast(temp);
						temp = "";
					}
				}

			}
		} catch (IOException e) {
			System.out.println(e);
		}

		int size = tokens.size();
		if( size == 0 ) return null;

		String[] lisp = new String[size];
		for(int i = 0; i < size; i++ ){
			lisp[i] = tokens.removeFirst();
		}

		return lisp;
	}

}
