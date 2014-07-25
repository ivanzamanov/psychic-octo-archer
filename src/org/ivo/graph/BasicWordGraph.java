package org.ivo.graph;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayDeque;
import java.util.List;

public class BasicWordGraph implements WordGraph {

	private BasicNode start;

	@Override
	public WordGraph build(List<String> strings) {
		BasicNode start = new BasicNode();
		for(String str : strings) {
			add(start, str);
		}
		this.start = start;
		return this;
	}

	private void add(BasicNode start, String str) {
		BasicNode current = start;
		for(int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			BasicNode next = current.next(c);
			if(next == null)
				current = current.add(c);
			else
				current = next;
		}
		current.setFinal();
	}

	@Override
	public BasicNode getStart() {
		return start;
	}

	public void serialize() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutputStream dataOS = new DataOutputStream(os);
		ArrayDeque<BasicNode> stack = new ArrayDeque<BasicNode>();
		stack.addLast(start);
		while(!stack.isEmpty()) {
			BasicNode node = stack.pollLast();
			node.write(dataOS);
//			int size = getSize(node);
		}
	}

//	private int getSize(BasicNode node) {
//		return 2 + node.getTransitionCount() * 2;
//	}
}
