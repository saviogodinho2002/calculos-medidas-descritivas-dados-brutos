

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ColumnWithoutClassInterval {

	private ArrayList<Double> datas;
	private double mediana, totalXimulFi, totalpowXiSubMed, totalfiMulpowXiSubMed;
	private ArrayList<Integer> fae;
	private ArrayList<Double> xiMulFi, powXiSubMed, fiMulpowXiSubMed, modas;
	public ArrayList<Double> getModas() {
		return modas;
	}
	public void setModas(ArrayList<Double> modas) {
		this.modas = modas;
	}
	private TreeMap<Double, Integer> xiAndFi;

	private double mediaArPonderada, coeficienteVariacao, varianciaAmostral,desvioPadrao;
	
	public ColumnWithoutClassInterval(ArrayList<Double> datas) {
		
		this.datas =  datas;
		calculateFi();
		calculateFaeAndMedianaAndMediaAriPonderadaAndModa();
		calculateVarianciaAmostral();
		
	}
	public ColumnWithoutClassInterval() {
		
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
		xiAndFi = new TreeMap<>();
		for (Double data : tempDatas) {
			
			if(xiAndFi.containsKey(data)) {
				int value = xiAndFi.get(data);
				xiAndFi.put(data, value+1);
			}else {
				xiAndFi.put(data, 1);
			}
					
		}	
	
	
	}
	
	public void calculateFaeAndMedianaAndMediaAriPonderadaAndModa(){
		// junto to fae eu também vou procurar a mediana, fazer isso depois daria trabalho
		int p = 0;
		if(this.datas.size()%2 != 0) {
			p = (this.datas.size() +1)/2;
		}else {
			int p1 = (this.datas.size())/2;
			int p2 = (this.datas.size() +1)/2;
			p = (p1+p2)/2;
		}
		
		this.fae = new ArrayList<>();
		this.xiMulFi = new ArrayList<>();
		
		int acumuladorFae = 0;
		int acumuladorXiFi = 0;
		this.mediana = 0;
		
		modas = new ArrayList<>();
		double moda = 0;
			
		for(Map.Entry<Double, Integer> pair : xiAndFi.entrySet()) {
			//fae
			acumuladorFae += pair.getValue();
			fae.add(acumuladorFae);
			
			// xi * fi
			double mul = pair.getKey() * pair.getValue();
			acumuladorXiFi += mul;
			xiMulFi.add(mul);
			
			// mediana
			if(acumuladorFae >= p && mediana == 0)
				this.mediana = pair.getKey();
			
			//moda 
			
			if(pair.getValue() > moda) {
				moda = pair.getValue();
				modas.clear();
				modas.add(pair.getKey());
			}else if(pair.getValue() == moda) {
				modas.add(pair.getKey());
			}
				
			
		}
		this.totalXimulFi = acumuladorXiFi;
		if(modas.size() == xiAndFi.size())
			modas.clear();
		//media ari ponderada
		/// GAMBIARRA PARA PEGAR SO O 1 MAIS SIGNIFICATIVOS
		this.mediaArPonderada  =  formatedDouble(this.totalXimulFi/getTotalFi(), 1);
		
	}
	public void calculateVarianciaAmostral() {
		powXiSubMed = new ArrayList<>();
		fiMulpowXiSubMed = new ArrayList<>();
		
		double acumuladorpowXiSubMed = 0;
		double acumuladorfiMulpowXiSubMed = 0;
		
		for (Map.Entry<Double, Integer> pair: xiAndFi.entrySet()) {		
			double calc = (pair.getKey() - mediaArPonderada);
			calc *= calc;
			calc = formatedDouble(calc, 2);
			// (xi - x)^2
			powXiSubMed.add(calc);
			acumuladorpowXiSubMed += calc;
			acumuladorpowXiSubMed = formatedDouble(acumuladorpowXiSubMed, 2);
			//fi * (xi - x)^2
			fiMulpowXiSubMed.add(formatedDouble(calc * pair.getValue(), 2)) ;
			acumuladorfiMulpowXiSubMed+= formatedDouble(calc * pair.getValue(), 2);
		}
		this.totalpowXiSubMed = acumuladorpowXiSubMed;
		this.totalfiMulpowXiSubMed = acumuladorfiMulpowXiSubMed;
		
		this.varianciaAmostral = totalfiMulpowXiSubMed/ (getTotalFi()-1);
		this.desvioPadrao = Math.sqrt(this.varianciaAmostral);
		this.coeficienteVariacao = (this.desvioPadrao/mediaArPonderada ) * 100;
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
		return xiAndFi;
	}
	public void setXiAndFi(TreeMap<Double, Integer> xiAndFi) {
		this.xiAndFi = xiAndFi;
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
	
	public String getRelatorio() {
		String relat = "";
		Iterator<Entry<Double, Integer>> iteratorXiFi = xiAndFi.entrySet().iterator();
		Iterator<Integer> iteratorFae = fae.iterator();
		Iterator<Double> iteratorXiMulFi = xiMulFi.iterator();
		Iterator<Double> iteratorPowXifisubMed = powXiSubMed.iterator();
		Iterator<Double> iteratorFiPowXiSubMed = fiMulpowXiSubMed.iterator();
		
		
		relat += 
				"XI"
				+"\t"+
				"FI"
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
			relat += 
					xiFi.getKey()
					+"\t"+
					xiFi.getValue()
					+"\t"+
					fae
					+"\t"+
					xiMulFi
					+"\t"+
					formatedDouble(powXifiSubMed, 2)///String.format("%.2f", powXifiSubMed).replace(",", ".") 
					+"\t\t"+
					fiPowXiSubMed
					+" \n";
			
		}
		relat+= "------------------------------------------------------------------------------\n";
		relat+="Total: "
				+"\t"+
				getTotalFi()
				+"\t"
				+"\t"+
				getTotalXimulFi()
				+"\t"+
				getTotalpowXiSubMed()
				+"\t\t"+
				getTotalfiMulpowXiSubMed()
				;
		relat+= "\n\n";
		relat+= "Mediana: "+ getMediana();
		relat+= "\n";
		relat+= "Moda(s): "+ getModas().toString().replaceAll("[\\[\\]]", "").replace(", ", " ");
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
