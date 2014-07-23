package org.ivo.regex;

public interface DfaNode {
	int payload();
	DfaNode next(char c);
	DfaNode add(char c, DfaNode dfaNext);
}
