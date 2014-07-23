package org.ivo.regex;

import java.util.ArrayList;
import java.util.List;

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

	private List<NdfaNode> getNdfaNexts(char c) {
		List<NdfaNode> result = new ArrayList<NdfaNode>();
		for (NdfaNode ndfa : ndfas) {
			result.addAll(ndfa.nexts(c));
		}
		return result;
	}

	private List<NdfaNode> getEpsilonNexts(char c) {
		List<NdfaNode> result = new ArrayList<NdfaNode>();
		for (NdfaNode ndfa : ndfas) {
			for (NdfaNode epsNext : ndfa.epsNexts()) {
				result.addAll(epsNext.nexts(c));
			}
		}
		return result;
	}
}
