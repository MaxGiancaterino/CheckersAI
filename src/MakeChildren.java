import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class MakeChildren implements Runnable {

	private Node node; 
	private CountDownLatch latch;
	
	@Override
	public void run() {
		node.getChildren();
		latch.countDown();
	}
	
	public MakeChildren(Node node, CountDownLatch latch) {
		this.node = node;
		this.latch = latch;
	}
	
}
