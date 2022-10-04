import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.print.StreamPrintService;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
			
			outputString +=("---------"+filePeso.getName()+"-----------\n");
			outputString +=("Sem intevalo de classe\n");
			
			outputString +=(columnWithoutPeso.getRelatorio());
			outputString +=("\nCom intervalo de classe\n");
			
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
			
			outputString +=("---------"+fileIdade.getName()+"-----------\n");
			outputString +=("Sem intevalo de classe\n");
			
			outputString +=(columnWithoutIdade.getRelatorio());
			outputString +=("\nCom intervalo de classe\n");
			
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
			
			outputString +=("---------"+fileAltura.getName()+"-----------\n");
			outputString +=("Sem intevalo de classe\n");
			fileAltura.getName();
			outputString +=(columnWithoutAltura.getRelatorio());
			outputString +=("\nCom intervalo de classe\n");
			
			outputString +=(columnWithAltura.getRelatorio());
			
			
			PrintStream out = new PrintStream("saida.txt");
			out.print( outputString );;
			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
