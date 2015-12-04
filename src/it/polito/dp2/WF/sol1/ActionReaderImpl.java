package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowReader;

public class ActionReaderImpl implements ActionReader {
	protected Element action;
	protected WorkflowReader enclosingWf;

	public ActionReaderImpl(Element action, WorkflowReader encWf) {
		this.action = action;
	}

	@Override
	public WorkflowReader getEnclosingWorkflow() {
		return enclosingWf;
	}

	@Override
	public String getName() {
		return action.getAttribute("name");
	}

	@Override
	public String getRole() {
		return action.getAttribute("role");
	}

	@Override
	public boolean isAutomaticallyInstantiated() {
		if(action.getAttribute("automInst").equals("true"))
			return true;
		else 
			return false;
	}

}
