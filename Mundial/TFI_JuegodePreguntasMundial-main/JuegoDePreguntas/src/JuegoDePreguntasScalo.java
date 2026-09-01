
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JuegoDePreguntasScalo {
    static final Color CELESTE = new Color(0x6C, 0xAC, 0xE4);
    static final Color CELESTE_OSCURO = new Color(0x3E, 0x74, 0xB0);
    static final Color DORADO = new Color(0xF3, 0xC5, 0x2C);
    static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
    static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 22);
    static final Font FUENTE_PREGUNTA = new Font("Segoe UI", Font.BOLD, 20);
    static final Font FUENTE_CONTADOR = new Font("Consolas", Font.BOLD, 20);

    static String urlBD = "https://docs.google.com/spreadsheets/d/e/2PACX-1vS-NCF_82uIlZQL3idzK7-zjn6yCsPdkzTajfwcltScO_oayu65t89icjq5JXrlz0vx0WoYU18xVVl4/pub?output=tsv";
    static String textoBaseDePreguntas;
    static String[] renglones;
    static int cantidadDePreguntas;
    static String[][] baseDePreguntas;
    static int cont;

    static String imgPrincipal;
    String[] preguntaEscogida;
    String pregunta;
    String respuesta;
    String imgJuego;
    ArrayList<String> Opciones = new ArrayList();
    ArrayList<Integer> preguntasDisponibles = new ArrayList();
    Integer n_pregunta = 0;

    private JPanel panelPrincipal1;
    private JPanel panelPrincipal2;
    private JPanel panelPrincipal3;
    private JLabel tituloPrincipal;
    private JButton botonIniciar;
    private JButton botonSalir;

    private JFrame frame;
    private JButton botonJuego1;
    private JButton botonJuego2;
    private JButton botonJuego3;
    private JButton botonJuego4;
    private JLabel labelJuego1;
    private JLabel labelJuego2;
    private JLabel labelJuegoLateral;
    private JPanel panelJuego1;
    private JPanel panelJuego2;
    private JPanel panelJuego3;
    private LluviaBanderas lluviaBanderas;

    private void botonIniciarActionPerformed(ActionEvent evt){
        frame.remove(panelPrincipal1);
        frame.remove(panelPrincipal2);
        frame.remove(panelPrincipal3);

        iniciarComponentesJuego();

        cargarPreguntas();
        this.jugar();
    }

    private void botonSalirActionPerformed(ActionEvent evt){
        frame.dispose();
    }
    public void iniciarComponentesPrincipal() {
        try{
            InputStream recursoAudio = JuegoDePreguntasScalo.class.getResourceAsStream("/musica.wav");
            if (recursoAudio == null) {
                throw new RuntimeException("No se encontro musica.wav en el classpath");
            }
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new BufferedInputStream(recursoAudio));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Dale papi que arranca");
        } catch (Exception e){
            System.out.println("No se pudo reproducir la musica, se continua sin sonido: " + e.getMessage());
        }


        frame = new JFrame();
        panelPrincipal1 = new JPanel();
        panelPrincipal2 = new JPanel();
        panelPrincipal3 = new JPanel();
        tituloPrincipal = new JLabel();
        botonIniciar = new JButton();
        botonSalir = new JButton();

        frame.setLayout(new BorderLayout());
        frame.setTitle("Juegaso");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        panelPrincipal1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelPrincipal1.setBackground(CELESTE);

        panelPrincipal2.setLayout(new BorderLayout(10, 0));
        panelPrincipal2.setBackground(CELESTE);

        panelPrincipal3.setLayout(new GridLayout(2, 1, 10, 5));
        panelPrincipal3.setBackground(CELESTE);

        tituloPrincipal.setText("<html><body>BIENVENDO AL JUEGO DE LA SCALONETA : <br/><p style='text-align:center;'>LE METEMOS???</p></body></html>");
        tituloPrincipal.setFont(FUENTE_TITULO);
        tituloPrincipal.setForeground(Color.WHITE);
        tituloPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal1.add(tituloPrincipal);

        JLabel gifCopa = null;
        try {
            ImageIcon gifImage = new ImageIcon(new URL(imgPrincipal));
            gifCopa = new JLabel(gifImage);
        } catch (Exception mue) {
            gifCopa = new JLabel("Error al cargar la imagen");
        } finally{
            panelPrincipal2.add(gifCopa, BorderLayout.CENTER);
        }
        botonIniciar.setText("Empezar juego");
        botonIniciar.setFont(FUENTE_BOTON);
        botonIniciar.setBackground(DORADO);
        botonIniciar.setFocusable(false);
        botonIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                botonIniciarActionPerformed(evt);
            }
        });
        panelPrincipal3.add(botonIniciar);

        botonSalir.setText("Salir");
        botonSalir.setFont(FUENTE_BOTON);
        botonSalir.setBackground(Color.WHITE);
        botonSalir.setFocusable(false);
        botonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });
        panelPrincipal3.add(botonSalir);


        frame.add(panelPrincipal1, BorderLayout.NORTH);
        frame.add(panelPrincipal2, BorderLayout.CENTER);
        frame.add(panelPrincipal3, BorderLayout.SOUTH);

        lluviaBanderas = new LluviaBanderas();
        frame.setGlassPane(lluviaBanderas);
        lluviaBanderas.setVisible(true);

        frame.setVisible(true);
    }
    private void iniciarComponentesJuego() {
        this.panelJuego1 = new JPanel();
        this.panelJuego2 = new JPanel();
        this.panelJuego3 = new JPanel();
        this.labelJuego1 = new JLabel();
        this.labelJuego2 = new JLabel();
        this.labelJuegoLateral = new JLabel();
        this.botonJuego1 = new JButton();
        this.botonJuego2 = new JButton();
        this.botonJuego4 = new JButton();
        this.botonJuego3 = new JButton();

        frame.setLayout(new GridLayout());

        this.panelJuego1.setLayout(new GridLayout(2, 0));
        this.panelJuego2.setLayout(new BorderLayout(5, 5));

        this.panelJuego2.setBackground(CELESTE);
        this.panelJuego3.setBackground(Color.white);
        this.labelJuego1.setFont(FUENTE_PREGUNTA);
        this.labelJuego1.setForeground(Color.WHITE);
        this.labelJuego1.setText("Pregunta");

        this.labelJuego1.setHorizontalAlignment(SwingConstants.CENTER);
        this.labelJuego2.setHorizontalAlignment(SwingConstants.CENTER);

        this.labelJuegoLateral.setText("0/52");
        this.labelJuegoLateral.setFont(FUENTE_CONTADOR);
        this.labelJuegoLateral.setForeground(DORADO);
        this.panelJuego2.add(labelJuegoLateral, BorderLayout.EAST);

        this.labelJuego2.setFont(FUENTE_PREGUNTA);
        this.labelJuego2.setText("Imagen");
        this.panelJuego2.add(this.labelJuego1, BorderLayout.NORTH);
        this.panelJuego2.add(this.labelJuego2, BorderLayout.CENTER);
        this.panelJuego1.add(this.panelJuego2);
        this.panelJuego3.setLayout(new GridLayout(0, 2, 30, 30));
        this.botonJuego1.setFont(FUENTE_BOTON);
        this.botonJuego1.setText("Opcion 1");
        this.botonJuego1.setBackground(Color.white);
        this.botonJuego1.setForeground(CELESTE_OSCURO);
        this.botonJuego1.setFocusable(false);

        this.botonJuego1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego1ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego1);
        this.botonJuego2.setFont(FUENTE_BOTON);
        this.botonJuego2.setText("Opcion 2");
        this.botonJuego2.setBackground(Color.white);
        this.botonJuego2.setForeground(CELESTE_OSCURO);
        this.botonJuego2.setFocusable(false);

        this.botonJuego2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego2ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego2);
        this.botonJuego4.setFont(FUENTE_BOTON);
        this.botonJuego4.setText("Opcion 3");
        this.botonJuego4.setBackground(Color.white);
        this.botonJuego4.setForeground(CELESTE_OSCURO);
        this.botonJuego4.setFocusable(false);

        this.botonJuego4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego4ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego4);
        this.botonJuego3.setFont(FUENTE_BOTON);
        this.botonJuego3.setText("Opcion 4");
        this.botonJuego3.setBackground(Color.white);
        this.botonJuego3.setForeground(CELESTE_OSCURO);
        this.botonJuego3.setFocusable(false);

        this.botonJuego3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego3ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego3);
        this.panelJuego1.add(this.panelJuego3);
        frame.add(this.panelJuego1);
        //frame.pack();
    }

    public void escogerPregunta(int n) {
        this.preguntaEscogida = baseDePreguntas[n];
        this.pregunta = this.preguntaEscogida[0];
        this.respuesta = this.preguntaEscogida[1];
        if (this.preguntaEscogida.length > 5) {
            this.imgJuego = this.preguntaEscogida[5];
        } else {
            this.imgJuego = "";
        }

        this.Opciones.clear();

        int i;
        for(i = 1; i < 5; ++i) {
            this.Opciones.add(this.preguntaEscogida[i]);
        }

        for(i = 0; i < 4; ++i) {
            Collections.shuffle(this.Opciones);
        }

    }

    public void mostrarPregunta() {
        this.labelJuego1.setText(this.pregunta);
        if (this.imgJuego.equals("")) {
            this.labelJuego2.setVisible(false);
        } else {
            this.labelJuego2.setVisible(true);
            this.labelJuego2.setText("");

            try {
                BufferedImage imagen = ImageIO.read(new URL(this.imgJuego));
                Image imagenEscalada = imagen.getScaledInstance(-350, 350, 1);
                this.labelJuego2.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception var3) {
                this.labelJuego2.setText("La imagen no se pudo cargar");
                this.labelJuego2.setIcon((Icon)null);
            }
        }

        this.labelJuegoLateral.setText((n_pregunta+1) + "/52");
        this.botonJuego1.setText((String)this.Opciones.get(0));
        this.botonJuego2.setText((String)this.Opciones.get(1));
        this.botonJuego4.setText((String)this.Opciones.get(2));
        this.botonJuego3.setText((String)this.Opciones.get(3));
    }

    void escogerRespuesta(int n) {
        if (((String)this.Opciones.get(n)).equals(this.respuesta)) {
            ++cont;
            JOptionPane.showMessageDialog(frame, "Su respuesta es correcta. Puntos: " + cont, "Muy bien :)", 1);
        } else {
            JOptionPane.showMessageDialog(frame, "Su respuesta es incorrecta, la respuesta es: " + this.respuesta + ". Puntos: " + cont, "Que mal :(", 0);
        }

        this.jugar();
    }

    public void cargarPreguntas(){
        for (int i = 0; i< cantidadDePreguntas; i++){
            preguntasDisponibles.add(i);
        }

        Collections.shuffle(preguntasDisponibles);
    }

    public void jugar() {
        if (n_pregunta == cantidadDePreguntas) {
            this.mostrarPantallaCampeones();
            return;
        }

        this.escogerPregunta(this.preguntasDisponibles.get(n_pregunta));
        this.mostrarPregunta();
        ++this.n_pregunta;
    }

    private void mostrarPantallaCampeones() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panelFinal = new JPanel(new GridBagLayout());
        panelFinal.setBackground(CELESTE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(12, 30, 12, 30);

        JLabel estrellas = new JLabel("★ ★ ★");
        estrellas.setFont(new Font("Segoe UI", Font.BOLD, 42));
        estrellas.setForeground(DORADO);
        gbc.gridy = 0;
        panelFinal.add(estrellas, gbc);

        JLabel titulo = new JLabel("<html><body style='text-align:center'>¡ARGENTINA CAMPEÓN<br/>DEL MUNDO 2022!</body></html>");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        panelFinal.add(titulo, gbc);

        JLabel puntaje = new JLabel("Puntaje final: " + cont + "/" + cantidadDePreguntas);
        puntaje.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        puntaje.setForeground(Color.WHITE);
        gbc.gridy = 2;
        panelFinal.add(puntaje, gbc);

        JButton botonSalirFinal = new JButton("Salir");
        botonSalirFinal.setFont(FUENTE_BOTON);
        botonSalirFinal.setBackground(DORADO);
        botonSalirFinal.setFocusable(false);
        botonSalirFinal.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        panelFinal.add(botonSalirFinal, gbc);

        frame.add(panelFinal, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        if (lluviaBanderas != null) {
            lluviaBanderas.intensificar();
        }
    }

    public JuegoDePreguntasScalo() {
        for(int i = 0; i < renglones.length; ++i) {
            String renglon = renglones[i];
            baseDePreguntas[i] = renglon.split("\t");
        }

        this.iniciarComponentesPrincipal();
    }

    public static String LeerArchivo(String ruta) {
        try {
            if (ruta == null) {
                throw new RuntimeException("Error, la URL de lectura no puede ser nula");
            } else {
                URL url = new URL(ruta);
                URLConnection conexion = url.openConnection();
                conexion.setConnectTimeout(8000);
                conexion.setReadTimeout(8000);
                InputStreamReader isr = new InputStreamReader(conexion.getInputStream());
                return LeerArchivo((Reader)isr);
            }
        } catch (Exception var4) {
            JOptionPane.showMessageDialog(null,
                    "No hay conexion a internet o la base de preguntas no esta disponible.\nVerifica tu conexion y volve a intentar.",
                    "Sin conexion", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return "";
        }
    }

    public static String LeerArchivo(Reader reader) throws Exception {
        BufferedReader br = new BufferedReader(reader);
        String texto = "";

        String linea;
        for(boolean primerRenglon = true; (linea = br.readLine()) != null; texto = texto + linea) {
            if (primerRenglon) {
                primerRenglon = false;
            } else {
                texto = texto + "\n";
            }
        }

        reader.close();
        br.close();
        return texto;
    }

    private void botonJuego1ActionPerformed(ActionEvent evt) {
        this.escogerRespuesta(0);
    }

    private void botonJuego2ActionPerformed(ActionEvent evt) {
        this.escogerRespuesta(1);
    }

    private void botonJuego4ActionPerformed(ActionEvent evt) {
        this.escogerRespuesta(2);
    }

    private void botonJuego3ActionPerformed(ActionEvent evt) {
        this.escogerRespuesta(3);
    }

    public static void main(String[] args) {
        new JuegoDePreguntasScalo();
    }

    static {
        textoBaseDePreguntas = LeerArchivo(urlBD);
        renglones = textoBaseDePreguntas.split("\n");
        cantidadDePreguntas = renglones.length;
        baseDePreguntas = new String[cantidadDePreguntas][renglones.length];
        cont = 0;
        imgPrincipal = "https://i.imgur.com/nzoV1N9.gif";
    }

    private static class LluviaBanderas extends JComponent {
        private static final int ANCHO = 26;
        private static final int ALTO = 17;

        private final ArrayList<double[]> banderas = new ArrayList<>();
        private final Random random = new Random();
        private long ultimoSpawn = 0;
        private int intervaloSpawnMs = 700;

        LluviaBanderas() {
            setOpaque(false);
            Timer timer = new Timer(30, e -> tick());
            timer.start();
        }

        void intensificar() {
            intervaloSpawnMs = 120;
        }

        private void tick() {
            long ahora = System.currentTimeMillis();
            if (getWidth() > 0 && ahora - ultimoSpawn > intervaloSpawnMs) {
                double x = random.nextInt(getWidth());
                double velocidad = 1.5 + random.nextDouble() * 2.5;
                double angulo = random.nextDouble() * 360;
                double velocidadAngular = (random.nextDouble() - 0.5) * 6;
                banderas.add(new double[]{x, -ALTO, velocidad, angulo, velocidadAngular});
                ultimoSpawn = ahora;
            }

            for (double[] b : banderas) {
                b[1] += b[2];
                b[3] += b[4];
            }
            banderas.removeIf(b -> b[1] > getHeight() + ALTO);

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (double[] b : banderas) {
                g2.translate(b[0], b[1]);
                g2.rotate(Math.toRadians(b[3]));

                g2.setColor(CELESTE);
                g2.fillRect(-ANCHO / 2, -ALTO / 2, ANCHO, ALTO / 3);
                g2.setColor(Color.WHITE);
                g2.fillRect(-ANCHO / 2, -ALTO / 2 + ALTO / 3, ANCHO, ALTO / 3);
                g2.setColor(CELESTE);
                g2.fillRect(-ANCHO / 2, -ALTO / 2 + 2 * (ALTO / 3), ANCHO, ALTO - 2 * (ALTO / 3));
                g2.setColor(DORADO);
                g2.fillOval(-3, -3, 6, 6);

                g2.rotate(-Math.toRadians(b[3]));
                g2.translate(-b[0], -b[1]);
            }

            g2.dispose();
        }
    }
}