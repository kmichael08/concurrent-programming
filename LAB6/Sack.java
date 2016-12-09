import java.util.ArrayList;
import java.util.List;


public class Sack<T> implements Worek<T> {
	private List<T> items;
	
	public Sack() {
		items = new ArrayList<T>();
	}
	
	@Override
	public synchronized void włóż(T wartość) {
		items.add(wartość);
		System.out.println("Dodano :" + wartość.toString());
	}

	@Override
	public synchronized void wyjmij(T wartość) throws InterruptedException {
		while (!items.contains(wartość))
			wait();
		System.out.println("Wyjmujemy " + wartość.toString());
		items.remove(wartość);
		notifyAll();
		
	}

}
