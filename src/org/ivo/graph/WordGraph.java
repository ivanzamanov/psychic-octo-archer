package org.ivo.graph;

import java.util.List;

public interface WordGraph {
	WordGraph build(List<String> strings);
	Node getStart();
}
