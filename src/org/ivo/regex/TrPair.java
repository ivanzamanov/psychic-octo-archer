package org.ivo.regex;

import org.ivo.regex.NdfaNode.Transition;

public class TrPair extends Pair<Character, NdfaNode> implements Transition {

	private int marker = -1;

	public TrPair(Character key, NdfaNode value) {
		super(key, value);
	}

	@Override
	public char key() {
		return key;
	}

	@Override
	public NdfaNode next() {
		return value;
	}

	@Override
	public int marker() {
		return marker;
	}

	public void marker(int i) {
		marker = i;
	}
	
	@Override
	public String toString() {
		return "" + key + "->" + value;
	}
}
