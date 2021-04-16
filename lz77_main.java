import java.io.*;
import java.util.*;
public class lz77_main{
public static void main(String [] args)throws IOException {
  Scanner in = new Scanner(System.in);

  lz77 lz = new lz77();

  System.out.println("Введите режим");
  String mod = in.nextLine();
  String name_file;

  if(mod.equals("Кодировать")){
    System.out.println("Введите название файла");
    name_file = in.nextLine();
    lz.compress(name_file);
  }else if(mod.equals("Декодировать")){
    System.out.println("Введите название файла");
    name_file = in.nextLine();
    lz.de_compress(name_file);
  }else{
    System.out.println("Неправильный режим");
  }

}
}
