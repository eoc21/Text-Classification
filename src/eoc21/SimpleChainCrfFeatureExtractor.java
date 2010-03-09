package eoc21;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aliasi.crf.ChainCrfFeatures;

public class SimpleChainCrfFeatureExtractor<E> extends ChainCrfFeatures<E> {

	public SimpleChainCrfFeatureExtractor(List<E> tokens, List<String> tags) {
		super(tokens, tags);
	}

	@Override
	public Map<String, ? extends Number> edgeFeatures(int n, int k) {
		return Collections
        .singletonMap("PREV_TAG_" + tag(k),
                      Integer.valueOf(1));
	}

	@Override
	public Map<String, ? extends Number> nodeFeatures(int n) {
		return Collections
        .singletonMap("TOK_" + token(n),
                      Integer.valueOf(1));
	}

}
