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

public class MyFrame extends JFrame{
	
	Connection conn=null;
	PreparedStatement state=null;
	ResultSet result=null;
	int id=-1;
	
	
	//initialize the 3 panels
	JPanel upPanel=new JPanel();
	JPanel midPanel=new JPanel();
	JPanel downPanel=new JPanel();
	
	
	//initialize labels
	JLabel fnameL=new JLabel("Име:");
	JLabel lnameL=new JLabel("Фамилия:");
	JLabel sexL=new JLabel("Пол:");
	JLabel ageL=new JLabel("Години:");
	JLabel salaryL=new JLabel("Заплата:");
	
	
	//initialize text input fileds
	JTextField fnameTF=new JTextField();
	JTextField lnameTF=new JTextField();
	JTextField ageTF=new JTextField();
	JTextField salaryTF=new JTextField();
	
	
	//prepare data for sex combo box
	String[] item= {"Мъж","Жена"};
	JComboBox<String> sexCombo=new JComboBox<String>(item);
	//JComboBox<String> personCombo=new JComboBox<String>();
	
	
	//initialize buttons
	JButton addBt=new JButton("Добавяне");
	JButton deleteBt=new JButton("Изтриване");
	JButton editBt=new JButton("Промяна");
	JButton searchBt=new JButton("Търсене по години");
	JButton refreshBt=new JButton("Обнови");
	
	
	//initialize table
	JTable table=new JTable();
	JScrollPane myScroll=new JScrollPane(table);
	
	
	//visualizing the frame on the screen
	public MyFrame() {
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3,1));
		
		//upPanel-----------------------------------
		upPanel.setLayout(new GridLayout(5,2));
		upPanel.add(fnameL);
		upPanel.add(fnameTF);
		upPanel.add(lnameL);
		upPanel.add(lnameTF);
		upPanel.add(sexL);
		upPanel.add(sexCombo);
		upPanel.add(ageL);
		upPanel.add(ageTF);
		upPanel.add(salaryL);
		upPanel.add(salaryTF);
		
		
		this.add(upPanel);
		
		
		//midPanel----------------------------------
		midPanel.add(addBt);
		midPanel.add(deleteBt);
		midPanel.add(editBt);
		midPanel.add(searchBt);
		midPanel.add(refreshBt);
		//midPanel.add(personCombo);
		
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
		//refreshPersonCombo();
		
		this.setVisible(true);
	}
	
	public void refreshTable() {
		conn=DBConnection.getConnection();
		try {
			state=conn.prepareStatement("select * from person");
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
	
//	public void refreshPersonCombo() {
//		personCombo.removeAllItems();
//		conn=DBConnection.getConnection();
//		String sql="select id, fname, lname from person";
//		String item="";
//		
//		try {
//			state=conn.prepareStatement(sql);
//			result=state.executeQuery();
//			while(result.next()) {
//				item=result.getObject(1).toString()+"."+
//			result.getObject(2).toString()+" "+
//			result.getObject(3).toString();
//				
//				personCombo.addItem(item);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void clearForm() {
		fnameTF.setText("");
		lnameTF.setText("");
		ageTF.setText("");
		salaryTF.setText("");
	}

	class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="insert into person(fname, lname, sex, age, salary) values(?,?,?,?,?)";
			
			try {
				state=conn.prepareStatement(sql);
				state.setString(1, fnameTF.getText());
				state.setString(2, lnameTF.getText());
				state.setString(3, sexCombo.getSelectedItem().toString());
				state.setInt(4, Integer.parseInt(ageTF.getText()));
				state.setFloat(5, Float.parseFloat(salaryTF.getText()));
				
				state.execute();
				refreshTable();
				//refreshPersonCombo();
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
			String sql="update person set fname=?, lname=?, sex=?, age=?, salary=? where id=?";
			
			try {
				state=conn.prepareStatement(sql);
				state.setString(1, fnameTF.getText());
				state.setString(2, lnameTF.getText());
				state.setString(3, sexCombo.getSelectedItem().toString());
				state.setInt(4, Integer.parseInt(ageTF.getText()));
				state.setFloat(5, Float.parseFloat(salaryTF.getText()));
				state.setInt(6, id);
				
				state.execute();
				refreshTable();
				//refreshPersonCombo();
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
			fnameTF.setText(table.getValueAt(row, 1).toString());
			lnameTF.setText(table.getValueAt(row, 2).toString());
			ageTF.setText(table.getValueAt(row, 4).toString());
			salaryTF.setText(table.getValueAt(row, 5).toString());
			if(table.getValueAt(row, 3).toString().equals("Мъж")) {
				sexCombo.setSelectedIndex(0);
			}
			else {
				sexCombo.setSelectedIndex(1);
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
	
	class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="delete from rent where personid=?; delete from person where id=?";
			
			try {
				state=conn.prepareStatement(sql);
				state.setInt(1, id);
				state.setInt(2, id);
				state.execute();
				refreshTable();
				//refreshPersonCombo();
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
			String sql="select * from person where age=?";
			try {
				if(!ageTF.getText().isEmpty()) 
				{					
					state=conn.prepareStatement(sql);
					state.setInt(1, Integer.parseInt(ageTF.getText()));
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
		}
		
	}
}
