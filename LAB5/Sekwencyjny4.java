import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class Sekwencyjny4 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    
    // rezultaty wierszy
    private static List<LinkedBlockingQueue<Integer> > res = 
    		Collections.synchronizedList (new ArrayList<LinkedBlockingQueue<Integer>>());
                
    public static void main(final String[] args) {
    	
    	for (int i = 0; i < N_KOLUMN; i++) {
    		LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<Integer>();
    		res.add(q);
    	}
    	
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
    				suma += res.get(row).take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			left--;
    		}
    		
    		System.out.println(row + "  " + suma);
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
					res.get(i).put(val);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}			
    		
    	}
    	
    }
}
