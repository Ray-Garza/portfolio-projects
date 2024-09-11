/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodomontante;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author raymu
 */
public class Solucion extends javax.swing.JDialog {

    int dimension2 = 0;
    
    Double[][] Matriz1;
    Double[][] Matriz2;
    Double[][] ArregloAux;
    Double[] independientes;
    Double[][] transpuesta;
    Double[][] inversa;
    Double[] solucion;
    String[] etiquetas;
    DefaultTableModel modelo1 = new DefaultTableModel();
    DefaultTableModel modelo2 = new DefaultTableModel();
    DefaultTableModel modelo3 = new DefaultTableModel();
    
    public Solucion(java.awt.Frame parent, boolean modal, Double[][] Arreglo, int dimension) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);            
        independientes = new Double[dimension];
        Matriz1 = new Double[dimension][dimension*2];        
        Matriz2 = new Double[dimension][dimension*2];
        ArregloAux = new Double[dimension][dimension+1];
        transpuesta = new Double[dimension][dimension];
        inversa = new Double[dimension][dimension];
        solucion = new Double[dimension];
        ArregloAux=Arreglo;
        dimension2=dimension;
        //Guardamos los valores de los coeficientes en Matriz1
        //Y los valores independientes en el arreglo independientes
        for(int q=0;q<dimension;q++){
            for(int w=0;w<dimension*2;w++){
                if(w<dimension){
                    Matriz1[q][w] = Arreglo[q][w];
                }else if(q+dimension==w){
                    Matriz1[q][w] = 1.0;
                }else{
                    Matriz1[q][w] = 0.0;
                }
            }
            independientes[q] = Arreglo[q][dimension];
        }
        /*
        Comprobación de los arreglos
        for(int q=0;q<dimension;q++){
            for(int w=0;w<dimension*2;w++){
               System.out.println(Matriz1[q][w]);
            }

        }*/
        
        //Método del montante
        int contador=0;
        int n = dimension;
        double determinante=0;
        double pivoteAnterior=1;
        while(contador < n){
            for(int i=0;i<n;i++){
                for(int j=0;j<(2*n);j++){
                    if(i == contador){
                        Matriz2[i][j] = Matriz1[i][j];
                    }else{
                        Matriz2[i][j] = ((Matriz1[contador][contador] * Matriz1[i][j]) - (Matriz1[i][contador] * Matriz1[contador][j])) / pivoteAnterior;
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
        }

        //Se guardan los valores en sus campos
        determinante=Matriz2[0][0];
        jLabel1.setText(String.valueOf("Determinante = "+determinante));
        //Se generan los modelos de las tablas
        try{    
            etiquetas = new String[2];
            double aux=0;
            
            modelo1.setColumnCount(dimension);
            modelo2.setColumnCount(dimension);
            modelo3.setColumnCount(2);
            modelo3.setRowCount(dimension);
            etiquetas[0] = "Variable";
            etiquetas[1] = "Solución";
            modelo3.setColumnIdentifiers(etiquetas);
            jTable3.setModel(modelo3);
            
            
            
            


            // Se pasan los valores de la transpuesta a su respectivo arreglo
            for(int i = 0;i<dimension;i++){
                for(int j=0;j<dimension;j++){
                    transpuesta[i][j]= Matriz2[i][j+dimension];
                    inversa[i][j] =  transpuesta[i][j] / determinante;
                    aux+= independientes[j] * inversa[i][j];
                    
                }
                
                solucion[i] = aux;                
                jTable3.setValueAt("X"+(i+1), i, 0);
                jTable3.setValueAt(Math.round(aux*1000.0)/1000.0, i, 1);
                aux=0;                
                modelo1.addRow(transpuesta[i]);
                modelo2.addRow( inversa[i]);     
            }                        
            jTable1.setModel(modelo1);
            jTable2.setModel(modelo2);            
            jTable1.setRowSelectionAllowed(false);
            jTable2.setRowSelectionAllowed(false);
            jTable3.setRowSelectionAllowed(false);
            
            
            

            
        }
        catch(Exception ex){
            
        }
        
        
        
        
        
    }
    public Solucion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSoluciones = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 255), 5, true));

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

        jLabel1.setFont(new java.awt.Font("Lucida Console", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 255));
        jLabel1.setText("Determinante: ");

        jLabel2.setFont(new java.awt.Font("Lucida Console", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 255));
        jLabel2.setText("Transpuesta:");

        jLabel3.setFont(new java.awt.Font("Lucida Console", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 255));
        jLabel3.setText("Inversa:");

        jSoluciones.setFont(new java.awt.Font("Lucida Console", 3, 18)); // NOI18N
        jSoluciones.setForeground(new java.awt.Color(153, 0, 255));
        jSoluciones.setText("Soluciones: ");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        jTable3.setFont(new java.awt.Font("Lucida Console", 1, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jButton1.setFont(new java.awt.Font("Lucida Console", 1, 14)); // NOI18N
        jButton1.setText("Paso a paso");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSoluciones)
                            .addComponent(jButton1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(94, 94, 94))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jSoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Generamos los arreglos nuevamente
        
        for(int q=0;q<dimension2;q++){
            for(int w=0;w<dimension2*2;w++){
                if(w<dimension2){
                    Matriz1[q][w] = ArregloAux[q][w];
                }else if(q+dimension2==w){
                    Matriz1[q][w] = 1.0;
                }else{
                    Matriz1[q][w] = 0.0;
                }
            }
            independientes[q] = ArregloAux[q][dimension2];
        }
        
        new Paso(Matriz2,Matriz1,dimension2).setVisible(true);
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
            java.util.logging.Logger.getLogger(Solucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Solucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Solucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Solucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Solucion dialog = new Solucion(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel jSoluciones;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
