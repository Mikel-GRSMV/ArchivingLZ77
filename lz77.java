import java.io.*;
public class lz77{
  int size_buff;//размер буфера
  Reader mIn;
  PrintWriter mOut;
  StringBuffer search_buff;

public lz77(){
  this(1024);
}

public lz77(int buffSize){
  size_buff = buffSize;
  search_buff = new StringBuffer(size_buff);
}
//смена размера буфера
private void change_buff(){
  if (search_buff.length() > size_buff){
    search_buff = search_buff.delete(0,  search_buff.length() - size_buff);
  }
}
// Метод кодирования
public void compress(String file)throws IOException{
  mIn = new BufferedReader(new FileReader(file));
  mOut = new PrintWriter(new BufferedWriter(new FileWriter(file + ".lz77")));

  int nextChar;
  String currentMatch = "";
  int matchIndex = 0;
  int tempIndex = 0;

  while ((nextChar = mIn.read()) != -1){
  // поиск совпадений в словаре
    tempIndex = search_buff.indexOf(currentMatch + (char)nextChar);
    if (tempIndex != -1) {
	     currentMatch += (char)nextChar;
	      matchIndex = tempIndex;
    } else {
    //кодирование наибольшего совпадения
	  String codedString = "~" + matchIndex + "~" + currentMatch.length() + "~" + (char)nextChar;
	  String concat = currentMatch + (char)nextChar;
	if (codedString.length() <= concat.length()){
	  mOut.print(codedString);
	  search_buff.append(concat);
	  currentMatch = "";
	  matchIndex = 0;
	} else{
    //добавление символы без кодирования
	  currentMatch = concat;
    matchIndex = -1;
	  while (currentMatch.length() > 1 && matchIndex == -1){
	    mOut.print(currentMatch.charAt(0));
	    search_buff.append(currentMatch.charAt(0));
	    currentMatch = currentMatch.substring(1, currentMatch.length());
	    matchIndex = search_buff.indexOf(currentMatch);
	 }
	 }
	change_buff();//если требуется меняем размер буфера
    }
  }
    //добавление в конец файла последних символов
    if (matchIndex != -1){
      String codedString = "~" + matchIndex + "~" + currentMatch.length();
      if (codedString.length() <= currentMatch.length()) {
	       mOut.print("~" + matchIndex + "~" + currentMatch.length());
      } else {
	       mOut.print(currentMatch);
      }
    }
    mIn.close();
    mOut.flush();
    mOut.close();
  }
//метод декодирования
public void de_compress(String file) throws IOException{
  mIn = new BufferedReader(new FileReader(file));
  mOut = new PrintWriter(new BufferedWriter(new FileWriter("decod_" + file)));
  StreamTokenizer st = new StreamTokenizer(mIn);

  st.ordinaryChar((int)' ');
  st.ordinaryChar((int)'.');
  st.ordinaryChar((int)'-');
  st.ordinaryChar((int)'\n');
  st.wordChars((int)'\n', (int)'\n');
  st.wordChars((int)' ', (int)'}');

  int offset, length;
  while (st.nextToken() != StreamTokenizer.TT_EOF){//конец потока был прочитан
    switch (st.ttype) {//тип токена
      case StreamTokenizer.TT_WORD:
        search_buff.append(st.sval);
         mOut.print(st.sval);
	        change_buff();
	    break;
      case StreamTokenizer.TT_NUMBER:
	     offset = (int)st.nval;// отступ
	      st.nextToken();//получаем разделитель
	       if (st.ttype == StreamTokenizer.TT_WORD){
	          search_buff.append(offset+st.sval);
            mOut.print(offset+st.sval);
	    break;
	   }
	st.nextToken();
	length = (int)st.nval;
// получение строки из буфера поиска
  String output = search_buff.substring(offset, offset+length);
  mOut.print(output);
  search_buff.append(output);
	change_buff();
	break;
      default:
      }
    }
  mIn.close();
  mOut.flush();
  mOut.close();
  }
}
