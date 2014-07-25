package org.ivo.regex;

import java.util.ArrayList;
import java.util.List;

import org.ivo.regex.NdfaNode.Transition;

public class LazyDfa extends DfaNodeImpl {

	private DfaSource dfaSource;
	private List<NdfaNode> ndfas;

	public LazyDfa(List<NdfaNode> ndfas, DfaSource dfaSource) {
		this.ndfas = ndfas;
		this.dfaSource = dfaSource;
		
		for(NdfaNode ndfa : this.ndfas) {
			if(ndfa.payload() > 0) {
				setFinal();
				break;
			}
		}
	}

	public LazyDfa(List<NdfaNode> ndfa) {
		this(ndfa, new DfaSource());
	}

	@Override
	public DfaNode next(char c) {
		DfaNode dfaNext = super.next(c);
		if (dfaNext == null) {
			List<NdfaNode> ndfaNexts = getNdfaNexts(c);
			ndfaNexts.addAll(getEpsilonNexts(c));
			dfaNext = dfaSource.getDfaNode(ndfaNexts);
			dfaNext = new LazyDfa(ndfaNexts, dfaSource);
			add(c, dfaNext);
		}
		return dfaNext;
	}

	private List<NdfaNode> getNdfaNexts(Character c) {
		List<NdfaNode> result = new ArrayList<NdfaNode>();
		for (NdfaNode ndfa : ndfas) {
			result.addAll(ndfa.nexts(c));
		}
		return result;
	}

	private List<NdfaNode> getEpsilonNexts(Character c) {
		List<NdfaNode> result = new ArrayList<NdfaNode>();
		for (NdfaNode ndfa : ndfas) {
			getEpsilonNexts(ndfa, c, result);
		}
		return result;
	}

	private void getEpsilonNexts(NdfaNode ndfa, Character c, List<NdfaNode> result) {
		result.addAll(ndfa.nexts(c));
		for (Transition tr : ndfa.epsNexts()) {
			NdfaNode epsNext = tr.next();
			getEpsilonNexts(epsNext, c, result);
		}
	}

	public List<NdfaNode> getNdfas() {
		return ndfas;
	}
}
