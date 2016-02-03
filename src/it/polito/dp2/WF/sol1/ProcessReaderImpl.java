package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm z");
		status = new LinkedList<ActionStatusReader>();
		
		Node child = proc.getFirstChild();
		while(child != null){
			if(child.getNodeType() == Node.ELEMENT_NODE)
				status.add(new ActionStatusReaderImpl((Element)child, monitor));
			child = child.getNextSibling();
		}
	}

	@Override
	public Calendar getStartTime(){	
		GregorianCalendar startTime = new GregorianCalendar();
		try {
			startTime.setTime(dateFormat.parse(proc.getAttribute("startDate")));
		} catch (ParseException e) {
			System.err.println("Error while parsing the start date of the process");
			e.printStackTrace();
		}
		return startTime;
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
