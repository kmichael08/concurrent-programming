import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;


public class Sekwencyjny5 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    
    // rezultaty wierszy
    private static ConcurrentMap<Integer, BlockingQueue<Integer> > results = 
    		new ConcurrentHashMap<>();
            

    public static void main(final String[] args) {
    	
    	for (int i = 0; i < N_KOLUMN; i++)
    		new Kolumna(i).start();
    	
    	// summing threads
    	for (int i = 0; i < N_WIERSZY; i++) {
			new Summator(i).start();
    	}
     
    }
    
    static class Summator extends Thread {
    	private int row;
    	
    	public Summator(int row) {
    		this.row = row;
    	}
    	
    	@Override
    	public void run() {
    		int suma = 0;
    		int left = N_KOLUMN;
    		
    		while (left > 0) {
    			try {
    				suma += results.get(row).take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			left--;
    		}
    		
    		System.out.println(row + "  " + suma);
    		results.remove(row);
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
    			try {
    				Integer val = Macierz.wartość(i, numer);
    	    		results.putIfAbsent(i, new LinkedBlockingQueue<Integer>());
					results.get(i).put(val);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}			
    		
    	}
    	
    }
}
