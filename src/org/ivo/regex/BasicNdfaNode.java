package org.ivo.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasicNdfaNode implements NdfaNode {

	int payload;
	List<? extends Transition> nexts;
	List<NdfaNode> epsNexts;

	private BasicNdfaNode(int payload, List<? extends Transition> nexts,
			List<NdfaNode> epsNexts) {
		this.payload = payload;
		this.nexts = nexts;
		this.epsNexts = epsNexts;
	}

	public static BasicNdfaNode payload(int payload) {
		return new BasicNdfaNode(payload, Collections.<Transition> emptyList(),
				Collections.<NdfaNode> emptyList());
	}

	public static BasicNdfaNode epsilonTo(NdfaNode... next) {
		return new BasicNdfaNode(0, Collections.<Transition> emptyList(),
				Arrays.asList(next));
	}

	public static NdfaNode epsilonTo(List<NdfaNode> next) {
		return new BasicNdfaNode(0, Collections.<Transition> emptyList(), next);
	}

	public static BasicNdfaNode transitionTo(char c, NdfaNode next) {
		return new BasicNdfaNode(0, Collections.singletonList(new TrPair(c,
				next)), Collections.<NdfaNode> emptyList());
	}

	@Override
	public int payload() {
		return payload;
	}

	@Override
	public List<? extends Transition> nexts() {
		return nexts;
	}

	@Override
	public List<NdfaNode> epsNexts() {
		return epsNexts;
	}

	@Override
	public String toString() {
		return "BasicNdfaNode [payload=" + payload + ", nexts=" + nexts
				+ ", epsNexts=" + epsNexts + "]";
	}

	@Override
	public List<NdfaNode> nexts(Character c) {
		List<NdfaNode> result = new ArrayList<NdfaNode>();
		for(Transition p : nexts) {
			if(p.key() == c) {
				result.add(p.next());
			}
		}
		return result;
	}
}
