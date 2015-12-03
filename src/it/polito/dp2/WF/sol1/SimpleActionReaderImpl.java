package it.polito.dp2.WF.sol1;

import java.util.Set;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.SimpleActionReader;

public class SimpleActionReaderImpl extends ActionReaderImpl implements
		SimpleActionReader {

	public SimpleActionReaderImpl(Element action) {
		super(action);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<ActionReader> getPossibleNextActions() {
		// TODO Auto-generated method stub
		return null;
	}

}
