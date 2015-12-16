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

import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;

public class WorkflowMonitorImpl implements WorkflowMonitor {
	private Document doc;
	Set<ProcessReader> processes;
	Set<WorkflowReader> workflows;
	Set<Actor> actors;
	
	
	public WorkflowMonitorImpl(){

		try {
			File fXmlFile = new File(System.getProperty("it.polito.dp2.WF.sol1.WFInfo.file"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(fXmlFile);
			this.doc.getDocumentElement().normalize();
			
			// Read processes
			ProcessReader proc;
			processes = new LinkedHashSet<ProcessReader>();
			
			NodeList procList = doc.getElementsByTagName("process");
			for(int i=0; i<procList.getLength(); i++){
				proc = new ProcessReaderImpl((Element)procList.item(i), this);
				processes.add(proc);
			}
			
			// Read workflows
			WorkflowReader wf;
			workflows = new LinkedHashSet<WorkflowReader>();
			
			NodeList wfList = doc.getElementsByTagName("process");
			for(int i=0; i<wfList.getLength(); i++){
				wf = new WorkflowReaderImpl((Element)wfList.item(i), this);
				workflows.add(wf);
			}
			
			for(WorkflowReader wit : workflows){
				for(ProcessReader pit : processes){
					if(pit.getWorkflow().equals(wit.getName())){
						((WorkflowReaderImpl)wit).addProcess(pit);
						((ProcessReaderImpl)pit).setWorkflow(wit);
					}
				}
			}
			
			// Read Actors
			Actor actor;
			actors = new LinkedHashSet<Actor>();
			
			NodeList actorList = doc.getElementsByTagName("actor");
			for(int i=0; i<actorList.getLength(); i++){
				actor = new Actor(((Element)actorList).getAttribute("name"), ((Element)actorList).getAttribute("role"));
				actors.add(actor);
			}
			
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
		return processes;
	}

	@Override
	public WorkflowReader getWorkflow(String wfName) {
		for(WorkflowReader wf : workflows){
			if(wf.getName().equals(wfName))
				return wf;
		}
		return null;
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {
		return workflows;
	}

}
