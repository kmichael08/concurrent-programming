import java.util.Random;


class Zadanie implements Runnable {

    private final int grupa;
    private final Serwer serwer;
    private static Random rand = new Random();

    private void własneSprawy() {
		try {
			Thread.sleep(getRandom(1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void korzystam(int zasób) {
    	System.out.println("Jestem sobie wątek z grupy : " + grupa + " " + Thread.currentThread().getName() + " Korzystam teraz z zasobu : " + zasób);
    	try {
    		Thread.sleep(getRandom(10000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("Jestem sobie wątek z grupy : " + grupa + " " + Thread.currentThread().getName() + " Wychodzę teraz z zasobu : " + zasób);   	
    }

    public Zadanie(final int grupa, final Serwer serwer) {
        this.grupa = grupa;
        this.serwer = serwer;
        System.out.println(" Jestem z grupy: " + grupa);
    }

    @Override
    public void run() {
        while (true) {
            własneSprawy();
            int zasób;
			try {
				zasób = serwer.chcęKorzystać(grupa);
				korzystam(zasób);
		        serwer.skończyłem(grupa, zasób);
		        
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
          
        }
    }
    
    public static int getRandom(int n) {
    	return rand.nextInt(n);
    }
    
    public static void main(String args[]) {
    	final int groups = 6;
    	final int nThreads = 10;
    	final int resources = 3;
    	
    	Serwer serwer = new Serwer(resources, groups);
    	
    	Thread zadania[] = new Thread[nThreads];
    	
    	for (int i = 0; i < nThreads; i++) {
    		zadania[i] = new Thread(new Zadanie(getRandom(groups), serwer));
    	}
    	
    	for (Thread thread : zadania) {
    		thread.start();
    	}
    	
    }

}

