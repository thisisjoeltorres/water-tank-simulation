
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.awt.geom.*;

public class ControlNivelTanque extends JPanel {
    private double nivel = 0.0;
    private double setPoint = 0.7;
    private boolean valvulaAbierta = false;

    private boolean simulando = false;
    private boolean consumoActivo = false;
    private double tiempo = 0;
    private Timer timer;
    private JTextField setPointField;

    private Rectangle areaValvula;

    public ControlNivelTanque(JTextField field) {
        this.setPointField = field;
        timer = new Timer(100, e -> actualizar());

        // Inicializar el área de la válvula
        int centroX = 230;  // Coordenadas aproximadas del centro
        int centroY = 135;
        int ancho = 50;
        int alto = 40;
        areaValvula = new Rectangle(centroX - ancho / 2, centroY - alto / 2, ancho, alto);

        // Añadir los listeners solo una vez en el constructor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (areaValvula.contains(e.getPoint())) {
                    valvulaAbierta = !valvulaAbierta;
                    if(valvulaAbierta){
                        abrirValvula();
                        repaint();
                        System.out.println("¡Válvula clickeada!");
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (areaValvula.contains(e.getPoint())) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private double funcionConsumo(double tiempo) {
        return 0.004 + 0.003 * Math.sin(tiempo);
    }

    private void actualizar() {
        tiempo += 0.1;
        if (!simulando) return;


        if(valvulaAbierta) {
            valvulaAbierta = nivel < setPoint;
            nivel += 0.01;
            if (nivel > 1.0) nivel = 1.0;
        }

        if (nivel >= setPoint) consumoActivo = true;


        if (consumoActivo) {
            double consumo = funcionConsumo(tiempo);
            nivel -= consumo;
            if (nivel < 0) nivel = 0;
        }

        repaint();
    }

    public void iniciar() {
        try {
            double sp = Double.parseDouble(setPointField.getText());
            if (sp < 0 || sp > 1.0) throw new NumberFormatException();
            this.setPoint = sp;
            simulando = true;
            consumoActivo = false;
            timer.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "SetPoint inválido (0-1)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void abrirValvula() {
        valvulaAbierta = true;
        simulando = true;
        repaint();
    }

    public void pausar() { simulando = false; valvulaAbierta = false; repaint(); }

    public void reiniciar() {
        simulando = false;
        consumoActivo = false;
        valvulaAbierta = false;
        nivel = 0.0;
        tiempo = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(250, 250, 250));
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tanqueX = 300, tanqueY = 100, tanqueW = 100, tanqueH = 300;
        int tuberiaEntradaX = tanqueX - 140; // Modificar estas coordenadas para mover la Valvula de Entrada horizontalmente.
        int tuberiaEntradaY = tanqueY + 30; // Modificar estas coordenadas para mover la Valvula de Entrada verticalmente.

        // ----------- TUBERIA ENTRADA -------------

        // Tuberia Izquierda
        g2.setColor(Color.BLUE);
        g2.fillRoundRect(tuberiaEntradaX, tuberiaEntradaY, 60, 10, 0, 0);

        // Tuberia Derecha
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(tuberiaEntradaX + 80, tuberiaEntradaY, 60, 10, 0, 0);

        // Coordenadas del centro de la válvula

        int centroX = tuberiaEntradaX + 70;
        int centroY = tuberiaEntradaY + 5;

        // ----------- VALVULA ------------

        if (valvulaAbierta) {
            dibujarValvula(g2, centroX, centroY, Color.GREEN);

            // Label

            g2.fillRoundRect(tuberiaEntradaX + 20, tuberiaEntradaY - 50, 100, 20, 10, 10);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Abierta", tuberiaEntradaX + 25, tuberiaEntradaY - 36);
        } else {
            dibujarValvula(g2, centroX, centroY, Color.RED);

            // Label

            g2.fillRoundRect(tuberiaEntradaX + 20, tuberiaEntradaY - 50, 100, 20, 10, 10);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Válvula Cerrada", tuberiaEntradaX + 25, tuberiaEntradaY - 36);
        }

        // ------------ Tanque ------------
        g2.setColor(Color.GRAY);
        g2.fillOval(tanqueX, tanqueY, tanqueW, 30);
        g2.fillRect(tanqueX, tanqueY + 15, tanqueW, tanqueH);
        g2.fillOval(tanqueX, tanqueY + tanqueH, tanqueW, 30);

        // ---------- AGUA DEL TANQUE -----------
        int nivelPix = (int) (tanqueH * nivel);
        g2.setColor(new Color(0, 120, 255, 200));
        g2.fillRect(tanqueX, tanqueY + tanqueH - nivelPix + 15, tanqueW, nivelPix);
        g2.fillOval(tanqueX, tanqueY + tanqueH - nivelPix + nivelPix - 1, tanqueW, 30);

        // ---------- SALIDA TUBERIA -----------

        int tuberiaSalidaY = tanqueY + tanqueH + 30;
        int tuberiaSalidaX = tanqueX + tanqueW / 2 - 5;

        // Salida tubería
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(tuberiaSalidaX, tuberiaSalidaY, 10, 20);
        g2.fillRect(tuberiaSalidaX, tuberiaSalidaY + 20, 100, 10);
        g2.fillRect(tuberiaSalidaX + 100, tuberiaSalidaY + 20, 10, 20);
        if (consumoActivo && nivel > 0.05) {
            g2.setColor(Color.BLUE);
            g2.fillOval(tanqueX + tanqueW / 2 + 97, tanqueY + tanqueH + 60, 6, 6);
        }

        // -------- LINEAS DE NIVEL ---------

        // LINEAS DE NIVEL - DERECHA

        int lineaNivelY = tanqueY + tanqueH - 280;
        int lineaNivelX = tanqueX + tanqueW + 10;
        int widthHorizontalLevelLine = 50;

        // Salida tubería
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(lineaNivelX, lineaNivelY, widthHorizontalLevelLine, 2); // Linea Horizontal Cima
        g2.fillRect(lineaNivelX + 50, lineaNivelY, 2, 282); // Linea Vertical
        g2.fillRect(lineaNivelX, lineaNivelY + 280, widthHorizontalLevelLine, 2); // Linea Horizontal Suelo

        // LT BOX
        g2.setColor(new Color(135, 206, 250));
        g2.fillOval(tanqueX + tanqueW + 40, tanqueY + 140, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawString("LT", tanqueX + tanqueW + 53, tanqueY + 165);

        // LC BOX
        g2.setColor(new Color(144, 238, 144));
        g2.fillOval(tanqueX + tanqueW + 100, tanqueY + 180, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawString("LC", tanqueX + tanqueW + 113, tanqueY + 205);

        // SP BOX
        g2.setColor(new Color(173, 216, 230));
        g2.fillRect(tanqueX + tanqueW + 160, tanqueY + 180, 90, 30);
        g2.setColor(Color.BLACK);
        g2.drawString("SP = " + String.format("%.2f", setPoint) + " m", tanqueX + tanqueW + 165, tanqueY + 200);

        // ----------- NIVEL NUMERICO - BOX ----------

        int numLevelBoxY = tanqueY + 80;
        int numLevelBoxX = tanqueX + tanqueW + 80;

        g2.setColor(Color.BLACK);
        g2.drawString("NIVEL NUMÉRICO", numLevelBoxX, numLevelBoxY - 10);
        g2.setColor(new Color(135, 206, 250));
        g2.fillRect(numLevelBoxX, numLevelBoxY, 70, 30);
        g2.setColor(Color.BLACK);
        g2.drawString(String.format("%.2f m", nivel), numLevelBoxX + 15, numLevelBoxY + 20);

        // Línea punteada control
        Stroke old = g2.getStroke();
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
        g2.setColor(Color.BLACK);
        g2.drawLine(tanqueX + tanqueW + 60, tanqueY + 200, tanqueX + tanqueW + 100, tanqueY + 200);
        g2.drawLine(tanqueX + tanqueW + 140, tanqueY + 200, tanqueX + tanqueW + 160, tanqueY + 200);
        g2.setStroke(old);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ControlNivelTanque");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        JPanel panelInferior = new JPanel();

        JTextField campoSP = new JTextField("0.70", 5);
        JButton iniciar = new JButton("Iniciar"), pausar = new JButton("Pausar"), reiniciar = new JButton("Reiniciar");

        panelSuperior.add(new JLabel("SetPoint (0–1):"));
        panelSuperior.add(campoSP);
        panelSuperior.add(iniciar);
        panelSuperior.add(pausar);
        panelSuperior.add(reiniciar);

        JLabel funcionConsumo = new JLabel("Función de consumo: f(t) = 0.004 + 0.003 * sen(t)");

        panelInferior.add(funcionConsumo);

        ControlNivelTanque sim = new ControlNivelTanque(campoSP);
        iniciar.addActionListener(e -> sim.iniciar());
        pausar.addActionListener(e -> sim.pausar());
        reiniciar.addActionListener(e -> sim.reiniciar());

        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(panelInferior, BorderLayout.SOUTH);
        frame.add(sim, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Metodos de Dibujo

    public void dibujarValvula(Graphics2D g2, int centroX, int centroY, Color colorValvula) {
        g2.setColor(colorValvula); // Verde fuerte

        // Triángulo izquierdo
        Polygon trianguloIzq = new Polygon();
        trianguloIzq.addPoint(centroX, centroY);         // Punta inferior
        trianguloIzq.addPoint(centroX - 20, centroY - 10); // Izquierda
        trianguloIzq.addPoint(centroX - 20, centroY + 10); // Izquierda abajo
        g2.fill(trianguloIzq);

        // Triángulo derecho
        Polygon trianguloDer = new Polygon();
        trianguloDer.addPoint(centroX, centroY);          // Punta inferior
        trianguloDer.addPoint(centroX + 20, centroY - 10);  // Derecha
        trianguloDer.addPoint(centroX + 20, centroY + 10);  // Derecha abajo
        g2.fill(trianguloDer);

        // Línea vertical que conecta a la "tapa"
        g2.fillRect(centroX - 3, centroY - 15, 4, 15); // Pequeño rectángulo vertical

        // Semicírculo (arco) en la parte superior
        g2.fillArc(centroX - 11, centroY - 20, 20, 20, 0, 180); // Semicírculo hacia abajo

    }
}
