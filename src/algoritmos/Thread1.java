/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmos;
/**
 *
 * @author Edson García
 */
public class Thread1 extends Thread{
    
    boolean eventFlag = Main.gui.getEventFlag();
    
    int dekkerNo = 0;
    int turno = 0;
    
    public void Dekker(int No){
        dekkerNo = No;
        turno = Main.gui.getTurno();
        Main.gui.setEventFlag(true);
        eventFlag = true;
    }
    
    @Override
    public void run() {
       while(true && !this.isInterrupted()) {
           while (eventFlag){
               switch(dekkerNo){
                   case 1:
                       dekker1();
                       break;
                   case 2:
                       dekker2();
                       break;
                   case 3:
                       dekker3();
                       break;
                   case 4:
                       dekker4();
                       break;
                   case 5:
                       dekker5();
                       break;
                   default:
                       break;
               }
               eventFlag = Main.gui.getEventFlag();
           }
       }
    }
    
    private void dekker1(){
            // Hace cosas
            hacerCosas("", 2);
            // Espera a que la región crítica se desocupe
            while (Main.gui.getTurno() == 1) {
                esperar();
            }
            // Accede a Región Crítica
            regionCritica(3);
            // Hace mas cosas
            hacerCosas("mas", 3);
            Main.gui.setTurno(1);
    }
    
    private void dekker2(){
        hacerCosas("", 2);
        Main.gui.setP1qe(true);
        while(Main.gui.getP2qe()){
            esperar();
        }
        regionCritica(3);
        Main.gui.setP1qe(false);
        hacerCosas("mas", 2);
    }
    
    private void dekker3(){
        hacerCosas("", 2);
        while(Main.gui.getP2qe()){
            esperar();
        }
        Main.gui.setP1qe(true);
        regionCritica(3);
        Main.gui.setP1qe(false);
        hacerCosas("mas", 3);
    }
    
    private void dekker4(){
        hacerCosas("", 2);
        Main.gui.setP1qe(true);
        while(Main.gui.getP2qe()){
            Main.gui.setP1qe(false);
            try{
                esperar();
                sleep(((int)((Math.random() * 2) + 2)) * 1000);
            }catch(InterruptedException e){}
            Main.gui.setP1qe(true);
        }
        regionCritica(3);
        Main.gui.setP1qe(false);
        hacerCosas("mas", 3);
    }
    
    private void dekker5(){
        hacerCosas("", 3);
        Main.gui.setP1qe(true);
        while(Main.gui.getP2qe()){
            if(Main.gui.getTurno() == 2){
                Main.gui.setP1qe(false);
                try{
                    esperar();
                    sleep(((int)((Math.random() * 2) + 2)) * 1000);
                }catch(InterruptedException e){}
                Main.gui.setP1qe(true);
            }
        }
        regionCritica(4);
        Main.gui.setTurno(2);
        Main.gui.setP1qe(false);
        hacerCosas("mas", 3);
    }
    
    private void hacerCosas(String s, int time){
        try{
            Main.gui.setVisibleProgressBarP1("show");
            Main.gui.updateStatusP1("Haciendo " + s + " cosas...");
            Main.gui.setOperationProgressBarP1("working");
            sleep(time * 1000);
        }catch(InterruptedException e){}
    }
    
    private void regionCritica(int time){
        try{
            Main.gui.setVisibleProgressBarP1("show");
            Main.gui.setOperationProgressBarP1("bussy");
            Main.gui.updateStatusP1("Usando región crítica...");
            //sleep((int)((Math.random() * 2) + 2) * 1000);
            sleep(time * 1000);
        }catch(InterruptedException e){}
    }
    
    private void esperar(){
        Main.gui.updateStatusP1("Esperando");
        Main.gui.setOperationProgressBarP1("wait");
        Main.gui.setVisibleProgressBarP1("show");
    }   
}
