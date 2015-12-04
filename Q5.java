/*
Construa uma classe que implementa uma fila segura para um número indeterminado de threads que funciona da seguinte maneira:
– Se apenas uma thread tentar inserir ou remover um elemento, ela consegue
– Se mais que uma estiver tentando ao mesmo tempo, uma consegue e as outras esperam. A próxima só conseguirá realizar a operação quando a anterior tiver terminado.
*/

public class Q5{

	public static void main(String[] args){
		Queue q = new Queue();
		int nThreads = 4;
		int itemsPerThread = 10;

		int[][] items = new int[nThreads][itemsPerThread];

		for(int i = 0; i < nThreads;i++){
			for(int j = 0; j < itemsPerThread ; j++){
				items[i][j] (i+1)*(j+1);
			}
		}

		for(int i = 0; i< nThreads ; i++){
			(new ThreadQueuer(q)).start();
		}

	}

}

class Queue{
	public Node first;
	public Queue(){
	}

	synchronized public void put(int e){
		if(first != null){
			first = (new Node(e)).next = first;
		}else{
			first = new Node(e);
		}
	}

	synchronized public int get(){
		if(first == null){
			throw (new Exception("A fila ja acabou camarada"));
		}
		int v = first.e;
		first = first.next;
		return v;
	}
}

class Node{
	public int e;
	public Node next;
	public Node(int e){
		this.e = e;
	}
}

class ThreadQueuer extends Thread{
	private Queue q;
	public ThreadQueuer(Queue q){
		this.q = q;
	}

	public void run(){

	}

	private void put(int e){
		q.put(e);
		System.out.println("put: "+e);
	}

	private int get(){
		int v = q.get();
		System.out.println("get: "+v);
	}

}