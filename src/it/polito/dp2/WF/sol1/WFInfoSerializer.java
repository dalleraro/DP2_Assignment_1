package it.polito.dp2.WF.sol1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;

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
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm z");
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		// factory.setNamespaceAware (true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();

		// Create the document
		doc = builder.newDocument();
		

		// Create and append the root element
		root = (Element) doc.createElement(rootname);
		doc.appendChild(root);
	}


	public static void main(String[] args) {
		WFInfoSerializer wf;
		try {
			FileOutputStream out = new FileOutputStream(args[0]);
			wf = new WFInfoSerializer("workflow_info");
			wf.appendAll();
			wf.serialize(out);
			out.close();

		} catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			System.exit(1);
		} catch (ParserConfigurationException e) {
			System.err.println("Could not configlure parser.");
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("Could not find output file");
			e.printStackTrace();
		} catch (TransformerException e) {
			System.err.println("An error occurred while serializing the XML");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error closing the output file");
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
			String wfName = prr.getWorkflow().getName();
			String startDate = dateFormat.format(prr.getStartTime().getTime());
			process.setAttribute("startDate", startDate);
			process.setAttribute("workflow", wfName);
			List<ActionStatusReader> statusSet = prr.getStatus();
			if(!statusSet.isEmpty()){
				for (ActionStatusReader asr : statusSet){
					Element action_execution = doc.createElement("action_execution");
					action_execution.setAttribute("action",	asr.getActionName());
					if(asr.isTakenInCharge()){
						String actorHash = computeHash("a", asr.getActor().getName(), asr.getActor().getRole());
						action_execution.setAttribute("actor", actorHash);
						if(doc.getElementById(actorHash) == null){
							Element actor = doc.createElement("actor");
							actor.setAttribute("id", actorHash);
							actor.setIdAttribute("id", true);
							actor.setAttribute("name", asr.getActor().getName());
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

	private String computeHash(String start, String str1, String str2) {
		String forhash = str1 + str2;
		return start+forhash.hashCode();
	}

	private void appendWorkflows() {
		// Get the list of workflows
		Set<WorkflowReader> set = monitor.getWorkflows();

		// For each workflow create the element
		for (WorkflowReader wfr: set) {
			Element workflow = doc.createElement("workflow");
			workflow.setAttribute("name", wfr.getName());
			workflow.setIdAttribute("name", true);

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

	public void serialize(OutputStream out) throws TransformerException {
		TransformerFactory xformFactory = TransformerFactory.newInstance ();
		Transformer idTransform;
		idTransform = xformFactory.newTransformer ();
		idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
		idTransform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "wfInfo.dtd");
		Source input = new DOMSource (doc);
		Result output = new StreamResult (out);
		idTransform.transform (input, output);
	}

}
