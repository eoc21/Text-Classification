package eoc21;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.tag.Tagging;

public class PolymerAbstractCorpus extends Corpus {

	public void visitTrain(ObjectHandler<Tagging<String>> handler,Tagging<String> tokensAndTags) {
            handler.handle(tokensAndTags);
        }
}
