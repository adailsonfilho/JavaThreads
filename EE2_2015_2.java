/*A classe Rooms gerencia uma colȩcão de quartos, indexada de 0 a m (onde m  ́e um argumento para o construtor). Threads podem entrar ou sair de de qualquer quarto nessa faixa. Cada quarto pode conter uma quantidade arbitraria de threads simultâneas, mas a qualquer momento, apenas um quarto pode estar ocupado. Por exemplo, se h́a dois quartos com  ́ındices 0 e 1, ent̃ao qualquer número de threads pode entrar no quarto 0 mas nenhuma pode entrar no quarto 1 enquanto o quarto 0 ainda estiver ocupado. Cada quarto pode ter atribuído a ele um tratador de saída: chamar setHandler(i, h) estabele que h  ́e o tratador de saída para o quarto i. O tratador de saída  ́e chamado pela  ́ultima thread a sair de um quarto, mas antes que qualquer thread entre em qualquer quarto. Este metodo  ́e chamado exatamente uma vez e, enquanto ele est́a rodando, nenhuma thread entra em nenhum dos quartos. Implemente a classe Rooms. Certifique-se de que:
- Se alguma thread esta no quarto i, nenhuma thread est ́a em um quarto j tal que i 6= j;
- A  ́ultima thread a sair de um quarto chama seu tratador de sa ́ıda e nenhuma thread está em qualquer um dos quartos enquanto esse tratador esta em
- Threads n ̃ao devem sofrer starvation ao tentar entrar nos quartos. Suponha que o escalonador  ́e justo e que a possibilidade de starvation depende apenas da aplica*/

import java.util.concurrent.locks.*;
import java.util.Random;

public class EE2_2015_2{
	public static void main(String[] args){
		int m = 10;
		Rooms rooms = new Rooms(10);
		Person[] person = new Person[m*2];

		for(int i = 0; i < m*2; i++){
			person[i] = new Person(rooms);
			person[i].start();
		}
	}
}

class Person extends Thread{
	Rooms rooms;
	public Person(Rooms rooms){
		this.rooms = rooms;
	}

	public void run(){
		while(true){
			rooms.getIn();
			rooms.leave();
		}
	}
}

class Rooms{

	int n;
	int m;
	int currentRoom;
	Random randGen;

	public Rooms(int m){
		this.n = 0;
		this.m = m;
		this.currentRoom = -1;
		this.randGen = new Random();
	}

	public synchronized void getIn(){

		if(currentRoom == -1){
			//select randomRoomToTryGetIn
			int r = randGen.nextInt(m);
			currentRoom = r;
		}
		n++;
		System.out.println(n+" threads in the room "+currentRoom);
		try{
			Thread.sleep(randGen.nextInt(300)+100);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void leave(){
		n--;
		if(n == 0){
			currentRoom = -1;
			System.out.println("All rooms free");
		}
	}
}