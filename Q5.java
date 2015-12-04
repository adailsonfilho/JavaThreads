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

		Operation[][] ops = new Operation[nThreads][itemsPerThread];

		for(int i = 0; i < nThreads;i++){
			for(int j = 0; j < itemsPerThread ; j++){
				ops[i][j] = new Operation();
				if(randGen.nextDouble() > 0.7) ops[i][j].setGet();
				else ops[i][j].setPut((1+i)*(1+j));
			}
		}

		for(int i = 0; i< nThreads ; i++){
			(new ThreadQueuer(q,ops[i])).start();
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
	public ThreadQueuer(Queue q, Operation[] ops){
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
	}

	private void put(int e){
		q.put(e);
		System.out.println("put: "+e);
	}

	private void get(){
		try{
			int v = q.get();
			System.out.println("get: "+v);
		}catch(Exception e){
			System.out.println("Ops! Houve um erro: "+e.getMessage());
		}
	}

}