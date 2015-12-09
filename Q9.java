/* Jantar dos filofos parametrizado
*/
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Q9{
	public static void main(String[] args) throws Exception{
		int p = 5;

		if(p < 5){
			throw new Exception("The number of philosophers must be 5 or more");
		}

		Philosopher[] ps = new Philosopher[p];
		Fork[] fs = new Fork[p];

		//initializa forks
		for(int i = 0; i < p; i++){
			fs[i] = new Fork(i);
		}

		//initialize philophers
		for(int i = 0; i < p; i++){
			ps[i] = new Philosopher(i);
		}

		for(int i = 0; i < p; i++){

			//fork1
			Fork fl = fs[i];
			if(i == 0) fl = fs[p-1];
			
			//fork2
			Fork fr = fs[i+1];
			if(i == 0) fr = fs[0];
			
			ps[i].f_left = fl;
			ps[i].f_right = fr;

			//indice do vizinho da esquerda
			int left = i -1;
			if(i == 0) left = p - 1;

			//indice do vizinho da direita
			int right = i+1;
			if(i == p - 1) right = 0;

			ps[i].p_left = ps[left];
			ps[i].p_right = ps[right];
			ps[i].start();

		}
	}
}

class Philosopher extends Thread{

	int id;
	Fork f_left;
	Fork f_right;
	Philosopher p_left;
	Philosopher p_right;

	boolean eating;

	public Philosopher(int id){
		this.id = id;
		this.f_left = f_left;
		this.f_right = f_right;
		this.eating = false;
	}

	public void run(){
		while(true){
			if(p_left.eating || p_right.eating){
				//nao da pra comer
				eating = false;
			}else{
				eating = true;
				System.out.println("Philosopher "+this.id+" eating with forks "+this.f_left.id+" e "+this.f_right.id);
			}
			eating = false;
		}

	}
}

class Fork{
	int id;
	public Fork(int id){
		this.id = id;
	}

}