package eoc21;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ChunkerEvaluator;
import com.aliasi.chunk.Chunking;
import com.aliasi.chunk.ChunkingEvaluation;
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
	private ArrayList<String> punctuationList;
	private ArrayList<String> miscellaneousList;
	private ApproxDictionaryChunker dictionaryChunker;
	private static String currentDir = System.getProperty("user.dir");

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
		String punctuation = curDir+"/Punctuation.txt";
		String miscellaneous = curDir+"/Miscellaneous.txt";
		chemicalEntityList = gettextdata(chemicalEntities);
		propertyList = gettextdata(properties);
		conditionsList = gettextdata(conditions);
		instrumentList = gettextdata(instruments);
		monomerNamesList = gettextdata(monomers);
		polymerNamesList = gettextdata(polymerNames);
		techniqueList = gettextdata(techniques);
		standardsList = gettextdata(standards);
		punctuationList = gettextdata(punctuation);
		miscellaneousList = gettextdata(miscellaneous);
		dictionary = new TrieDictionary<String>();
		addToDictionary(chemicalEntityList,dictionary,"ChemicalEntity");
		addToDictionary(propertyList,dictionary,"Property");
		addToDictionary(conditionsList,dictionary,"Condition");
		addToDictionary(instrumentList,dictionary,"Instrument");
		addToDictionary(monomerNamesList,dictionary,"Monomer");
		addToDictionary(polymerNamesList,dictionary,"Polymer");
		addToDictionary(techniqueList,dictionary,"Technique");
		addToDictionary(standardsList,dictionary,"Standard");
	//	addToDictionary(punctuationList,dictionary,"Punctuation");
	//	addToDictionary(miscellaneousList,dictionary,"Miscellaneous");
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
		double maxDistance = 1.0;
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
		dictionaryChunker = new ApproxDictionaryChunker(dictionary,tokenizerFactory,
                editDistance,maxDistance);
		return dictionaryChunker;
	}
	
	
	
	public static void main(String[] args) throws IOException{
		TrieDictionary<String> dict = PolymerOntologicalDictionary.INSTANCE.populateDictionary();
		ApproxDictionaryChunker dictChunker =  PolymerOntologicalDictionary.INSTANCE.createDictionaryChunker();
	    ChunkerEvaluator evaluator = new ChunkerEvaluator(dictChunker);
	    evaluator.setVerbose(true);	
	    ArrayList<String>filteredTerms = new ArrayList<String>();
	    String filterFile = PolymerOntologicalDictionary.currentDir+"/Filter.txt";
	    BufferedReader ba = new BufferedReader(new FileReader(filterFile));
	    String filteredTerm;
	    while((filteredTerm = ba.readLine())!=null){
	    	filteredTerms.add(filteredTerm);
	    }
	    String inputFile = PolymerOntologicalDictionary.currentDir+"/Training/PolymerPapers/PolymerTr1.txt";
	    BufferedReader br = new BufferedReader(new FileReader(inputFile));
	    String line;
	    while((line = br.readLine()) != null ) {
	    	System.out.println(line);
	    Chunking chunking = dictChunker.chunk(line);
	    CharSequence cs = chunking.charSequence();
        Set<Chunk> chunkSet = chunking.chunkSet();
        for (Chunk chunk : chunkSet) {
            int start = chunk.start();
            int end = chunk.end();
            CharSequence str = cs.subSequence(start,end);
            double distance = chunk.score();
            String match = chunk.type();
            //filter on size >3
            if(str.length()>3){
            	boolean filterIt = false;
            	for(int i=0;i<filteredTerms.size();i++){
            		if(str.equals(filteredTerms.get(i))){
            			filterIt = true;
            		}
            	}
            	if(filterIt != true){
                    System.out.printf("%15s  %15s   %8.1f\n",
                            str, match, distance);	            		
            	}
            }
        }
	    }
	}
	
	
}
