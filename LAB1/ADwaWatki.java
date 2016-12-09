
public class ADwaWatki {

	public static void main(String[] args) {
		
		Thread t = Thread.currentThread();
		System.out.println("Główny wątek: " + t.getName());

		Runnable runner = new MyRun();

		Thread newThread = new Thread(runner);

		newThread.start();

	}

}

class MyRun implements Runnable {
	public void run() {
		Thread t = Thread.currentThread();
		System.out.println("Nowy wątek: " + t.getName());
		System.out.println("Razem wątków: " + Thread.activeCount() );
		System.out.println("Witaj na labie z PW!");
	

	}

}
