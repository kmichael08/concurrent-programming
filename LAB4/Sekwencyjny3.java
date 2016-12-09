import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class Sekwencyjny3 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    
    // rezultaty wierszy
    private static List<List<Integer> > results = new ArrayList<List<Integer> >();
    
    private static final CountDownLatch[] zatrzaski = new CountDownLatch[N_WIERSZY];


    public static void main(final String[] args) {
    	
    	for (int i = 0; i < N_WIERSZY; i++) {
    		results.add(Collections.synchronizedList(new ArrayList<Integer>()));
    		zatrzaski[i] = new CountDownLatch(N_KOLUMN);
    	}
    	
    	for (int i = 0; i < N_KOLUMN; i++)
    		new Kolumna(i).start();
    	
    	for (int i = 0; i < N_WIERSZY; i++) {
			try {
				zatrzaski[i].await();
				Integer suma = 0;
				for (Integer skladnik : results.get(i))
					suma += skladnik;
				
				System.out.println(i + " " + suma);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
     
    }
    
        
    static class Kolumna extends Thread {
    	private int numer;
    	
    	public Kolumna(int i) {
    		numer = i;
    	}
    	
    	@Override
    	public void run() {
    		for (int i = 0; i < N_WIERSZY; i++) {
    			results.get(i).add(Macierz.wartość(i, numer));
    			zatrzaski[i].countDown();
    		}			
    		
    	}
    	
    }
}
