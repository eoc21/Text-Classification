package eoc21;

import java.util.ArrayList;

/**
 * This class extracts all the OWL classes from the polymer ontologies we have
 * created: properties, techniques, conditions and units and adds the terms to
 * a dictionary that may assist text classification.
 * @author ed
 *
 */
public class PolymerOntologicalDictionary {
	public static final PolymerOntologicalDictionary INSTANCE = new PolymerOntologicalDictionary();
	private ArrayList<String> properties = new ArrayList<String>();
	private ArrayList<String> techniques = new ArrayList<String>();
	//Make want instruments separate from techniques.
	private ArrayList<String> conditions = new ArrayList<String>();
	private ArrayList<String> units = new ArrayList<String>();
	
	private PolymerOntologicalDictionary(){
		
	}
	
	
}
