package org.ivo.graph;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class BasicWordGraph implements WordGraph {

	private Node start;

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
	public Node getStart() {
		return start;
	}

	public void serialize() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutputStream dataOS = new DataOutputStream(os);
	}
}
