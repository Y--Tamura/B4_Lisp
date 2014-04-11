import java.util.LinkedList;
import java.util.Queue;

//字句解析
public class LexAnalysis {

	public String token;
	public String[] tokens;

	public LexAnalysis(String str){
		this.token = str ;
	}

	public String[] returnStr(){

		String[] results ,temps_str ;						// 結果／スペースで分割したものの格納用
		String temp_str ,connect;							// 作業用
		Queue<String> queue_str = new LinkedList<String>();	// 解析後の字句用キュー
		char temp_char;										// 作業用
		int i;												// カウンタ

		//スペースで分割してから、くっついているトークンを考慮
		temps_str = this.token.split(" ");					//スペースで分割

		//分割後に改めてくっついているトークンを考慮
		for( i=0; i<temps_str.length; i++ ){

			temp_str = temps_str[i];

			if( temp_str.length() > 1 ){

				connect = "";

				for(int j=0; j < temp_str.length(); j++){
					temp_char = temp_str.charAt( j );

					if(temp_char == ')' || temp_char == '(' ){
						if( !("".equals(connect)) ){
							queue_str.offer( connect );
							connect = "";
						}
						queue_str.offer( "" + temp_char );
					} else {
					connect += temp_char;
					}
				}

				if( !("".equals(connect)) ){
					queue_str.offer( connect );
				}

			} else queue_str.offer( temp_str );
		}

		i = 0;
		int size = queue_str.size();
		results = new String[ size ];
		while(  i < size ){
			if( queue_str.peek() != null ){
				results[i] = queue_str.poll() ;
				i++;
			}
		}

		return results;

	}

}
