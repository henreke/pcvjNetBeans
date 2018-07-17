package processo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import comunicacaoJava.ComunicacaoSerial;
import processo.Tanque.relogioUpdate;


public class Vazoes {

	static int Nmedidores = 4;
	ArrayList<Vazao> vazoes = new ArrayList<Vazao>();
	ComunicacaoSerial comunicacao;;

	Timer timerUpdate;

	public Vazoes() {
		for (int i=0; i<Nmedidores;i++)
			vazoes.add(new Vazao(i));

		//timerUpdate = new Timer();
		//timerUpdate.scheduleAtFixedRate(new relogioUpdate(), 2000, 1000);
	}
	public void setComunicacao(ComunicacaoSerial comunicacao) {
		this.comunicacao = comunicacao;
	}
	public Vazao getVazao(int indice) {
		return vazoes.get(indice);
	}
	public float getAcumulado(int indice) {
		return vazoes.get(indice).getAcumulado();
	}

	public void updateVazoes() {
		float[][] Vvazoes = comunicacao.getFlows();
		if (Vvazoes == null)
			return;
		for (int i=0;i<Nmedidores;i++){
			vazoes.get(i).setInstAcumulado(Vvazoes[i][0], Vvazoes[i][1]);
//			System.out.println("VAzao e acumulado");
//			System.out.println(vazoes.get(i).getInstantaneo());
//			System.out.println(vazoes.get(i).getAcumulado());
		}
	}
	public class Vazao{
		private float instantaneo;
		private float acumulado;
		private int Nsensor;

		public Vazao(int Nsensor){
			this.Nsensor = Nsensor;
		}

		public float getInstantaneo() {
			return instantaneo;
		}

		public float getAcumulado() {
			return acumulado;
		}

		public int getNsensor(){
			return Nsensor;
		}
		public void setInstantaneo(float instantaneo) {
			this.instantaneo = instantaneo;
		}
		public void setAcumulado(float acumulado) {
			this.acumulado = acumulado;
		}
		public void setInstAcumulado(float instantaneo, float acumulado) {
			this.instantaneo = instantaneo;
			this.acumulado = acumulado;
		}
		
		public void resetAcumulado() {
			try {
				comunicacao.resetAcumulado(Nsensor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class relogioUpdate extends TimerTask{


		@Override
		public void run() {
			// TODO Auto-generated method stub
			updateVazoes();


	}

	}
}


