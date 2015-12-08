// A correct implementation of a producer and consumer. 
class Q { 
	int buffer; 
	boolean bufferUpdated = false; 

	synchronized int get() { 
		if(!bufferUpdated) 
			try { 
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

	Producer(Q q) { 
		this.q = q; 
		new Thread(this, "Producer").start(); 
	} 

	public void run() { 
		int i = 0; 
		while(true) {
			try{//simulate production time
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
			q.put(i++); 
		} 
	} 
}

class Consumer implements Runnable { 
	Q q;

	Consumer(Q q) { 
		this.q = q; 
		new Thread(this, "Consumer").start(); 
	} 

	public void run() { 
		while(true) {
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