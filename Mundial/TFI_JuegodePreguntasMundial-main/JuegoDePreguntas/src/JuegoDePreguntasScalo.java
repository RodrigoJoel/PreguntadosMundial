
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class JuegoDePreguntasScalo {
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
            String filePath = "C:\\Users\\rodri\\IdeaProjects\\Mundial\\TFI_JuegodePreguntasMundial-main\\JuegoDePreguntas\\src\\musica.wav";
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Dale papi que arranca");
        } catch (Exception e){
            e.printStackTrace();
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
        panelPrincipal1.setBackground(Color.CYAN);

        panelPrincipal2.setLayout(new BorderLayout(10, 0));
        panelPrincipal2.setBackground(Color.CYAN);

        panelPrincipal3.setLayout(new GridLayout(2, 1, 10, 5));
        panelPrincipal3.setBackground(Color.CYAN);

        tituloPrincipal.setText("<html><body>BIENVENDO AL JUEGO DE LA SCALONETA : <br/><p style='text-align:center;'>LE METEMOS???</p></body></html>");
        tituloPrincipal.setFont(new Font("Comic sans ms", 0, 30));
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
        botonIniciar.setFont(new Font("Comic sans ms", 2, 24));
        botonIniciar.setFocusable(false);
        botonIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                botonIniciarActionPerformed(evt);
            }
        });
        panelPrincipal3.add(botonIniciar);

        botonSalir.setText("Salir");
        botonSalir.setFont(new Font("Comic sans ms", 2, 24));
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

        this.panelJuego2.setBackground(Color.cyan);
        this.panelJuego3.setBackground(Color.white);
        this.botonJuego1.setBackground(Color.white);
        this.labelJuego1.setFont(new Font("Arial", 1, 20));
        this.labelJuego1.setText("Pregunta");

        this.labelJuego1.setHorizontalAlignment(SwingConstants.CENTER);
        this.labelJuego2.setHorizontalAlignment(SwingConstants.CENTER);

        this.labelJuegoLateral.setText("0/52");
        this.labelJuegoLateral.setFont(new Font("Consolas", 0, 20));
        this.panelJuego2.add(labelJuegoLateral, BorderLayout.EAST);

        this.labelJuego2.setFont(new Font("Comic sans ms", 1, 20));
        this.labelJuego2.setText("Imagen");
        this.panelJuego2.add(this.labelJuego1, BorderLayout.NORTH);
        this.panelJuego2.add(this.labelJuego2, BorderLayout.CENTER);
        this.panelJuego1.add(this.panelJuego2);
        this.panelJuego3.setLayout(new GridLayout(0, 2, 30, 30));
        this.botonJuego1.setFont(new Font("Comic sans ms", 2, 24));
        this.botonJuego1.setText("Opcion 1");
        this.botonJuego1.setFocusable(false);

        this.botonJuego1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego1ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego1);
        this.botonJuego2.setFont(new Font("Comic sans ms", 2, 24));
        this.botonJuego2.setText("Opcion 2");
        this.botonJuego2.setBackground(Color.white);
        this.botonJuego2.setFocusable(false);

        this.botonJuego2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego2ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego2);
        this.botonJuego4.setFont(new Font("Comic sans ms", 2, 24));
        this.botonJuego4.setText("Opcion 3");
        this.botonJuego4.setBackground(Color.cyan);
        this.botonJuego4.setFocusable(false);

        this.botonJuego4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JuegoDePreguntasScalo.this.botonJuego4ActionPerformed(evt);
            }
        });

        this.panelJuego3.add(this.botonJuego4);
        this.botonJuego3.setFont(new Font("Comic sans ms", 2, 24));
        this.botonJuego3.setText("Opcion 4");
        this.botonJuego3.setBackground(Color.cyan);
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
            JOptionPane.showMessageDialog(frame, "Juego Terminado. *Puntaje Total* " +cont, "Muy bien :)", -1);
            System.exit(0);
        }

        this.escogerPregunta(this.preguntasDisponibles.get(n_pregunta));
        this.mostrarPregunta();
        ++this.n_pregunta;
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
                InputStreamReader isr = new InputStreamReader(conexion.getInputStream());
                return LeerArchivo((Reader)isr);
            }
        } catch (Exception var4) {
            System.out.println("No hay conexion a internet, la base de datos no pudo ser cargada");
            System.exit(0);
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
}