package org.ivo.regex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DfaSource {
	
	@SuppressWarnings("serial")
	private static final class NdfaNodeSet extends HashSet<NdfaNode> {
		
		public NdfaNodeSet(Collection<NdfaNode> list) {
			super(list);
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof NdfaNodeSet) {
				NdfaNodeSet set = (NdfaNodeSet) other;
				return containsAll(set) && set.containsAll(this);
			} else
				return false;
		}
	}

	private Map<NdfaNodeSet, DfaNode> map = new HashMap<DfaSource.NdfaNodeSet, DfaNode>();

	public DfaNode getDfaNode(List<NdfaNode> ndfaNexts) {
		if(ndfaNexts.size() == 0)
			return null;
		NdfaNodeSet set = new NdfaNodeSet(ndfaNexts);
		DfaNode result = map.get(set);
		if(result == null)
			map.put(set, result);
		return result;
	}
}
