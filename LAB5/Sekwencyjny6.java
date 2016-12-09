import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;


public class Sekwencyjny6 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final int K_WĄTKÓW = 4;

    // rezultaty wierszy
    private static List<LinkedBlockingQueue<Future<Integer> > > res = 
    		Collections.synchronizedList (new ArrayList<LinkedBlockingQueue<Future<Integer>>>());

    public static void main(final String[] args) {
    
        
    	for (int i = 0; i < N_KOLUMN; i++) {
    		LinkedBlockingQueue<Future<Integer> > q = new LinkedBlockingQueue<>();
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
    				suma += res.get(row).take().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			left--;
    		}
    		
    		System.out.println(row + "  " + suma);
    	}
    	
    }
        
    
    private static class Matrix implements Callable<Integer> {

        private final int i, j;

        private Matrix(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public Integer call() {
            return Macierz.wartość(i, j); 
         }

    }

    
    private static class Kolumna extends Thread {
    	private int numer;
    	
    	public Kolumna(int i) {
    		numer = i;
    	}
    	
    	@Override
    	public void run() {
	        final ExecutorService pulaWątków =
	                Executors.newFixedThreadPool(K_WĄTKÓW);
    		
    		for (int i = 0; i < N_WIERSZY; i++) {			    			
    			try {
    				Future<Integer> val = pulaWątków.submit(new Matrix(i, numer));
					res.get(i).put(val);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			

    		}
    		
    		pulaWątków.shutdown();
    		
    	}
    	
    }
}
