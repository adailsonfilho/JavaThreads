/*Nesta variante do Problema do Banheiro Compartilhado1,
ha dois tipos de threads, homem e mulher, e dois recursos do tipo banheiro
que precisam ser usados da seguinte maneira:
1. Exclus~ao mutua: pessoas de sexos distintos n~ao podem ocupar um banheiro
simultaneamente.
2. Aus^encia de starvation: todos que precisam usar um banheiro conseguem
usa-lo em algum momento.
3. Recursos ociosos primeiro: sempre que uma thread de um tipo t (homem
ou mulher) chega para usar o banheiro e um dos dois banheiros
esta vazio, ela usa esse banheiro vazio, mesmo que o outro esteja ocupado
apenas com threads do mesmo tipo.
O protocolo e implementado atraves dos seguintes procedimentos: entrarHomem()
atrasa a thread que o invoca ate que esteja ok para o homem entrar no banheiro,
sairHomem() e chamado quando um homem deixa o banheiro e entrarMulher()
e sairMulher() funcionam analogamente para mulheres.
Implemente esse cenario usando a abordagem que preferir para garantir exclus~
ao mutua (travas explcitas ou monitores). Explique porque sua implementa
c~ao satisfaz exclus~ao mutua e aus^encia de starvation.*/

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;


public class Banheiros{

	public static void main(String[] args){

		Banheiro b1 = new Banheiro(1);
		Banheiro b2 = new Banheiro(2);

		int totalPessoas = 10;
		int nThreads = 0;

		Random randGen = new Random();

		while(nThreads < totalPessoas){

			//metade de cada genero
			Genero genero = Genero.MULHER;

			if(nThreads < totalPessoas/2){
				genero = Genero.HOMEM;
			}

			System.out.println("TPessoa criada como: "+genero.name());			

			(new TPessoa(genero, nThreads,b1,b2)).start();

			nThreads++;
		}

	}

}

class Banheiro{

	AtomicInteger quantidade;
	Genero generoAtual;
	int id;
	Lock lock;
	Condition empty;
	
	public Banheiro(int id){
		generoAtual = Genero.NENHUM;
		this.id = id;
		this.quantidade = new AtomicInteger(0);
		lock = new ReentrantLock();
		empty = lock.newCondition(); 
	}

	public void entrarHomem() throws InterruptedException{

		lock.lock();
		try{

			if(generoAtual == Genero.MULHER){
				if(empty.await(300,TimeUnit.MILLISECONDS) && generoAtual != Genero.MULHER){
					generoAtual = Genero.HOMEM;
					quantidade.getAndIncrement();
				}
			}else{
				generoAtual = Genero.HOMEM;
				quantidade.getAndIncrement();
			}
			
		}finally{
			lock.unlock();
		}
		System.out.println("Banheiro "+id+" com "+quantidade.get()+" "+generoAtual.name()+"(s)");
	}

	public void entrarMulher() throws InterruptedException{
		lock.lock();
		try{

			if(generoAtual == Genero.HOMEM){
				if(empty.await(300,TimeUnit.MILLISECONDS) && generoAtual != Genero.HOMEM){
					generoAtual = Genero.MULHER;
					quantidade.getAndIncrement();
				}
			}else{
				generoAtual = Genero.MULHER;
				quantidade.getAndIncrement();
			}
			
		}finally{
			lock.unlock();
		}
		System.out.println("Banheiro "+id+" com "+quantidade.get()+" "+generoAtual.name()+"(s)");

	}

	public void sairHomem() throws InterruptedException{

		lock.lock();

		try{
			if(quantidade.get() != 0){
				quantidade.getAndDecrement();
			}else{
				generoAtual = Genero.NENHUM;
				empty.signalAll();
			}

		}finally{
			lock.unlock();
		}

		System.out.println("Banheiro "+id+" com "+quantidade.get()+" "+generoAtual.name()+"(s)");
	}

	public void sairMulher() throws InterruptedException{
		lock.lock();

		try{
			if(quantidade.get() > 0){
				quantidade.getAndDecrement();
			}else{
				generoAtual = Genero.NENHUM;
				empty.signalAll();
			}

		}finally{
			lock.unlock();
		}
		System.out.println("Banheiro "+id+" com "+quantidade.get()+" "+generoAtual.name()+"(s)");
	}

}

class TPessoa extends Thread{

	Genero genero;
	Random randGen;
	Banheiro b1;
	Banheiro b2;
	Banheiro current;
	int id;
	public TPessoa(Genero genero,int id, Banheiro b1,Banheiro b2){
		this.genero = genero;
		this.id = id;
		this.b1 = b1;
		this.b2 = b2;
		randGen = new Random();
		current = null;
	}

	public void entrar(Banheiro b){
		current = b;
		try{
			if(genero == Genero.HOMEM)
				b.entrarHomem();
			else if(genero == Genero.MULHER)
				b.entrarMulher();
		}catch(Exception e){
			System.out.println("ops, houve um erro =/ na thread "+id);
			e.printStackTrace();
		}

	}

	public void sair(){
		try{
			if(genero == Genero.HOMEM)
				current.sairHomem();
			else if(genero == Genero.MULHER)
				current.sairMulher();

			current = null;
		}catch(Exception e){
			System.out.println("ops, houve um erro =/ na thread "+id);
			e.printStackTrace();
		}
	}

	public void run(){

		while(true){

			if(b1.quantidade.get() == 0){//prioriza recurso ocioso
				entrar(b1);
			}else if(b2.quantidade.get() == 0){//prioriza recurso ocioso
				entrar(b2);
			}else if(b1.generoAtual == genero){//prioriza recurso acessível
				entrar(b1);
			}else if(b2.generoAtual == genero){//prioriza recurso acessível
				entrar(b2);
			}else{//sorteia um para entrar
				if(randGen.nextInt(100) < 50){
					entrar(b1);
				}else{
					entrar(b2);
				}
			}

			int time = randGen.nextInt(500)+50;
			if(time > 100){
				System.out.println(genero.name()+" "+id+" xixando");
			}else{
				System.out.println(genero.name()+" "+id+" obrando");
			}

			System.out.println(genero.name()+" "+id+" deu descarga");

			sair();
		}
	}
}

enum Genero {
    HOMEM, MULHER, NENHUM
}