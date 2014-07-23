package org.ivo.graph;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicNode implements Node {

	private int isFinal;
	private List<BasicNode> targets = new ArrayList<BasicNode>();
	private List<Character> chars = new ArrayList<Character>();

	@Override
	public int isFinal() {
		return isFinal;
	}

	@Override
	public BasicNode next(char c) {
		int index = chars.indexOf(c);
		if(index >= 0)
			return targets.get(index);
		return null;
	}

	void setFinal() {
		isFinal = 1;
	}

	public BasicNode add(char c) {
		BasicNode result = new BasicNode();
		targets.add(result);
		chars.add(c);
		return result;
	}

	public int getTransitionCount() {
		return targets.size();
	}

	public void write(DataOutputStream dataOS) {
//		dataOS.writeInt(targets.size());
//		dataOS.writeInt(isFinal);
	}
}
