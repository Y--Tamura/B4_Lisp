import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;


public class lispFileOpener {

//	private FileReader lispFile;
	private Deque<FileReader> lispFiles;

//	public lispFileOpener(String filepath){
//
//		try{
//				File file = new File(filepath);
//				FileReader fr = new FileReader(file);
//				this.lispFile = fr;
//			}catch(FileNotFoundException e){
//				System.out.println(e);
//			}
//
//	}


	public lispFileOpener(String[] filepaths){

		int i = 0;
		Deque<FileReader> frs = new ArrayDeque<FileReader>();

		while( i < filepaths.length ){

			try{
				File file = new File(filepaths[i]);
				FileReader fr = new FileReader(file);
				frs.addLast(fr);
			}catch(FileNotFoundException e){
				System.out.println(e);
			}
			i++;
		}

		this.lispFiles = frs;
	}


	public String[] lispReader(){
		int c = 0;
		char ch = 0;
		int bracketCount = 0;
		String temp = "";
		Deque<String> tokens = new ArrayDeque<String>();

		while(this.lispFiles.peekFirst() != null){
			try{
				while( (c = this.lispFiles.peekFirst().read()) != -1 ){
					ch = (char)c;
					if(ch == '(') bracketCount++;
					else if(ch == ')') bracketCount--;

					if( ch != '\n' && ch != '\r'){
						temp += ch;

						if(bracketCount == 0){
							if(!temp.matches("^ +$")) tokens.addLast(temp);
							temp = "";
						}
					}

				}

				this.lispFiles.removeFirst();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		int size = tokens.size();
		if( size == 0 ) return null;

		String[] lisp = new String[size];
		for(int i = 0; i < size; i++ ){
			lisp[i] = tokens.removeFirst();
		}

		tokens = null;
		this.lispFiles = null;
		return lisp;
	}

}
