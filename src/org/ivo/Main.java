package org.ivo;

import org.ivo.graph.GraphTest;

public class Main implements Runnable {

	public static void main(String[] args) {
		GraphTest.exec();
	}

	public static void exec() {
		new Main() {
			public void run() {
				final String[] tests = Tests.benchmarks;
				for (int i = 0; i < tests.length - 1; i += 2) {
					String str1 = tests[i];
					String str2 = tests[i + 1];
					boolean equal = match(str1, str2) == matchPattern(str1,
							str2);
					if (!equal) {
						System.out.println("Fail " + i);
						System.out.println(match(str1, str2));
						System.out.println(matchPattern(str1, str2));
						System.exit(1);
					}
				}
			};
		}.run();

		final String[] tests = Tests.benchmarks;
		System.out.println("External");
		Benchmarker.benchmark(new Main() {
			public void run() {
				for (int i = 0; i < tests.length - 1; i += 2) {
					match(tests[i], tests[i + 1]);
				}
			};
		});
		System.out.println("Internal");
		Benchmarker.benchmark(new Main() {
			public void run() {
				for (int i = 0; i < tests.length - 1; i += 2) {
					matchPattern(tests[i], tests[i + 1]);
				}
			};
		});
	}

	public void run() {
		for (int i = 0; i < Tests.tests.length - 1; i += 2) {
			match(Tests.tests[i], Tests.tests[i + 1]);
		}
	}

	public boolean matchPattern(String pat, String str) {
		String regex = pat.replaceAll("\\*", ".*").replaceAll("\\?", ".");
		return str.matches(regex);
	}

	public boolean match(String pattern, String str) {
		boolean[][] cache = new boolean[pattern.length() + 1][str.length() + 1];
		cache[0][0] = true;
		return match(pattern, str, 0, 0, cache);
	}

	private boolean match(String pattern, String str, int pi, int si,
			boolean[][] cache) {
		if (pi >= pattern.length())
			return si >= str.length();
		if (pattern.charAt(pi) == '*') {
			return cmatch(pattern, str, pi + 1, si, cache)
					|| (si < str.length() && cmatch(pattern, str, pi, si + 1,
							cache));
		} else if (si >= str.length())
			return false;
		return (pattern.charAt(pi) == '?' || pattern.charAt(pi) == str
				.charAt(si)) && cmatch(pattern, str, pi + 1, si + 1, cache);
	}

	private boolean cmatch(String pattern, String str, int pi, int si,
			boolean[][] cache) {
		if (cache[pi][si])
			return false;
		boolean result = match(pattern, str, pi, si, cache);
		cache[pi][si] = true;
		return result;
	}
}
