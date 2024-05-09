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

public class CarFrame extends JFrame{
	
	Connection conn=null;
	PreparedStatement state=null;
	ResultSet result=null;
	int id=-1;
	
	
	//initialize the 3 panels
	JPanel upPanel=new JPanel();
	JPanel midPanel=new JPanel();
	JPanel downPanel=new JPanel();
	
	
	//initialize labels
	JLabel makeL=new JLabel("Марка:");
	JLabel modelL=new JLabel("Модел:");
	JLabel yearL=new JLabel("Година на производство:");
	
	
	//initialize text input fileds
	JTextField makeTF=new JTextField();
	JTextField modelTF=new JTextField();
	JTextField yearTF=new JTextField();
	
	//initialize buttons
	JButton addBt=new JButton("Добавяне");
	JButton deleteBt=new JButton("Изтриване");
	JButton editBt=new JButton("Промяна");
	JButton searchBt=new JButton("Търсене по марка");
	JButton refreshBt=new JButton("Обнови");
	
	
	//initialize table
	JTable table=new JTable();
	JScrollPane myScroll=new JScrollPane(table);
	
	
	//visualizing the frame on the screen
	public CarFrame() {
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(3,1));
		
		//upPanel-----------------------------------
		upPanel.setLayout(new GridLayout(5,2));
		upPanel.add(makeL);
		upPanel.add(makeTF);
		upPanel.add(modelL);
		upPanel.add(modelTF);
		upPanel.add(yearL);
		upPanel.add(yearTF);
		
		
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
		
		this.setVisible(true);
	}
	
	public void refreshTable() {
		conn=DBConnection.getConnection();
		try {
			state=conn.prepareStatement("select * from Cars");
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
		makeTF.setText("");
		modelTF.setText("");
		yearTF.setText("");
	}

	class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			conn=DBConnection.getConnection();
			String sql="insert into Cars(Make, Model, ProductionYear) values(?,?,?)";
			
			try {
				state=conn.prepareStatement(sql);
				state.setString(1, makeTF.getText());
				state.setString(2, modelTF.getText());
				state.setInt(3, Integer.parseInt(yearTF.getText()));
				
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
			String sql="update Cars set Make=?, Model=?, ProductionYear=? where CarID=?";
			
			try {
				state=conn.prepareStatement(sql);
				state.setString(1, makeTF.getText());
				state.setString(2, modelTF.getText());
				state.setInt(3, Integer.parseInt(yearTF.getText()));
				state.setInt(4, id);
				
				state.execute();
				refreshTable();
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
			makeTF.setText(table.getValueAt(row, 1).toString());
			modelTF.setText(table.getValueAt(row, 2).toString());
			yearTF.setText(table.getValueAt(row, 3).toString());	
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
			String sql="delete from Cars where CarID=?";
			
			try {
				state=conn.prepareStatement(sql);
				state.setInt(1, id);
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
			String sql="select * from Cars where Make=?";
			try {
				if(!makeTF.getText().isEmpty())
				{					
					state=conn.prepareStatement(sql);
					state.setString(1, makeTF.getText());
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
