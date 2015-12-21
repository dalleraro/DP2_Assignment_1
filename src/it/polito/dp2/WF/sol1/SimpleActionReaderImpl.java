package it.polito.dp2.WF.sol1;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowReader;

public class SimpleActionReaderImpl extends ActionReaderImpl implements
		SimpleActionReader {

	public SimpleActionReaderImpl(Element action, WorkflowReader enclosingWf) {
		super(action, enclosingWf);
	}

	@Override
	public Set<ActionReader> getPossibleNextActions() {
		String[] nextActions = this.action.getAttribute("nextPossActions").split("\\s");
		Set<ActionReader> actions = new LinkedHashSet<ActionReader>();
		for(String str : nextActions)
			for(ActionReader action : enclosingWf.actions)
				if(str.equals(action.getName()))
					actions.add(action);
		return actions;
	}

}
