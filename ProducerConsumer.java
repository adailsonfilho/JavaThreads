import java.util.Random;

// A correct implementation of a producer and consumer. 
class Q { 
	int buffer; 
	boolean bufferUpdated = false; 

	synchronized int get() { 
		if(!bufferUpdated) 
			try { 
				System.out.println("Ops! Not ready yet, in production... Keep waiting");
				wait(); 
			}catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 

		System.out.println("Got: " + buffer);
		bufferUpdated = false; 
		notify(); 
		return buffer; 
	} 

	synchronized void put(int n) { 
		if(bufferUpdated)
			try { 
				System.out.println("Ops! Not ready yet, consuming... Keep waiting");
				wait(); 
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 
		this.buffer = n; 
		bufferUpdated = true; 
		System.out.println("Put: " + n); 
		notify(); 
	} 
}

class Producer implements Runnable {

	Q q;
	Random randGen = new Random();

	Producer(Q q) {
		this.q = q; 
		new Thread(this, "Producer").start(); 
	} 

	public void run() {
		int i = 0; 
		while(true) {

			try{//simulate production time
				Thread.sleep(1000*(randGen.nextInt(5)+1));
			}catch(Exception e){
				e.printStackTrace();
			}

			q.put(i++); 
		} 
	} 
}

class Consumer implements Runnable { 
	Q q;
	Random randGen = new Random();
	Consumer(Q q) { 
		this.q = q; 
		new Thread(this, "Consumer").start(); 
	} 

	public void run() { 
		while(true) {

			try{//simulate production time
				Thread.sleep(1000*(randGen.nextInt(5)+1));
			}catch(Exception e){
				e.printStackTrace();
			}
			q.get(); 
		} 
	} 
}

class ProducerConsumer { 
	public static void main(String args[]) { 
		Q q = new Q(); 
		new Producer(q); 
		new Consumer(q); 
		System.out.println("Press Control-C to stop."); 
	} 
}