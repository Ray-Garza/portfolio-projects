
package tic_tac_toe;

import javax.swing.JLabel;
import java.lang.Math;


public class Bot {
    public int[] jugada(JLabel lbs[][]){
        
        int mejor_Jugada[] = new int[2];
        int menor=0, filaMin=0, columnaMin=0, mejor=0;
        int centinela=0;
        //Arreglo donde se guardará el valor minimo o máximo que tiene cada jugada.
        int min[][] = new int[3][3];
        int max[][] = new int[3][3];
        int prom[][] = new int[3][3];
        
        //Se inicializan los valores a 0
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                min[x][y]=0;
                max[x][y]=0;
            }
        }
        
        //Se recorren los labels para buscar jugadas disponibles
        for(int i = 0;i<3;i++){
            for(int j=0;j<3;j++){
                //Se encuentra una jugada disponible y se procede a calcular su valor
                if(lbs[i][j].getText().equals(" ")){
                    min[i][j] = valor_Minimo(i,j,lbs);
                    max[i][j] = valor_Maximo(i,j,lbs);
 
                }
            }
        }
        //Se busca si existe una jugada ganadora
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(lbs[i][j].getText().equals(" ")){
                    if(max[i][j]>=10){
                        mejor_Jugada[0] = i;
                        mejor_Jugada[1] = j;
                        centinela=1;
                    }
                }
            }
        }
        //Se busca si el rival tiene una jugada ganadora
        if(centinela==0){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(lbs[i][j].getText().equals(" ")){
                        if(min[i][j]<=-10){
                            mejor_Jugada[0] = i;
                            mejor_Jugada[1] = j;
                            centinela=1;
                        }
                    }
                }
            }
        }
        //Se encuentra la mejor jugada
        if(centinela==0){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(lbs[i][j].getText().equals(" ")){
                        prom[i][j] = Math.abs(max[i][j]-min[i][j]);
                        if(prom[i][j]>= mejor){
                            mejor=prom[i][j];

                            mejor_Jugada[0]=i;
                            mejor_Jugada[1]=j;
                        }
                    }
                }
            }
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                System.out.println("("+i+", "+j+")"+" valorMin: "+min[i][j]);
                System.out.println("("+i+", "+j+")"+" valorMax: "+max[i][j]);
                System.out.println("("+i+", "+j+")"+" valorProm: "+prom[i][j]);
            }
        }
        System.out.println(mejor_Jugada[0]+","+mejor_Jugada[1]);
        return mejor_Jugada;
    }
    
    
    public int valor_Minimo(int i, int j, JLabel lbs[][]){
        int valor=0;
        int auxCol = siguiente(i);
        int auxFil = siguiente(j);
        int auxDiagIzq = siguiente(i);
        int auxDiagDer1 = siguiente(i);
        int auxDiagDer2 = anterior(j);
       
        //Se comprueba la columna
        if(!(lbs[auxCol][j].getText().equals("O") || "O".equals(lbs[siguiente(auxCol)][j].getText()))){
            valor--;
        }
        if(lbs[auxCol][j].getText().equals("X") && " ".equals(lbs[siguiente(auxCol)][j].getText())){
            valor--;
        }else if(lbs[auxCol][j].getText().equals(" ") && "X".equals(lbs[siguiente(auxCol)][j].getText())){
            valor--;
        }else if(lbs[auxCol][j].getText().equals("X") && "X".equals(lbs[siguiente(auxCol)][j].getText())){
            valor+=-10;
        }
        
        //Se comprueba la fila
        if(!(lbs[i][auxFil].getText().equals("O") || lbs[i][siguiente(auxFil)].getText().equals("O"))){
            valor--;
        }
        if(lbs[i][auxFil].getText().equals("X") && lbs[i][siguiente(auxFil)].getText().equals(" ")){
            valor--;
        }else if(lbs[i][auxFil].getText().equals(" ") && lbs[i][siguiente(auxFil)].getText().equals("X")){
            valor--;
        }if(lbs[i][auxFil].getText().equals("X") && lbs[i][siguiente(auxFil)].getText().equals("X")){
            valor+=-10;
        }
        
        //Se comprueba la diagonal izquierda
        if(i==j){
            if(!(lbs[auxDiagIzq][auxDiagIzq].getText().equals("O") || lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("O"))){
                valor--;
            }
            if(lbs[auxDiagIzq][auxDiagIzq].getText().equals("X") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals(" ")){
                valor--;
            }else if(lbs[auxDiagIzq][auxDiagIzq].getText().equals(" ") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("X")){
                valor--;
            }else if(lbs[auxDiagIzq][auxDiagIzq].getText().equals("X") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("X")){
                valor+=-10;
            }
        }
        
        //Se comprueba la diagonal derecha
        if((i+j) == 2){
            if(!(lbs[auxDiagDer1][auxDiagDer2].getText().equals("O") || lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("O"))){
                valor--;
            }
            if(lbs[auxDiagDer1][auxDiagDer2].getText().equals("X") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals(" ")){
                valor--;
            }else if(lbs[auxDiagDer1][auxDiagDer2].getText().equals(" ") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("X")){
                valor--;
            }else if(lbs[auxDiagDer1][auxDiagDer2].getText().equals("X") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("X")){
                valor+=-10;
            }
        }

        return valor;
    }
    public int valor_Maximo(int i, int j, JLabel lbs[][]){
        int valor=0;
        
       
        //Se comprueba la columna
        int auxCol = siguiente(i);
        if(!(lbs[auxCol][j].getText().equals("X") || "X".equals(lbs[siguiente(auxCol)][j].getText()))){
            valor++;
        }
        if(lbs[auxCol][j].getText().equals(" ") && "O".equals(lbs[siguiente(auxCol)][j].getText())){
            valor++;
        }else if(lbs[auxCol][j].getText().equals("O") && " ".equals(lbs[siguiente(auxCol)][j].getText())){
            valor++;
        }else if(lbs[auxCol][j].getText().equals("O") && "O".equals(lbs[siguiente(auxCol)][j].getText())){
            valor+=10;
        }
        
        //Se comprueba la fila
        int auxFil = siguiente(j);
        if(!(lbs[i][auxFil].getText().equals("X") || lbs[i][siguiente(auxFil)].getText().equals("X"))){
            valor++;
        }
        if(lbs[i][auxFil].getText().equals(" ") && lbs[i][siguiente(auxFil)].getText().equals("O")){
            valor++;
        }else if(lbs[i][auxFil].getText().equals("O") && lbs[i][siguiente(auxFil)].getText().equals(" ")){
            valor++;
        }else if(lbs[i][auxFil].getText().equals("O") && lbs[i][siguiente(auxFil)].getText().equals("O")){
            valor+=10;
        }
        
        //Se comprueba la diagonal izquierda
        if(i==j){
            int auxDiagIzq= siguiente(i);
            if(!(lbs[auxDiagIzq][auxDiagIzq].getText().equals("X") || lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("X"))){
                valor++;
            }if(lbs[auxDiagIzq][auxDiagIzq].getText().equals(" ") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("O")){
                valor++;
            }else if(lbs[auxDiagIzq][auxDiagIzq].getText().equals("O") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals(" ")){
                valor++;
            }else if(lbs[auxDiagIzq][auxDiagIzq].getText().equals("O") && lbs[siguiente(auxDiagIzq)][siguiente(auxDiagIzq)].getText().equals("O")){
                valor+=10;
            }
        }
        //Se comprueba la diagonal derecha
        if((i+j) == 2){
            int auxDiagDer1 = siguiente(i);
            int auxDiagDer2 = anterior(j);
            if(!(lbs[auxDiagDer1][auxDiagDer2].getText().equals("X") || lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("X"))){
                valor++;
            }if(lbs[auxDiagDer1][auxDiagDer2].getText().equals(" ") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("O")){
                valor++;
            }else if(lbs[auxDiagDer1][auxDiagDer2].getText().equals("O") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals(" ")){
                valor++;
            }else if(lbs[auxDiagDer1][auxDiagDer2].getText().equals("O") && lbs[siguiente(auxDiagDer1)][anterior(auxDiagDer2)].getText().equals("O")){
                valor+=10;
            }
        }
        
        
        return valor;
    }
    
    public int siguiente(int a){
        a++;
        if(a==3){
            return 0;
        }if(a==4){
            return 1;
        }else{
            return a;
        }
        
    }
    
    public int anterior(int a){
        a = siguiente(siguiente(a));
        return a;
    }
}

