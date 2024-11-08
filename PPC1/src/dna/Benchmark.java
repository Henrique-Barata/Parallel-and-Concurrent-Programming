package dna;

import java.util.List;
import java.util.function.BiFunction;


import dna.problems.P1Small;
import dna.problems.P2Large;
import dna.problems.P3Huge;
import dna.problems.Problem;



public class Benchmark {
	
	public static Result sequential(Problem p, Integer ncores) {
				
		int[] results = new int[p.getPatterns().size()]; 
		countSeq(results,0,p.getSearchSequence().length,p);
		return new Result(results);
	}
	

	private static void countSeq(int[] results, int start, int end, Problem p) {
		char[] seq = p.getSearchSequence();	
		List <String> patterns = p.getPatterns();
		
		for(int i = 0; i < patterns.size() ; i++) {			
			String pattern = patterns.get(i);				
			for(int k = start; k < end ; k++) {
				if(isIn(seq, k, pattern)) 
					results[i]++;
			}
		}		
	}
	

	public static Result parallel(Problem p, Integer ncores) {
		
		int[] results = new int[p.getPatterns().size()]; 
		int[][] threadResults = new int[ncores][p.getPatterns().size()];
		Thread[] threads = new Thread[ncores];
		int tam = p.getSearchSequence().length;
		
		for(int i = 0; i< threads.length; i++) {
			final int numT = i;
			threads[numT] = new Thread( () ->{
				int start = numT * tam / ncores;
				int end = (numT+1) * tam / ncores;
				countSeq(threadResults[numT], start, end, p);
				
			});
			threads[numT].start();
		}
			for (Thread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {
					System.out.println("Thread " + t + " was interrupted");
				}
			}
			
			for(int k = 0; k < p.getPatterns().size(); k++) {
				for(int j = 0; j < ncores; j++) {
					results[k] += threadResults[j][k];
				}
			}
			
		
		return new Result(results);
	}
	
	
	
	public static boolean isIn(char[] arr, int start, String pattern) {
		if ( (arr.length - start) < pattern.length()) return false;
		for (int i=0; i < pattern.length(); i++) {
			if (arr[start + i] != pattern.charAt(i)) return false;
		}
		return true;
	}
	
	public static void bench(Problem p, BiFunction<Problem, Integer, Result> f, String name) {
		
		int maxCores = Runtime.getRuntime().availableProcessors();
		for (int ncores=1; ncores<=maxCores; ncores *= 2) {

			for (int i=0; i< 10; i++) {
				long tseq = System.nanoTime();
				Result r = f.apply(p, ncores);
				tseq = System.nanoTime() - tseq;
			
				
				if (!r.compare(p.getSolution())) {
					System.out.println("Wrong result for " + name + ".");
					System.exit(1);
				}
				System.out.println(tseq);
			}
		}
	}
	
	public static void main(String[] args) {
		Problem p = (Runtime.getRuntime().availableProcessors() == 64) ? new P2Large() : new P1Small();
		//Problem p = new P3Huge();
		Benchmark.bench(p, Benchmark::sequential, "seq");
		System.out.println("--");
		Benchmark.bench(p, Benchmark::parallel, "par");
	}
}
