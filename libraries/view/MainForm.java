/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.SwingConstants;
import comunicacaoJava.ComunicacaoSerial;
import java.awt.Color;
import processo.*;
import java.util.TimerTask;
import java.util.Timer;
import processo.Resistencias.Resistencia;

/**
 *
 * @author henre
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        //tq1.setOrientation(SwingConstants.VERTICAL);
       // tq1.setSize(40, 40);
        tq3View.setValue(50);
        inicializarPlanta();
    }
    
    //Codigo de COntrole da cerveja
    //========================================================================
    
    ComunicacaoSerial comunicacao = new ComunicacaoSerial(Util.Configuracoes.portaSerial);
    Tanque HLT, MLT, BK;
    Temperaturas temperaturas;
    Vazoes vazoes;
    Resistencias resistencias;
    Timer timerUpdate;
    Bomba bomba;
    Valvulas valvulas;
    Etapas etapas;
    private void inicializarPlanta(){
        
        try {
            comunicacao.conectar();
	} catch (Exception e) {
	
            e.printStackTrace();
	}
        //=======Variaveis de processo==============
        resistencias = new Resistencias(3);
	resistencias.setComunicacao(comunicacao);
        temperaturas = new Temperaturas(3);
        temperaturas.setComunicacao(comunicacao);
        vazoes = new Vazoes();
        vazoes.setComunicacao(comunicacao);
        bomba = new Bomba();
	bomba.setComunicacao(comunicacao);
        valvulas = new Valvulas();
	valvulas.setComunicacao(comunicacao);
        adicionarValvulas();
        etapas = new Etapas();
        //==========HLT=============
        HLT = new Tanque(vazoes.getVazao(0),vazoes.getVazao(1),temperaturas.getTemperatura(0),0,1,1);
        HLT.setComunicacao(comunicacao);
        HLT.setResistencia(resistencias.getResistencia(0));
        //=========MLT==============
        MLT = new Tanque(vazoes.getVazao(1),vazoes.getVazao(2),temperaturas.getTemperatura(1),1,2,2);
        MLT.setResistencia(resistencias.getResistencia(1));
        MLT.setPIdNumber(2);
	MLT.setComunicacao(comunicacao);
        
        //==========BK==============
        BK = new Tanque(vazoes.getVazao(2),vazoes.getVazao(2),temperaturas.getTemperatura(2),5,6,3);
	BK.setResistencia(resistencias.getResistencia(2));
	BK.setPIdNumber(3);
	BK.setComunicacao(comunicacao);
        
        //=====Timer======
        timerUpdate = new Timer();
	timerUpdate.scheduleAtFixedRate(new RelogioUpdate(), 2000, 1000);
        
    }
    
    private void adicionarValvulas() {
		valvulas.addValvula(0, Valvulas.SOLENOIDE, valvula0);
		valvulas.addValvula(1, Valvulas.MOTORIZADA, valvula1);
		valvulas.addValvula(2, Valvulas.MOTORIZADA, valvula2);
		valvulas.addValvula(3, Valvulas.MOTORIZADA, valvula3);
		valvulas.addValvula(4, Valvulas.MOTORIZADA, valvula4);
		valvulas.addValvula(5, Valvulas.MOTORIZADA, valvula5);
		valvulas.addValvula(6, Valvulas.MOTORIZADA, valvula6);
		valvulas.addValvula(7, Valvulas.MOTORIZADA, valvula7);
		//valvulas.addValvula(8, Valvulas.SOLENOIDE, valvula8);
	}
    
    class RelogioUpdate extends TimerTask{


		@Override
		public void run() {
			
			try {
			valvulas.updateStatus();

			vazoes.updateVazoes();
			//System.out.println("Updating");
			temperaturas.updateTemperaturas();
			resistencias.updateResistencias();
			bomba.updateStatus();
			txtVolTq1.setText(String.valueOf(HLT.getLevel())+"l");
                        tq1View.setValue( (int) (HLT.getLevel()*2));
			txtTempTq1.setText(String.valueOf(HLT.getTemperatura())+"oC");
                        txtVolTq2.setText(String.valueOf(MLT.getLevel())+"l");
                        tq2View.setValue((int) (MLT.getLevel()*2));
			txtTempTq2.setText(String.valueOf(temperaturas.getTemperatura(1).getTemperatura())+"oC");
                        txtVolTq3.setText(String.valueOf(BK.getLevel())+"l");
                        tq3View.setValue((int) (BK.getLevel()*2));
			txtTempTq3.setText(String.valueOf(temperaturas.getTemperatura(2).getTemperatura())+"oC");
			txtvazao1.setText(String.valueOf(vazoes.getVazao(0).getInstantaneo())+"l/m");
			txtvazao2.setText(String.valueOf(vazoes.getVazao(1).getInstantaneo())+"l/m");
			
                        if (resistencias.getResistencia(0).getStatus()== Resistencia.LIGADA)
                            resistencia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOn.png")));
                        else
                            resistencia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png")));
                        
                        if (resistencias.getResistencia(1).getStatus()== Resistencia.LIGADA)
                            resistencia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOn.png")));
                        else
                            resistencia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png")));
                        
                        if (resistencias.getResistencia(2).getStatus()== Resistencia.LIGADA)
                            resistencia3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOn.png")));
                        else
                            resistencia3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png")));
                        
			

			//valorresistencia1.setText(String.valueOf((resistencias.getResistencia(0).getPotencia()/255)*100));
			//valorresistencia2.setText(String.valueOf((resistencias.getResistencia(1).getPotencia()/255)*100));
			//valorresistencia3.setText(String.valueOf((resistencias.getResistencia(2).getPotencia()/255)*100));
			if (bomba.getStatus() == Resistencia.LIGADA)
                            pump.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/pumpOn.png")));
                        else
                            pump.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/pumpOff.png")));

			//if (etapas.etapacorrente != null)
			//	etapas.etapacorrente.verificarExecutando();
			
			//Status do sistema
			//if (HLT.Aquecendo()) {
			//	statusAquecimento.setText(HLT.getMsgStatus());
			//	tempo.setText(String.valueOf(HLT.getTempoDecorrido()));
			
			//}

			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}


	}

    ///=======================================================================

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelProcesso = new javax.swing.JPanel();
        resistencia1 = new javax.swing.JLabel();
        resistencia2 = new javax.swing.JLabel();
        resistencia3 = new javax.swing.JLabel();
        tq3View = new javax.swing.JProgressBar();
        tq1View = new javax.swing.JProgressBar();
        tq2View = new javax.swing.JProgressBar();
        panelVariaveisTq1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtVolTq1 = new javax.swing.JTextField();
        txtTempTq1 = new javax.swing.JTextField();
        valvula6 = new javax.swing.JLabel();
        valvula3 = new javax.swing.JLabel();
        valvula0 = new javax.swing.JLabel();
        valvula1 = new javax.swing.JLabel();
        valvula5 = new javax.swing.JLabel();
        valvula2 = new javax.swing.JLabel();
        valvula4 = new javax.swing.JLabel();
        valvula7 = new javax.swing.JLabel();
        pump = new javax.swing.JLabel();
        txtvazao2 = new javax.swing.JTextField();
        txtvazao1 = new javax.swing.JTextField();
        panelVariaveisTq2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtVolTq2 = new javax.swing.JTextField();
        txtTempTq2 = new javax.swing.JTextField();
        panelVariaveisTq3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtVolTq3 = new javax.swing.JTextField();
        txtTempTq3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtdebug = new javax.swing.JTextField();
        btEtapa1 = new javax.swing.JButton();
        btFinaliza = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cerveja");
        setPreferredSize(new java.awt.Dimension(800, 480));
        setSize(new java.awt.Dimension(800, 480));

        panelProcesso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resistencia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png"))); // NOI18N
        panelProcesso.add(resistencia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 150, -1, -1));

        resistencia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png"))); // NOI18N
        panelProcesso.add(resistencia2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, -1, -1));

        resistencia3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/resistenciaOff.png"))); // NOI18N
        panelProcesso.add(resistencia3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 360, -1, -1));

        tq3View.setOrientation(1);
        tq3View.setValue(40);
        panelProcesso.add(tq3View, new org.netbeans.lib.awtextra.AbsoluteConstraints(569, 242, 103, 134));

        tq1View.setOrientation(1);
        tq1View.setValue(30);
        panelProcesso.add(tq1View, new org.netbeans.lib.awtextra.AbsoluteConstraints(323, 30, 103, 134));

        tq2View.setOrientation(1);
        tq2View.setValue(20);
        panelProcesso.add(tq2View, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 244, 103, 134));

        panelVariaveisTq1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Volume:");

        jLabel2.setText("Temp:");

        txtVolTq1.setText("jTextField1");

        txtTempTq1.setText("jTextField2");

        javax.swing.GroupLayout panelVariaveisTq1Layout = new javax.swing.GroupLayout(panelVariaveisTq1);
        panelVariaveisTq1.setLayout(panelVariaveisTq1Layout);
        panelVariaveisTq1Layout.setHorizontalGroup(
            panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVariaveisTq1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18))
                    .addGroup(panelVariaveisTq1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)))
                .addGroup(panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtVolTq1, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(txtTempTq1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelVariaveisTq1Layout.setVerticalGroup(
            panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtVolTq1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelVariaveisTq1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTempTq1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProcesso.add(panelVariaveisTq1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 120, 70));

        valvula6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula6, new org.netbeans.lib.awtextra.AbsoluteConstraints(507, 209, 34, 60));

        valvula3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula3, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 211, 34, 60));

        valvula0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        valvula0.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                valvula0MouseClicked(evt);
            }
        });
        panelProcesso.add(valvula0, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, -5, 34, 60));

        valvula1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        valvula1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                valvula1MouseClicked(evt);
            }
        });
        panelProcesso.add(valvula1, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 159, 34, 60));

        valvula5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula5, new org.netbeans.lib.awtextra.AbsoluteConstraints(528, 136, 34, 60));

        valvula2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula2, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 311, 34, 60));

        valvula4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula4, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 294, 34, 60));

        valvula7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valvula7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/valveClosed.png"))); // NOI18N
        panelProcesso.add(valvula7, new org.netbeans.lib.awtextra.AbsoluteConstraints(521, 325, 34, 60));

        pump.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/pumpOff.png"))); // NOI18N
        panelProcesso.add(pump, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 327, -1, -1));

        txtvazao2.setFont(new java.awt.Font("Times New Roman", 0, 8)); // NOI18N
        txtvazao2.setText("15l/m");
        panelProcesso.add(txtvazao2, new org.netbeans.lib.awtextra.AbsoluteConstraints(415, 291, 30, -1));

        txtvazao1.setFont(new java.awt.Font("Times New Roman", 0, 8)); // NOI18N
        txtvazao1.setText("15l/m");
        panelProcesso.add(txtvazao1, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 156, 30, -1));

        panelVariaveisTq2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Volume:");

        jLabel5.setText("Temperatura:");

        txtVolTq2.setText("jTextField1");

        txtTempTq2.setText("jTextField2");

        javax.swing.GroupLayout panelVariaveisTq2Layout = new javax.swing.GroupLayout(panelVariaveisTq2);
        panelVariaveisTq2.setLayout(panelVariaveisTq2Layout);
        panelVariaveisTq2Layout.setHorizontalGroup(
            panelVariaveisTq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVariaveisTq2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(22, 22, 22)
                        .addComponent(txtVolTq2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelVariaveisTq2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTempTq2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelVariaveisTq2Layout.setVerticalGroup(
            panelVariaveisTq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtVolTq2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelVariaveisTq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTempTq2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProcesso.add(panelVariaveisTq2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 120, 70));

        panelVariaveisTq3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel6.setText("Volume:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel7.setText("Temperatura:");

        txtVolTq3.setFont(new java.awt.Font("Times New Roman", 0, 8)); // NOI18N
        txtVolTq3.setText("jTextField1");

        txtTempTq3.setFont(new java.awt.Font("Times New Roman", 0, 8)); // NOI18N
        txtTempTq3.setText("jTextField2");

        javax.swing.GroupLayout panelVariaveisTq3Layout = new javax.swing.GroupLayout(panelVariaveisTq3);
        panelVariaveisTq3.setLayout(panelVariaveisTq3Layout);
        panelVariaveisTq3Layout.setHorizontalGroup(
            panelVariaveisTq3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVariaveisTq3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(22, 22, 22)
                        .addComponent(txtVolTq3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelVariaveisTq3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTempTq3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelVariaveisTq3Layout.setVerticalGroup(
            panelVariaveisTq3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVariaveisTq3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVariaveisTq3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtVolTq3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelVariaveisTq3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTempTq3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelProcesso.add(panelVariaveisTq3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, 120, 70));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/fundo.png"))); // NOI18N
        panelProcesso.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 480));

        txtdebug.setText("jTextField1");

        btEtapa1.setText("Etapa 1");
        btEtapa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEtapa1ActionPerformed(evt);
            }
        });

        btFinaliza.setText("Finaliza");
        btFinaliza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFinalizaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btEtapa1)
                            .addGap(18, 18, 18)
                            .addComponent(btFinaliza))
                        .addComponent(txtdebug, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 336, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btEtapa1)
                    .addComponent(btFinaliza))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtdebug, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void valvula0MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_valvula0MouseClicked
        if (!valvulas.getValvula(0).isOpen())
             valvulas.getValvula(0).abrir();
        else
            valvulas.getValvula(0).fechar();
    }//GEN-LAST:event_valvula0MouseClicked

    private void valvula1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_valvula1MouseClicked
        if (!valvulas.getValvula(1).isOpen())
             valvulas.getValvula(1).abrir();
        else
            valvulas.getValvula(1).fechar();
    }//GEN-LAST:event_valvula1MouseClicked

    private void btEtapa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEtapa1ActionPerformed
        //etapas.iniciaEtapa1(HLT, 40);
        //etapas.iniciaEtapa2(HLT, 35);
        //etapas.iniciaEtapa3(HLT, MLT, 20);
        etapas.iniciaEtapa4(HLT, MLT, TOP_ALIGNMENT, valvulas.getValvula(2), valvulas.getValvula(3), bomba, vazoes.getVazao(2), 8);
    }//GEN-LAST:event_btEtapa1ActionPerformed

    private void btFinalizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFinalizaActionPerformed
        etapas.etapacorrente.verificarConcluida();
    }//GEN-LAST:event_btFinalizaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEtapa1;
    private javax.swing.JButton btFinaliza;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel panelProcesso;
    private javax.swing.JPanel panelVariaveisTq1;
    private javax.swing.JPanel panelVariaveisTq2;
    private javax.swing.JPanel panelVariaveisTq3;
    private javax.swing.JLabel pump;
    private javax.swing.JLabel resistencia1;
    private javax.swing.JLabel resistencia2;
    private javax.swing.JLabel resistencia3;
    private javax.swing.JProgressBar tq1View;
    private javax.swing.JProgressBar tq2View;
    private javax.swing.JProgressBar tq3View;
    private javax.swing.JTextField txtTempTq1;
    private javax.swing.JTextField txtTempTq2;
    private javax.swing.JTextField txtTempTq3;
    private javax.swing.JTextField txtVolTq1;
    private javax.swing.JTextField txtVolTq2;
    private javax.swing.JTextField txtVolTq3;
    private javax.swing.JTextField txtdebug;
    private javax.swing.JTextField txtvazao1;
    private javax.swing.JTextField txtvazao2;
    private javax.swing.JLabel valvula0;
    private javax.swing.JLabel valvula1;
    private javax.swing.JLabel valvula2;
    private javax.swing.JLabel valvula3;
    private javax.swing.JLabel valvula4;
    private javax.swing.JLabel valvula5;
    private javax.swing.JLabel valvula6;
    private javax.swing.JLabel valvula7;
    // End of variables declaration//GEN-END:variables
}
