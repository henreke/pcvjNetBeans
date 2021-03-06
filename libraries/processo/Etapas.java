package processo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import processo.Valvulas.Valvula;
import processo.Vazoes.Vazao;

public class Etapas{




	public Etapa etapacorrente;
	

	public void teste(){
		Etapa1 tesste = new Etapa1();

	etapacorrente = tesste;

	}

	public void iniciaEtapa1(Tanque HLT, float volumeEncher){
		etapacorrente = new Etapa1();
		((Etapa1)etapacorrente).setTanqueHLT(HLT);
		((Etapa1)etapacorrente).setVolumeEncher(volumeEncher);
		etapacorrente.executarEtapa();
	}
	
	public void iniciaEtapa2(Tanque HLT, float temperaturaAquecer) {
		
		etapacorrente = new Etapa2();
		((Etapa2)etapacorrente).setHLT(HLT);
		((Etapa2)etapacorrente).setTemperaturaAquecer(temperaturaAquecer);
		etapacorrente.executarEtapa();
		
	}
	
	public void iniciaEtapa3(Tanque HLT, Tanque MLT, float volumeTransferir) {
		etapacorrente = new Etapa3();
		((Etapa3)etapacorrente).setTanques(HLT, MLT);
		((Etapa3)etapacorrente).setVolumeTransferirMLT(volumeTransferir);
		etapacorrente.executarEtapa();
	}

	public void iniciaEtapa4(Tanque HLT, Tanque MLT, float volumeTransferir, Valvula valvulaSuccao, Valvula valvulaDescarga, Bomba bomba, Vazao medidorVazao,float setVazao) {
		
		etapacorrente = new Etapa4();
		((Etapa4)etapacorrente).setTanques(HLT, MLT);
		((Etapa4)etapacorrente).setValvulas(valvulaSuccao, valvulaDescarga);
		((Etapa4)etapacorrente).setBomba(bomba);
		((Etapa4)etapacorrente).setSetVazao(setVazao);
		((Etapa4)etapacorrente).setMedidorVazao(medidorVazao);
		((Etapa4)etapacorrente).setVolumeTransferir(volumeTransferir);
		etapacorrente.executarEtapa();
		
	}
        public void iniciaEtapa5A(Tanque MLT, Tanque BK, float volumeTransferir,Bomba bomba, Vazao medidorBK,float setVazao,
                Valvula saidaMLT, Valvula entradaBK){
            etapacorrente = new Etapa5A();
            ((Etapa5A)etapacorrente).setBomba(bomba);
            ((Etapa5A)etapacorrente).setTanques(MLT, BK);
            ((Etapa5A)etapacorrente).setMedidorVazao(medidorBK);
            ((Etapa5A)etapacorrente).setValvula(saidaMLT, entradaBK);
            ((Etapa5A)etapacorrente).setVazao(setVazao);
            etapacorrente.executarEtapa();
        }
        public void iniciaEtapa5B(Tanque HLT,Tanque MLT, Tanque BK, float volumeTransferir,Bomba bomba, Vazao medidorBK, Vazao medidorHLT,
                Valvula saidaHLT,Valvula saidaMLT, Valvula entradaBK){
            etapacorrente = new Etapa5B();
            ((Etapa5B)etapacorrente).setBomba(bomba);
            ((Etapa5B)etapacorrente).setTanques(HLT,MLT, BK);
            ((Etapa5B)etapacorrente).setMedidorVazao(medidorBK, medidorHLT);
            ((Etapa5B)etapacorrente).setValvula(saidaMLT, entradaBK, saidaHLT);
            etapacorrente.executarEtapa();
        }
        public void iniciaEtapa6(Tanque BK, float temperaturaAquecer, float tempoMinutos){
            etapacorrente = new Etapa6();
            ((Etapa6)etapacorrente).setBK(BK);
            ((Etapa6)etapacorrente).setTemperaturaAquecer(temperaturaAquecer);
            ((Etapa6)etapacorrente).setTempoFervura(tempoMinutos);
            etapacorrente.executarEtapa();
        }
        public void iniciaEtapa7(Tanque BK,Valvula saidaBK, Valvula entradaBK, Bomba bomba, float tempo,float setVazao, Vazao medidorBK){
            etapacorrente = new Etapa7();
            ((Etapa7)etapacorrente).setTanques(BK);
            ((Etapa7)etapacorrente).setMedidorVazao(medidorBK);
            ((Etapa7)etapacorrente).setValvulas(saidaBK, entradaBK);
            ((Etapa7)etapacorrente).setTempo(tempo);
            ((Etapa7)etapacorrente).setBomba(bomba);
            ((Etapa7)etapacorrente).setVazao(setVazao);
            etapacorrente.executarEtapa();
        }
	public class Etapa{

		public int numero;
		public boolean concluida;
		public boolean executando;

		public boolean verificarConcluida(){

			return concluida;
		}

		public boolean verificarExecutando(){
			return executando;
		}

		public void executarEtapa(){

		}

	}

	public class Etapa1 extends Etapa{


		private Tanque HLT;
		private float volumeEncher;


		public void setTanqueHLT(Tanque HLT){
			this.HLT = HLT;
		}

		public void setVolumeEncher(float volumeEncher){
			this.volumeEncher = volumeEncher;
		}
                @Override
		public void executarEtapa(){

			executando = true;
			concluida = false;
			HLT.encher(volumeEncher);


		}
                @Override
		public boolean verificarConcluida(){

			if (HLT.getLevel() >= volumeEncher){
				executando = false;
				concluida = true;
			}

			return concluida;
		}

	}

	public class Etapa2 extends Etapa{

		private Tanque HLT;
		private float temperaturaAquecer;

		public void setTemperaturaAquecer(float temperaturaAquecer){
			this.temperaturaAquecer = temperaturaAquecer;
		}

		public void setHLT(Tanque HLT){
			this.HLT = HLT;
		}

                @Override
		public void executarEtapa(){
			HLT.addRampaAquecimento(0, temperaturaAquecer);
			HLT.aquecer(HLT.getRampa(0));
			executando = true;
			concluida = false;
		}
                @Override
		public boolean verificarConcluida(){
			if (HLT.getTemperatura() >= temperaturaAquecer){
				executando = false;
				concluida = true;
                                
			}

			return concluida;
		}

	}

	public class Etapa3 extends Etapa{
		private Tanque HLT, MLT;
		private float volumeTransferir;
		public void setTanques(Tanque HLT,Tanque MLT){
			this.HLT = HLT;
			this.MLT = MLT;
		}

		public void setVolumeTransferirMLT(float volume){
			this.volumeTransferir = volume;
		}

		public void executarEtapa(){
			executando = true;
			concluida = false;
			MLT.encher(volumeTransferir);

		}

		public boolean verificarConcluida(){
			if (MLT.getLevelMedidorVazao() >= volumeTransferir){
				executando = false;
				concluida = true;
			}
			return concluida;
		}
	}

	public class Etapa4 extends Etapa{
		private Tanque HLT, MLT;
		private float volumeTransferir;
		private Valvula valvulaSuccao, valvulaDescarga;
		private Bomba bomba;
		private Vazao medidorVazao;
		private float setVazao;

		public void setTanques(Tanque HLT,Tanque MLT){
			this.HLT = HLT;
			this.MLT = MLT;
		}

		public void setValvulas(Valvula valvulaSuccao, Valvula valvulaDescarga){
			this.valvulaSuccao = valvulaSuccao;
			this.valvulaDescarga = valvulaDescarga;
		}

		public void setBomba(Bomba bomba){
			this.bomba = bomba;
		}

		public void setMedidorVazao(Vazao medidorVazao){
			this.medidorVazao = medidorVazao;
		}

		public void setSetVazao(float setVazao){
			this.setVazao = setVazao;
		}
		
		public void setVolumeTransferir(float volumeTransferir) {
			this.volumeTransferir = volumeTransferir;
		}

		public void executarEtapa(){
			executando = true;
			concluida = false;
			valvulaSuccao.abrir();
			valvulaDescarga.abrir();

			//Aguardar succao abrir
			//while(valvulaSuccao.isClosed());

			bomba.setMedidor(medidorVazao);
			bomba.setVazao(setVazao);
			bomba.ligar();
                        MLT.addRampaAquecimento(0, 30);
                        MLT.addRampaAquecimento(0, 33);
			MLT.startRampaAquecimento();



		}

		public boolean verificarConcluida(){
			executando =  !MLT.AquecimentoConcluido();
			concluida = MLT.AquecimentoConcluido();
			if (concluida) {
				bomba.desligar();
				valvulaDescarga.fechar();
				valvulaSuccao.fechar();
			}
			return concluida;
		}

	}
	public class Etapa5A extends Etapa{
		private Tanque MLT, BK;
		private Bomba bomba;
		private Vazao medidorBK, medidorHLT;
		private Valvula saidaMLT, entradaBK;
		private float setVazao, volumeAserTransferido;
		public void setTanques(Tanque MLT, Tanque BK) {
			this.MLT = MLT;
			this.BK = BK;
                       
		}
		public void setBomba(Bomba bomba) {
			this.bomba = bomba;
		}

		public void setMedidorVazao(Vazao medidorBK) {
			this.medidorBK = medidorBK;
		}
                public void setMedidorHLT(Vazao medidorHLT){
                    this.medidorHLT = medidorHLT;
                }
                public void setVolumeAserTransferido(float volumeAserTransferido){
                    this.volumeAserTransferido = volumeAserTransferido;
                }
                public void setValvula( Valvula saidaMLT, Valvula entradaBK){
                   
                    this.saidaMLT = saidaMLT;
                    this.entradaBK = entradaBK;
                }
                public void setVazao(float setVazao){
                    this.setVazao = setVazao;
                }
                @Override
		public void executarEtapa() {
                    try {
                        executando = true;
                        concluida = false;
                        saidaMLT.abrir();
                        Thread.sleep(500);
                        entradaBK.abrir();
                        Thread.sleep(500);
                        //saidaHLT.abrir();
                        //Thread.sleep(500);
                        //while(saidaMLT.isClosed());
                        //medidorBK.resetAcumulado();
                        
                        bomba.setMedidor(medidorBK);
                        bomba.setVazao(setVazao);
                        Thread.sleep(500);
                        bomba.ligar();
                        Thread.sleep(500);
                        volumeAserTransferido+=BK.getLevel();
                        BK.encher(volumeAserTransferido);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Etapas.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
                @Override
		public boolean verificarConcluida() {
                        
                    if (BK.getLevel()>= volumeAserTransferido) {
                        bomba.desligar();
                        executando = false;
                        concluida = true;
                    }
                    
                        
                    return concluida;
		}

	}

	public class Etapa5B extends Etapa{
		private Tanque HLT, MLT, BK;
		private Bomba bomba;
		private Vazao medidorBK, medidorHLT;
		private Valvula saidaMLT, entradaBK,saidaHLT;
		private float volumeAserTransferido1, volumeInicialHLT = 0;
		public void setTanques(Tanque HLT, Tanque MLT, Tanque BK) {
			this.HLT = HLT;
			this.MLT = MLT;
			this.BK = BK;
		}
		public void setBomba(Bomba bomba) {
			this.bomba = bomba;
		}
                public void setValvula(Valvula saidaMLT, Valvula entradaBK, Valvula saidaHLT){
                    this.saidaHLT = saidaHLT;
                    this.saidaMLT = saidaMLT;
                    this.entradaBK = entradaBK;
                }
		public void setMedidorVazao(Vazao medidorBK, Vazao medidorHLT) {
			this.medidorBK = medidorBK;
			this.medidorHLT = medidorHLT;
		}
                public void setVolumeASerTransferido(float volumeASerTransferido){
                    this.volumeAserTransferido1 = volumeASerTransferido;
                }
                @Override
		public void executarEtapa() {
                    try {
                        executando = true;
                        concluida = false;
                        volumeInicialHLT = HLT.getLevel();
                        volumeAserTransferido1 = volumeInicialHLT - volumeAserTransferido1;
                        saidaMLT.abrir();
                        Thread.sleep(500);
                        entradaBK.abrir();
                        Thread.sleep(500);
                        saidaHLT.abrir();
                        Thread.sleep(1000);
                        bomba.setMedidor(medidorBK);
                        bomba.setVazao(10);
                        Thread.sleep(500);
                        bomba.ligar();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Etapas.class.getName()).log(Level.SEVERE, null, ex);
                    }


		}
                   @Override
                   public boolean verificarConcluida() {
                        
                    if (HLT.getLevel()<= volumeAserTransferido1) {
                        try {
                            bomba.desligar();
                            Thread.sleep(500);
                            saidaHLT.fechar();
                            executando = false;
                            concluida = true;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Etapas.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else
                        bomba.setVazao(medidorHLT.getInstantaneo());
                    
                        
                    return concluida;
		}
	}

	public class Etapa6 extends Etapa{

		private Tanque BK;
		private float temperaturaAquecer;
		private int tempoFervura;
		private boolean tempAtigida;
		private long tempoInicial = 0;
		public void setTemperaturaAquecer(float temperaturaAquecer){
			this.temperaturaAquecer = temperaturaAquecer;
		}

		public void setTempoFervura(float tempoFervura) {
			this.tempoFervura = (int)tempoFervura*1000;
		}

		public void setBK(Tanque BK){
			this.BK = BK;
		}
                @Override
		public void executarEtapa(){
			BK.addRampaAquecimento(tempoFervura, temperaturaAquecer);
                        BK.startRampaAquecimento();
			executando = true;
			concluida = false;
			tempAtigida = false;
		}
                @Override
		public boolean verificarConcluida(){
			if ((BK.getTemperatura() >= temperaturaAquecer) || (tempAtigida)){
				if (!tempAtigida) {
					tempoInicial = System.currentTimeMillis();
					tempAtigida = true;
				}

				if ((System.currentTimeMillis() - tempoInicial) >= tempoFervura) {
					executando = false;
					concluida = true;
				}
			}

			return concluida;
		}

	}
	public class Etapa7 extends Etapa{
		private Tanque BK;
		private Bomba bomba;
		private Vazao medidorBK;
		private Valvula saidaBk, entradaBK;
		private float setVazao = 8;
		private long tempo=180, tempoInical = 0;
		public void setTanques(Tanque BK) {

			this.BK = BK;
		}
		public void setBomba(Bomba bomba) {
			this.bomba = bomba;
		}
		public void setValvulas(Valvula saidaBK, Valvula entradaBK) {
			this.saidaBk = saidaBK;
			this.entradaBK = entradaBK;
		}
		public void setMedidorVazao(Vazao medidorBK) {
			this.medidorBK = medidorBK;
		}

		public void setTempo(float tempo) {
			this.tempo = (long)tempo*1000;
		}
                public void setVazao(float setVazao){
                    this.setVazao =  setVazao;
                }
                @Override
		public void executarEtapa() {
                    try {
                        executando = true;
                        concluida = false;
                        saidaBk.abrir();
                        Thread.sleep(500);
                        entradaBK.abrir();
                        Thread.sleep(500);
                        bomba.setMedidor(medidorBK);
                        Thread.sleep(500);
                        bomba.setVazao(setVazao);
                        Thread.sleep(500);
                        tempoInical = System.currentTimeMillis();
                        bomba.ligar();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Etapas.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
                @Override
		public boolean verificarConcluida() {
			if ((System.currentTimeMillis()-tempoInical) >= tempo) {
                            try {
                                executando = false;
                                concluida = true;
                                
                                bomba.desligar();
                                Thread.sleep(500);
                                saidaBk.fechar();
                                Thread.sleep(500);
                                entradaBK.fechar();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Etapas.class.getName()).log(Level.SEVERE, null, ex);
                            }
			}
			return concluida;
		}

	}

	public class Etapa8 extends Etapa{
		private Tanque BK;
		private Bomba bomba;
		private Vazao medidorBK, medidorChiller;
		private Valvula saidaBk, entradaChiller, entradaAguaChiller;
		private float nivelMinimo;
		private float setVazao;
		public void setTanques(Tanque BK) {
			this.BK = BK;
		}
		public void setBomba(Bomba bomba) {
			this.bomba = bomba;
		}
		public void setValvulas(Valvula saidaBK, Valvula entradaChiller, Valvula entradaAguaChiller) {
			this.saidaBk = saidaBK;
			this.entradaChiller = entradaChiller;
			this.entradaAguaChiller = entradaAguaChiller;
		}
		public void setMedidorVazao(Vazao medidorBK, Vazao medidorChiller) {
			this.medidorBK = medidorBK;
			this.medidorChiller = medidorChiller;
		}


		public void executarEtapa() {
			executando = true;
			concluida = false;
			saidaBk.abrir();
			entradaChiller.abrir();
			entradaAguaChiller.abrir();
			while(saidaBk.isClosed());
			medidorBK.resetAcumulado();
			medidorChiller.resetAcumulado();
			bomba.setMedidor(medidorBK);
			bomba.setVazao(setVazao);
			bomba.ligar();
		}

		public boolean verificarConcluida() {
			if (BK.getLevelMedidorVazao() <= nivelMinimo) {
				executando = false;
				concluida = true;
				bomba.desligar();
				entradaAguaChiller.fechar();
				entradaChiller.fechar();
				saidaBk.fechar();
			}
			return concluida;
		}

	}

}
