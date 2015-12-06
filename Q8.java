/*Implemente um vetor (tamanho fixo) de inteiros seguro para threads com operações para ler e escrever em determinado índice e para trocar os valores armazenados em dois índices distintos.
Sua solução deve satisfazer as seguintes propriedades:
- Safety: duas threads nunca obtém acesso a uma posição do vetor ao mesmo tempo.
- Safety: a operação de troca (swap) dos valores em posições distintas deve ser atômica
- Liveness: para um vetor de tamanho N, até K <= N threads podem manipular elementos do vetor simultaneamente (considere que N é múltiplo de K).
Liveness: o programa não entra em deadlock*/

import java.util.concurrent.locks.*;
import java.util.Random;

public class Q8{

	public static void main(String[] args){

		int n = 10; 	//size of array
		int coef = 2;	//coef to garantee that k is multiple of n; Yep, there is the oposite in de text, but k <= n, so it's wrong consider n multiple of k
		int k = n*coef;	//number of threads
		

		int[] array = new int[n];
		Lock[] locks = new Lock[n];

		ThreadArrayOperator[] threads = new ThreadArrayOperator[k];

		//create locks
		for(int i = 0; i < n; i++){
			locks[i] = new ReentrantLock();
		}

		//create thraeds
		for(int i = 0; i < k; i++){
			threads[i] = new ThreadArrayOperator(i,array,locks);
			threads[i].start();
		}
	}
}



class ThreadArrayOperator extends Thread{

	int[] array;
	Lock[] locks;
	int id;
	Random randGen = new Random();

	public ThreadArrayOperator(int id, int[] array, Lock[] locks){
		this.array = array;
		this.locks = locks;
		this.id = id;
	}

	public void run(){
		while(true){
			// System.out.println("Thread "+id+" attempted");
			//simulate array operations
			double randomOperation = randGen.nextDouble();
			if( randomOperation < 0.10){
					//10% --> Swap
					int index1 = randGen.nextInt(array.length);
					int index2 = randGen.nextInt(array.length);
					locks[index1].lock();
					boolean l2 = locks[index2].tryLock();
					if(!l2){
						locks[index1].unlock();
					}else{
						try{
							int temp1 = array[index1];
							array[index1] = array[index2];

							System.out.println("From thread: "+id+" SWAP "+index1+" and "+index2);

							locks[index1].unlock();
							array[index2] = temp1;
							locks[index2].unlock();

						}catch(Exception e){
							locks[index1].unlock();
							locks[index2].unlock();
							e.printStackTrace();
						}
					}
				}else if(randomOperation < 0.33){
					//33% --> Write
					int index = randGen.nextInt(array.length);
					int value = randGen.nextInt(10000);
					locks[index].lock();
					try{
						array[index] = value;

						System.out.println("From thread: "+id+" WRITE "+value+" in "+index);

						locks[index].unlock();
					}catch(Exception e){
						locks[index].unlock();
						e.printStackTrace();
					}

				}else{
					//33% --> Read
					int index = randGen.nextInt(array.length);
					locks[index].lock();
					try{
						int value = array[index];
						System.out.println("From thread: "+id+" READ "+value+" from "+index);
						locks[index].unlock();
					}catch(Exception e){
						locks[index].unlock();
						e.printStackTrace();
					}
				}
			// int simulateInterval = randGen.nextInt(50);
			// sleep(simulateInterval);
		}
	}
}