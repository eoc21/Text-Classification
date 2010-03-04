package eoc21;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.aliasi.dict.ApproxDictionaryChunker;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.dict.TrieDictionary;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.spell.WeightedEditDistance;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

/**
 * This class extracts all the OWL classes from the polymer ontologies we have
 * created: properties, techniques, conditions and units and adds the terms to
 * a dictionary that may assist text classification.
 * @author ed
 *
 */
public class PolymerOntologicalDictionary {
	private static final double CHUNK_SCORE = 1.0;
	public static final PolymerOntologicalDictionary INSTANCE = new PolymerOntologicalDictionary();
	private TrieDictionary<String> dictionary;
	private ArrayList<String> propertyList;
	private ArrayList<String> techniqueList;
	private ArrayList<String> instrumentList;
	private ArrayList<String> chemicalEntityList;
	private ArrayList<String> monomerNamesList;
	private ArrayList<String> polymerNamesList;
	private ArrayList<String> conditionsList;
	private ArrayList<String> standardsList;
	private ApproxDictionaryChunker dictionaryChunker;
	
	private PolymerOntologicalDictionary(){
		
	}
	
	public TrieDictionary<String> populateDictionary() throws IOException{
		String curDir = System.getProperty("user.dir"); 
		String chemicalEntities =  curDir+"/ChemicalEntities.txt";
		String properties = curDir+"/Properties.txt";
		String conditions = curDir+"/Conditions.txt";
		String instruments = curDir+"/Instruments.txt";
		String monomers = curDir+"/MonomerNames.txt";
		String polymerNames = curDir+"/PolymerNames.txt";
		String techniques = curDir+"/Techniques.txt";
		String standards = curDir+"/Standards.txt";
		chemicalEntityList = gettextdata(chemicalEntities);
		propertyList = gettextdata(properties);
		conditionsList = gettextdata(conditions);
		instrumentList = gettextdata(instruments);
		monomerNamesList = gettextdata(monomers);
		polymerNamesList = gettextdata(polymerNames);
		techniqueList = gettextdata(techniques);
		standardsList = gettextdata(standards);
		dictionary = new TrieDictionary<String>();;
		addToDictionary(chemicalEntityList,dictionary,"ChemicalEntity");
		addToDictionary(propertyList,dictionary,"Property");
		addToDictionary(conditionsList,dictionary,"Condition");
		addToDictionary(instrumentList,dictionary,"Instrument");
		addToDictionary(monomerNamesList,dictionary,"Monomer");
		addToDictionary(polymerNamesList,dictionary,"Polymer");
		addToDictionary(techniqueList,dictionary,"Technique");
		addToDictionary(standardsList,dictionary,"Standard");
		return dictionary;
	}
	
	private ArrayList<String> gettextdata(String fileName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		ArrayList<String> dataStorage = new ArrayList<String>();
		String line;  
		while ((line = br.readLine())!= null)  
		 {  
			dataStorage.add(line);
		 }
		br.close();
		return dataStorage;
	}
	
	private void addToDictionary(ArrayList<String> data, TrieDictionary dict,String label){
		for(int i=0;i<data.size();i++){
			   dictionary.addEntry(new DictionaryEntry<String>(data.get(i),label));	
		}
	}
	
	public ApproxDictionaryChunker createDictionaryChunker(){
		WeightedEditDistance editDistance
        = new FixedWeightEditDistance(0,-1,-1,-1,Double.NaN);
		double maxDistance = 2.0;
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
		dictionaryChunker = new ApproxDictionaryChunker(dictionary,tokenizerFactory,
                editDistance,maxDistance);
		return dictionaryChunker;
	}
	
	public static void main(String[] args) throws IOException{
		TrieDictionary<String> dict = PolymerOntologicalDictionary.INSTANCE.populateDictionary();
		ApproxDictionaryChunker dictChunker =  PolymerOntologicalDictionary.INSTANCE.createDictionaryChunker();
	
	}
	
	
}
