/*
Implemente um programa que calcula todos os números primos entre 1 e um valor N fornecido como
argumento. Seu programa deve dividir o trabalho a ser realizado entre X threads (onde X também é uma
entrada do programa) para tentar realizar o trabalho de maneira mais rápida que uma versão puramente
sequencial. A thread principal do programa deve imprimir os números primos identificados apenas
quando as outras threads terminarem.
*/


public class Q2{

	public static void main(String[] args){

		int n = 1000;		//prime search limit
		int x = 4; 			//Threads numbers

		ThreadPrime[] threads = new ThreadPrime[x];

		//Create threads
		for(int i =0; i < x;i++){
			int begin = i*(n/x);
			int end = begin + (n/x);
			threads[i] = new ThreadPrime(begin,end);
			threads[i].start();
		}

		//join all of them
		for(int i = 0; i < x;i++){
			try{
				threads[i].join();
			}catch(Exception e){
				System.out.println("Ops! Ocorreu um erro: "+e.getMessage());
			}
		}

		//print all primes found
		for(int i = 0; i < x;i++){
			int[] primes = threads[i].primes;
			for(int j = 0; j < threads[i].getAmount() ; j++){
				System.out.println(primes[j]);
			}
		}

	}
}

class ThreadPrime extends Thread{

	public int[] primes;
	private int begin;
	private int end;
	private int amount;

	public int getAmount(){
		return amount;
	}

	public ThreadPrime(int begin, int end){ primes = new int[(end-begin)/2];
	this.begin = begin; this.end = end; } public void run(){ int acc = 0; for
	(int i = begin; i <= end ; i++) { if(isPrime(i)){ primes[acc] = i;
	System.out.println("prime: "+i); acc++; } } amount = acc; }

	boolean isPrime(int n) {
	    if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    if(n%2 == 0 || n%3 == 0) return false;
	    for(int i = 5; i < n; i++) {
	    	if( n%i == 0){
	    		return false;
	    	}
    	}
    	return true;
	}
}