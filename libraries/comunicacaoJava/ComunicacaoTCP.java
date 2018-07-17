package comunicacaoJava;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import processo.PID;
import processo.Resistencias.Resistencia;

/**
 * Created by henreke on 7/24/2017.
 */

public class ComunicacaoTCP {

    Socket conexao;
    OutputStream canal;
    private String ip;
    private int porta;

    public static String ip_default = "192.168.25.177";
    public static int porta_default = 1188;

    public void setIp(String ip){
        this.ip = ip;
    }
    public String getIp(){
        return this.ip;
    }

    public void setPorta(int porta){
        this.porta = porta;
    }
    public int getPorta(){
        return this.porta;
    }
    public OutputStream conectar(){

        try {
            //if (conexao == null){
                conexao = new Socket(this.ip,this.porta);

               // conexao.connect(conexao.getRemoteSocketAddress(), 3000);
          //  }
            return conexao.getOutputStream();
        } catch (IOException e) {
            return null;
        }
    }
    public boolean desconectar(){
        try {
            conexao.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public ComunicacaoTCP (String ip, int porta)
    {
        this.ip = ip;
        this.porta = porta;
    }

    public void sendMessage(String msg) throws IOException {
        canal = conectar();
        if (canal != null) {
            String msg2 ='$'+msg+'$';
            canal.write(msg2.getBytes());
            desconectar();
        }
    }

    public String sendMessageUpdate(String msg) throws IOException {
        canal = conectar();
        String resposta ="";
        if (canal != null) {

            String msg2 ="$"+msg+"$";
            canal.write(msg2.getBytes());
            InputStream aws = conexao.getInputStream();
            byte[] respostaB = new byte[50];

            try {
    			Thread.sleep(20);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            aws.read(respostaB);
            resposta = new String(respostaB,StandardCharsets.UTF_8);

            desconectar();
        }

        return resposta.trim();
    }

    public void abrirValvula(int nValvula) throws IOException {
            String msg = TipoMSG.VALVULA+"#" + String.valueOf(nValvula)+"#" + String.valueOf(Comandos.ABRIR)+"#";
            sendMessage(msg);
    }

    public void fecharValvula(int nValvula) throws IOException {
        String msg = TipoMSG.VALVULA+"#" + String.valueOf(nValvula)+"#" + String.valueOf(Comandos.FECHAR)+"#";
        sendMessage(msg);
    }
    
    public void ligarResistencia(char nResistencia) throws IOException {
    	String msg =  TipoMSG.RESISTENCIA+"#"+nResistencia+"#"+Resistencia.LIGADA+"#";
    	sendMessage(msg);
    }
    
    public void desligarResistencia(char nResistencia) throws IOException {
    	String msg =  TipoMSG.RESISTENCIA+"#"+nResistencia+"#"+Resistencia.DESLIGADA+"#";
    	sendMessage(msg);
    }

    public void ligarBomba() throws IOException {
    	String msg = TipoMSG.BOMBA+"#"+Comandos.LIGAR+"#";
    	sendMessage(msg);
    }

    public void desligarBomba() throws IOException {
    	String msg = TipoMSG.BOMBA +"#"+Comandos.DESLIGAR+"#";
    	sendMessage(msg);
    }
    public void sendPID(PID pid) throws IOException {
        String msg = TipoMSG.PID+"#"+String.valueOf(pid.setPoint) +"#"
                +String.valueOf(pid.nPID)+"#"+String.valueOf(pid.P)
                +"#"+String.valueOf(pid.I)+"#"+String.valueOf(pid.D)+"#";
        sendMessage(msg);
    }

    public String getUpdate(int tipo, String msg) throws IOException
    {
    	String msgenvio = TipoMSG.UPDATE+"#"+msg;
    	return sendMessageUpdate(msgenvio);
    }

    public void sendEncher(int Nsensor, float quantidade, int Nvalvula) throws IOException{
    	String msgenvio = TipoMSG.ENCHERTANQUE+"#"+quantidade+"#"+Nvalvula +"#"+Nsensor+"#";
    	sendMessage(msgenvio);
    }
    
    public void resetAcumulado(int Nsensor) throws IOException {
    	String msg = TipoMSG.RESET_ACUMULADO+"#"+Nsensor+"#";
    	sendMessage(msg);
    }

    public float getLevel(int Nsensor) {

    	String msg =TipoMSG.UPDATE+"#"+ TipoUpdate.LEVEL+"#"+Nsensor+"#";
    	try {
			String retorno = sendMessageUpdate(msg);
			if (retorno.charAt(0) == '$' && retorno.charAt(retorno.length() -1) =='$') {
				retorno =  retorno.substring(1, retorno.length()-1);
				String[] valores = retorno.split("#");
				if ( Integer.parseInt(valores[0]) == Nsensor)
				{
					return Float.parseFloat(valores[1]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0f;
		}
    	return 0.0f;
    }

    public float[] getLevelTemperature(int NsensorLevel, int NsensorTemperatura) {

    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.LEVEL_TEMPERATURE+"#"+NsensorLevel+"#"+NsensorTemperatura+"#";
    	float[] saida = new float[2];
    	try {
			String retorno = sendMessageUpdate(msg);
			System.out.println(retorno);
			if (retorno.charAt(0) == '$' && retorno.charAt(retorno.length() -1) =='$') {
				retorno =  retorno.substring(1, retorno.length()-1);
				String[] valores =  retorno.split("#");
				if (Integer.parseInt(valores[0])==NsensorLevel)
					saida[0] = Float.parseFloat(valores[1]);
				if (Integer.parseInt(valores[2]) == NsensorTemperatura)
					saida[1] = Float.parseFloat(valores[3]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return saida;
    }

    public String getStatusValvulas(int[] valvulas) {

    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.VALVULAS+"#"+valvulas.length;
    	for (int i=0;i<valvulas.length;i++) {
    		msg+="#"+valvulas[i];
    	}
    	msg+="#";

    	try {
			String retorno = sendMessageUpdate(msg);
			System.out.println(retorno);
			if (retorno.length() < 2)
				return "";
			if (retorno.charAt(0) == '$' && retorno.charAt(retorno.length() -1) =='$') {
				retorno =  retorno.substring(2, retorno.length()-1);
				return retorno;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    	return "";
    }

    public float[][] getFlows() {
    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.FLOW+"#";
    	try {
    		String retorno = sendMessageUpdate(msg);
    		if (retorno.length() > 1) {
	    		if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
	    			retorno = retorno.substring(2, retorno.length()-2);
	    			String[] valores = retorno.split("#");
	    			float[][] valoresretorno = new float[valores.length/2][2];
	    			for (int i =0; i< valores.length/2; i++) {
	    				valoresretorno[i][0] = Float.parseFloat(valores[2*i]);
	    				valoresretorno[i][1] = Float.parseFloat(valores[2*i+1]);
	    			}
	    			return valoresretorno;
	    		}
    		}
    	}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


    	return null;
    }
    public float[] getTemperaturas(){
    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.TEMPERATURES+"#";
    	try {
			String retorno = sendMessageUpdate(msg);
			if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {

				retorno = retorno.substring(2, retorno.length()-2);
				String[] valores = retorno.split("#");
				float[] valoresretorno = new float[valores.length];
				for (int i=0; i< valoresretorno.length;i++)
					valoresretorno[i] = Float.parseFloat(valores[i]);

				return valoresretorno;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return null;
    }
    
    public int[] getResistencias() {
    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.UPDATE_STATUS_RESISTENCIAS+"#";
    	
    	try {
			String retorno = sendMessageUpdate(msg);
			if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
				retorno = retorno.substring(2, retorno.length()-2);
				String[] valores = retorno.split("#");
				int[] valoresretorno = new int[valores.length];
				for (int i=0; i< valoresretorno.length;i++)
					valoresretorno[i] = Integer.parseInt(valores[i]);

				return valoresretorno;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    	return null;
    }
    public int[] getStatusBomba() {
    	String msg = TipoMSG.UPDATE+"#"+TipoUpdate.UPDATE_STATUS_BOMBA+"#";
    	
    	try {
			String retorno = sendMessageUpdate(msg);
			if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
				retorno = retorno.substring(2, retorno.length()-2);
				String[] valores = retorno.split("#");
				int[] valoresretorno = new int[valores.length];
				for (int i=0; i< valoresretorno.length;i++)
					valoresretorno[i] = Integer.parseInt(valores[i]);

				return valoresretorno;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return null;
    }
    
    
   
}
class TipoMSG{

    public static final char VALVULA = 10;
    public static final char PID = 11;
    public static final char BOMBA = 12;
    public static final char ENCHERTANQUE = 14;
    public static final char UPDATE = 16;
    public static final char RESISTENCIA = 17;
    public static final char RESET_ACUMULADO = 18;
}
class TipoUpdate{
	public static final char LEVEL = 33;
	public static final char FLOW = 36;
	public static final char LEVEL_TEMPERATURE = 38;
	public static final char VALVULAS = 39;
	public static final char TEMPERATURES = 40;
	public static final char UPDATE_STATUS_BOMBA = 41;
	public static final char UPDATE_STATUS_RESISTENCIAS = 42;
}
class Comandos{

    public static final char ABRIR = 'A';
    public static final char FECHAR = 'F';
    public static final char LIGAR = 'L';
    public static final char DESLIGAR = 'D';

}




