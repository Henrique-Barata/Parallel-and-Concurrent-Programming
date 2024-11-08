package dna.problems;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dna.Result;

public class P3Huge implements Problem {
	@Override
	public char[] getSearchSequence() {
	final var alphabet = new char[] {'a', 't', 'c', 'g'};
	final var random = new Random();
	//random.setSeed(0L);
	final var searchSequence = new char[1024*1024*64];
	for (int i = 0; i < searchSequence.length; i++) {
	searchSequence[i] = alphabet[random.nextInt() & Integer.MAX_VALUE % 4];
	}
	return searchSequence;
	}
	@Override
	public List getPatterns() {
	return Arrays.asList("c", "cc", "aa","ct", "tac","ag","g");
	}
	
	public Result getSolution() {
	return new Result(new int[] {});
	}
}
