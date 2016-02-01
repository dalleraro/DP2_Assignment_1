package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.WorkflowMonitor;

public class ActionStatusReaderImpl implements ActionStatusReader {
	WorkflowMonitorImpl monitor;
	Element actionExec;
	private DateFormat dateFormat;

	public ActionStatusReaderImpl(Element actionExec, WorkflowMonitor monitor) {
		this.monitor = (WorkflowMonitorImpl) monitor;
		this.actionExec = actionExec;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm z");
	}

	@Override
	public String getActionName() {
		return actionExec.getAttribute("action");
	}

	@Override
	public Actor getActor() {
		for(Actor actor : monitor.actors)
			if(computeHash("a", actor.getName(), actor.getRole()).equals(actionExec.getAttribute("actor")))
				return actor;
		return null;
	}

	@Override
	public Calendar getTerminationTime() {
		GregorianCalendar terminationTime;
		String time = actionExec.getAttribute("terminationTime");
		if(time.isEmpty())
			return null;
		try {
			terminationTime = new GregorianCalendar();
			terminationTime.setTime(dateFormat.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return terminationTime;
	}

	@Override
	public boolean isTakenInCharge() {
		if(actionExec.getAttribute("actor").isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public boolean isTerminated() {
		if(actionExec.getAttribute("terminationTime").isEmpty())
			return false;
		else
			return true;
	}
	
	private String computeHash(String start, String str1, String str2) {
		String forhash = str1 + str2;
		return start+forhash.hashCode();
	}

}
