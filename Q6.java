/*
Defina uma classe ArvoreBusca que implementa uma árvore de busca onde é possível realizar inserções de elementos. Essa estrutura de dados deve funcionar com várias threads. Faça o que é pedido: 
– Implemente um método main() que cria 50 threads onde cada uma insere 2000 números aleatórios nessa árvore.
– Meça o tempo de execução do seu programa, comparando-o com o de uma execução puramente sequencial.
– O que significa “funcionar com várias threads?
*/

import java.util.Random;

public class Q6{
	public static void main(String[] args){

		Tree t = new Tree();
		int nThreads = 50;

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
		int i = 0;
		for(; i < 2000; i++){
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

	public Tree(){
		this.root = null;
	}

	public synchronized void insert(int value){
		if(this.root == null){
			this.root = new Node(value);
		}else{
			this.root.insert(value);
		}
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

	public Node(int value){
		this.value = value;
		this.left = null;
		this.right = null;
	}

	public synchronized void insert(int value){
		if(value > this.value){
			if(this.right == null) this.right = new Node(value);
			else this.right.insert(value);
		}else if(value <= this.value){
			if(this.left == null) this.left = new Node(value);
			else this.left.insert(value);
		}//se for igual nao
	}

	public int countNodes(){
		int acc = 0;
		if(this.left != null) acc += this.left.countNodes();
		if(this.right != null) acc += this.right.countNodes();
		return 1 + acc;
	}

}