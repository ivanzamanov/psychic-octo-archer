package org.ivo.regex;

import java.util.Collections;
import java.util.List;

import org.ivo.Benchmarker;

public class RegexTest implements Runnable {
	
	public static void main(String[] args) {
		Benchmarker.benchmark(new RegexTest());
//		new RegexTest().run();
	}

	@Override
	public void run() {
		List<NdfaNode> ndfa = Collections.singletonList(ParseRegex.build("a*bc"));
		LazyDfa lazy = new LazyDfa(ndfa);

		System.out.println(match(lazy, "aaaaabc"));
		System.out.println(match(lazy, "abaabc"));
		System.out.println(match(lazy, "abbac"));
		System.out.println(match(lazy, "bc"));
		System.out.println(match(lazy, "aaaaabac"));
	}

	private boolean match(LazyDfa lazy, String string) {
		DfaNode node = lazy;
		for(int i=0; i<string.length(); i++) {
			char c = string.charAt(i);
			node = node.next(c);
		}
		return node != null && node.payload() > 0;
	}

}
