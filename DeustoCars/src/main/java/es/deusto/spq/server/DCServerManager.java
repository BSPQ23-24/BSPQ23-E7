package es.deusto.spq.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.serialization.Customer;
import es.deusto.spq.serialization.Vehicle;

// Server manager will serve as a test class for the server functions, when running the app with the
// true client it is not necessary to run: mvn exec:java -Pserver
public class DCServerManager implements ActionListener, Runnable {

	protected static final Logger logger = LogManager.getLogger();

	private JFrame frame;
	private JButton buttonEnd;
	private JLabel customerLabel;
	private JTextField customerText;
	private JLabel vehicleLabel;
	private JTextField vehicleText;
	private JLabel message;

	private Client client;
	private WebTarget webTarget;

	private Thread thread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public DCServerManager(String hostname, String port) {
		client = ClientBuilder.newClient();
		// this is the default server path, then the extensions for each method should be added
		// the 'deustocars' extension part should match the web.xml file
		// probably if pom not changed http://localhost:8080/deustocars/...
		webTarget = client.target(String.format("http://%s:%s/deustocars", hostname, port));

		this.buttonEnd = new JButton("End DCServer Process");
		this.buttonEnd.addActionListener(this);
		this.message = new JLabel("REST Donation Manager running...");
		this.message.setOpaque(true);
		this.message.setForeground(Color.yellow);
		this.message.setBackground(Color.gray);

		JPanel panelSuperior = new JPanel();
		this.customerLabel = new JLabel("Customers:");
		this.vehicleLabel = new JLabel("Vehicles:");
		panelSuperior.add(this.customerLabel);
		panelSuperior.add(this.vehicleLabel);
		this.customerText = new JTextField("...");
		this.vehicleText = new JTextField("...");
		this.customerText.setPreferredSize( new Dimension( 200, 24 ) );
		this.vehicleText.setPreferredSize( new Dimension( 200, 24 ) );
		panelSuperior.add(this.customerText);
		panelSuperior.add(this.vehicleText);

		JPanel panelBoton = new JPanel();
		panelBoton.add(this.buttonEnd);

		this.frame = new JFrame("DeustoCars: Server Manager");
		this.frame.setSize(1000, 500);
		this.frame.setResizable(false);		
		this.frame.getContentPane().add(panelSuperior, "North");
		this.frame.getContentPane().add(panelBoton);
		this.frame.getContentPane().add(this.message, "South");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//WindowManager.middleCenter(this.frame);
		this.frame.setVisible(true);

		thread = new Thread(this);
		thread.start();
	}

	public static void main(String[] args) {
		String hostname = args[0];
		String port = args[1];

		new DCServerManager(hostname, port);
	}

	public String getCustomersString(){
		WebTarget donationsWebTarget = webTarget.path("server/getcustomers");
		Invocation.Builder invocationBuilder = donationsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
			return "Error";
		} else {
			String responseMessage = response.readEntity(String.class);
			logger.info("* Customers coming from the server: '{}'", responseMessage);
			return responseMessage;
		}
	}
	public String getVehiclesString(){
		WebTarget donationsWebTarget = webTarget.path("server/getvehicles");
		Invocation.Builder invocationBuilder = donationsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
			return "Error";
		} else {
			String responseMessage = response.readEntity(String.class);
			logger.info("* Vehicles coming from the server: '{}'", responseMessage);
			return responseMessage;
		}
	}

	@Override
	public void run() {
		running.set(true);
		while(running.get()) {
			try {
				Thread.sleep(2000);
				System.out.println("Obtaining data from server...");
				//DonationInfo donationInfo = getDonationInfo();
				this.customerText.setText(getCustomersString());
				this.vehicleText.setText(getVehiclesString());
			} catch (InterruptedException e){ 
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, Failed to complete operation");
            }
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton)e.getSource();
		if (target == this.buttonEnd) {
			this.stop();
			System.exit(0);
		}
	}

	public void stop() {
		this.running.set(false);
	}

}