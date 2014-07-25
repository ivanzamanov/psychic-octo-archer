package org.ivo.regex;

import java.util.ArrayList;
import java.util.List;

import org.ivo.regex.NdfaNode.Transition;

public class ParseRegex {
	
	private static NdfaNode build(String regex, int pos, NdfaNode continuation, int markCount) {
		return build(regex, pos, continuation, false, markCount);
	}
    
    private static NdfaNode mark(NdfaNode node, boolean mark, int markCount) {
    	if(mark) {
    		for(Transition tr : node.nexts()) {
    			((TrPair)tr).marker(markCount-1);
    		}
    		for(Transition tr : node.epsNexts()) {
    			((TrPair)tr).marker(markCount-1);
    		}
    	}
		return node;
	}

    private static NdfaNode build(String regex, int pos, NdfaNode continuation, boolean mark, int markCount) {
        if (pos < 0) return continuation;
        char c = regex.charAt(pos);
        switch(c) {
        case '?': {
            int start = argStart(regex, pos - 1);
            String subregex = regex.substring(start, pos);
            NdfaNode sub = build(subregex, subregex.length() - 1, continuation, markCount);
            return build(regex, start - 1, mark(BasicNdfaNode.epsilonTo(sub, continuation), mark, markCount), markCount);
        }
        case '*': {
            NdfaNode[] nexts = new NdfaNode[2];
            nexts[1] = continuation;
            NdfaNode loop = mark(BasicNdfaNode.epsilonTo(nexts), mark, markCount);
            int start = argStart(regex, pos - 1);
            String subregex = regex.substring(start, pos);
            NdfaNode sub = build(subregex, subregex.length() - 1, loop, markCount);
            nexts[0] = sub;
            return build(regex, start - 1, loop, markCount);
        }
        case ')':
            int index = matchingOpenBracket(regex, pos);
            String subregex = regex.substring(index + 1, pos);
            return build(regex, index - 1, buildAlt(subregex, continuation, true, ++markCount), true, ++markCount);
        case '.':
            return build(regex, pos - 1, mark(BasicNdfaNode.dotTo(continuation), mark, markCount), markCount);
        default:
            return build(regex, pos - 1, mark(BasicNdfaNode.transitionTo(c, continuation), mark, markCount), markCount);
        }
    }

	private static int argStart(String regex, int index) {
        if (regex.charAt(index) == ')')
            return matchingOpenBracket(regex, index);
        else return index;
    }
    
    private static int matchingOpenBracket(String regex, int index) {
        for (int i=index - 1; i>=0; --i) {
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
        for (int i=index - 1; i>=0; --i) {
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
    
    private static NdfaNode buildAlt(String regex, NdfaNode continuation, boolean mark, int markCount) {
        int start;
        int end = regex.length();
        List<NdfaNode> children = new ArrayList<NdfaNode>();
        do {
            start = lastSplit(regex, end);
            String subregex = regex.substring(start + 1, end);
            children.add(build(subregex, subregex.length() - 1, continuation, markCount));
            end = start;
        } while (end >= 0);
        return children.size() == 1 ? children.get(0) : mark(BasicNdfaNode.epsilonTo(children), mark, markCount);
    }
    
    public static NdfaNode build(String regex) {
        return buildAlt(regex, BasicNdfaNode.payload(1), false, 0);
    }   
}