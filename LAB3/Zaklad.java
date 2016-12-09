import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;
import java.util.concurrent.Semaphore;


public class Zaklad {

	
	public static void main(String args[]) {
		int K = 10; // liczba klientow
		int chairs = 5; // liczba krzesel w poczekalni
		
		Poczekalnia poczekalnia = new Poczekalnia(chairs);
		
		new Fryzjer(poczekalnia);
		
		for (int i = 0; i < K; i++)
			new Klient(i, poczekalnia);
				
	}
}

class Poczekalnia {
	int freeChairs;
	Queue<Integer> waitingClients;
	
	Semaphore fotel; // na fotelu siedzi maksymalnie jeden klient w danym momencie
	Semaphore ochronaCh; // ochrona zmiennych poczekalni
	Semaphore klienci; // fryzjer czeka na klientow
	
	private Random r = new Random();
	
	public Poczekalnia(int chairs) {
		fotel = new Semaphore(1);
		
		freeChairs = chairs;
		waitingClients = new LinkedList<Integer>();
		
		ochronaCh = new Semaphore(1, true);
		klienci = new Semaphore(0);
	}
	
	public void wejdz(int myNumber) {
		try {
			ochronaCh.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(freeChairs == 0) {
			System.out.println("Klient " + myNumber + " Brak miejsca w poczekalni!");
		}
		else {
			System.out.println("Klient " + myNumber + " Wszedl do poczekalni");
			freeChairs--;
			waitingClients.add(myNumber);
			klienci.release();
		}
		
		
		ochronaCh.release();
	}
	
	public void strzyzenie(Integer k) {
		
		System.out.println("Strzyzenie klienta " + k);
		
		// Symulacja strzyzenia
		try {
			Thread.sleep(getNextRandomInt(200));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}
	
	public void ostrzyz() {
		try {
			fotel.acquire();
			System.out.println("Fotel zwolniony");			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			klienci.acquire();
			System.out.println("Klient gotowy");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		try {
			ochronaCh.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// zwalniamy miejsce w poczekalni
		freeChairs++;
		
		// strzyzemy
		strzyzenie(waitingClients.poll());
						
		ochronaCh.release();
		
		fotel.release();	
		
	}
	
	int getNextRandomInt(int time) {
		return r.nextInt(time);
	}
			
}
	

class Fryzjer extends Thread {
	private Poczekalnia pocz;
	
	public Fryzjer(Poczekalnia pocz) {
		this.pocz = pocz;
		this.start();
	}
	
	@Override
	public void run() {
		while(true) {
			pocz.ostrzyz();				
		}
		
	}
			
}

class Klient extends Thread {
	private int myNumber;
	private Poczekalnia pocz;
	
	public Klient(int i, Poczekalnia pocz) {
		this.start();
		myNumber = i;
		this.pocz = pocz;
	}
	
	@Override
	public void run() {
		
		System.out.println("Klient " + myNumber + " probuje wejsc do zakladu fryzjerskiego");
		
		try {
			Thread.sleep(pocz.getNextRandomInt(500));

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pocz.wejdz(myNumber);
					
	}
}
	
	

