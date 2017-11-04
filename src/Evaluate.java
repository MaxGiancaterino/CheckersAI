import java.util.concurrent.CountDownLatch;

public class Evaluate implements Runnable {
	
	private Node node; 
	private CountDownLatch latch;
	
	@Override
	public void run() {
		node.evaluate();
		latch.countDown();
	}

	public Evaluate(Node node, CountDownLatch latch) {
		this.node = node;
		this.latch = latch;
	}
}
