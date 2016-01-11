
import java.util.concurrent.locks.*;
import java.util.Random;

public class EE2_2015_2{
	public static void main(String[] args){
		int m = 10;
		Rooms rooms = new Rooms(10);
		int p = m*2;
		Person[] person = new Person[p];

		for(int i = 0; i < p; i++){
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