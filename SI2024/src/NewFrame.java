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
        
        // Get the content panes of the passed frames
        panelPerson = (JPanel)framePerson.getContentPane();
        panelCar = (JPanel)frameCar.getContentPane();
        panelRenta = (JPanel)frameRenta.getContentPane();
        panelSpr = (JPanel)frameSpr.getContentPane();
        
        tab = new JTabbedPane();
        
        tab.add("Клиенти", panelPerson); // Assuming the frames have titles, otherwise, use custom labels
        tab.add("Коли", panelCar);
        tab.add("Наем", panelRenta);
        tab.add("Справка по ...", panelSpr);
        
        this.add(tab);
        
        this.setVisible(true); // Set visibility to true after adding components
    }

    public static void main(String[] args) {
        // Assuming you have JFrame objects for personFrame, carFrame, rentaFrame, and sprFrame
        JFrame personFrame = new JFrame();
        JFrame carFrame = new JFrame();
        JFrame rentaFrame = new JFrame();
        JFrame sprFrame = new JFrame();

        // Populate the frames with components as needed
        
        NewFrame newFrame = new NewFrame(personFrame, carFrame, rentaFrame, sprFrame);
    }
}
