package it.polito.dp2.WF.sol1;

import java.util.Set;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class WorkflowReaderImpl implements WorkflowReader {
	Element wf;

	public WorkflowReaderImpl(Element workflow) {
		this.wf = workflow;
	}

	@Override
	public ActionReader getAction(String arg0) {
		ActionReader acRead;
		Element action = (Element)wf.getFirstChild();
		do{
			acRead = new ActionReaderImpl(action);
			if(acRead.getName().equals(arg0))
				return acRead;
		}while((action = (Element)action.getNextSibling()) != null);
		return null;
	}

	@Override
	public Set<ActionReader> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		// TODO Auto-generated method stub
		return null;
	}

}
