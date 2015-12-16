package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;

public class ProcessReaderImpl implements ProcessReader {
	private Element proc;
	private WorkflowReader workflow;
	private DateFormat dateFormat;
	private List<ActionStatusReader> status;

	public ProcessReaderImpl(Element proc, WorkflowMonitor monitor) {
		this.proc = proc;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		status = new LinkedList<ActionStatusReader>();
		
		Element child = (Element)proc.getFirstChild();
		while(child != null){
			status.add(new ActionStatusReaderImpl(child, monitor));
			child = (Element)child.getNextSibling();
		}
	}

	@Override
	public Calendar getStartTime() {	
		try {
			Calendar startTime = Calendar.getInstance();	
			startTime.setTime(dateFormat.parse(proc.getAttribute("startDate")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ActionStatusReader> getStatus() {
		return status;
	}

	@Override
	public WorkflowReader getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowReader wf) {
		this.workflow = wf;
	}

}
