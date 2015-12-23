package cloudFinal11;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Startup implements Runnable{
	
	public Startup() {
    }

	@Override
	public void run() {
    	Runnable pool = new WorkerPool(5);
    	ExecutorService executor = Executors.newFixedThreadPool(2);
    	executor.execute(pool);
	}
}