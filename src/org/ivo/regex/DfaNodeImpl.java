package org.ivo.regex;

import java.util.ArrayList;
import java.util.List;

public class DfaNodeImpl implements DfaNode {
	
	private int isFinal;
	private List<DfaNode> targets = new ArrayList<DfaNode>();
	private List<Character> chars = new ArrayList<Character>();
	
	@Override
	public int payload() {
		return isFinal;
	}

	@Override
	public DfaNode next(char c) {
		int index = chars.indexOf(c);
		if(index >= 0)
			return targets.get(index);
		return null;
	}

	public void setFinal() {
		isFinal = 1;
	}

	public DfaNode add(char c, DfaNode node) {
		addNext(c, node);
		return node;
	}

	protected void addNext(char c, DfaNode nextNode) {
		targets.add(nextNode);
		chars.add(c);
	}
}
