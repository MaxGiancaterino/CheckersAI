import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class TurnTree {
	
	private ExecutorService executor;
	private CountDownLatch latch;
	private List<Node> levelNodes = new LinkedList<Node>();
	
	public TurnTree(Node root, ExecutorService executor) {
		this.executor = executor;
		latch = new CountDownLatch(1);
		levelNodes.add(root);
	}
	public void constructTree(int level) {

		while (level != 0) {
			List<Node> newLevelNodes= new LinkedList<Node>();
			for (Node node : levelNodes) {
				MakeChildren task = new MakeChildren(node, latch);
				executor.submit(task);
			}
			try {
			latch.await();
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			//System.out.println("-----------------------------------------------------------------");
			//System.out.println("Motherfucking Size: " + levelNodes.size());
			//System.out.println("-----------------------------------------------------------------");
			for (Node node : levelNodes) {
				//if (node.getTurn() == null) {
					//System.out.println("ROOT");
				//} else {
					//for (Coord c : node.getTurn()) {
						//System.out.println(c);
					//}
				//}
				//System.out.println("");
				newLevelNodes.addAll(node.returnChildren());
			}
			levelNodes.clear();
			levelNodes = newLevelNodes;
			latch = new CountDownLatch(levelNodes.size());
			level--;
		}
		
		for (Node node : levelNodes) {
			Evaluate task = new Evaluate(node, latch);
			executor.submit(task);
		}
		try {
		latch.await();
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	public static List<Coord> getBotMove(Board board, boolean blackBot, boolean blacksTurn, int depth, ExecutorService executor) {
		Node root = new Node();
		root.makeRoot(board, blacksTurn);
		TurnTree t = new TurnTree(root, executor);
		
		long startTime = System.nanoTime();
		t.constructTree(depth);
		long endTime = System.nanoTime();
		
		System.out.println("Construction Time: " + (endTime - startTime) / 1000000000.0 + " seconds");
		
		startTime = System.nanoTime();
		int moveValue = Node.evaluateNode(root, blackBot);
		endTime = System.nanoTime();
		
		System.out.println("Evaluation Time: " + (endTime - startTime) / 1000000000.0 + " seconds");
		Runtime r = Runtime.getRuntime();
        System.out.printf("%.2f", r.freeMemory() / (double) r.totalMemory() * 100);
        System.out.println("%");
        r.gc();
        System.out.printf("%.2f", r.freeMemory() / (double) r.totalMemory() * 100);
        System.out.println("%");
        System.out.printf("%.2f", r.maxMemory() / 1000000.0);
        System.out.println("mb");
		Set<Node> children = root.returnChildren();
		//System.out.println(children.isEmpty());
		//System.out.println(moveValue);
		int counter = 0;
		for (Node child : children) {
			if (child.getValue() == moveValue) {
				counter++;
			}
		}
		int choice = new Random().nextInt(counter);
		counter = 0;
		for (Node child : children) {
			if (child.getValue() == moveValue) {
				if (choice == counter) {
					return child.getTurn();
				} else {
				counter++;
				}
			}
		}
		//System.out.println("LOST");
		return null;
	}
}
