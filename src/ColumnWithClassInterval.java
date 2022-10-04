

import java.security.interfaces.DSAKeyPairGenerator;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Iterator;
import java.util.Map;

import java.util.TreeMap;

public class ColumnWithClassInterval {

	private ArrayList<Double> datas;
	private double mediana, totalXi,totalXimulFi, totalpowXiSubMed, totalfiMulpowXiSubMed, amplitudeTotal, numeroClasses, intervaloClasse;
	private ArrayList<Integer> fae;
	private ArrayList<Double> xi,xiMulFi, powXiSubMed, fiMulpowXiSubMed;
	
	private TreeMap<Double, Integer> countDatas,classesIntervalAndFi;

	private double mediaArPonderada, coeficienteVariacao, varianciaAmostral,desvioPadrao, moda;
	
	public ColumnWithClassInterval(ArrayList<Double> datas) {
		
		this.datas =  datas;
		calculateFi();
		calculateFaeAndMedianaAndMediaAriPonderadaAndModa();
		calculateVarianciaAmostral();
		
	}
	public ColumnWithClassInterval() {
		
		this.datas =  new ArrayList<>();
	}
	public ArrayList<Double> getDatas() {
		return datas;
	}
	public void addData(double data) {
		this.datas.add(data);
	}
	public boolean removeData(double data) {
		return this.datas.remove(data);
	}
	
	public void calculateFi(){
		ArrayList<Double> tempDatas = new ArrayList<>(datas);
		
		
		Collections.sort(tempDatas);
		countDatas = new TreeMap<>();
		for (Double data : tempDatas) {
			
			if(countDatas.containsKey(data)) {
				int value = countDatas.get(data);
				countDatas.put(data, value+1);
			}else {
				countDatas.put(data, 1);
			}
					
		}	
		double lowerKey = countDatas.firstKey();
		double highestKey = countDatas.lastKey();

		this.numeroClasses = Math.round( Math.sqrt(getTotalFi()) );
		this.amplitudeTotal = formatedDouble(highestKey- lowerKey , 2) ;
		this.intervaloClasse =  formatedDouble(this.amplitudeTotal/this.numeroClasses, 2);
	
		this.intervaloClasse = roundDecimal(this.intervaloClasse);
		
		
		double key = lowerKey;
		double keyPlusInterval=  formatedDouble(key+intervaloClasse, 2);
		
		classesIntervalAndFi = new TreeMap<>();

			
		Iterator<Map.Entry<Double, Integer>> iteratorXiandFi = countDatas.entrySet().iterator();
			
			
		while(iteratorXiandFi.hasNext()) {
			Map.Entry<Double, Integer> pair = (Map.Entry<Double, Integer>) iteratorXiandFi.next();
			
			
			
			if(pair.getKey() >= keyPlusInterval) {
				key = keyPlusInterval;
				keyPlusInterval = formatedDouble(key+intervaloClasse, 2) ;
				
			}
			
			if(classesIntervalAndFi.containsKey(key)) {
				int value = classesIntervalAndFi.get(key);
				classesIntervalAndFi.put(key, value + pair.getValue());
				
			}else {
				classesIntervalAndFi.put(key,pair.getValue());
				
			}
				
			
		}
	
	
	
	}
	
	
	public void calculateFaeAndMedianaAndMediaAriPonderadaAndModa(){
		// junto to fae eu também vou procurar a mediana, fazer isso depois daria trabalho
		int p = 0;
		if(getTotalFi()%2 != 0) {
			p = (this.datas.size() +1)/2;
		}else {
			int p1 = (this.datas.size())/2;
			int p2 = (this.datas.size() +1)/2;
			p = (p1+p2)/2;
		}
		this.xi = new ArrayList<>();
		this.fae = new ArrayList<>();
		this.xiMulFi = new ArrayList<>();
		
		int acumuladorFae = 0;
		double acumuladorXiFi = 0;
		double acumuladorXi = 0;
		this.mediana = 0;
		
		
		double tempModa = 0;
		double biggerValue = 0;
				
		for(Map.Entry<Double, Integer> pair : classesIntervalAndFi.entrySet()) {
			
			//xi
			double xiCalc = (pair.getKey() + (pair.getKey()+ this.intervaloClasse))/2;
			xiCalc = formatedDouble(xiCalc, 2);
			xi.add(xiCalc);
			acumuladorXi += xiCalc;
			
			// mediana
			if(acumuladorFae+pair.getValue()  >= p && mediana == 0) {
				this.mediana = pair.getKey()+( (p - acumuladorFae)/(double)pair.getValue() ) * this.intervaloClasse;
				this.mediana = formatedDouble(mediana, 1);
				
			}
				
			
			//fae
			acumuladorFae += pair.getValue();
			fae.add(acumuladorFae);
			
			// xi * fi
			double mul = xiCalc * pair.getValue();
			mul = formatedDouble(mul, 2);
			acumuladorXiFi += mul;
			xiMulFi.add(mul);
			
			if(pair.getValue() > biggerValue) {
				tempModa = pair.getKey();
				biggerValue = pair.getValue();
			}
		
			
		}
		Integer subD1 = this.classesIntervalAndFi.get(formatedDouble(tempModa-intervaloClasse,2));
		Integer subD2 = this.classesIntervalAndFi.get(formatedDouble(tempModa+intervaloClasse,2));
		
		double d1 = this.classesIntervalAndFi.get(tempModa) - (subD1 == null? 0:subD1); 
				 
		double d2 = this.classesIntervalAndFi.get(tempModa) - (subD2 == null? 0:subD2); 
				 
		this.moda = tempModa + (d1 / (d1 + d2)) *  intervaloClasse;
		
		
		this.totalXimulFi = formatedDouble(acumuladorXiFi, 2) ;
		this.totalXi = acumuladorXi;
		
		//media ari ponderada
		/// GAMBIARRA PARA PEGAR SO O 1 MAIS SIGNIFICATIVOS
		this.mediaArPonderada  =  formatedDouble( this.totalXimulFi/getTotalFi()  ,2);
		
	}
	public void calculateVarianciaAmostral() {
		powXiSubMed = new ArrayList<>();
		fiMulpowXiSubMed = new ArrayList<>();
		
		double acumuladorpowXiSubMed = 0;
		double acumuladorfiMulpowXiSubMed = 0;
		
		Iterator<Double> iteratorXi = this.xi.iterator();
		Iterator<Integer> iteratorFi = this.classesIntervalAndFi.values().iterator();
		
		while(iteratorXi.hasNext()) {
			double currentXi = (double) iteratorXi.next();
			int currentFi = (int) iteratorFi.next();
					
			// (xi - x)^2
			double calc = (currentXi - mediaArPonderada);
			calc *= calc;
			calc = formatedDouble(calc, 2);
			powXiSubMed.add(calc);
			acumuladorpowXiSubMed += calc;
			//fi * (xi - x)^2
			double fPXM = formatedDouble(currentFi * calc, 2);
			fiMulpowXiSubMed.add(fPXM);
			acumuladorfiMulpowXiSubMed+= fPXM;
			
		}
		this.totalpowXiSubMed = formatedDouble(acumuladorpowXiSubMed, 2) ;
		this.totalfiMulpowXiSubMed = formatedDouble(acumuladorfiMulpowXiSubMed, 2) ;
		
		this.varianciaAmostral = totalfiMulpowXiSubMed/ (getTotalFi()-1);
		this.desvioPadrao =  Math.sqrt(this.varianciaAmostral);
		this.desvioPadrao = formatedDouble(this.desvioPadrao, 2);
		this.coeficienteVariacao = (this.desvioPadrao/mediaArPonderada ) * 100;
		
	}
	
	
	public String getRelatorio() {
		String relat = "";
		Iterator<Map.Entry<Double, Integer>> iteratorXiFi = classesIntervalAndFi.entrySet().iterator();
		Iterator<Integer> iteratorFae = fae.iterator();
		Iterator<Double> iteratorXiMulFi = xiMulFi.iterator();
		Iterator<Double> iteratorPowXifisubMed = powXiSubMed.iterator();
		Iterator<Double> iteratorFiPowXiSubMed = fiMulpowXiSubMed.iterator();
		Iterator<Double> iteratorXi = xi.iterator();
		
		relat += 
				"Classes"
				+"\t\t"+
				"FI"
				+"\t"+
				"XI"
				+"\t"+
				"Fae"
				+"\t"+
				"Xi*FI"
				+"\t"+
				"(XI - X)^2"
				+"\t"+
				"FI * (XI - X)^2"
				+" \n";
		
		while (iteratorXiFi.hasNext()) {
			
			Map.Entry<Double, Integer> xiFi = (Map.Entry<Double, Integer>) iteratorXiFi.next();
			int fae = (int) iteratorFae.next();
			double xiMulFi = (double) iteratorXiMulFi.next();
			double powXifiSubMed = (double) iteratorPowXifisubMed.next();
			double fiPowXiSubMed = (double) iteratorFiPowXiSubMed.next();
			double currentXi = (double) iteratorXi.next();
			
			relat += 
					(xiFi.getKey()+" |- "+formatedDouble(xiFi.getKey()+intervaloClasse,2))
					+"\t"+
					xiFi.getValue()
					+"\t"+
					currentXi
					+"\t"+
					fae
					+"\t"+
					xiMulFi
					+"\t"+
					formatedDouble(powXifiSubMed, 2)
					+"\t\t"+
					fiPowXiSubMed
					+" \n";
			
			
		}
		
		relat+= "------------------------------------------------------------------------------\n";
		relat+="Total: "
				+"\t\t"+
				getTotalFi()
				+"\t"+
				formatedDouble(this.totalXi, 2)
				
				+"\t"+
				
				"\t"+
				getTotalXimulFi()
				+"\t"+
				getTotalpowXiSubMed()
				+"\t\t"+
				getTotalfiMulpowXiSubMed()
				;
		
		relat+= "\n\n";
		relat+= "Amplitude total: " + getAmplitudeTotal();
		relat+= "\n";
		relat+= "Numero de classes: "+ formatedDouble(getNumeroClasses(), 1) ;
		relat+= "\n";
		relat+= "Incremento de classe: "+ getIntervaloClasse();
		relat+= "\n";
		relat+= "Mediana: "+ getMediana();
		relat+= "\n";
		relat+= "Moda(s): "+ formatedDouble(this.moda, 2);
		relat+= "\n";
		relat+= "Media aritimetica ponderada: " + getMediaArPonderada();
		relat+= "\n";
		relat+= "Variância amostral: " + formatedDouble(getVarianciaAmostral(), 2);
		relat+= "\n";
		relat+= "Desvio Padrão: " + formatedDouble(getDesvioPadrao(), 2);
		relat+= "\n";
		relat+= "Coeficiente de variação: " + formatedDouble(getCoeficienteVariacao(), 2) +"%";
		
		return relat;
	}
	
public int getTotalFi() {
		
		return this.datas.size();
		
	}
	public double getMediana() {
		return mediana;
	}
	public void setMediana(double mediana) {
		this.mediana = mediana;
	}
	public double getTotalXimulFi() {
		return totalXimulFi;
	}
	public void setTotalXimulFi(double totalXimulFi) {
		this.totalXimulFi = totalXimulFi;
	}
	public double getTotalpowXiSubMed() {
		return totalpowXiSubMed;
	}
	public void setTotalpowXiSubMed(double totalpowXiSubMed) {
		this.totalpowXiSubMed = totalpowXiSubMed;
	}
	public double getTotalfiMulpowXiSubMed() {
		return totalfiMulpowXiSubMed;
	}
	public void setTotalfiMulpowXiSubMed(double totalfiMulpowXiSubMed) {
		this.totalfiMulpowXiSubMed = totalfiMulpowXiSubMed;
	}
	public ArrayList<Integer> getFae() {
		return fae;
	}
	public void setFae(ArrayList<Integer> fae) {
		this.fae = fae;
	}
	public ArrayList<Double> getXiMulFi() {
		return xiMulFi;
	}
	public void setXiMulFi(ArrayList<Double> xiMulFi) {
		this.xiMulFi = xiMulFi;
	}
	public ArrayList<Double> getPowXiSubMed() {
		return powXiSubMed;
	}
	public void setPowXiSubMed(ArrayList<Double> powXiSubMed) {
		this.powXiSubMed = powXiSubMed;
	}
	public ArrayList<Double> getFiMulpowXiSubMed() {
		return fiMulpowXiSubMed;
	}
	public void setFiMulpowXiSubMed(ArrayList<Double> fiMulpowXiSubMed) {
		this.fiMulpowXiSubMed = fiMulpowXiSubMed;
	}
	public Map<Double, Integer> getXiAndFi() {
		return countDatas;
	}
	public void setXiAndFi(TreeMap<Double, Integer> xiAndFi) {
		this.countDatas = xiAndFi;
	}
	public double getMediaArPonderada() {
		return mediaArPonderada;
	}
	public void setMediaArPonderada(double mediaArPonderada) {
		this.mediaArPonderada = mediaArPonderada;
	}
	public double getCoeficienteVariacao() {
		return coeficienteVariacao;
	}
	public void setCoeficienteVariacao(double coeficienteVariacao) {
		this.coeficienteVariacao = coeficienteVariacao;
	}
	public double getVarianciaAmostral() {
		return varianciaAmostral;
	}
	public void setVarianciaAmostral(double varianciaAmostral) {
		this.varianciaAmostral = varianciaAmostral;
	}
	public double getDesvioPadrao() {
		return desvioPadrao;
	}
	public void setDesvioPadrao(double desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}
	public void setDatas(ArrayList<Double> datas) {
		this.datas = datas;
	}
	
	public double getIntervaloClasse() {
		return intervaloClasse;
	}
	public void setIntervaloClasse(double intervaloClasse) {
		this.intervaloClasse = intervaloClasse;
	}
	public double getAmplitudeTotal() {
		return amplitudeTotal;
	}
	public void setAmplitudeTotal(double amplitudeTotal) {
		this.amplitudeTotal = amplitudeTotal;
	}
	public double getNumeroClasses() {
		return numeroClasses;
	}
	public void setNumeroClasses(double numeroClasses) {
		this.numeroClasses = numeroClasses;
	}
	public TreeMap<Double, Integer> getIntervalXiAndFi() {
		return classesIntervalAndFi;
	}
	public void setIntervalXiAndFi(TreeMap<Double, Integer> intervalXiAndFi) {
		this.classesIntervalAndFi = intervalXiAndFi;
	}
	
	private double formatedDouble(double value, int digits) {
		return  Double.parseDouble( String.format("%."+digits+"f", value).replace(",", "."));
	}
	
	private double roundDecimal(double number) { //top 10 gambiarras
		String temp[] = Double.toString(number).split("[.]");
		String decimal = temp[1];
		if(decimal.length() < 2)
			return number;
		double roundedDecimal = Double.parseDouble(decimal.charAt(0)+"."+decimal.charAt(1));
		roundedDecimal = Math.ceil(roundedDecimal);
		
		temp[1] = Double.toString(roundedDecimal);
		number = Double.parseDouble( temp[0]+"."+temp[1].replace(".", ""));
		
		return number;
	} 

	
}