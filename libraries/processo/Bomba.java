package processo;

import java.io.IOException;


import comunicacaoJava.ComunicacaoSerial;
import javafx.scene.paint.Color;
import processo.Resistencias.Resistencia;
import processo.Vazoes.Vazao;

public class Bomba{


	private float setVazao;
	private Vazao medidor;
	int potencia;
	char status;
	PID pid = new PID(4,13,5.5f,5.01f,0);
	ComunicacaoSerial comunicacao;
	
	public void setComunicacao(ComunicacaoSerial comunicacao) {
		this.comunicacao = comunicacao;
	}
	public void ligar()
	{
		try {
			comunicacao.sendPID(pid);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comunicacao.ligarBomba();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void desligar(){

		try {
			comunicacao.desligarBomba();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setMedidor(Vazao medidor){
		this.medidor = medidor;
	}
	public void setVazao(float setVazao){
		this.setVazao = setVazao;
		pid.setSetPoint(setVazao);
		try {
			comunicacao.sendPID(pid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateStatus() {
		int[] stat = comunicacao.getStatusBomba();
		if (stat == null)
			return;
		status = (char) stat[0];
		potencia = stat[1];
	}

	public char getStatus() {
		return status;
	}
	public int getPotencia() {
		return potencia;
	}

	public Color getColorStatus() {

		if (status == Resistencia.LIGADA)
			return Color.GREEN;
		else
			return Color.RED;

	}

}
