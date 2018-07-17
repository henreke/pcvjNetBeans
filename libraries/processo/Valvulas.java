package processo;
import java.io.IOException;
import java.util.ArrayList;

import comunicacaoJava.ComunicacaoSerial;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Valvulas {

	public static char ABERTA = 68;
	public static char ABRINDO = 65;
	public static char FECHADA = 69;
	public static char FECHANDO = 66;
	public static char MOTORIZADA = 10;
	public static char SOLENOIDE = 11;

	private ArrayList<Valvula> valvulas = new ArrayList<Valvula>();
	//ComunicacaoTCP comunicacao = new ComunicacaoTCP(ComunicacaoTCP.ip_default, ComunicacaoTCP.porta_default);
	ComunicacaoSerial comunicacao = new ComunicacaoSerial(Util.Configuracoes.portaSerial);


	public void addValvula(int numero,int tipo, Rectangle atuador, Polygon corpo1, Polygon corpo2, Line linha) {

		valvulas.add(new Valvula(numero,tipo,atuador,corpo1,corpo2,linha));
	}
	public void setComunicacao(ComunicacaoSerial comunicacao)
	{
		this.comunicacao = comunicacao;
	}
	public int getStatusValvula(int indice) {
		return valvulas.get(indice).getStatus();
	}
	public Valvula getValvula(int indice){
		return valvulas.get(indice);
	}
	public void updateStatus() {

		int[] valvulasstatus = new int[valvulas.size()];
		for (int i=0;i<valvulas.size();i++)
			valvulasstatus[i] = valvulas.get(i).getNumero();

		String Sstatus = comunicacao.getStatusValvulas();
		if (Sstatus.length()<2)
			return;
		//System.out.println(Sstatus);
		String[] statusdivido = Sstatus.split("#");
		for (int i=0; i<statusdivido.length; i++) {
				valvulas.get(i).setStatus(statusdivido[i].charAt(0));
				valvulas.get(i).checkStatus();
		}

	}



public class Valvula {

	private int status;
	private int numero;
	private int tipo;
	Rectangle atuador;
	Polygon corpo1,corpo2;
	Line linha;
	public Valvula(int numero,int tipo, Rectangle atuador, Polygon corpo1, Polygon corpo2, Line linha) {
		this.numero = numero;
		this.atuador = atuador;
		this.corpo1 = corpo1;
		this.corpo2 = corpo2;
		this.linha = linha;
		this.tipo = tipo;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	public int getNumero() {
		return numero;
	}
	public void abrir() {
		atuador.setFill(Color.GREEN);
		linha.setFill(Color.GREEN);
		try {
			comunicacao.abrirValvula(numero);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fechar() {
		atuador.setFill(Color.RED);
		linha.setFill(Color.RED);
		try {
			comunicacao.fecharValvula(numero);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void checkStatus() {
		if ((status == Valvulas.ABERTA) ||(tipo == Valvulas.SOLENOIDE && status == Valvulas.ABRINDO)) {
			//System.out.println("Aberta");
			corpo1.setFill(Color.GREEN);
			corpo2.setFill(Color.GREEN);
			atuador.setFill(Color.GREEN);
			linha.setStroke(Color.GREEN);
		} else if ((status == Valvulas.FECHADA) || (tipo == Valvulas.SOLENOIDE && status == Valvulas.FECHANDO)) {
			corpo1.setFill(Color.RED);
			corpo2.setFill(Color.RED);
			atuador.setFill(Color.RED);
			linha.setStroke(Color.RED);
		}
	}

	public boolean isClosed(){

		if (tipo == Valvulas.MOTORIZADA)
			return status == Valvulas.FECHADA;
		else if (tipo == Valvulas.SOLENOIDE)
			return status == Valvulas.FECHANDO;

		return false;
	}

	public boolean isOpen(){
		if (tipo == Valvulas.MOTORIZADA)
			return status == Valvulas.ABERTA;
		else if (tipo == Valvulas.SOLENOIDE)
			return status == Valvulas.ABRINDO;

		return false;
	}



}
}