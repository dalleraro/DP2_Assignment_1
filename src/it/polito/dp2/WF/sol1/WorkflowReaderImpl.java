package it.polito.dp2.WF.sol1;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class WorkflowReaderImpl implements WorkflowReader {
	private Set<ActionReader> actions;
	private Set<ProcessReader> processes;
	private Element wf;

	public WorkflowReaderImpl(Element workflow) {
		this.wf = workflow;
		this.processes = new LinkedHashSet<ProcessReader>();
		ActionReader ac;
		actions = new LinkedHashSet<ActionReader>();
		
		NodeList list = wf.getElementsByTagName("process");
		for(int i=0; i<list.getLength(); i++){
			ac = new ActionReaderImpl((Element)list.item(i), this);
			actions.add(ac);
		}
	}
	
	public void addProcess(ProcessReader proc){
		processes.add(proc);
	}

	@Override
	public ActionReader getAction(String name) {
		for(ActionReader ac : actions){
			if(ac.getName().equals(name))
				return ac;
		}
		return null;
	}

	@Override
	public Set<ActionReader> getActions() {
		return actions;
	}

	@Override
	public String getName() {
		return wf.getAttribute("name");
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return processes;
	}

}
