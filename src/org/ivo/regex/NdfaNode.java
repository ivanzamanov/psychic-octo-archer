package org.ivo.regex;

import java.util.List;

public interface NdfaNode {
	int payload();
	List<NdfaNode> nexts(Character c);
	List<? extends Transition> nexts();
	List<? extends Transition> epsNexts();

	static interface Transition {
		static final char EPSILON = '\0';
		public char key();
		public NdfaNode next();
		public int marker();
	}
}
