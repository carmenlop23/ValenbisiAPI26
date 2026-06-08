package es.gva.edu.iesjuandegaray.bicis;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConexionBDD extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtTextfield;
	private JTextArea txtrTextarea;
	private JLabel lblEstadoConexion;
	

// Variables de conexión a base de datos
	private static Connection con;
	private static Statement s;
	private static DatosJSon dJSon;
	private static int numEst = 3;

	
	
	private static final String driver = "com.mysql.cj.jdbc.Driver";
	private static final String user = "root";
	private static final String pass = "alumno";
	private static final String url = "jdbc:mysql://localhost:3306/valenbicibd";

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConexionBDD frame = new ConexionBDD();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ConexionBDD() {

		dJSon = new DatosJSon(numEst);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblJlabel = new JLabel("Introduce el número de estaciones a consultar:");
		lblJlabel.setBounds(106, 12, 329, 17);
		contentPane.add(lblJlabel);

		txtTextfield = new JTextField();
		txtTextfield.setBounds(438, 10, 114, 21);
		txtTextfield.setColumns(10);
		contentPane.add(txtTextfield);

		
		
// ############# TextArea con ScrollPane #############
		txtrTextarea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtrTextarea);
		scrollPane.setBounds(208, 79, 310, 165);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane);

		JLabel lblNewLabel1 = new JLabel("Obtener datos de Estaciones:");
		lblNewLabel1.setBounds(209, 63, 277, 17);
		contentPane.add(lblNewLabel1);
		
		

// ############# Botón DATOS #############
		JButton btnBotonDatos = new JButton("Datos");
		btnBotonDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					numEst = Integer.parseInt(txtTextfield.getText().trim());
				} catch (NumberFormatException ex) {
					txtrTextarea.setText("Introduce un número válido de estaciones");
				}
				dJSon.mostrarDatos(numEst);
				txtrTextarea.setText(dJSon.getDatos());
			}
		});
		btnBotonDatos.setBounds(24, 58, 105, 27);
		contentPane.add(btnBotonDatos);

		JLabel lblNewLabel_2 = new JLabel("Estado Conexión:");
		lblNewLabel_2.setBounds(147, 286, 120, 17);
		contentPane.add(lblNewLabel_2);

		lblEstadoConexion = new JLabel("");
		lblEstadoConexion.setBounds(270, 286, 250, 17);
		contentPane.add(lblEstadoConexion);
		
		

// ############# Botón CONECTAR #############
		JButton btnNewButtonConectar = new JButton("Conectar");
		btnNewButtonConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Connection c = conector();
				if (c != null) {
					lblEstadoConexion.setText("Conexión establecida");
				} else {
					lblEstadoConexion.setText("Error al conectar");
				}
			}
		});
		btnNewButtonConectar.setBounds(24, 281, 105, 27);
		contentPane.add(btnNewButtonConectar);

		JLabel lblNewLabel3 = new JLabel("Primero Obtener Datos de Estaciones y Conectar con BDD");
		lblNewLabel3.setBounds(170, 335, 358, 17);
		contentPane.add(lblNewLabel3);
		
		

// ############# Botón AÑADIR A BDD #############
		JButton btnNewButtonAñadir = new JButton("Añadir a BDD");
		btnNewButtonAñadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (con == null || dJSon.getValues() == null) return;
				try {
					for (String v : dJSon.getValues()) {
						if (v == null || v.isEmpty()) continue;
						String[] campos = v.split(";");
						String sql = "INSERT INTO historico " + "(estacion_id, direccion, bicis_disponibles, anclajes_libres, estado_operativo, ubicacion) "			
                               + "VALUES ("+ campos[0] + ", '"+ campos[1] + "', "+ campos[2] + ", "+ campos[3] + ", "+ campos[4] + ", "+ "ST_GeomFromText('POINT(" + campos[5] + " " + campos[6] + ")'))";
						s.executeUpdate(sql);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButtonAñadir.setBounds(24, 330, 131, 27);
		contentPane.add(btnNewButtonAñadir);
		
		

// ############# Botón CERRAR CONEXIÓN #############
		JButton btnNewButtonCerrar = new JButton("Cerrar Conexión");
		btnNewButtonCerrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnNewButtonCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (con != null && !con.isClosed()) {
						con.close();
						lblEstadoConexion.setText("Conexión cerrada");
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnNewButtonCerrar.setBounds(205, 381, 152, 27);
		contentPane.add(btnNewButtonCerrar);
	}

	
	
	// ############# Método conector #############
	public Connection conector() {
		con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			s = con.createStatement();
			return con;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}