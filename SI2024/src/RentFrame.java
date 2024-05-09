import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class RentFrame extends JFrame{
	
	Connection conn=null;
	PreparedStatement state=null;
	ResultSet result=null;
	int id=-1;
	int personId=-1;
	int carId=-1;
	
	
	//initialize the 3 panels
	JPanel upPanel=new JPanel();
	JPanel midPanel=new JPanel();
	JPanel downPanel=new JPanel();
	
	
	//initialize labels
	JLabel personL=new JLabel("Име на наемателя:");
	JLabel carL=new JLabel("Автомобил:");
	JLabel startDateL=new JLabel("Дата на наемане:");
	JLabel endDateL=new JLabel("Дата на връщане:");
	JLabel priceL=new JLabel("Цена:");
	
	
	//initialize text input fileds
	JTextField startDateTF=new JTextField();
	JTextField endDateTF=new JTextField();
	JTextField priceTF=new JTextField();

	
	//person and car combo boxes
	JComboBox<String> personCombo=new JComboBox<String>();
	JComboBox<String> carCombo=new JComboBox<String>();
	
	//initialize buttons
	JButton addBt=new JButton("Добавяне");
	JButton deleteBt=new JButton("Изтриване");
	JButton editBt=new JButton("Промяна");
	JButton searchBt=new JButton("Търсене по максимална цена");
	JButton refreshBt=new JButton("Обнови");
	
	
	//initialize table
	JTable table=new JTable();
	JScrollPane myScroll=new JScrollPane(table);
	
	
	//visualizing the frame on the screen
	public RentFrame() {
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3,1));
		
		//upPanel-----------------------------------
		upPanel.setLayout(new GridLayout(5,2));
		upPanel.add(personL);
		upPanel.add(personCombo);
		upPanel.add(carL);
		upPanel.add(carCombo);
		upPanel.add(startDateL);
		upPanel.add(startDateTF);
		upPanel.add(endDateL);
		upPanel.add(endDateTF);
		upPanel.add(priceL);
		upPanel.add(priceTF);
		
		
		this.add(upPanel);
		
		
		//midPanel----------------------------------
		midPanel.add(addBt);
		midPanel.add(deleteBt);
		midPanel.add(editBt);
		midPanel.add(searchBt);
		midPanel.add(refreshBt);
		
		this.add(midPanel);
		
		
		//attach event listeners
		addBt.addActionListener(new AddAction());
		deleteBt.addActionListener(new DeleteAction());
		searchBt.addActionListener(new SearchAction());
		refreshBt.addActionListener(new RefreshAction());
		editBt.addActionListener(new EditAction());
		
		//downPanel---------------------------------
		
		myScroll.setPreferredSize(new Dimension(950, 150));
		downPanel.add(myScroll);
		
		this.add(downPanel);
		table.addMouseListener(new MouseAction());
		
		
		refreshTable();
		refreshPersonCombo();
		refreshCarCombo();
		
		this.setVisible(true);
	}
	
	public void refreshTable() {
		conn=DBConnection.getConnection();
		try {
			state=conn.prepareStatement("SELECT RentID, fname, lname, make, model, RentalDate, ReturnDate, RentalFee from person "
									  + "JOIN Rent ON person.id=rent.PersonID "
									  + "JOIN Cars ON rent.CarID=cars.CarID");
			
			result=state.executeQuery();
			table.setModel(new MyModel(result));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshPersonCombo() {
		personCombo.removeAllItems();
		conn=DBConnection.getConnection();
		String sql="select ID, fname, lname from person";
		String item="";
		
		try {
			state=conn.prepareStatement(sql);
			result=state.executeQuery();
			while(result.next()) {
				item=
				result.getObject(2).toString()+" "+
				result.getObject(3).toString();
				
				personId=Integer.parseInt(result.getObject(1).toString());
				
				personCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshCarCombo() {
		carCombo.removeAllItems();
		conn=DBConnection.getConnection();
		String sql="select CarID, Make, Model from Cars";
		String item="";
		
		try {
			state=conn.prepareStatement(sql);
			result=state.executeQuery();
			while(result.next()) {
				item=
				result.getObject(2).toString()+" "+
				result.getObject(3).toString();
				
				carId=Integer.parseInt(result.getObject(1).toString());
				
				carCombo.addItem(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearForm() {
		startDateTF.setText("");
		endDateTF.setText("");
		priceTF.setText("");
	}

	class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="insert into Rent(PersonID, CarID, RentalDate, ReturnDate, RentalFee) values(?,?,?,?,?)";
			
			try {
				updatePersonId(personCombo.getItemAt(personCombo.getSelectedIndex()));
				updateCarId(carCombo.getItemAt(carCombo.getSelectedIndex()));
				state=conn.prepareStatement(sql);
				state.setInt(1, personId);
				state.setInt(2, carId);
				state.setString(3, startDateTF.getText());
				state.setString(4, endDateTF.getText());
				state.setFloat(5, Float.parseFloat(priceTF.getText()));
				state.execute();
				refreshTable();
				refreshPersonCombo();
				refreshCarCombo();
				clearForm();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	class EditAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="update Rent set PersonID=?, CarID=?, RentalDate=?, ReturnDate=?, RentalFee=? where RentID=?";
			
			try {
				updatePersonId(personCombo.getItemAt(personCombo.getSelectedIndex()));
				updateCarId(carCombo.getItemAt(carCombo.getSelectedIndex()));
				state=conn.prepareStatement(sql);
				state.setInt(1, personId);
				state.setInt(2, carId);
				state.setString(3, startDateTF.getText());
				state.setString(4, endDateTF.getText());
				state.setFloat(5, Float.parseFloat(priceTF.getText()));
				state.setInt(6, id);
				
				state.execute();
				refreshTable();
				refreshPersonCombo();
				refreshCarCombo();
				clearForm();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	class MouseAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			int row=table.getSelectedRow();
			id=Integer.parseInt(table.getValueAt(row, 0).toString());
			
			String personFName=table.getValueAt(row, 1).toString();
			String personLName=table.getValueAt(row, 2).toString();
			updatePersonCombo(personFName, personLName);
			
			String carMake=table.getValueAt(row, 3).toString();
			String carModel=table.getValueAt(row, 4).toString();
			updateCarCombo(carMake,carModel);
			
			startDateTF.setText(table.getValueAt(row, 5).toString());
			endDateTF.setText(table.getValueAt(row, 6).toString());
			priceTF.setText(table.getValueAt(row, 7).toString());
		}
		
		private void updatePersonCombo(String firstName, String lastName) {
		    String fullNameToFind = firstName + " " + lastName;
		    for (int i = 0; i < personCombo.getItemCount(); i++) {
		        String item = personCombo.getItemAt(i);
		        if (item.equals(fullNameToFind)) {
		            personCombo.setSelectedIndex(i);
		            break;
		        }
		    }
		}
		
		
		private void updateCarCombo(String make, String model) {
		    String fullNameToFind = make + " " + model;
		    for (int i = 0; i < carCombo.getItemCount(); i++) {
		        String item = carCombo.getItemAt(i);
		        if (item.equals(fullNameToFind)) {
		        	carCombo.setSelectedIndex(i);
		            break;
		        }
		    }
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void updatePersonId(String fullName) {
		conn=DBConnection.getConnection();
		String sql="select ID, fname, lname from person";

		try {
			state=conn.prepareStatement(sql);
			result=state.executeQuery();
			while(result.next()) 
			{			
				String currentFullName=result.getObject(2).toString()+" "+result.getObject(3).toString();
				if(currentFullName.equals(fullName))
				{
					personId=Integer.parseInt(result.getObject(1).toString());
					return;
				}
			}
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateCarId(String fullName) {
		conn=DBConnection.getConnection();
		String sql="select CarID, Make, Model from Cars";

		try {
			state=conn.prepareStatement(sql);
			result=state.executeQuery();
			while(result.next()) 
			{			
				String currentFullName=result.getObject(2).toString()+" "+result.getObject(3).toString();
				if(currentFullName.equals(fullName))
				{
					carId=Integer.parseInt(result.getObject(1).toString());
					return;
				}
			}
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="delete from Rent where RentID=?";
			
			try {
				state=conn.prepareStatement(sql);
				state.setInt(1, id);
				state.execute();
				refreshTable();
				refreshPersonCombo();
				refreshCarCombo();
				clearForm();
				id=-1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	class SearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="select * from Rent where RentalFee<=?";
			try {
				if(!priceTF.getText().isEmpty())
				{
					state=conn.prepareStatement(sql);
					state.setFloat(1, Float.parseFloat(priceTF.getText()));
					result=state.executeQuery();
					table.setModel(new MyModel(result));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	class RefreshAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			refreshTable();
			clearForm();
			refreshPersonCombo();
			refreshCarCombo();
		}
		
	}
}
