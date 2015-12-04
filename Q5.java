/*
Construa uma classe que implementa uma fila segura para um número indeterminado de threads que funciona da seguinte maneira:
– Se apenas uma thread tentar inserir ou remover um elemento, ela consegue
– Se mais que uma estiver tentando ao mesmo tempo, uma consegue e as outras esperam. A próxima só conseguirá realizar a operação quando a anterior tiver terminado.
*/
import java.util.Random;

public class Q5{

	public static void main(String[] args){
		Queue q = new Queue();
		int nThreads = 4;
		int itemsPerThread = 10;
		Random randGen = new Random();

		//Class operation for simulate some dinamic activities over the queue
		Operation[][] ops = new Operation[nThreads][itemsPerThread];

		// //FOR TESTING
		// int[][] numbers = new int[nThreads][itemsPerThread];

		// //Numbers hard coded 
		// numbers[0] = new int[]{1,2,3,4,5,6,7,8,9,10};
		// numbers[1] = new int[]{11,12,13,14,15,16,17,18,19,20};
		// numbers[2] = new int[]{21,22,23,24,25,26,27,28,29,30};
		// numbers[3] = new int[]{31,32,33,34,35,36,37,38,39,40};
		

		for(int i = 0; i < nThreads;i++){
			for(int j = 0; j < itemsPerThread ; j++){
				ops[i][j] = new Operation();
				if(randGen.nextDouble() > 0.7) ops[i][j].setGet();
				else ops[i][j].setPut((i*10)+j+1); //Each item is generated sorted
			}
		}

		for(int i = 0; i < nThreads ; i++){
			(new ThreadQueuer(i+1,q,ops[i])).start();
		}

	}

}

class Operation{
	public int e;
	public String op;

	public Operation(){
		this.e = -1;
	}

	public void setPut(int e){
		this.e = e;
		this.op = "put";
	}

	public void setGet(){
		this.e = -1;
		this.op = "get";
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

	synchronized public void printQueue(){
		printQueue(this.first);
	}
	synchronized private void printQueue(Node n){
		if(n != null){
			System.out.print(n.e);
			printQueue(n.next);
			if(n.next != null) System.out.print(',');
		}else{
			System.out.println();
		}

	}

	synchronized public int get() throws Exception{
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
	private Operation[] ops;
	private int id;
	public ThreadQueuer(int id, Queue q, Operation[] ops){
		this.id = id;
		this.q = q;
		this.ops = ops;
	}

	public void run(){
		for(int i = 0; i < ops.length ; i++ ){
			try{
				execute(ops[i]);
			}catch(Exception e){
				System.out.println("Ops! Houve um erro: "+e.getMessage());
			}
		}
	}

	private void execute(Operation op) throws Exception{
		if( op.op == "put"){
			this.put(op.e);
		}else if(op.op == "get"){
			this.get();
		}else{
			throw (new Exception("Operation Type Invalid For Queue"));
		}
		// q.printQueue();
	}

	private void put(int e){
		q.put(e);
		System.out.println("From: "+id+" -> "+"put: "+e);
	}

	private void get(){
		try{
			int v = q.get();
			System.out.println("From: "+id+" -> "+"get: "+v);
		}catch(Exception e){
			System.out.println("Ops! Houve um erro: "+e.getMessage());
		}
	}

}