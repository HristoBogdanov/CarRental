import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class NewFrame extends JFrame{
    
    JPanel panelPerson;
    JPanel panelCar;
    JPanel panelRenta;
    JPanel panelSpr;
    
    JTabbedPane tab;

    public NewFrame() {
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JFrame personFrame = new MyFrame();
        JFrame carFrame = new CarFrame();
        JFrame rentFrame = new RentFrame();
        JFrame searchFrame = new SearchFrame();
        
        tab = new JTabbedPane();
        
        tab.add("Клиенти", personFrame.getContentPane());
        tab.add("Коли", carFrame.getContentPane());
        tab.add("Наем", rentFrame.getContentPane());
        tab.add("Справка", searchFrame.getContentPane());
        
        this.add(tab);
        
        this.setVisible(true);
    }

    public static void main(String[] args) {
        
    }
}
