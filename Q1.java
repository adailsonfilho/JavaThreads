/*
	Implemente um programa que imprime todos os números entre 1 e 2 bilhões usando várias threads para particionar o trabalho.
 */

public class Q1{

	public static void main(String[] args){

		int n = 5;
		int total = 2000000000;

		for(int i =0; i < n;i++){
			int begin = i*(total/n);
			int end = begin + (total/n);
			(new ThreadPrinter(begin,end)).start();
		}

	}
}

class ThreadPrinter extends Thread{
	private int begin;
	private int end;

	public ThreadPrinter(int begin, int end){
		this.begin = begin;
		this.end = end;

	}
	public void run(){
		for(int i = begin; i <= end; i++){
			System.out.println(i);
		}
	}
}