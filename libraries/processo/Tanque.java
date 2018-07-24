package processo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import comunicacaoJava.ComunicacaoSerial;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Rectangle;
import processo.Resistencias.Resistencia;
import processo.Temperaturas.Temperatura;
import processo.Vazoes.Vazao;

public class Tanque {

	private int numero;
	private float level;
        private float calibracaoLevel = 20;
	private Temperatura temperatura;
	private Vazao vazaoFill, vazaoDrain;
	private int tempoAquecimento, tempoDecorridoAquecimento;
	private Timer timer;
	private int valvulaEncher, valvulaSecar;
	private Resistencia resistencia;
	public PID pid = new PID(1,20,10,8,10000);
	private boolean Aquecendo = false;
	private boolean AquecimentoConcluido = false;
	private ArrayList<RampaAquecimento> rampa = new ArrayList<RampaAquecimento>();
	private int rampaAtual = 0;
	public int CapacidadeTanque = 40;
	private boolean atingiuTemperatura = false;
	private String msgStatus;
	
	ComunicacaoSerial comunicacao;// = new ComunicacaoTCP(ComunicacaoTCP.ip_default, ComunicacaoTCP.porta_default);
	Timer timerUpdate;
	private float setTemperaturaAtual;
	
	public Tanque(Vazao vazaoFill, Vazao vazaoDrain,Temperatura temperatura, int valvulaEncher, int valvulaSecar, int numeroTQ) {
		this.vazaoFill = vazaoFill;
		this.vazaoDrain = vazaoDrain;
		this.valvulaEncher = valvulaEncher;
		this.valvulaSecar = valvulaSecar;
		this.numero = numeroTQ;
		this.temperatura =  temperatura;
		//timerUpdate = new Timer();
		//timerUpdate.scheduleAtFixedRate(new relogioUpdate(), 2000, 5000);
	}
        public void setCalibracaoLevel(float fator){
            this.calibracaoLevel = fator;
        }
	public void setComunicacao(ComunicacaoSerial comunicacao) {
		this.comunicacao = comunicacao;
	}
	public String getMsgStatus() {
		return msgStatus;
	}
	public int getTempoDecorrido() {
		return tempoDecorridoAquecimento;
	}
	public void aquecer(int tempo, float temperatura){
            resistencia.ligar();
            tempoDecorridoAquecimento = 0;
            tempoAquecimento = tempo;
            timer = new Timer();
            timer.scheduleAtFixedRate(new relogio(),0, 1000);
            setTemperaturaAtual = temperatura;
                    atingiuTemperatura = false;
            msgStatus = "Aquecendo o Tanque a temperatura de "+ String.valueOf(temperatura)+ "por "+String.valueOf(tempo)+"segundos";
            pid.setSetPoint(temperatura);
            try {
                            comunicacao.sendPID(pid);
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
            alternarStatusAquecimento(true);

	}
	public void setPIdNumber(int n)
	{
		pid.nPID = n;
	}
	public void setResistencia(Resistencia resistencia) {
		this.resistencia = resistencia;
	}
	public void setCapacidade(int capacidade) {
		this.CapacidadeTanque = capacidade;
	}
	public double calcLevelGraphics() {
		double saida = (getLevelMedidorVazao() / (CapacidadeTanque*1.00));
		return saida;
	}
	private void alternarStatusAquecimento(boolean status){
		Aquecendo = status;
		AquecimentoConcluido = !status;
	}
	public void aquecer(RampaAquecimento rampaquecer){

		resistencia.ligar();
		tempoDecorridoAquecimento = 0;
		tempoAquecimento = rampaquecer.getTempo();
		pid.setSetPoint(rampaquecer.getTemperatura());
		setTemperaturaAtual = rampaquecer.getTemperatura();
		atingiuTemperatura = false;
		try {
			comunicacao.sendPID(pid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer = new Timer();

		timer.scheduleAtFixedRate(new RelogioRampa(),0, 1000);
		msgStatus = "Aquecendo o Tanque a temperatura de "+ String.valueOf(rampaquecer.getTemperatura())+ "por "+String.valueOf(rampaquecer.getTempo())+"segundos";
		alternarStatusAquecimento(true);
	}

	public void pararAquecer(){

		alternarStatusAquecimento(false);
		resistencia.desligar();
	}

	public int addRampaAquecimento(int tempo, float temperatura){

		RampaAquecimento novarampa = new RampaAquecimento(tempo,temperatura);

		rampa.add(novarampa);
		return rampa.size()-1;
	}

	public RampaAquecimento getRampa(int i){
		return rampa.get(i);
	}

	public void removeRampaAquecimento(int index){
		rampa.remove(index);
	}

	public boolean startRampaAquecimento(){

		rampaAtual = 0;

		if (rampa.isEmpty())
			return false;
		while (rampa.get(rampaAtual).isFinished()){
			rampaAtual++;
			if (rampaAtual >= rampa.size())
				return false;
		}
		aquecer(rampa.get(rampaAtual));
		return true;
	}

	public ArrayList<RampaAquecimento> getArrayRampaAquecimento(){
		return rampa;
	}

	public void drenar(){}

	public void encher(float volume){

		try {
			comunicacao.sendEncher(vazaoFill.getNsensor(), volume*calibracaoLevel, valvulaEncher);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public float getLevelMedidorVazao(){
		return vazaoFill.getAcumulado()-vazaoDrain.getAcumulado();
	}

	public float getLevel() {
		return vazaoFill.getAcumulado()/calibracaoLevel;
	}
	public int getTempoDecorridoAquecimento(){
		return tempoDecorridoAquecimento;
	}


	public float getTemperaturaAquecimentoAtual(){
		if (rampaAtual < rampa.size())
			return rampa.get(rampaAtual).getTemperatura();
		else
			return -1;
	}
	public int getTempoAquecimentoAtual(){
		if (rampaAtual < rampa.size())
			return tempoDecorridoAquecimento;
		else
			return -1;
	}

	public int getTempoAquecimentoTotal(){
		if (rampaAtual < rampa.size())
			return tempoAquecimento;
		else
			return -1;
	}
	public boolean Aquecendo(){
		return Aquecendo;
	}
	public boolean AquecimentoConcluido(){
		return AquecimentoConcluido;
	}


	public float getTemperatura() {
		//comunicacao.ge
		return temperatura.getTemperatura();
	}




	class relogio extends TimerTask{


		@Override
		public void run() {
			tempoDecorridoAquecimento++;
			if (tempoDecorridoAquecimento > tempoAquecimento)
			{
				pararAquecer();
				timer.cancel();
			}
		}


	}

	class RelogioRampa extends TimerTask{


		@Override
		public void run() {

			if (atingiuTemperatura) {
				tempoDecorridoAquecimento++;
				if (tempoDecorridoAquecimento >= tempoAquecimento)
				{
					timer.cancel();
					rampa.get(rampaAtual).finish();
					rampaAtual++;
					if (rampaAtual >= rampa.size())
						pararAquecer();
	
					else
						aquecer(rampa.get(rampaAtual));
	
				}
			}
			else
				atingiuTemperatura = (setTemperaturaAtual <= temperatura.getTemperatura());
		}
	}
		class relogioUpdate extends TimerTask{


			@Override
			public void run() {
				//Nivel
				float[] dados = {0};//comunicacao.getLevelTemperature(vazaoFill.getNsensor(), temperatura.getNsensor());
				//level = comunicacao.getLevel(sensorVazaoEncher);
				level = dados[0];
				temperatura.setTemperatura(dados[1]);


			}


		}



	public class RampaAquecimento{
		private final IntegerProperty tempo;
		private final FloatProperty temperatura;
		private final BooleanProperty finished;


		//private final IntegerProperty postalCode;

		public RampaAquecimento(int tempo, float temperatura){
			this.tempo = new SimpleIntegerProperty(tempo);
			this.temperatura = new SimpleFloatProperty(temperatura);
			this.finished = new SimpleBooleanProperty(false);
		}

		public RampaAquecimento(){
			this.tempo = new SimpleIntegerProperty(0);
			this.temperatura = new SimpleFloatProperty(0);
			this.finished = new SimpleBooleanProperty(false);
		}


		public void setTempo(int tempo){
			this.tempo.set(tempo);
		}
		public void setTemperatura(float temperatura){
			this.temperatura.set(temperatura);
		}

		public int getTempo(){
			return this.tempo.get();
		}
		public float getTemperatura(){
			return this.temperatura.get();
		}

		public boolean isFinished(){
			return this.finished.get();
		}
		public void finish(){
			this.finished.set(true);
		}

		public IntegerProperty tempoProperty() {
	        return this.tempo;
	    }
		public FloatProperty temperaturaProperty(){
			return this.temperatura;
		}
		public BooleanProperty finishedProperty(){
			return this.finished;
		}
	}



}
