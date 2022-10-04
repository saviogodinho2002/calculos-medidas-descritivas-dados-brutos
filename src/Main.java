import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {

		File filePeso = new File("peso.txt");
		File fileIdade = new File("idade.txt");
		File fileAltura = new File("altura.txt");
		
		ArrayList<Double> datas = new ArrayList<>();
		
		try {
			Scanner input = new Scanner(filePeso);
			String outputString = "";
			while(input.hasNext()) {
				datas.add( Double.parseDouble(  input.next() ));
			}
			input.close();
			
			
			
			ColumnWithoutClassInterval columnWithoutPeso = new ColumnWithoutClassInterval(datas); 
			
			ColumnWithClassInterval columnWithPeso = new ColumnWithClassInterval(datas);
			
			outputString +=("\n---------"+filePeso.getName()+"-----------\n");
			outputString +=("\n\nSem intevalo de classe\n\n");
			
			outputString +=(columnWithoutPeso.getRelatorio());
			outputString +=("\n\nCom intervalo de classe\n\n");
			
			outputString +=(columnWithPeso.getRelatorio());
			////
			
			input = new Scanner(fileIdade);
			datas.clear();
			while(input.hasNext()) {
				datas.add( Double.parseDouble(  input.next() ));
			}
			
			input.close();
			
			ColumnWithoutClassInterval columnWithoutIdade = new ColumnWithoutClassInterval(datas); 
			
			ColumnWithClassInterval columnWithIdade = new ColumnWithClassInterval(datas);
			
			outputString +=("\n---------"+fileIdade.getName()+"-----------\n");
			outputString +=("\n\nSem intevalo de classe\n\n");
			
			outputString +=(columnWithoutIdade.getRelatorio());
			outputString +=("\n\nCom intervalo de classe\n\n");
			
			outputString +=(columnWithIdade.getRelatorio());
			
			////
			
			input = new Scanner(fileAltura);
			
			datas.clear();
			while(input.hasNext()) {
				datas.add( Double.parseDouble(  input.next() ));
			}
			input.close();
			
			
			ColumnWithoutClassInterval columnWithoutAltura = new ColumnWithoutClassInterval(datas); 
			
			ColumnWithClassInterval columnWithAltura = new ColumnWithClassInterval(datas);
			
			outputString +=("\n---------"+fileAltura.getName()+"-----------\n");
			outputString +=("\n\nSem intevalo de classe\n\n");
			fileAltura.getName();
			outputString +=(columnWithoutAltura.getRelatorio());
			outputString +=("\n\nCom intervalo de classe\n\n");
			
			outputString +=(columnWithAltura.getRelatorio());
			
			
			PrintStream out = new PrintStream("saida.txt");
			out.print( outputString );;
			
			System.out.println(outputString);
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
