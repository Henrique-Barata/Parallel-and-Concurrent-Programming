import java.util.concurrent.RecursiveTask;

public class Coin extends RecursiveTask<Integer>{

	public static final int LIMIT = 999;
	private static int[] coins; int index; int accumulator; static int maxDepth;
	
	public static int[] createRandomCoinSet(int N) {
		int[] r = new int[N];
		for (int i = 0; i < N ; i++) {
			if (i % 10 == 0) {
				r[i] = 400;
			} else {
				r[i] = 4;
			}
		}
		return r;
	}
	

	public static void main(String[] args) {
		int nCores = Runtime.getRuntime().availableProcessors();
		maxDepth = 5;
		coins = createRandomCoinSet(30);

		int repeats = 5;
		for (int i=0; i<repeats; i++) {
			long seqInitialTime = System.nanoTime();
			int rs = seq(coins, 0, 0);
			long seqEndTime = System.nanoTime() - seqInitialTime;
			System.out.println(nCores + ";Sequential;" + seqEndTime);
			
			long parInitialTime = System.nanoTime();
			int rp = par(coins, 0, 0);
			long parEndTime = System.nanoTime() - parInitialTime;
			System.out.println(nCores + ";Parallel  ;" + parEndTime);
			
			if (rp != rs) {
				System.out.println("Wrong Result!");
				System.exit(-1);
			}
		}

	}

	private static int seq(int[] coins, int index, int accumulator) {
		
		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) {
			return -1;
		}
		
		int a = seq(coins, index+1, accumulator);
		int b = seq(coins, index+1, accumulator + coins[index]);
		return Math.max(a,  b);
		
	}
	
	private static int par(int[] coins, int index, int accumulator) {
		Coin f2 = new Coin(coins, index, accumulator);
		return f2.compute();
	}
	
	public Coin(int[] coins, int n, int accumulator) {
		Coin.coins = coins;
		this.index = n;
		this.accumulator = accumulator;
	}


	@Override
	protected Integer compute() {			
		
		//the depth is the same as the index		
		if (index >= maxDepth) return seq(coins,index, accumulator);

		if ( this.getQueuedTaskCount() > 2 * Runtime.getRuntime().availableProcessors() ) return seq(coins,index, accumulator);
		
		if ( this.getSurplusQueuedTaskCount() > 3 ) return seq(coins,index, accumulator);
		
		if (index >= coins.length) {
			if (accumulator < LIMIT) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > LIMIT) {
			return -1;
		}
		
		
		Coin f1 = new Coin(coins, index+1, accumulator);
		f1.fork();

		Coin f2 = new Coin(coins, index+1, accumulator + coins[index]);
		//f2.fork();
		
		int b = f2.compute();
		int a = f1.join();
					
		return Math.max(a,  b);
	}
}

