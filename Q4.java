/*
Modifique o último programa que você construiu para que, a cada iteração, a thread espere 1ms. A thread que terminar a contagem primeiro (realizar todas as iterações) deve interromper todas as outras que estão executando.*/

public class Q4{
	public static void main(String[] args){
		int n = 100;
		int counter = 0;
		int xThreads = 4;

		ThreadCounter[] threads = new ThreadCounter[xThreads];

		for(int i =0; i < xThreads ; i++){
			threads[i] = new ThreadCounter(i,n,counter);
			threads[i].start();
		}

		while(true){
			for(int i =0; i < xThreads ; i++){
				if(threads[i].finished){
					for(int j =0; j < xThreads ; j++){
						if(j != i) threads[i].interrupt();
					}
					return;
				}
			}
		}
	}
}

class ThreadCounter extends Thread{
	private int id;
	private int max;
	private int counter;
	public boolean finished;

	public ThreadCounter(int id, int max,int counter){
		this.id = id;
		this.max = max;
		this.counter = counter;
		this.finished = false;
	}

	public void run(){
		while(counter < max){
			counter++;
			System.out.println("From "+id+": "+counter);
			try{
				this.sleep(1);
			}catch(Exception e){
				System.out.println("Ops! Houve um erro: "+e.getMessage());

			}
		}
		finished = true;
	}
}