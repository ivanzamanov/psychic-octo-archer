package org.ivo;

import java.util.Random;

public class IntSum implements Runnable {

	private int[] a;
	private int sum;

	public IntSum() {
		Random gen = new Random();
		int intCount = 1000;
		int[] a = new int[intCount];
		final int UPPER = 10000;
		for (int i = 0; i < a.length; i++) {
			a[i] = gen.nextInt(UPPER * 2) - UPPER;
		}
		this.a = a;
	}

	public static void exec() {
		Benchmarker.benchmark(new IntSum());
	}

	public void run() {
		sum = sum(a);
	}

	private static int sum(int[] a) {
		int result = 0;
		for (int i = 0; i < a.length; i += 4) {
			int sign, value, j;
			j = i;
			result += (a[j] >= 0 ? a[j] : -a[j]);
			j = i + 1;
			result += (a[j] >= 0 ? a[j] : -a[j]);
			j = i + 2;
			result += (a[j] >= 0 ? a[j] : -a[j]);
			j = i + 3;
			result += (a[j] >= 0 ? a[j] : -a[j]);

//			j = i;
//			sign = a[j] >> 31;
//			value = (a[j] ^ sign) + sign;
//			result += value;
//
//			j = i + 1;
//			sign = a[j] >> 31;
//			value = (a[j] ^ sign) + sign;
//			result += value;
//
//			j = i + 2;
//			sign = a[j] >> 31;
//			value = (a[j] ^ sign) + sign;
//			result += value;
//
//			j = i + 3;
//			sign = a[j] >> 31;
//			value = (a[j] ^ sign) + sign;
//			result += value;
		}
		return result;
	}
}
