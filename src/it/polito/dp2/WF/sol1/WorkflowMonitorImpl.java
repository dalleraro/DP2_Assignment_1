package it.polito.dp2.WF.sol1;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;

public class WorkflowMonitorImpl implements WorkflowMonitor {
	Document doc;
	
	
	public WorkflowMonitorImpl(){

		try {
			File fXmlFile = new File(System.getProperty("it.polito.dp2.WF.sol1.WFInfo.file"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(fXmlFile);
			this.doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Set<ProcessReader> getProcesses() {
		ProcessReader proc;
		Set<ProcessReader> processes = new LinkedHashSet<ProcessReader>();
		
		NodeList list = doc.getElementsByTagName("process");
		for(int i=0; i<list.getLength(); i++){
			proc = new ProcessReaderImpl((Element)list.item(i));
			processes.add(proc);
		}
		return processes;
	}

	@Override
	public WorkflowReader getWorkflow(String wf) {
		Element workflow = doc.getElementById(wf);
		if(workflow != null)
			return new WorkflowReaderImpl(workflow);
		else
			return null;
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {
		WorkflowReader wf;
		Set<WorkflowReader> workflows = new LinkedHashSet<WorkflowReader>();
		
		NodeList list = doc.getElementsByTagName("process");
		for(int i=0; i<list.getLength(); i++){
			wf = new WorkflowReaderImpl((Element)list.item(i));
			workflows.add(wf);
		}
		return workflows;
	}

}
