import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

public class StoreObject {

	public StoreObject() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PreparedStatement preparedStatement;
		Connection connection = null;
		
		TestSerializableObject testSerializableObject = new TestSerializableObject("Dean", 1234567);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			connection = DriverManager.getConnection
			      ("jdbc:sqlite:javabook.db");
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(testSerializableObject);
		  out.flush();
		  byte[] yourBytes = bos.toByteArray();
		  String insertString = "INSERT INTO objectstore VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertString);
			preparedStatement.setBytes(2, yourBytes);
			preparedStatement.setInt(1, 1234567);
			preparedStatement.setLong(3, TestSerializableObject.serialVersionUID);
		  preparedStatement.execute();
		  
		  String queryString = "SELECT objectdata FROM objectstore WHERE id = ?";
		  preparedStatement = connection.prepareStatement(queryString);
		  preparedStatement.setInt(1, 1234567);
		  ResultSet rs = preparedStatement.executeQuery();
			rs.next();

			byte[] buf = rs.getBytes(1);
			ObjectInputStream objectIn = null;
			if (buf != null)
				objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

			Object deSerializedObject = objectIn.readObject();
			TestSerializableObject receivedObject = (TestSerializableObject)deSerializedObject;
			rs.close();
			preparedStatement.close();
			System.out.println("receivedObject is: " + receivedObject.toString());
				  
		  
		} catch (IOException | SQLException | ClassNotFoundException ex) {
		    ex.printStackTrace();
		 }

	}

}
