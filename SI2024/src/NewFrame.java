import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class NewFrame extends JFrame{
    
    JPanel panelPerson;
    JPanel panelCar;
    JPanel panelRenta;
    JPanel panelSpr;
    
    JTabbedPane tab;

    public NewFrame(JFrame framePerson, JFrame frameCar, JFrame frameRenta, JFrame frameSpr) {
        this.setSize(400, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        panelPerson = (JPanel)framePerson.getContentPane();
        panelCar = (JPanel)frameCar.getContentPane();
        panelRenta = (JPanel)frameRenta.getContentPane();
        panelSpr = (JPanel)frameSpr.getContentPane();
        
        tab = new JTabbedPane();
        
        tab.add("Клиенти", panelPerson);
        tab.add("Коли", panelCar);
        tab.add("Наем", panelRenta);
        tab.add("Справка по ...", panelSpr);
        
        this.add(tab);
        
        this.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame personFrame = new JFrame();
        JFrame carFrame = new JFrame();
        JFrame rentaFrame = new JFrame();
        JFrame sprFrame = new JFrame();
        
        NewFrame newFrame = new NewFrame(personFrame, carFrame, rentaFrame, sprFrame);
    }
}
