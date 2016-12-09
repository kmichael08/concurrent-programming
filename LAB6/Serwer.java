import java.util.ArrayList;
import java.util.Collections;


public class Serwer {

	private int zasoby;
	private int grupy;
	private ArrayList<Integer> ktoKorzysta;
	private int ileKorzysta[];
	private ArrayList<Boolean> czyCzeka;	// czy grupa czeka
	
	public Serwer(int zasoby, int grupy) {
		this.zasoby = zasoby;
		this.grupy = grupy;
		ktoKorzysta = new ArrayList<Integer>(Collections.nCopies(this.zasoby, -1));
		ileKorzysta = new int[zasoby];
		czyCzeka = new ArrayList<Boolean>(Collections.nCopies(this.grupy, false));
	}
	
		
	public synchronized int chcęKorzystać(int grupa) throws InterruptedException {
		while(!ktoKorzysta.contains(grupa) && !ktoKorzysta.contains(-1)) {
			czyCzeka.set(grupa, true);
			wait();
		}
		
		int pos;
		
		if (ktoKorzysta.contains(grupa)) {
			while (czyCzeka.contains(true)) {
				wait();
			}
			pos = ktoKorzysta.indexOf(grupa);
		}
		else if (ktoKorzysta.contains(-1)) {
			pos = ktoKorzysta.indexOf(-1);
			ktoKorzysta.set(pos, grupa);
		}
		else
			throw new InterruptedException("Something went wrong.");
		
		ileKorzysta[pos]++;
				
		return pos;
	}

	public synchronized void skończyłem(int grupa, int zasób) {
		ileKorzysta[zasób]--;
		if (ileKorzysta[zasób] == 0) {
			ktoKorzysta.set(zasób, -1);
			notifyAll();
		}
		
	}

}
