/*A classe Rooms gerencia uma colȩcão de quartos, indexada de 0 a m (onde m  ́e um argumento para o construtor). Threads podem entrar ou sair de de qualquer quarto nessa faixa. Cada quarto pode conter uma quantidade arbitraria de threads simultâneas, mas a qualquer momento, apenas um quarto pode estar ocupado. Por exemplo, se h́a dois quartos com  ́ındices 0 e 1, ent̃ao qualquer número de threads pode entrar no quarto 0 mas nenhuma pode entrar no quarto 1 enquanto o quarto 0 ainda estiver ocupado. Cada quarto pode ter atribuído a ele um tratador de saída: chamar setHandler(i, h) estabele que h  ́e o tratador de saída para o quarto i. O tratador de saída  ́e chamado pela  ́ultima thread a sair de um quarto, mas antes que qualquer thread entre em qualquer quarto. Este metodo  ́e chamado exatamente uma vez e, enquanto ele est́a rodando, nenhuma thread entra em nenhum dos quartos. Implemente a classe Rooms. Certifique-se de que:
- Se alguma thread esta no quarto i, nenhuma thread est ́a em um quarto j tal que i 6= j;
- A  ́ultima thread a sair de um quarto chama seu tratador de sa ́ıda e nenhuma thread está em qualquer um dos quartos enquanto esse tratador esta em
- Threads n ̃ao devem sofrer starvation ao tentar entrar nos quartos. Suponha que o escalonador  ́e justo e que a possibilidade de starvation depende apenas da aplica*/

import java.util.concurrent.locks.*;
import java.util.Random;

public class EE2_2015_2{//Rooms

	public static void main(String[] args){

		Random randGen = new Random();

		int nRooms = randGen.nextInt(20)+2;
		Room[] rooms = new Room[nRooms];

		int nThreads = randGen.nextInt(20)+2;

		System.out.println("Today are " nRooms+" rooms for "+nThreads +" party members.");

		for(int i = 0; i < nRooms; i++){
			rooms[i] = new Room(i);
		}

		for(int i = 0; i < nThreads; i++){
			(new PartyMember()).start();
		}
	}

}

class PartyMember extends Thread{
	int id;
	Room[] rooms;
	Lock[] locks

	Random randGen = new Random();

	public PartyMember(int id, Room[] rooms){
		this.id = id;
		this.rooms = rooms;
		this.locks = locks;
	}

	public void run(){
		while(true){
			//random room to try get in
			int index = randGen.nextInt(rooms.length);
			while(){
				rooms[index].tryGetIn();
			}
		}
	}
}

class Room {

	int id;
	int threads;
	private Lock lock;
	public Room(Int id){
		this.id = id;
		this.threads = 0;
		this.lock = new ReentrantLock();
	}

	public boolean tryGetIn(){
		lock.lock();
		try{
			threads++;
			while(){
					wait();
			}
		}finally{
			lock.unlock();
		}
		return true;
	}

	public synchronized getOut(){
		threads--;
	}

}