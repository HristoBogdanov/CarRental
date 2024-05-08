
public class MainClass {

	public static void main(String[] args) {
		MyFrame peopleFrame=new MyFrame();
		CarFrame carsFrame=new CarFrame();
		RentFrame rentalFrame=new RentFrame();
		MyFrame searchFrame=new MyFrame();
		NewFrame newFrame=new NewFrame(peopleFrame, carsFrame, rentalFrame, searchFrame);
	}

}
