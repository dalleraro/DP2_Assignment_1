package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;

public class ActionStatusReaderImpl implements ActionStatusReader {
	Element actionExec;
	private DateFormat dateFormat;

	public ActionStatusReaderImpl(Element actionExec) {
		this.actionExec = actionExec;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	}

	@Override
	public String getActionName() {
		actionExec.getAttribute("action");
		return null;
	}

	@Override
	public Actor getActor() {
		// TODO
		return null;
	}

	@Override
	public Calendar getTerminationTime() {
		Calendar terminationTime;
		String time = actionExec.getAttribute("terminationTime");
		if(time.isEmpty())
			return null;
		try {
			terminationTime = Calendar.getInstance();
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

}
