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

public class SearchFrame extends JFrame{
	
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
	JLabel firstNameL=new JLabel("Име на наемателя:");
	JLabel lastNameL=new JLabel("Фамилия на наемателя:");
	JLabel makeL=new JLabel("Марка:");
	JLabel modelL=new JLabel("Модел:");
	JLabel minYearL=new JLabel("Минималка година на производство:");
	JLabel maxYearL=new JLabel("Максимална година на производство:");
	JLabel minPriceL=new JLabel("Минимална цена:");
	JLabel maxPriceL=new JLabel("Максимална цена:");
	
	
	//initialize text input fileds
	JTextField firstNameTF=new JTextField();
	JTextField lastNameTF=new JTextField();
	JTextField makeTF=new JTextField();
	JTextField modelTF=new JTextField();
	JTextField minYearTF=new JTextField();
	JTextField maxYearTF=new JTextField();
	JTextField minPriceTF=new JTextField();
	JTextField maxPriceTF=new JTextField();

	
	//initialize buttons
	JButton searchBt=new JButton("Търсенe");
	JButton refreshBt=new JButton("Обнови");
	
	
	//initialize table
	JTable table=new JTable();
	JScrollPane myScroll=new JScrollPane(table);
	
	
	//visualizing the frame on the screen
	public SearchFrame() {
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3,1));
		
		//upPanel-----------------------------------
		upPanel.setLayout(new GridLayout(5,2));
		upPanel.add(firstNameL);
		upPanel.add(firstNameTF);
		upPanel.add(lastNameL);
		upPanel.add(lastNameTF);
		upPanel.add(makeL);
		upPanel.add(makeTF);
		upPanel.add(modelL);
		upPanel.add(modelTF);
		upPanel.add(minYearL);
		upPanel.add(minYearTF);
		upPanel.add(maxYearL);
		upPanel.add(maxYearTF);
		upPanel.add(minPriceL);
		upPanel.add(minPriceTF);
		upPanel.add(maxPriceL);
		upPanel.add(maxPriceTF);
		
		
		this.add(upPanel);
		
		
		//midPanel----------------------------------
		midPanel.add(searchBt);
		midPanel.add(refreshBt);
		
		this.add(midPanel);
		
		
		//attach event listeners
		searchBt.addActionListener(new SearchAction());
		refreshBt.addActionListener(new RefreshAction());
		
		//downPanel---------------------------------
		
		myScroll.setPreferredSize(new Dimension(950, 150));
		downPanel.add(myScroll);
		
		this.add(downPanel);
		
		
		refreshTable();
		this.setVisible(true);
	}
	
	public void refreshTable() {
		conn=DBConnection.getConnection();
		try {
			state=conn.prepareStatement("SELECT RentID, fname, lname, make, model, ProductionYear, RentalDate, ReturnDate, RentalFee from person "
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
	
	public void clearForm() {
		firstNameTF.setText("");
		lastNameTF.setText("");
		makeTF.setText("");
		modelTF.setText("");
		minYearTF.setText("");
		maxYearTF.setText("");
		minPriceTF.setText("");
		maxPriceTF.setText("");
	}
	
	
	
	class SearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="SELECT RentID, fname, lname, make, model, ProductionYear, RentalDate, ReturnDate, RentalFee FROM person "
					 + "JOIN rent ON person.id=rent.personid "
					 + "JOIN cars ON cars.carid=rent.carid WHERE ";
			
			int criteriaAdded=0;
			
			 if (!firstNameTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "person.fname = '" + firstNameTF.getText() + "'";
		            criteriaAdded ++;
		        }
			 
			 if (!lastNameTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "person.lname = '" + lastNameTF.getText() + "'";
		            criteriaAdded ++;
		        }
			 
			 if (!makeTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "cars.make = '" + makeTF.getText() + "'";
		            criteriaAdded ++;
		        }
			 
			 if (!modelTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "cars.model = '" + modelTF.getText() + "'";
		            criteriaAdded ++;
		        }
			 
			 if (!minYearTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "cars.productionYear >= " + minYearTF.getText();
		            criteriaAdded ++;
		        }
			 
			 if (!maxYearTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "cars.productionYear <= " + maxYearTF.getText();
		            criteriaAdded ++;
		        }
			 
			 if (!minPriceTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "rent.RentalFee >= " + minPriceTF.getText();
		            criteriaAdded ++;
		        }
			 
			 if (!maxPriceTF.getText().isEmpty()) {
				 if(criteriaAdded>0) {
					 sql+="AND ";
				 }
		            sql += "rent.RentalFee <= " + maxPriceTF.getText();
		            criteriaAdded ++;
		        }
			 
			try {
				state=conn.prepareStatement(sql);
				result=state.executeQuery();
				table.setModel(new MyModel(result));
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
		}
		
	}
}
