package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	 * @throws ParserConfigurationException 
	 */
	public WFInfoSerializer(String rootname) throws WorkflowMonitorException, ParserConfigurationException {
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		monitor = factory.newWorkflowMonitor();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		// factory.setNamespaceAware (true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();

		// Create the document
		doc = builder.newDocument();
		

		// Create and append the root element
		root = (Element) doc.createElement(rootname);
		doc.appendChild(root);
	}

	public WFInfoSerializer(String rootname, WorkflowMonitor monitor) throws ParserConfigurationException {
		super();
		this.monitor = monitor;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// factory.setNamespaceAware (true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Create the document
		doc = builder.newDocument();

		// Create and append the root element
		root = (Element) doc.createElement(rootname);
		doc.appendChild(root);
	}


	public static void main(String[] args) {
		WFInfoSerializer wf;
		try {
			File out = new File(args[0]);
			wf = new WFInfoSerializer("workflow_info");
			wf.appendAll();
			wf.serialize(new PrintStream(out));

		} catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			System.exit(1);
		} catch (ParserConfigurationException e) {
			System.err.println("Could not configlure parser.");
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void appendAll() {
		appendWorkflows();
		appendProcesses();
	}

	private void appendProcesses() {
		// Get the list of processes
		Set<ProcessReader> set = monitor.getProcesses();

		// For each process create the element
		for (ProcessReader prr: set) {
			Element process = doc.createElement("process");
			process.setAttribute("startDate", dateFormat.format(prr.getStartTime().getTime()));
			process.setAttribute("workflow", prr.getWorkflow().getName());

			List<ActionStatusReader> statusSet = prr.getStatus();
			if(!statusSet.isEmpty()){
				for (ActionStatusReader asr : statusSet){
					Element action_execution = doc.createElement("action_execution");
					action_execution.setAttribute("action",	asr.getActionName());
					if(asr.isTakenInCharge()){
						action_execution.setAttribute("actor", asr.getActor().getName());
						if(doc.getElementById(asr.getActor().getName()) == null){
							Element actor = doc.createElement("actor");
							actor.setAttribute("name", asr.getActor().getName());
							actor.setIdAttribute("name", true);
							actor.setAttribute("role", asr.getActor().getRole());
							
							root.appendChild(actor);
						}
					}
					if(asr.isTerminated())
						action_execution.setAttribute("terminationTime", dateFormat.format(asr.getTerminationTime().getTime()));
						
					process.appendChild(action_execution);
				}
			}
			root.appendChild(process);
		}
	}

	private void appendWorkflows() {
		// Get the list of workflows
		Set<WorkflowReader> set = monitor.getWorkflows();

		// For each workflow create the element
		for (WorkflowReader wfr: set) {
			Element workflow = doc.createElement("workflow");
			workflow.setAttribute("name", wfr.getName());

			Set<ActionReader> setAct = wfr.getActions();
			for (ActionReader ar: setAct) {
				Element action = doc.createElement("action");
				action.setAttribute("name", ar.getName());
				action.setAttribute("role", ar.getRole());
				action.setAttribute("automInst", ar.isAutomaticallyInstantiated() ? "true" : "false");
				if(ar instanceof SimpleActionReader) {
					Element simpleAct = doc.createElement("simple_action");
					StringBuffer buff = new StringBuffer();
					Set<ActionReader> setNxt = ((SimpleActionReader)ar).getPossibleNextActions();
					if(!setNxt.isEmpty()){
						for (ActionReader nAct: setNxt)
							buff.append(nAct.getName()+" ");
						simpleAct.setAttribute("nextPossActions", buff.toString().trim());
					}
					action.appendChild(simpleAct);
				}
				else if (ar instanceof ProcessActionReader) {
					Element processAct = doc.createElement("process_action");
					processAct.setAttribute("workflow", ((ProcessActionReader)ar).getActionWorkflow().getName());
					action.appendChild(processAct);
				}
				workflow.appendChild(action);
			}
			root.appendChild(workflow);
		}

	}

	public void serialize(PrintStream out) throws TransformerException {
		TransformerFactory xformFactory = TransformerFactory.newInstance ();
		Transformer idTransform;
		idTransform = xformFactory.newTransformer ();
		idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
		idTransform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "WfInfo.dtd");
		Source input = new DOMSource (doc);
		Result output = new StreamResult (out);
		idTransform.transform (input, output);
	}

}
