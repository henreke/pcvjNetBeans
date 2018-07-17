package processo;

import java.io.IOException;
import java.util.ArrayList;

import comunicacaoJava.ComunicacaoSerial;
import javafx.scene.paint.Color;

public class Resistencias {
	
	
	int numeroResistencias =3;
	ArrayList<Resistencia> resistencias = new ArrayList<Resistencia>();
	ComunicacaoSerial comunicacao;
	
	public void setComunicacao(ComunicacaoSerial comunicacao) {
		this.comunicacao = comunicacao;
	}
	public Resistencias() {
		for (int i=0;i<numeroResistencias;i++)
			resistencias.add(new Resistencia((char)(Resistencia.R1+i)));
	}
	
	public Resistencias(int numeroResistencias) {
		this.numeroResistencias = numeroResistencias;
		for (int i=0;i<numeroResistencias;i++)
			resistencias.add(new Resistencia((char)(Resistencia.R1+i)));
	}
	
	public Resistencia getResistencia(int indice) {
		return resistencias.get(indice);
	}
	
	public void updateResistencias() {
		
		int[] estados =  comunicacao.getResistencias();		
		if (estados == null)
			return;
		if (estados.length != resistencias.size()*2)
			return;
				
		for (int i = 0; i< resistencias.size();i++) 
			resistencias.get(i).setPotenciaStatus(estados[2*i+1], (char)estados[2*i]);		
	}
	
	
	public class Resistencia{
		
		
		private char numero;
		private int potencia;
		private char status;
		
		public static final char LIGADA = 'L';
		public static final char DESLIGADA = 'D';
		public static final char R1 = '1', R2 = '2', R3 = '3';
		
		public Resistencia(char numero) {
			this.numero = numero;
		}
		
		public void setPotencia(int potencia) {
			this.potencia = potencia;
		}
		
		public void setStatus(char status) {
			this.status =  status;
		}
		
		public void setPotenciaStatus(int potencia, char status) {
			this.potencia = potencia;
			this.status = status;
		}
		public char getStatus() {
			return status;
		}
		public int getPotencia() {
			return potencia;
		}
		public int getNumero() {
			return numero;
		}
		
		public void ligar() {
			try {
				comunicacao.ligarResistencia(numero);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void desligar() {
			try {
				comunicacao.desligarResistencia(numero);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public Color getColorStatus() {
			if (status == Resistencia.LIGADA)
				return Color.RED;
			else
				return Color.BLUE;
		}
	}

}
