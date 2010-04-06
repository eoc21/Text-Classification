package eoc21;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.crf.ChainCrf;
import com.aliasi.crf.ChainCrfFeatureExtractor;
import com.aliasi.io.LogLevel;
import com.aliasi.io.Reporter;
import com.aliasi.io.Reporters;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.RegressionPrior;
import com.aliasi.tag.Tagging;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Files;

/**
 * Builds a conditional random field tagger from a specified corpus of tagged text.
 * @author ed
 *
 */
public class ConditionalRandomFieldTagger {
	private boolean addIntercept;
	private int minFeatureCount;
	private boolean cacheFeatures;
	private boolean unSeenTransitions;
	private double priorVariance;
	private boolean uninformativeIntercept;
	private double initialLearningRate;
	private double learningRateDecay;
	private double minImprovement;
    private int minEpochs;
    private int maxEpochs;
    private RegressionPrior prior;
    private AnnealingSchedule annealingSchedule;
    private int priorBlockSize;
    private ChainCrfFeatureExtractor<String> featureExtractor;
    private ChainCrf<String> crf;
    private Reporter reporter;
  
    public ConditionalRandomFieldTagger(final boolean intercept, final int featureCount, final boolean cacheTheFeatures,final boolean transitions) throws IOException{
		this.addIntercept = intercept;
		this.minFeatureCount = featureCount;
		this.cacheFeatures = cacheTheFeatures;
		this.unSeenTransitions = transitions;
		 reporter = Reporters.stdOut().setLevel(LogLevel.DEBUG);
//		 featureExtractor = (ChainCrfFeatureExtractor<String>) new SimpleChainCrfFeatureExtractor();
		 Corpus v = processTrainingData("");	
    }
	
	public void setMinImprovement(final double improvementMinimum){
		this.minImprovement = improvementMinimum;
	}
	
	public void setMinEpochs(final int epochMinimum){
		this.minEpochs = epochMinimum;
	}
	
	public void setMaxEpochs(final int epochMaximum){
		this.maxEpochs = epochMaximum;
	}
	
	public void setPriorBlockSize(final int blockSize){
		this.priorBlockSize = blockSize;
	}
	
	public void setRegressionPrior(final double pVariance, final boolean unInformativeIntercept){
		this.priorVariance = pVariance;
		this.uninformativeIntercept = unInformativeIntercept;
		prior = RegressionPrior.gaussian(pVariance, uninformativeIntercept);
	}
	
	public void setAnnealingSchedule(final double learningRateInitial, final double learningDecayRate){
		this.initialLearningRate = learningRateInitial;
		this.learningRateDecay = learningDecayRate;
		annealingSchedule = AnnealingSchedule.exponential(initialLearningRate,
                                        learningRateDecay);
	}
	
/*	public ChainCrf<String> buildModel(String fileToWriteModelTo){
		crf = ChainCrf.estimate(corpus,
                featureExtractor,
                addIntercept,
                minFeatureCount,
                cacheFeatures,
                unSeenTransitions,
                prior,
                priorBlockSize,
                annealingSchedule,
                minImprovement,
                minEpochs,
                maxEpochs,
                reporter);
		File modelFile = new File(fileToWriteModelTo);
        System.out.println("\nCompiling to file=" + modelFile);
        AbstractExternalizable.serializeTo(crf,modelFile);

		return null;
	}*/
	
	private Corpus processTrainingData(String trainingDirectory) throws IOException{
		String currentDir = System.getProperty("user.dir");
		String files =  currentDir+"/Training/ManuallyMarkedUp/";
		File classDir = new File(files);
			if (!classDir.isDirectory()) {
				String msg = "Could not find training directory=" + classDir
						+ "\nHave you unpacked 4 newsgroups?";
				System.out.println(msg); // in case exception gets lost in shell
				throw new IllegalArgumentException(msg);}
			else{
				String[] trainingFiles = classDir.list();
			for (int j = 0; j < trainingFiles.length; ++j) {
				File file = new File(classDir, trainingFiles[j]);
				String text = Files.readFromFile(file, "ISO-8859-1");
				String[] delimittedText = text.split(" ");
				ArrayList<String> tokens = new ArrayList<String>();
				ArrayList<String> tags = new ArrayList<String>();
				for(String words: delimittedText){
					String[] tokenAndTag = words.split("_");
					tokens.add(tokenAndTag[0]);
					tags.add(tokenAndTag[1]);
				}
				Tagging<String> tagging = new Tagging(tokens,tags);
				PolymerAbstractCorpus pacs = new PolymerAbstractCorpus();
				ObjectHandler<Tagging<String>> handler = null;
				pacs.visitTrain(handler,tagging);
				
			}}
		return null;
		
	}
	
	public static void main(String[] args) throws IOException{
		ConditionalRandomFieldTagger crft = new ConditionalRandomFieldTagger(true, 1, true, true);
		
	}
}
