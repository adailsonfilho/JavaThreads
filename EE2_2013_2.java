import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class EE2_2013_2{
	public static void main(String[] args){
		int n = 4;

		Deque dq = new Deque();

		while(n > 0){
			n--;
			(new ThreadOperator(n,dq)).start();
		}
		
	}
}

class ThreadOperator extends Thread{

	public int id;
	private Deque dq;
	private Random randGen;

	public ThreadOperator(int id, Deque dq){
		this.id = id;
		this.dq = dq;
		this.randGen = new Random();
	}

	public void run(){

		//simulate variant bahavior
		while(true){
			int value = randGen.nextInt(20);
			int operation = randGen.nextInt(4);
			operation = 0;
			if(operation == 0){
				System.out.println(id+" Push left: "+value);
				dq.pushLeft(value);
			}
			// else if(operation == 1){
			// 	System.out.println(id+" Push right: "+value);
			// 	dq.pushRight(value);
				
			// }else if(operation == 2){
			// 	System.out.println(id+" Pop left: "+value);
			// 	value = dq.popLeft();
				
			// }else if(operation == 3){
			// 	System.out.println(id+" Pop right: "+value);
			// 	value = dq.popRight();
				
			// }

		}

	}
}

class Deque{

	DLNode left;
	DLNode right;
	Lock local;

	public Deque(){
		this.left = null;
		this.right = null;
		this.local = new ReentrantLock(true);
	}

	public void pushLeft(int value){

		//Blocks whole deque to check if is empty
		boolean localLock = local.tryLock();
		boolean leftLock = false;
		System.out.println("trava local ativa");

		if(localLock)
		try{
			if(left == null && right ==null){
				left = new DLNode(value);
				right = left;
				System.out.println("tava vazia");

				//controle
				localLock = false;
				local.unlock();
			}else{
				leftLock = left.tryLock();
				if(leftLock){

					System.out.println("conseguiu left");
					left.left = new DLNode(value);
					left.left.right = left;
					left = left.left;

					//controle
					leftLock = false;
					left.unlock();
					System.out.println("liberou left");

					//controle
					localLock = false;
					local.unlock();//release deque if left is locked
				}else{
					//controle
					localLock = false;
					local.unlock();//if couldn't lock left, unlock deque
				}
			}
		}finally{
			// e.printStackTrace();
			if(localLock) local.unlock();
			if(leftLock) left.unlock();
		}

	}

	// public void pushRight(int value){

	// 	boolean done = false;

	// 	while(!done){

	// 		//Blocks whole deque to check if is empty
	// 		local.lock();

	// 		try{
	// 			if(left == null && right ==null){
	// 				left = new DLNode(value);
	// 				right = left;
	// 				local.unlock();
	// 			}else{
	// 				if(right.lock.tryLock()){
	// 					local.unlock();//release deque if left is locked
	// 					right.right = new DLNode(value);
	// 					right = right.right;
	// 					right.lock.unlock();
	// 				}else{
	// 					local.unlock();//if couldn't lock left, unlock deque
	// 				}
	// 			}
	// 		}catch(Exception e){
	// 			local.unlock();
	// 			if(right!=null) right.lock.unlock();
	// 			e.printStackTrace();
	// 		}
	// 	}
	// }

	// public int popRight(){

	// 	boolean done = false;
	// 	int value = 0;

	// 	while(!done){

	// 		//Blocks whole deque to check if is empty
	// 		local.lock();
			
	// 		try{
	// 			if(right==null)
	// 				System.out.println("Empty");
	// 				// throw new Exception("Empty");
	// 			else
	// 			if(right.lock.tryLock()){
	// 				local.unlock();
	// 				value = right.value;
	// 				right = right.left;
	// 				right.lock.unlock();
	// 				done = true;
	// 			}else{
	// 				local.unlock();
	// 			}
	// 		}catch(Exception e){
	// 			local.unlock();
	// 			if(right!=null) right.lock.unlock();
	// 			e.printStackTrace();
	// 		}
	// 	}
	// 	return value; 
	// }

	// public int popLeft(){

	// 	boolean done = false;
	// 	int value = 0;

	// 	while(!done){
	// 		//Blocks whole deque to check if is empty
	// 		local.lock();
	// 		try{
	// 			if(left==null)
	// 				System.out.println("Empty");
	// 				// throw new Exception("Empty");
	// 			else
	// 			if(left.lock.tryLock()){
	// 				local.unlock();
	// 				value = left.value;
	// 				left = left.right;
	// 				left.lock.unlock();
	// 				done = true;
	// 			}else{
	// 				local.unlock();
	// 			}

	// 		}catch(Exception e){
	// 			local.unlock();
	// 			if(right!=null) right.lock.unlock();
	// 			e.printStackTrace();
	// 		}
	// 	}
	// 	return value;
	// }

}

//Double linked node
class DLNode{

	Lock lock;
	DLNode left;
	DLNode right;
	int value;

	public DLNode(int value){
		this.value = value;
		this.lock = new ReentrantLock(true);
	}

	public void lock(){
		lock.lock();
	}

	public void unlock(){
		lock.unlock();
	}

	public boolean tryLock(){
		return lock.tryLock();
	}

}