package org.ivo.regex;

import java.util.ArrayList;
import java.util.List;

import org.ivo.regex.NdfaNode.Transition;

public class ParseRegex {

	private static NdfaNode build(String regex, int pos, NdfaNode continuation) {
		if (pos < 0)
			return continuation;
		char c = regex.charAt(pos);
		switch (c) {
		case '?': {
			NdfaNode sub = build(regex, pos - 1, continuation);
			return BasicNdfaNode.epsilonTo(sub, continuation);
		}
		case '*': {
			NdfaNode[] nexts = new NdfaNode[2];
			nexts[0] = continuation;
			NdfaNode loop = BasicNdfaNode.epsilonTo(nexts);
			NdfaNode sub = build(regex, pos - 1, loop);
			nexts[1] = sub;
			return loop;
		}
		case ')':
			int index = matchingOpenBracket(regex, pos);
			String subregex = regex.substring(index + 1, pos - 1);
			return build(regex, index - 1, buildAlt(subregex, continuation));
		default:
			return build(regex, pos - 1,
					BasicNdfaNode.transitionTo(c, continuation));
		}
	}

	private static int matchingOpenBracket(String regex, int index) {
		for (int i = index - 1; i >= 0; --i) {
			char c = regex.charAt(i);
			switch (c) {
			case '(':
				return i;
			case ')':
				i = matchingOpenBracket(regex, i);
				break;
			}
		}
		throw new RuntimeException("Invalid regex.");
	}

	private static int lastSplit(String regex, int index) {
		for (int i = index - 1; i >= 0; --i) {
			char c = regex.charAt(i);
			switch (c) {
			case '|':
				return i;
			case ')':
				i = matchingOpenBracket(regex, i);
				break;
			}
		}
		return -1;
	}

	private static NdfaNode buildAlt(String regex, NdfaNode continuation) {
        int start;
        int end = regex.length();
        List<NdfaNode> children = new ArrayList<NdfaNode>();
        do {
            start = lastSplit(regex, end);
            String subregex = regex.substring(start + 1, end);
            children.add(build(subregex, subregex.length() - 1, continuation));
            end = start;
        } while (end >= 0);
        return children.size() == 1 ? children.get(0) : BasicNdfaNode.epsilonTo(children);
    }

	public static NdfaNode build(String regex) {
		return buildAlt(regex, BasicNdfaNode.payload(1));
	}

	public static boolean match(NdfaNode node, String s) {
		if (s.isEmpty())
			return node.payload() > 0;
		char c = s.charAt(0);
		for (Transition t : node.nexts()) {
			if (t.key() == c && match(t.next(), s.substring(1)))
				return true;
		}
		for (NdfaNode next : node.epsNexts()) {
			if (match(next, s))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		NdfaNode t = build("a*bc");
		System.out.println(t);

		System.out.println(match(t, "aaaaabc"));
		System.out.println(match(t, "abaabc"));
		System.out.println(match(t, "abbac"));
		System.out.println(match(t, "bc"));
		System.out.println(match(t, "aaaaabac"));

		t = build("a|bc");
		System.out.println(t);
		t = build("(bc|d)*e");
		// System.out.println(t);

	}
}
