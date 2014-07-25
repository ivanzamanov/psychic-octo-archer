package org.ivo.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ivo.regex.NdfaNode.Transition;

public class Regex {

	public static void main(String[] args) {
		Regex regex = new Regex("ab(a|b)d(bac|abc)d");
		String[] strings = regex.find("abadabc");
		print(strings);
//		strings = regex.find("abbcbac");
//		print(strings);
	}

	private static void print(String[] strings) {
		for (String str : strings) {
			if(str != null)
				System.out.println(str);
		}
	}

	private LazyDfa ndfa;
	private String pattern;

	public Regex(String pattern) {
		ndfa = new LazyDfa(Collections.singletonList(ParseRegex.build(pattern)));
		this.pattern = pattern;
	}

	public boolean match(String text) {
		return match(ndfa, text);
	}

	private boolean match(LazyDfa lazy, String string) {
		DfaNode node = lazy;
		for (int i = 0; i < string.length() && node != null; i++) {
			char c = string.charAt(i);
			node = node.next(c);
		}
		return node != null && node.payload() > 0;
	}

	public String[] find(String text) {
		NdfaNode ndfa = this.ndfa.getNdfas().get(0);
		int bracketCount = 10;
		int[] result = find(ndfa, text, bracketCount);
		String[] strings = new String[bracketCount];
		strings[0] = text;
		for (int i = 0; i < bracketCount; i += 2) {
			if(result[i + 1] == -1)
				continue;
			strings[1 + i / 2] = text.substring(result[i + 1], result[i]);
		}
		return strings;
	}

	private static class State {
		public State(NdfaNode ndfa, int[] is) {
			node = ndfa;
			indices = is;
		}

		NdfaNode node;
		int[] indices;

		@Override
		public boolean equals(Object obj) {
			State other = (State) obj;
			return other.node.equals(node);
		}

		@Override
		public String toString() {
			return node.toString();
		}
	}

	private int[] find(NdfaNode ndfa, String text, int indexCount) {
		List<State> input = new ArrayList<Regex.State>();
		int[] init = new int[indexCount];
		Arrays.fill(init, -1);
		input.add(new State(ndfa, init));
		List<State> output = new ArrayList<Regex.State>();
		for (int pos = 0; pos < text.length(); pos++) {
			char c = text.charAt(pos);
			for (State state : input) {
				for (Transition tr : state.node.nexts()) {
					if (tr.key() == c)
						addOutput(input, output, pos, state, tr, tr.marker());
				}
				for (Transition tr : state.node.epsNexts()) {
					for (Transition tr2 : tr.next().nexts()) {
						if (tr2.key() == c)
							addOutput(input, output, pos, state, tr2,
									tr.marker());
					}
				}
			}
			List<State> t = output;
			output = input;
			input = t;
			output.clear();
		}
		for (State state : input)
			if (state.node.payload() != 0)
				return state.indices;
		return null;
	}

	private static void addOutput(List<State> input, List<State> output,
			int pos, State state, Transition tr, int marker) {
		State os;
		if (marker == -1) {
			os = new State(tr.next(), state.indices);
		} else {
			int[] indices = Arrays.copyOf(state.indices, state.indices.length);
			indices[marker] = pos + 1;
			os = new State(tr.next(), indices);
		}
		if (!input.contains(os)) {
			output.add(os);
		}
	}
}
