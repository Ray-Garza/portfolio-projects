/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodomontante;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author raymu
 */
public class Paso extends javax.swing.JFrame {


    int dimension=0;
    Double[][] Matriz1;
    Double[][] Matriz2;
    Double pivoteAnterior=1.0;
    
    
   DefaultTableModel modelo = new DefaultTableModel();
   
    public Paso() {
        initComponents();
    }
    
    public Paso(Double[][] matriz2, Double[][] matriz1, int dimension1 ) {
        initComponents();
        setLocationRelativeTo(null);
        dimension = dimension1;
        modelo.setColumnCount(dimension*2);
        modelo.setRowCount(dimension);
        jTable1.setModel(modelo);
        jTable1.setEnabled(false);
        Matriz1 = new Double[dimension][dimension*2];        
        Matriz2 = new Double[dimension][dimension*2];
        Matriz1 = matriz1;
        Matriz2 = matriz2;
        
        System.out.println("Constructroll");
        
        //Se rellena la tabla con los valores iniciales
        int n = dimension;        
        
        
                                    
                for(int i=0;i<n;i++){
                    for(int j=0;j<(2*n);j++){
                        if(i == 0){
                            jNumerador.setText("");
                            jDenominador.setText("");                       
                            jTable1.setValueAt(Matriz1[i][j], i, j);
                        }else{                                                                                    
                            jTable1.setValueAt(Matriz1[i][j], i, j);
                        }
                    }
                }                
            
            
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jNumerador = new javax.swing.JLabel();
        jDenominador = new javax.swing.JLabel();
        jDenominador1 = new javax.swing.JLabel();
        jDenominador2 = new javax.swing.JLabel();
        jValor = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTransformacion = new javax.swing.JLabel();
        jCoordenada = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 204), 5, true));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jNumerador.setFont(new java.awt.Font("Lucida Console", 1, 14)); // NOI18N
        jNumerador.setForeground(new java.awt.Color(153, 0, 255));
        jNumerador.setText("jLabel1");

        jDenominador.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jDenominador.setForeground(new java.awt.Color(153, 0, 255));
        jDenominador.setText("jLabel1");

        jDenominador1.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jDenominador1.setForeground(new java.awt.Color(153, 0, 255));
        jDenominador1.setText("-------------");

        jDenominador2.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jDenominador2.setForeground(new java.awt.Color(153, 0, 255));
        jDenominador2.setText("=");

        jValor.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jValor.setForeground(new java.awt.Color(153, 0, 255));
        jValor.setText("-");

        jButton1.setText("Siguiente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTransformacion.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jTransformacion.setForeground(new java.awt.Color(153, 0, 255));
        jTransformacion.setText("Transformación 0:");

        jCoordenada.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        jCoordenada.setForeground(new java.awt.Color(153, 0, 255));
        jCoordenada.setText("Coordenada: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 387, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jDenominador))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jNumerador))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDenominador1)
                                .addGap(34, 34, 34)
                                .addComponent(jDenominador2)
                                .addGap(18, 18, 18)
                                .addComponent(jValor))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTransformacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCoordenada)))))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTransformacion)
                    .addComponent(jCoordenada))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addGap(47, 47, 47)
                .addComponent(jNumerador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDenominador1)
                    .addComponent(jDenominador2)
                    .addComponent(jValor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDenominador)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
int i=0;
int j=0;
int contador=0;
double aux2=0;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTransformacion.setText("Transformación "+(contador+1)+":");

        Double[] aux;
        aux = new Double[5];
        double prueba=0;
        int n = dimension;

        if(j<(2*n)){
                    if(i<contador &&  i==j){
                        Matriz2[i][j] = Matriz1[contador][contador];
                        jNumerador.setText("");
                        jDenominador.setText("");
                        jValor.setText(String.valueOf(Matriz2[i][j]));
                        jTable1.setValueAt(Matriz2[i][j], i, j);
                    }else if(i == contador){
                        Matriz2[i][j] = Matriz1[i][j];
                        jNumerador.setText("");
                        jDenominador.setText("");

                        jValor.setText(String.valueOf(Matriz2[i][j]));
                        jTable1.setValueAt(Matriz2[i][j], i, j);
                    }else{
                        aux2 = Matriz1[contador][contador];
                        System.out.println(Matriz1[contador][contador]+" "+Matriz1[i][j]+" "+ Matriz1[i][contador] + Matriz1[contador][j]);
                        Matriz2[i][j] = (aux2 * Matriz1[i][j] - Matriz1[i][contador] * Matriz1[contador][j]) / pivoteAnterior;  
                        System.out.println(Matriz2[i][j]);
                        jNumerador.setText(Matriz1[contador][contador] +" * "+Matriz1[i][j]+" - " + Matriz1[i][contador] + " * "+Matriz1[contador][j]);
                        jDenominador.setText(String.valueOf(pivoteAnterior));
                        jValor.setText(String.valueOf(Matriz2[i][j]));
                        jTable1.setValueAt(Matriz2[i][j], i, j);
                    }
                    jCoordenada.setText("Coordenada: "+i+", "+j);
                    j++;
        }else if(i<n-1){
            j=0;
            i++;
            System.out.println(i + " " + j + " " + contador);
        }else{
            System.out.println("XD");
            for(int i = 0; i < n; i++){
                for(int j = 0; j<(2*n); j++){
                    Matriz1[i][j] = Matriz2[i][j];
                }                
            }
            pivoteAnterior = Matriz2[contador][contador];

            if(contador==n-1){
                JOptionPane.showMessageDialog(null, "El proceso ha finalizado, el determinante es: "+Matriz2[0][0]+" y la transpuesta se encuentra a partir de la columna "+(n)+"\n Para conseguir las respuestas solo resta hacer el producto del vector de términos independientes por la transpuesta y dividir sobre el determinante. ");
            }else{
                i=0;
                j=0;
                contador++;
            }
        }
        
        
        /*Esto no es importante, lo hice con mis lágrimas
        int n = dimension;
        new Thread(new Runnable(){
            public void run(){
                if(stop!=dimension){
                    for(int h=0;h<n;h++){
                        for(int g=0;g<(2*n);g++){
                            z=h;
                            w=g;
                            if(z == contador){
                                Matriz2[z][w] = Matriz1[z][w];
                                
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Paso.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                SwingUtilities.invokeLater(new Runnable(){
                                    @Override
                                    public void run(){
                                        teodiojava++;
                                        if(teodiojava==1){
                                            jNumerador.setText("");
                                            jDenominador.setText("");
                                            System.out.println(z+" "+w + " iguales");
                                            jValor.setText(String.valueOf(Matriz2[z][w]));
                                            jTable1.setValueAt(Matriz2[z][w], z, w);
                                        }
                                        
                                    }
                                });
                                teodiojava=0;
                                
                            }else{
                                Matriz2[z][w] = ((Matriz1[contador][contador] * Matriz1[z][w]) - (Matriz1[z][contador] * Matriz1[contador][w])) / pivoteAnterior;
                                System.out.println("1-  Matriz1[contador][contador]: "+Matriz1[contador][contador]+"Matriz1[z][w] "+Matriz1[z][w] +" w:"+w+ " z:" + z);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Paso.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                SwingUtilities.invokeLater(new Runnable(){
                                    @Override
                                    public void run(){
                                        teodiojava++;
                                        if(teodiojava==1){
                                            System.out.println("2-Matriz1[contador][contador]: "+Matriz1[contador][contador]+" Matriz1[z][w] "+Matriz1[z][w] + "Matriz1[z][contador]: "+Matriz1[z][contador]+ " Matriz1[contador][w]: "+Matriz1[contador][w]+ "w:"+w+ " z:" + z);
                                            jNumerador.setText(Matriz1[contador][contador] +" * "+Matriz1[z][w]+" - " + Matriz1[z][contador] + " * "+Matriz1[contador][w]);
                                            jDenominador.setText(String.valueOf(pivoteAnterior));
                                            jValor.setText(String.valueOf(Matriz2[z][w]));
                                            jTable1.setValueAt(Matriz2[z][w], z, w);
                                        }
                                    }
                                });
                                teodiojava=0;
                                
                            }
                            //Hace que el programa espere medio segundo
                            System.out.println("zawarudo");
                            
                            
                        }
                    }
                }

                for(int i = 0; i < n; i++){
                    for(int j = 0; j<(2*n); j++){
                        Matriz1[i][j] = Matriz2[i][j];
                    }                
                }
                pivoteAnterior = Matriz2[contador][contador];
                contador++;
                stop++;
                
            }}).start();
        */
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Paso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Paso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Paso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Paso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Paso().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jCoordenada;
    private javax.swing.JLabel jDenominador;
    private javax.swing.JLabel jDenominador1;
    private javax.swing.JLabel jDenominador2;
    private javax.swing.JLabel jNumerador;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel jTransformacion;
    private javax.swing.JLabel jValor;
    // End of variables declaration//GEN-END:variables
}
