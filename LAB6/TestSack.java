import java.util.Random;


public class TestSack {

	private static final int N_THREADS = 3;
	private static Thread wątki[] = new Thread[N_THREADS];
	private static final int pieces = 5;
	private static final int limit = 3;
	private static final Sack<Integer> sack = new Sack<Integer>();
	private static Random rand = new Random();

	
	private static final int getRand() {
		return rand.nextInt(limit);
	}
	
	private static class Pracownik implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < pieces; i++)
				sack.włóż(getRand());
			
			for (int i = 0; i < 4 * pieces; i++)
				try {
					sack.wyjmij(getRand());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
	}
	
	public static void main(final String[] args) {
		
		for (int i = 0; i < N_THREADS; i++) {
			wątki[i] = new Thread(new Pracownik());
			wątki[i].start();
		}
		
		
	}
	
}
