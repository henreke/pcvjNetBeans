package comunicacaoJava;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import processo.PID;
import processo.Resistencias.Resistencia;

/**
 * Created by henreke on 7/24/2017.
 */

public class ComunicacaoSerial {

    

    
	String porta;
	SerialPort serialPort;
	InputStream in;
	OutputStream out;
	public static String valvulas, temperaturas, bombas, resistencias,vazoes;
    public void setPortaCOM(String porta){
        this.porta = porta;
    }
    public String getPortaCOM(){
        return this.porta;
    }
    public void conectar() throws Exception
    {
    	String portName = porta;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                               
                
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    public boolean desconectar() throws IOException{
        //conexao.close();
		return true;
    }
    public ComunicacaoSerial(String  porta)
    {
        this.porta = porta;
    }

    public void sendMessage(String msg) throws IOException {
        
        if (out != null) {
            String msg2 ='$'+msg+'$';
            out.write(msg2.getBytes());
        }
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



    public void sendEncher(int Nsensor, float quantidade, int Nvalvula) throws IOException{
    	String msgenvio = TipoMSG.ENCHERTANQUE+"#"+quantidade+"#"+Nvalvula +"#"+Nsensor+"#";
    	sendMessage(msgenvio);
    }
    
    public void resetAcumulado(int Nsensor) throws IOException {
    	String msg = TipoMSG.RESET_ACUMULADO+"#"+Nsensor+"#";
    	sendMessage(msg);
    }



    public String getStatusValvulas() {

    	if (valvulas == null)
    		return "";
    	if (valvulas.length() < 2)
			return "";
		if (valvulas.charAt(0) == '$' && valvulas.charAt(valvulas.length() -1) =='$') {
			valvulas =  valvulas.substring(2, valvulas.length()-1);
			return valvulas;
		}
    	return "";
    }

    public float[][] getFlows() {
    	
    	String retorno = vazoes;
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


    	return null;
    }
    public float[] getTemperaturas(){
    	
    	if (temperaturas == null)
    		return null;
    	String retorno = temperaturas;
    	 
		if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
			
			retorno = retorno.substring(2, retorno.length()-2);
			String[] valores = retorno.split("#");
			float[] valoresretorno = new float[valores.length];
			for (int i=0; i< valoresretorno.length;i++)
				valoresretorno[i] = Float.parseFloat(valores[i]);

			return valoresretorno;
		}
    	return null;
    }
    
    public int[] getResistencias() {
    	
    	if (resistencias == null)
    			return null;
    	String retorno = resistencias;
		if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
			retorno = retorno.substring(2, retorno.length()-2);
			String[] valores = retorno.split("#");
			int[] valoresretorno = new int[valores.length];
			for (int i=0; i< valoresretorno.length;i++)
				valoresretorno[i] = Integer.parseInt(valores[i]);

			return valoresretorno;
		}
    	
    	return null;
    }
    public int[] getStatusBomba() {
    	
    	if (bombas ==null)
    		return null;
    	String retorno = bombas;
    	
		if (retorno.charAt(0)=='$' && retorno.charAt(retorno.length() - 1)=='$') {
			retorno = retorno.substring(2, retorno.length()-2);
			String[] valores = retorno.split("#");
			int[] valoresretorno = new int[valores.length];
			for (int i=0; i< valoresretorno.length;i++)
				valoresretorno[i] = Integer.parseInt(valores[i]);

			return valoresretorno;
		}
    	return null;
    }
    
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                String recebido = new String(buffer,0,len);
                System.out.println(recebido);
                String[] array_recebido = recebido.split(">");
                if (array_recebido.length < 5)
                	return;
                valvulas = array_recebido[1];
                temperaturas = array_recebido[2];
                bombas = array_recebido[3];
                resistencias = array_recebido[4];
                vazoes = array_recebido[5];
                //System.out.println("Novo");
                //for (int i=0;i<array_recebido.length;i++)
                //	System.out.println(array_recebido[i]);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
   
}
