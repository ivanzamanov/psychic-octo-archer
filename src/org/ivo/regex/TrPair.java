package org.ivo.regex;

import org.ivo.regex.NdfaNode.Transition;

public class TrPair extends Pair<Character, NdfaNode> implements Transition {

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

}
