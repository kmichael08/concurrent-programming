import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Sekwencyjny1 {

    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    // wiersz z policzonymi wartosciami
    private static int[] result = new int[N_KOLUMN];
    private static final CyclicBarrier bariera1 = new CyclicBarrier(N_KOLUMN, new Sumuj());


    public static void main(final String[] args) {
    	
    	for (int i = 0; i < N_KOLUMN; i++)
    		new Kolumna(i).start();
     
    }
    
    
    static class Sumuj implements Runnable {
    
    	
		@Override
		public void run() {
			int suma = 0;
			for (int i = 0; i < N_KOLUMN; i++)
				suma += result[i];
			
			System.out.println(suma);
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
    			result[numer] = Macierz.wartość(i, numer);

    			try {
					bariera1.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}   			
    		
    	}
    	
    }
}
