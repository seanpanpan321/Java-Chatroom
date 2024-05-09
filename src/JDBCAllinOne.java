
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JDBCAllinOne implements ActionListener {
	
	JLabel lblFname,lblLname,lblAddress,lblSalary,lblf,lbll,lbla,lbls;
	JLabel lblfVal,lbllVal,lblaVal,lblsVal;
	JTextField txtFname,txtLname,txtAddress,txtSalary;
	JButton btnInsert,btnUpdate,btnDelete,btnPrev,btnNext,btnClear;
	PreparedStatement insertStatement, updateStatement; // only insertStatement is used in this example
	ResultSet rs ;
	Connection con;
	public static void main(String[] args) {
		JDBCAllinOne obj = new JDBCAllinOne();
	
		obj.createUI();
	}
	
	public JDBCAllinOne() {
		try
		{
		    // Connect to a database
		    this.con = DriverManager.getConnection
		      ("jdbc:sqlite:javabook.db");
		} catch (Exception e) {
			System.exit(0);
		}
		
		/* sets up the prepared statement for SQL inserts */
		String insertSQL = "Insert Into Employee (FName,LName,Address,Salary) " +
				"Values (?,?,?,?)";
		try {
			insertStatement = con.prepareStatement(insertSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	private void createUI()
	{
		JFrame frame = new JFrame("JDBC All in One");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Layout of Main Window
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
		
		lblFname = new JLabel("First Name :");
		lblLname = new JLabel("Last Name :");
		lblAddress = new JLabel("Address :");
		lblSalary = new JLabel("Salary :");
		
		txtFname = new JTextField("",15);//To adjust width
		txtLname = new JTextField();
		txtAddress = new JTextField();
		txtSalary = new JTextField();
		
		JPanel pnlInput = new JPanel(new GridLayout(4,2));
		
		pnlInput.add(lblFname);
		pnlInput.add(txtFname);
		
		pnlInput.add(lblLname);
		pnlInput.add(txtLname);
		
		pnlInput.add(lblAddress);
		pnlInput.add(txtAddress);
		
		pnlInput.add(lblSalary);
		pnlInput.add(txtSalary);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(this);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(this);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(this);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(this);
		
		JPanel pnlButton = new JPanel(new GridLayout(1,4));
		
		pnlButton.add(btnInsert);
		pnlButton.add(btnUpdate);
		pnlButton.add(btnDelete);
		pnlButton.add(btnClear);
		
		JPanel pnlAns = new JPanel(new GridLayout(4,2));
		
		lblf = new JLabel("First Name :");
		lbll = new JLabel("Last Name :");
		lbla = new JLabel("Address :");
		lbls = new JLabel("Salary :");
		
		lblfVal = new JLabel("");
		lbllVal = new JLabel("");
		lblaVal = new JLabel("");
		lblsVal = new JLabel("");
		
		pnlAns.add(lblf);
		pnlAns.add(lblfVal);
		
		pnlAns.add(lbll);
		pnlAns.add(lbllVal);
		
		pnlAns.add(lbla);
		pnlAns.add(lblaVal);
		
		pnlAns.add(lbls);
		pnlAns.add(lblsVal);
		
		btnNext = new JButton(" >> ");
		btnNext.setActionCommand("Next");
		btnNext.addActionListener(this);
		
		JPanel pnlNavigate = new JPanel(new GridLayout(1,2));
		
		pnlNavigate.add(btnNext);
		
		frame.add(pnlInput);
		frame.add(pnlButton);
		frame.add(pnlAns);
		frame.add(pnlNavigate);
		
		frame.pack();
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {		
		String cmd = evt.getActionCommand();
		
		if(cmd.equals("Insert"))
		{
			insertData();
		}else if(cmd.equals("Update"))
		{
			updateData();
		}else if(cmd.equals("Delete"))
		{
			deleteData();
		}else if(cmd.equals("Next"))
		{
			next();
		}else if(cmd.equals("Clear"))
		{
			clearControls();	
		}
	}
	
	private void insertData()
	{
		try
		{

			insertStatement.setString(1, txtFname.getText());
			insertStatement.setString(2, txtLname.getText());
			insertStatement.setString(3, txtAddress.getText());
			insertStatement.setString(4, txtSalary.getText());
			insertStatement.execute();

			createMessageBox("Inserted Successfully");
			clearControls();
		}
		catch(Exception e)
		{
			createMessageBox(e.getMessage());
		}
	}
	private void updateData()
	{
		Connection con;
		try
		{
			con = DriverManager.getConnection
				      ("jdbc:sqlite:javabook.db");
			String sql = "Update Employee Set LName='"+txtLname.getText()+"'," +
					"Address='"+txtAddress.getText()+"',Salary='"+
					txtSalary.getText()+"' Where FName='"+txtFname.getText()+"'";
			Statement statement = con.createStatement();
			statement.execute(sql);
			createMessageBox("Updated Successfully");
			clearControls();
		}
		catch(Exception e)
		{
			createMessageBox(e.getMessage());
		}
	}
	private void deleteData()
	{
		Connection con;
		try
		{

			con = DriverManager.getConnection
				      ("jdbc:sqlite:javabook.db");
			String sql = "delete from Employee where FName = '"+txtFname.getText()+"'";
			Statement statement = con.createStatement();
			statement.execute(sql);
			createMessageBox("Record of "+txtFname.getText()+" Deleted Successfully");
			clearControls();
		}
		catch(Exception e)
		{
			createMessageBox(e.getMessage());
		}
	}
	
	private void next()
	{
		try
		{
			if(rs == null)
			{
				
				Connection con = DriverManager.getConnection
					      ("jdbc:sqlite:javabook.db");
				String sql = "Select FName,LName,Address,Salary from Employee";
				PreparedStatement statement = con.prepareStatement(sql);
				rs = statement.executeQuery();				
			}

			if(rs.next() && !rs.isAfterLast())//After Last was giving invalid cursor state error
			{
				populateControls();
			}
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void createMessageBox(String msg)
	{
		JFrame frame = new JFrame("Result");
		JLabel lbl = new JLabel(msg);
		frame.add(lbl);
		frame.setSize(200,200);
		frame.setVisible(true);
	}
	private void clearControls()
	{
		String empty = "";
		
		txtFname.setText(empty);
		txtLname.setText(empty);
		txtAddress.setText(empty);
		txtSalary.setText(empty);
		
		lblfVal.setText(empty);
		lbllVal.setText(empty);
		lblaVal.setText(empty);
		lblsVal.setText(empty);
		rs = null;
	}
	private void populateControls()
	{
		try{
			lblfVal.setText(rs.getString("fName"));
			lbllVal.setText(rs.getString("lName"));
			lblaVal.setText(rs.getString("Address"));
			lblsVal.setText(rs.getString("Salary"));
		
			txtFname.setText(lblfVal.getText());
			txtLname.setText(lbllVal.getText());
			txtAddress.setText(lblaVal.getText());
			txtSalary.setText(lblsVal.getText());
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}




// package com.wxy;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.io.*;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.ArrayList;
// import java.sql.*;

// public class ServerChat {
//     public static void main(String[] args) {
//         ServerFrame serverFrame = new ServerFrame();
//         serverFrame.initialize();
//     }
// }

// class ServerFrame extends JFrame {
//     private JTextArea textArea = new JTextArea();
//     private JButton startButton = new JButton("Start");
//     private JButton stopButton = new JButton("Stop");
//     private static final int PORT = 8888;
//     private ServerSocket serverSocket;
//     private ArrayList<ClientConnection> clientConnections = new ArrayList<>();
//     private boolean isRunning = false;

//     public ServerFrame() {
//         super("Server Window");
//     }

//     public void initialize() {
//         setLayout(new BorderLayout());
//         add(textArea, BorderLayout.CENTER);

//         JPanel buttonPanel = new JPanel();
//         buttonPanel.add(startButton);
//         buttonPanel.add(stopButton);
//         add(buttonPanel, BorderLayout.SOUTH);

//         setBounds(0, 0, 500, 500);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setVisible(true);

//         startButton.addActionListener(this::startServer);
//         stopButton.addActionListener(this::stopServer);
//     }

//     private void startServer(ActionEvent event) {
//         if (!isRunning) {
//             try {
//                 serverSocket = new ServerSocket(PORT);
//                 isRunning = true;
//                 textArea.append("Server started!\n");
//                 new Thread(this::acceptClients).start();
//             } catch (IOException e) {
//                 textArea.append("Failed to start server: " + e.getMessage() + "\n");
//             }
//         }
//     }

//     // private void setupConnection() {
//     //     try {
//     //         socket = new Socket(HOST, PORT);
//     //         outputStream = new DataOutputStream(socket.getOutputStream());
//     //         inputStream = new DataInputStream(socket.getInputStream());
//     //         isConnected = true;
//     //     } catch (IOException e) {
//     //         JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
//     //         isConnected = false;
//     //     }
//     // }
    
    

//     private void stopServer(ActionEvent event) {
//         try {
//             if (serverSocket != null) {
//                 isRunning = false;
//                 serverSocket.close();
//                 textArea.append("Server stopped.\n");
//                 System.exit(0);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private void acceptClients() {
//         while (isRunning) {
//             try {
//                 Socket socket = serverSocket.accept();
//                 ClientConnection connection = new ClientConnection(socket, this);
//                 clientConnections.add(connection);
//                 textArea.append("Client connected: " + socket.getInetAddress() + "/" + socket.getPort() + "\n");
//             } catch (IOException e) {
//                 if (isRunning) {
//                     textArea.append("Error accepting client connection.\n");
//                     e.printStackTrace();
//                 } else {
//                     textArea.append("Server has stopped accepting new connections.\n");
//                 }
//             }
//         }
//     }

//     void broadcastMessage(String message) {
//         clientConnections.forEach(conn -> conn.sendMessage(message));
//     }

//     void broadcastImage(byte[] imageData) {
//         clientConnections.forEach(conn -> conn.sendImage(imageData));
//     }

//     void broadcastAudio(byte[] audioData) {
//         clientConnections.forEach(conn -> conn.sendAudio(audioData));
//     }

//     class ClientConnection implements Runnable {
//         private Socket socket;
//         private DataInputStream inputStream;
//         private DataOutputStream outputStream;
//         private ServerFrame server;

//         public ClientConnection(Socket socket, ServerFrame server) {  // Pass ServerFrame reference here
//             this.socket = socket;
//             this.server = server;  // Set the server instance
//             try {
//                 inputStream = new DataInputStream(socket.getInputStream());
//                 outputStream = new DataOutputStream(socket.getOutputStream());
//                 new Thread(this).start();
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }

//         @Override
//         public void run() {
//             try {
//                 // while (isRunning) {
//                 //     int type = inputStream.readInt(); // Read message type
//                 //     if (type == 0) { // Text message
//                 //         String receivedMessage = inputStream.readUTF();
//                 //         broadcastMessage(receivedMessage);
//                 //     } else if (type == 1) { // Image data
//                 //         int length = inputStream.readInt();
//                 //         byte[] imageData = new byte[length];
//                 //         inputStream.readFully(imageData);
//                 //         broadcastImage(imageData);
//                 //     } else if (type == 2) { // Audio data
//                 //         int length = inputStream.readInt();
//                 //         byte[] audioData = new byte[length];
//                 //         inputStream.readFully(audioData);
//                 //         broadcastAudio(audioData);
//                 //     }
//                 // }
//                 while (server.isRunning) {
//                         int commandType = inputStream.readInt();
//                         switch (commandType) {
//                             case 0: // Text message
//                                 String receivedMessage = inputStream.readUTF();
//                                 broadcastMessage(receivedMessage);
//                                 break;
//                             case 1: // Image data
//                                 int length = inputStream.readInt();
//                                 byte[] imageData = new byte[length];
//                                 inputStream.readFully(imageData);
//                                 broadcastImage(imageData);
//                                 break;
//                             case 2: // Audio data
//                                 length = inputStream.readInt();
//                                 byte[] audioData = new byte[length];
//                                 inputStream.readFully(audioData);
//                                 broadcastAudio(audioData);
//                                 break;
//                             case 3: // Login
//                                 handleLogin();
//                                 break;
//                             case 4: // Signup
//                                 handleSignup();
//                                 break;
//                             default:
//                                 server.textArea.append("Received unknown command type: " + commandType + "\n");
//                                 break;
//                         }
//                     }
//             } catch (IOException e) {
//                 textArea.append("Client disconnected: " + socket.getInetAddress() + "/" + socket.getPort() + "\n");
//             } finally {
//                 try {
//                     socket.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
                
//         }
        

//         public void sendMessage(String message) {
//             try {
//                 outputStream.writeInt(0); // Indicate this is a text message
//                 outputStream.writeUTF(message);
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }

//         public void sendImage(byte[] imageData) {
//             try {
//                 outputStream.writeInt(1); // Indicate this is an image
//                 outputStream.writeInt(imageData.length);
//                 outputStream.write(imageData);
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }

//         public void sendAudio(byte[] audioData) {
//             try {
//                 outputStream.writeInt(2); // Indicate this is an audio
//                 outputStream.writeInt(audioData.length);
//                 outputStream.write(audioData, 0, audioData.length);
//                 outputStream.flush(); // Ensure all data is sent immediately
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//         private void handleLogin() throws IOException {
//             String username = inputStream.readUTF();
//             String password = inputStream.readUTF();
//             try (Connection conn = DriverManager.getConnection("jdbc:sqlite:JavaFinal.db")) {
//                 server.textArea.append("Database connected successfully for login.\n");
//                 PreparedStatement stmt = conn.prepareStatement("SELECT password FROM Users WHERE username = ?");
//                 stmt.setString(1, username);
//                 ResultSet rs = stmt.executeQuery();
//                 if (rs.next() && rs.getString("password").equals(password)) {
//                     outputStream.writeInt(1);
//                     server.textArea.append(username + " logged in successfully.\n");
//                 } else {
//                     outputStream.writeInt(0);
//                     server.textArea.append("Login failed for user: " + username + "\n");
//                 }
//             } catch (SQLException e) {
//                 outputStream.writeInt(0);
//                 server.textArea.append("Database error on login: " + e.getMessage() + "\n");
//             }
//         }
    
//         private void handleSignup() throws IOException {
//             String username = inputStream.readUTF();
//             String password = inputStream.readUTF();

//             try (Connection conn = DriverManager.getConnection("jdbc:sqlite:JavaFinal.db")) {
//                 server.textArea.append("Database connected successfully for signup.\n");
//                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)");
//                 stmt.setString(1, username);
//                 stmt.setString(2, password);
//                 stmt.executeUpdate();
//                 outputStream.writeInt(1);
//                 server.textArea.append(username + " signed up successfully.\n");
//             } catch (SQLException e) {
//                 outputStream.writeInt(0);
//                 server.textArea.append("Database error on signup: " + e.getMessage() + "\n");
//             }
//         }

//     }
// }




// //------------------

// package com.wxy;

// import javax.sound.sampled.*;
// import javax.swing.*;
// import javax.swing.filechooser.FileNameExtensionFilter;
// import javax.swing.text.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.io.*;
// import java.net.Socket;

// public class ClientChat {
//     public static void main(String[] args) {
//         // ClientFrame clientFrame = new ClientFrame();
//         // clientFrame.initialize();
//         LoginFrame loginFrame = new LoginFrame();
//         loginFrame.setVisible(true);
//     }
// }

// class LoginFrame extends JFrame {
//     private JTextField usernameField = new JTextField(15);
//     private JPasswordField passwordField = new JPasswordField(15);
//     private JButton loginButton = new JButton("Login");
//     private JButton signupButton = new JButton("Signup");
//     private static final String HOST = "127.0.0.1";
//     private static final int PORT = 8888;

//     public LoginFrame() {
//         super("Login");
//         setLayout(new FlowLayout());
//         add(new JLabel("Username:"));
//         add(usernameField);
//         add(new JLabel("Password:"));
//         add(passwordField);
//         add(loginButton);
//         add(signupButton);
//         pack();
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         loginButton.addActionListener(this::login);
//         signupButton.addActionListener(this::signup);
//     }

//     private void login(ActionEvent e) {
//         try (Socket socket = new Socket(HOST, PORT);
//              DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//              DataInputStream in = new DataInputStream(socket.getInputStream())) {
//             out.writeInt(3); // Login command index updated to match server's expectation.
//             out.writeUTF(usernameField.getText());
//             out.writeUTF(new String(passwordField.getPassword()));
//             int response = in.readInt();
//             if (response == 1) {
//                 JOptionPane.showMessageDialog(this, "Login Successful", "Login", JOptionPane.INFORMATION_MESSAGE);
//             } else {
//                 JOptionPane.showMessageDialog(this, "Login Failed", "Login", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (IOException ex) {
//             JOptionPane.showMessageDialog(this, "Server Connection Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void signup(ActionEvent e) {
//         try (Socket socket = new Socket(HOST, PORT);
//              DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//              DataInputStream in = new DataInputStream(socket.getInputStream())) {
//             out.writeInt(4); // Signup command index updated to match server's expectation.
//             out.writeUTF(usernameField.getText());
//             out.writeUTF(new String(passwordField.getPassword()));
//             int response = in.readInt();
//             if (response == 1) {
//                 JOptionPane.showMessageDialog(this, "Signup Successful", "Signup", JOptionPane.INFORMATION_MESSAGE);
//             } else {
//                 JOptionPane.showMessageDialog(this, "Signup Failed", "Signup", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (IOException ex) {
//             JOptionPane.showMessageDialog(this, "Server Connection Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }
    
// }

// class ClientFrame extends JFrame {
//     private JTextPane chatDisplayPane = new JTextPane();
//     private JTextField chatInputField = new JTextField(20);
//     private JButton sendImageButton = new JButton("Send Image");
//     private JButton recordAudioButton = new JButton("Record Audio");
//     private JButton stopRecordButton = new JButton("Stop & Send");
//     private JScrollPane scrollPane;

//     private JTextField usernameField = new JTextField(15);
//     private JPasswordField passwordField = new JPasswordField(15);
//     private JButton loginButton = new JButton("Login");
//     private JButton signupButton = new JButton("Signup");

//     private static final String HOST = "127.0.0.1";
//     private static final int PORT = 8888;
//     private Socket socket;
//     private DataOutputStream outputStream;
//     private DataInputStream inputStream;
//     private boolean isConnected = false;

//     private TargetDataLine audioLine;
//     private AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000.0f, 16, 1, 2, 16000.0f, false);
//     private ByteArrayOutputStream audioOutputStream;

//     public ClientFrame() {
//         super("Client Chat Window");
//     }

//     public void initialize() {
//         setLayout(new BorderLayout());
//         chatDisplayPane.setEditable(false);
//         scrollPane = new JScrollPane(chatDisplayPane);
//         add(scrollPane, BorderLayout.CENTER);

//         JPanel bottomPanel = new JPanel(new BorderLayout());
//         bottomPanel.add(chatInputField, BorderLayout.CENTER);

//         JPanel buttonPanel = new JPanel(new FlowLayout());
//         buttonPanel.add(sendImageButton);
//         buttonPanel.add(recordAudioButton);
//         buttonPanel.add(stopRecordButton);
//         stopRecordButton.setEnabled(false); // Initially disable the stop button

//         bottomPanel.add(buttonPanel, BorderLayout.EAST);
//         add(bottomPanel, BorderLayout.SOUTH);

//         JPanel loginPanel = new JPanel();
//         loginPanel.add(new JLabel("Username:"));
//         loginPanel.add(usernameField);
//         loginPanel.add(new JLabel("Password:"));
//         loginPanel.add(passwordField);
//         loginPanel.add(loginButton);
//         loginPanel.add(signupButton);

//         add(loginPanel, BorderLayout.NORTH);

//         setBounds(300, 300, 500, 400);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         chatInputField.requestFocus();


//         setupConnection();
//         setupInputFieldListener();
//         setupSendImageButtonListener();
//         setupAudioButtons();
//         startReceivingMessages();
//         setupActions();

//         setVisible(true);
//     }

//     private void setupActions() {
//         loginButton.addActionListener(e -> sendLogin());
//         signupButton.addActionListener(e -> sendSignup());
//     }

//     private void setupConnection() {
//         try {
//             socket = new Socket(HOST, PORT);
//             outputStream = new DataOutputStream(socket.getOutputStream());
//             isConnected = true;
//         } catch (IOException e) {
//             JOptionPane.showMessageDialog(this, "Failed to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void setupInputFieldListener() {
//         chatInputField.addActionListener(e -> {
//             String message = chatInputField.getText().trim();
//             if (!message.isEmpty()) {
//                 sendMessage(message);
//                 chatInputField.setText("");
//             }
//         });
//     }

//     private void setupSendImageButtonListener() {
//         sendImageButton.addActionListener(e -> {
//             JFileChooser fileChooser = new JFileChooser();
//             fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
//             int result = fileChooser.showOpenDialog(this);
//             if (result == JFileChooser.APPROVE_OPTION) {
//                 File selectedFile = fileChooser.getSelectedFile();
//                 sendImage(selectedFile);
//             }
//         });
//     }

//     private void setupAudioButtons() {
//         recordAudioButton.addActionListener(e -> {
//             startRecording();
//             recordAudioButton.setEnabled(false);
//             stopRecordButton.setEnabled(true);
//         });

//         stopRecordButton.addActionListener(e -> {
//             stopRecording();
//             recordAudioButton.setEnabled(true);
//             stopRecordButton.setEnabled(false);
//             sendAudio();
//         });
//     }

//     private void startRecording() {
//         try {
//             DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
//             audioLine = (TargetDataLine) AudioSystem.getLine(info);
//             audioLine.open(format);
//             audioLine.start();
//             audioOutputStream = new ByteArrayOutputStream();
//             Thread thread = new Thread(this::captureAudio);
//             thread.start();
//         } catch (LineUnavailableException e) {
//             JOptionPane.showMessageDialog(this, "Audio line unavailable.", "Recording Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void captureAudio() {
//         byte[] buffer = new byte[1024];
//         int bytesRead;
//         while (audioLine.isOpen()) {
//             bytesRead = audioLine.read(buffer, 0, buffer.length);
//             if (bytesRead > 0) {
//                 audioOutputStream.write(buffer, 0, bytesRead);
//             }
//         }
//     }

//     private void stopRecording() {
//         try {
//             audioLine.stop();
//             audioLine.close();
            
// //            // Save the recorded audio to a file
// //            File outputFile = new File("recorded_audio.wav");
// //            byte[] audioBytes = audioOutputStream.toByteArray();
// //            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
// //            AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());
// //            
// //            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
// //            JOptionPane.showMessageDialog(null, "Audio recorded successfully and saved to " + outputFile.getAbsolutePath());
//         } finally {
//             if (audioOutputStream != null) {
//                 try {
//                     audioOutputStream.close();
//                 } catch (IOException ex) {
//                     ex.printStackTrace();
//                 }
//             }
//         }
//     }


//     private void sendAudio() {
//         try {
//             outputStream.writeInt(2); // 2 means audio message
//             byte[] audioBytes = audioOutputStream.toByteArray();
//             outputStream.writeInt(audioBytes.length);
//             outputStream.write(audioBytes);
//         } catch (IOException e) {
//             JOptionPane.showMessageDialog(this, "Failed to send audio.", "Sending Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void sendMessage(String message) {
//         try {
//             outputStream.writeInt(0); // 0 means text message
//             outputStream.writeUTF(message);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     private void sendImage(File file) {
//         try (FileInputStream fis = new FileInputStream(file)) {
//             byte[] buffer = new byte[fis.available()];
//             fis.read(buffer);
//             outputStream.writeInt(1); // 1 means image
//             outputStream.writeInt(buffer.length);
//             outputStream.write(buffer);
//         } catch (IOException ex) {
//             JOptionPane.showMessageDialog(this, "Failed to send image.", "Sending Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void sendLogin() {
//         try {
//             outputStream.writeInt(0); // Login command
//             outputStream.writeUTF(usernameField.getText());
//             outputStream.writeUTF(new String(passwordField.getPassword())); // Send password as hashed
//             if (inputStream.readInt() == 1) {
//                 // Login success
//                 JOptionPane.showMessageDialog(this, "Login Successful", "Login", JOptionPane.INFORMATION_MESSAGE);
//             } else {
//                 // Login failure
//                 JOptionPane.showMessageDialog(this, "Login Failed", "Login", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (IOException ex) {
//             JOptionPane.showMessageDialog(this, "Error sending login data", "Network Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void sendSignup() {
//         try {
//             outputStream.writeInt(1); // Signup command
//             outputStream.writeUTF(usernameField.getText());
//             outputStream.writeUTF(new String(passwordField.getPassword())); // Send password as hashed
//             if (inputStream.readInt() == 1) {
//                 // Signup success
//                 JOptionPane.showMessageDialog(this, "Signup Successful", "Signup", JOptionPane.INFORMATION_MESSAGE);
//             } else {
//                 // Signup failure
//                 JOptionPane.showMessageDialog(this, "Signup Failed", "Signup", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (IOException ex) {
//             JOptionPane.showMessageDialog(this, "Error sending signup data", "Network Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }


//     private void startReceivingMessages() {
//         new Thread(new MessageReceiver()).start();
//     }

//     class MessageReceiver implements Runnable {
//         @Override
//         public void run() {
//             try {
//                 DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//                 while (isConnected) {
//                     int type = inputStream.readInt();
//                     if (type == 0) {
//                         handleTextMessage(inputStream);
//                     } else if (type == 1) {
//                         handleImageMessage(inputStream);
//                     } else if (type == 2) {
//                         handleAudioMessage(inputStream);
//                     }
//                 }
//             } catch (IOException e) {
//                 System.err.println("Connection error: " + e.getMessage());
//                 isConnected = false;
//             }
//         }

//         private void handleTextMessage(DataInputStream inputStream) throws IOException {
//             String messageReceived = inputStream.readUTF();
//             SwingUtilities.invokeLater(() -> {
//                 try {
//                     Document doc = chatDisplayPane.getDocument();
//                     doc.insertString(doc.getLength(), messageReceived + "\n", null);
//                 } catch (BadLocationException e) {
//                     e.printStackTrace();
//                 }
//             });
//         }

//         private void handleImageMessage(DataInputStream inputStream) throws IOException {
//             int length = inputStream.readInt();
//             byte[] messageBytes = new byte[length];
//             inputStream.readFully(messageBytes);
//             ImageIcon originalIcon = new ImageIcon(messageBytes);
//             SwingUtilities.invokeLater(() -> {
//                 Image image = originalIcon.getImage();
//                 Image resizedImage = image.getScaledInstance(image.getWidth(null) / 20, image.getHeight(null) / 20, Image.SCALE_SMOOTH);
//                 ImageIcon resizedIcon = new ImageIcon(resizedImage);
//                 chatDisplayPane.insertComponent(new JLabel(resizedIcon));
//                 try {
//                     Document doc = chatDisplayPane.getDocument();
//                     doc.insertString(doc.getLength(), "\n", null);
//                 } catch (BadLocationException e) {
//                     e.printStackTrace();
//                 }
//             });
//         }

//         private void handleAudioMessage(DataInputStream inputStream) throws IOException {
//         	int length = inputStream.readInt();
//             byte[] audioBytes = new byte[length];
//             int bytesRead = 0;
//             while (bytesRead < length) {
//                 bytesRead += inputStream.read(audioBytes, bytesRead, length - bytesRead);
//             }
//             SwingUtilities.invokeLater(() -> {
//                 JButton playButton = new JButton("Play Audio");
//                 playButton.addActionListener(e -> playAudio(audioBytes));
//                 try {
//                     Document doc = chatDisplayPane.getDocument();
//                     Style style = chatDisplayPane.addStyle("StyleName", null);
//                     StyleConstants.setComponent(style, playButton);
//                     doc.insertString(doc.getLength(), "Incoming audio: ", null);
//                     doc.insertString(doc.getLength(), "\n", style);
//                 } catch (BadLocationException ex) {
//                     ex.printStackTrace();
//                 }
//             });
//         }

//         private void playAudio(byte[] audioBytes) {
//             try {
//                 InputStream input = new ByteArrayInputStream(audioBytes);
//                 AudioInputStream audioStream = new AudioInputStream(input, format, audioBytes.length / format.getFrameSize());
// //            	File audioFile = new File("/Users/panxuanen/Documents/Projects/CS9053 Java/Final_Project/2.wav");
// //                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
//                 DataLine.Info info = new DataLine.Info(Clip.class, format);
//                 Clip clip = (Clip) AudioSystem.getLine(info);
//                 clip.open(audioStream);
//                 clip.start();
//                 clip.addLineListener(event -> {
//                     if (event.getType() == LineEvent.Type.STOP) {
//                         clip.close();
//                     }
//                 });
//             } catch (Exception e) {
//                 JOptionPane.showMessageDialog(null, "Error playing audio: " + e.getMessage(), "Playback Error", JOptionPane.ERROR_MESSAGE);
//             }
//         }
//     }
// }
