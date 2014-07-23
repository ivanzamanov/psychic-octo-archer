package org.ivo.regex;

import java.util.List;

public interface NdfaNode {
	int payload();

	List<NdfaNode> nexts(Character c);
	List<? extends Transition> nexts();

	List<? extends NdfaNode> epsNexts();

	static interface Transition {
		public char key();

		public NdfaNode next();
	}
}
