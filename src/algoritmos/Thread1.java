package algoritmos;

/**
 * Clase Thread1: Implementa un hilo (thread) que utiliza la solución de 
 * exclusión mutua de Dekker para coordinar acceso a una región crítica. 
 * Es parte de una interfaz gráfica controlada por la clase `Main.gui`.
 * 
 * @author Edson García
 */
public class Thread1 extends Thread {
    // Indicador de evento que determina si el hilo debe realizar operaciones
    boolean eventFlag = Main.gui.getEventFlag();
    // Variables para el algoritmo de Dekker
    int dekkerNo = 0; // Número que identifica el método de Dekker utilizado
    int turno = 0; // Indica cuál hilo tiene permiso para usar la región crítica
    /**
     * Configura las variables para el algoritmo de Dekker según el número proporcionado.
     * @param No Número que indica el método de Dekker que se ejecutará.
     */
    public void Dekker(int No) {
        dekkerNo = No; // Asigna el número del método de Dekker
        turno = Main.gui.getTurno(); // Obtiene el turno actual desde la interfaz
        Main.gui.setEventFlag(true); // Indica que hay un evento activo
        eventFlag = true; // Actualiza el estado del evento en la clase
    }

    @Override
    public void run() {
        // Ciclo infinito que realiza las operaciones mientras no se interrumpa el hilo
        while (true && !this.isInterrupted()) {
            // Ejecución condicionada al estado del indicador de eventos
            while (eventFlag) {
                // Selección del método de Dekker según el número configurado
                switch (dekkerNo) {
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
                        break; // No hay método asociado para números desconocidos
                }
                // Actualiza el estado del indicador de eventos desde la interfaz gráfica
                eventFlag = Main.gui.getEventFlag();
            }
        }
    }

    /**
     * Método Dekker1: Primera implementación del algoritmo de exclusión mutua.
     * Se asegura de esperar su turno antes de acceder a la región crítica.
     */
    private void dekker1() {
        hacerCosas("", 2); // Realiza operaciones previas
        while (Main.gui.getTurno() == 1) { // Espera su turno
            esperar();
        }
        regionCritica(3); // Accede a la región crítica
        hacerCosas("más", 3); // Realiza operaciones posteriores
        Main.gui.setTurno(1); // Indica que terminó y actualiza el turno
    }

    /**
     * Método Dekker2: Variante del algoritmo que utiliza indicadores booleanos (P1qe y P2qe).
     */
    private void dekker2() {
        hacerCosas("", 2); // Realiza operaciones previas
        Main.gui.setP1qe(true); // Indica que quiere entrar a la región crítica
        while (Main.gui.getP2qe()) { // Espera mientras el otro hilo esté queriendo entrar
            esperar();
        }
        regionCritica(3); // Accede a la región crítica
        Main.gui.setP1qe(false); // Indica que ha terminado
        hacerCosas("más", 2); // Realiza operaciones posteriores
    }

    /**
     * Método Dekker3: Similar a Dekker2 pero con orden diferente de operaciones.
     */
    private void dekker3() {
        hacerCosas("", 2); // Realiza operaciones previas
        while (Main.gui.getP2qe()) { // Espera mientras el otro hilo quiera entrar
            esperar();
        }
        Main.gui.setP1qe(true); // Indica intención de acceder
        regionCritica(3); // Accede a la región crítica
        Main.gui.setP1qe(false); // Indica que ha terminado
        hacerCosas("más", 3); // Realiza operaciones posteriores
    }

    /**
     * Método Dekker4: Implementa espera activa con tiempos aleatorios.
     */
    private void dekker4() {
        hacerCosas("", 2); // Realiza operaciones previas
        Main.gui.setP1qe(true); // Indica que quiere entrar a la región crítica
        while (Main.gui.getP2qe()) { // Si el otro hilo quiere entrar:
            Main.gui.setP1qe(false); // Desactiva intención de entrar
            try {
                esperar(); // Muestra que está esperando
                sleep(((int) ((Math.random() * 2) + 2)) * 1000); // Espera un tiempo aleatorio
            } catch (InterruptedException e) {}
            Main.gui.setP1qe(true); // Reactiva intención de entrar
        }
        regionCritica(3); // Accede a la región crítica
        Main.gui.setP1qe(false); // Indica que ha terminado
        hacerCosas("más", 3); // Realiza operaciones posteriores
    }

    /**
     * Método Dekker5: Variante que utiliza turnos junto con indicadores.
     */
    private void dekker5() {
        hacerCosas("", 3); // Realiza operaciones previas
        Main.gui.setP1qe(true); // Indica que quiere entrar a la región crítica
        while (Main.gui.getP2qe()) { // Si el otro hilo quiere entrar:
            if (Main.gui.getTurno() == 2) { // Si no es su turno:
                Main.gui.setP1qe(false); // Desactiva intención de entrar
                try {
                    esperar(); // Muestra que está esperando
                    sleep(((int) ((Math.random() * 2) + 2)) * 1000); // Espera un tiempo aleatorio
                } catch (InterruptedException e) {}
                Main.gui.setP1qe(true); // Reactiva intención de entrar
            }
        }
        regionCritica(4); // Accede a la región crítica
        Main.gui.setTurno(2); // Cambia el turno al otro hilo
        Main.gui.setP1qe(false); // Indica que ha terminado
        hacerCosas("más", 3); // Realiza operaciones posteriores
    }

    /**
     * Simula trabajo del hilo fuera de la región crítica.
     * @param s Mensaje para mostrar en el estado.
     * @param time Tiempo de espera en segundos.
     */
    private void hacerCosas(String s, int time) {
        try {
            Main.gui.setVisibleProgressBarP1("show"); // Muestra barra de progreso
            Main.gui.updateStatusP1("Haciendo " + s + " cosas..."); // Actualiza el estado
            Main.gui.setOperationProgressBarP1("working"); // Cambia operación a "trabajando"
            sleep(time * 1000); // Pausa por tiempo indicado
        } catch (InterruptedException e) {}
    }

    /**
     * Simula el acceso del hilo a la región crítica.
     * @param time Tiempo de espera en segundos.
     */
    private void regionCritica(int time) {
        try {
            Main.gui.setVisibleProgressBarP1("show"); // Muestra barra de progreso
            Main.gui.setOperationProgressBarP1("bussy"); // Cambia operación a "ocupado"
            Main.gui.updateStatusP1("Usando región crítica..."); // Actualiza el estado
            sleep(time * 1000); // Pausa por tiempo indicado
        } catch (InterruptedException e) {}
    }

    /**
     * Simula el estado de espera del hilo.
     */
    private void esperar() {
        Main.gui.updateStatusP1("Esperando"); // Actualiza el estado a "esperando"
        Main.gui.setOperationProgressBarP1("wait"); // Cambia operación a "espera"
        Main.gui.setVisibleProgressBarP1("show"); // Muestra barra de progreso
    }
}
