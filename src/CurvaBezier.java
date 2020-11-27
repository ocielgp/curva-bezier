import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Un programa que permite trazar la curva de bezier entre varios puntos
 *
 * @author ociel
 */

public class CurvaBezier {
    /**
     * Panel contenedor de la aplicacion
     */
    private JPanel panel;


    /**
     * Panel contenedor de botones
     */
    private JPanel opciones;

    /**
     * Panel donde se dibuja la curva y los puntos
     */
    private JPanel pizarron;

    /**
     * Boton que invoca al metodo calcularCurvaBezier
     *
     * @see #calcularCurvaBezier(Graphics)
     */
    private JButton calcularCurva;

    /**
     * Boton para limpiar el pizarron
     *
     * @see #pizarron
     */
    private JButton borrar;

    /**
     * Label con el nombre del autor
     */
    private JLabel creditos;

    /**
     * Variable donde se guardaran las coordenadas de los puntos
     * del pizarron
     *
     * @see Point
     * @see #pizarron
     */
    private ArrayList<Point> puntos;

    /**
     * Tamaño de los puntos dibujados en el pizarron
     *
     * @see #pizarron
     */
    private final int SIZE = 10;

    /**
     * Marca que tantos intervalos seran evaluados al trazar la curva
     * Entre mas pequeño mas definida estara la curva
     */
    private final double t = 0.001f;

    public CurvaBezier() {
        this.puntos = new ArrayList<Point>();
        this.opciones.setBackground(new Color(147, 248, 255));
        this.panel.setBackground(new Color(0, 0, 0));
        this.pizarron.setBackground(new Color(255, 255, 255));

        pizarron.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                puntos.add(new Point(e.getX(), e.getY()));
                pintarPunto(pizarron.getGraphics(), e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        calcularCurva.addActionListener(e -> {
            // Solo traza la curva si hay dos puntos
            if (puntos.size() > 1) {
                calcularCurvaBezier(pizarron.getGraphics());
            }
        });
        borrar.addActionListener(e -> borrarPizarron());
    }

    /**
     * Pinta los puntos de la curva en los graficos del panel proporcionado
     *
     * @param graphics Graphics del contenedor
     */
    public void calcularCurvaBezier(Graphics graphics) {
        // Variables donde se guarda el punto X, Y de la curva
        double bezierX = 0, bezierY = 0;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setPaint(Color.BLACK);

        // Bucle que traza el camino del primer punto al ultimo
        for (double t = this.t; t <= 1; t = t + this.t) {
            // Algoritmo para obtener el punto de la curva en x,y
            for (int i = 0; i < puntos.size(); i++) {
                if (i == 0) { // Primer termino
                    bezierX += Math.pow(1 - t, puntos.size() - 1) * puntos.get(i).getX();
                    bezierY += Math.pow(1 - t, puntos.size() - 1) * puntos.get(i).getY();
                } else if (i == puntos.size() - 1) { // Ultimo termino
                    bezierX += Math.pow(t, i) * puntos.get(i).getX();
                    bezierY += Math.pow(t, i) * puntos.get(i).getY();
                } else { // Terminos del mecio
                    bezierX += (puntos.size() - 1) * Math.pow(1 - t, (puntos.size() - 1) - i) * puntos.get(i).getX() * Math.pow(t, i);
                    bezierY += (puntos.size() - 1) * Math.pow(1 - t, (puntos.size() - 1) - i) * puntos.get(i).getY() * Math.pow(t, i);
                }
            }

            // Dibujar punto en el pizarron
            graphics2D.fillOval((int) bezierX, (int) bezierY, SIZE, SIZE);

            // Reiniciar valores
            bezierX = 0;
            bezierY = 0;
        }
        // Enviar los graficos del panel con la curva
        this.pizarron.paintComponents(graphics2D);

    }

    /**
     * Redibujar el pizarron
     *
     * @see #pizarron
     */
    public void paintComponents(Graphics g) {
        pizarron.paintComponents(g);
    }

    /**
     * Dibuja un nuevo punto en el pizarron
     *
     * @see #pizarron
     */
    public void pintarPunto(Graphics graphics, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fillOval(x, y, SIZE, SIZE);
        this.pizarron.paintComponents(graphics2D);
    }

    /**
     * Limpia el pizarron y borra todos los puntos almacenados
     *
     * @see #pizarron
     * @see #puntos
     */
    public void borrarPizarron() {
        this.puntos.clear();
        this.pizarron.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Curva de Bezier");
        frame.setContentPane(new CurvaBezier().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
