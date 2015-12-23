package cloudFinal11;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartupInit implements Runnable{
	
	public StartupInit() {
    }

	@Override
	public void run() {
    	Runnable pool = new ThreadPool(5);
    	ExecutorService executor = Executors.newFixedThreadPool(2);
    	executor.execute(pool);
	}
}