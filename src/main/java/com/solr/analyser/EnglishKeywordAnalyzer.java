package com.solr.analyser;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;

public final class EnglishKeywordAnalyzer extends StopwordAnalyzerBase {

	private CharArraySet stopWords;

	public EnglishKeywordAnalyzer(CharArraySet stopWords) {
		super();
		this.stopWords = stopWords;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new StandardTokenizer();
		TokenStream filter = new StandardFilter(source);
		filter = new LowerCaseFilter(filter);
		filter = new StopFilter(filter, this.stopWords);
		filter = new KStemFilter(filter);
		filter = new ASCIIFoldingFilter(filter);
		return new TokenStreamComponents(source, filter);
	}
}
