package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.WorkflowReader;

public class ProcessActionReaderImpl extends ActionReaderImpl implements
		ProcessActionReader {

	public ProcessActionReaderImpl(Element action, WorkflowReader enclosingWf) {
		super(action, enclosingWf);
	}

	@Override
	public WorkflowReader getActionWorkflow() {
		for(WorkflowReader wf : enclosingWf.monitor.workflows)
			if(wf.getName().equals(this.action.getAttribute("workflow")))
				return wf;
		return null;
	}

}
