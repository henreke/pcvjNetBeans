package processo;

import java.util.ArrayList;

import comunicacaoJava.ComunicacaoSerial;

public class Temperaturas{

	ComunicacaoSerial comunicacao;

	ArrayList<Temperatura> temperaturas = new ArrayList<Temperatura>();

	public Temperaturas(int Nsensores){

		for (int i=0;i<Nsensores;i++)
			temperaturas.add(new Temperatura(i));
	}
	public Temperatura getTemperatura(int i){
		return temperaturas.get(i);
	}
	public void setComunicacao(ComunicacaoSerial comunicacao) {
		this.comunicacao = comunicacao;
	}

	public void updateTemperaturas(){

		float[] temperaturasF = comunicacao.getTemperaturas();
		if (temperaturasF == null)
				return;
		if (temperaturasF.length >= temperaturas.size()) {
			for (int i=0; i< temperaturas.size(); i++) {
				
				temperaturas.get(i).setTemperatura(temperaturasF[i]);
			}
		}

	}


	public class Temperatura{

		private int Nsensor;
		private float temperatura;

		public Temperatura(int Nsensor){
			this.Nsensor = Nsensor;
		}

		public void setNsensor(int Nsensor){
			this.Nsensor = Nsensor;
		}
		public void setTemperatura(float temperatura){
			this.temperatura = temperatura;
		}
		public int getNsensor(){
			return Nsensor;
		}

		public float getTemperatura(){
			return temperatura;
		}

	}

}

