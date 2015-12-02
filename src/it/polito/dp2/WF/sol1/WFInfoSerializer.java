package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WFInfoSerializer {

	private Document doc;
	private Element root;
	private WorkflowMonitor monitor;
	private DateFormat dateFormat;

	
	/**
	 * Default constructror
	 * @throws WorkflowMonitorException 
	 */
	public WFInfoSerializer() throws WorkflowMonitorException {
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		monitor = factory.newWorkflowMonitor();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	}
	
	public WFInfoSerializer(WorkflowMonitor monitor) {
		super();
		this.monitor = monitor;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	}

	
	public static void main(String[] args) {
		WFInfoSerializer wf;
		try {
			wf = new WFInfoSerializer();
			wf.printAll();
		} catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			System.exit(1);
		}
	}


	public void printAll() {
		printWorkflows();
		printProcesses();
	}

	private void printProcesses() {
		// TODO Auto-generated method stub
		
	}

	private void printWorkflows() {
		// TODO Auto-generated method stub
		
	}

}
