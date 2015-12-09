/*Modifique o programa do exercício anterior (Q6) para tornar mais fina a granularidade do travamento. Em outras palavras, faça com que o travamento seja feito por nó, ao invés de afetar a árvore inteira. 
– Compare o desempenho desta versão com o da sequencial e o da que usa apenas uma trava*/

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q7{
	public static void main(String[] args){

		Tree t = new Tree();
		int nThreads = 10;
		long millis = System.currentTimeMillis();

		ThreadTree[] threads = new ThreadTree[nThreads];


		for(int i = 0; i < nThreads ; i++){
			threads[i] = new ThreadTree(i, t);
			threads[i].start();
		}

		for(int i = 0; i < nThreads ; i++){
			try{
				threads[i].join();
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Ops!Rolou um erro: "+e.getMessage());
			}
		}

		int size = t.countNodes();
		System.out.println(size);

		long time = (System.currentTimeMillis() - millis);
		System.out.println("Time: "+time);	
	}
}

class ThreadTree extends Thread{
	int id;
	Tree t;
	int[] items;
	Random randGen = new Random();

	public ThreadTree(int id,Tree t){
		this.id = id;
		this.t = t;
	}

	public void run(){
		for(int i = 0; i < 10000; i++){
			int value = randGen.nextInt(10000);
			t.insert(value);
		}
	}

	public int countNodes(){
		return t.countNodes();
	}
}

class Tree{
	Node root;

	private Lock treeLock = new ReentrantLock();

	public Tree(){
		this.root = null;
	}

	public void insert(int value){

		treeLock.lock();
		//garantee only one root initialization
		try{

			if(this.root == null){
				this.root = new Node(value);
				System.out.println("Root created");
				treeLock.unlock();
			}else{
				this.root.lock();
				treeLock.unlock();				
				this.root.insert(value);
			}

		}catch(Exception e){
			e.printStackTrace();
			treeLock.unlock();
		}

		//try insert if root is not null
	}

	public int countNodes(){
		if(root != null){
			return this.root.countNodes();
		}
		return 0; 
	}

}

class Node{
	public int value;
	public Node left;
	public Node right;

	private Lock l = new ReentrantLock();

	public Node(int value){
		this.value = value;
		this.left = null;
		this.right = null;
	}

	public void lock(){
		this.l.lock();
	}

	public void unlock(){
		this.l.unlock();
	}

	public void insert(int value){

		try{
			if(value > this.value){
				if(this.right == null){
					this.right = new Node(value);
					unlock();
				}
				else{
					this.right.lock();
					unlock();
					this.right.insert(value);
				}
			}else if(value <= this.value){
				if(this.left == null){
					this.left = new Node(value);
					unlock();
				}
				else{
					this.left.lock();
					unlock();
					this.left.insert(value);
				}
			}//se for igual nao adiciona
		}catch(Exception e){
			e.printStackTrace();
			unlock();
		}
		
	}

	public int countNodes(){
		int acc = 0;
		if(this.left != null) acc += this.left.countNodes();
		if(this.right != null) acc += this.right.countNodes();
		return 1 + acc;
	}

}