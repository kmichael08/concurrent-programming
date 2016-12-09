
public class Wektor {
	
	private int[] wektor = null;
	private int size;
	
	public Wektor(int N) {
		wektor = new int[N];
		size = N;
		for (int i = 0; i < size; i++)
			wektor[i] = 0;
	}
	
	public Wektor(int N, int skladowe[]) {
		wektor = skladowe.clone();
		size = N;
	}
	
	public int getCoordinate(int pos) {
		return wektor[pos];
	}
	
	public Wektor dodaj(Wektor w) {
		Adder[] runners = new Adder[size];
		Thread[] threads = new Thread[size];
		int[] result = new int[size];
				
		for (int i = 0; i < size; i++) {
			runners[i] = new Adder(i, this, w, result);
			threads[i] = new Thread(runners[i]);
			threads[i].start();
		}
		
		// Waiting for threads to finish
		for (Thread t : threads) {
			try {
				t.join();
			}
			catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		
		return new Wektor(size, result);		
	}
	
	public int razy(Wektor w) {
		int coordinates[] = new int[size];
		
		Skalar[] runners = new Skalar[size];
		Thread[] threads = new Thread[size];
				
		for (int i = 0; i < size; i++) {
			runners[i] = new Skalar(i, this, w, coordinates);
			threads[i] = new Thread(runners[i]);
			threads[i].start();
		}
		
		// Waiting for threads to finish
		for (Thread t : threads) {
			try {
				t.join();
			}
			catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		
		// summing up the coordinates
		int skalarny = 0;
		for (int coordinate : coordinates)
			skalarny += coordinate;
			
		return skalarny;
	}
	

	class Adder implements Runnable {
		private int pos;
		private Wektor w1, w2;
		private int result[];
		
		
		public Adder(int pos, Wektor w1, Wektor w2, int[] result) {
			this.pos = pos;
			this.w1 = w1;
			this.w2 = w2;
			this.result = result;
		}
		
		public void run() {
			System.out.println("Starting - " + Thread.currentThread().getName());
			result[pos] = w1.getCoordinate(pos) + w2.getCoordinate(pos);
			System.out.println("Ending - " + Thread.currentThread().getName());
		}
	}
	
	class Skalar implements Runnable {
		private int pos;
		private Wektor w1, w2;
		private int[] coordinates;
		
		public Skalar(int pos, Wektor w1, Wektor w2, int[] coordinates) {
			this.pos = pos;
			this.w1 = w1;
			this.w2 = w2;
			this.coordinates = coordinates;
		}

		public void run() {
			System.out.println("Starting - " + Thread.currentThread().getName());
			coordinates[pos] = w1.getCoordinate(pos) * w2.getCoordinate(pos);
			System.out.println("Ending - " + Thread.currentThread().getName());
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int liczba : wektor)
			result.append(liczba + " ");
		
		return result.toString();
	}
	
	
	public static void main(String args[]) {
		int w1[] = {1, 2, 3, 4, 5};
		int w2[] = {4, 7, 11, 5, 12};
		
		Wektor v1 = new Wektor(5, w1);
		Wektor v2 = new Wektor(5, w2);
				
		System.out.println(v1);
		
		System.out.println(v1.dodaj(v2));
		
		System.out.println(v1.razy(v2));
		
		System.out.println(v2.razy(v1));
		
		System.out.println(v1.razy(v1));
		
		System.out.println(v2.dodaj(v2));
		
		
	}
	
}
