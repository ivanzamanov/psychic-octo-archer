package org.ivo.graph;

public interface Node {
	int isFinal();
	Node next(char c);
}
