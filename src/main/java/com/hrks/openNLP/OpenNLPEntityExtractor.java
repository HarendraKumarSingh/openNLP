package com.hrks.openNLP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;

public class OpenNLPEntityExtractor {

	/**
	 * 
	 * @param inputStr
	 *            Input String from which entity(or key value pair) to be
	 *            extracted
	 */
	public void getEntities(String inputStr) {
		HashMap<String, String> entityMap = new HashMap<String, String>();

		// Loading name finder model into memory from disk
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream(
					"resources/customModels/nameModel.bin");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		TokenNameFinderModel model = null;
		try {
			model = new TokenNameFinderModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}

		// After the model is loaded the NameFinderME is instantiated.
		NameFinderME nameFinder = new NameFinderME(model);

		// extract entity using the loaded model
		String tokens[] = stringTokenizer(inputStr);
		Span nameSpans[] = nameFinder.find(tokens);

		displayEntity(nameSpans, tokens);

	}

	/**
	 * 
	 * @param inputStr
	 *            String to be tokenized using pre-trained tokenizer model
	 * @return token array
	 */
	public String[] stringTokenizer(String inputStr) {
		InputStream is = null;
		try {
			is = new FileInputStream("resources/defaultModels/en-token.bin");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		TokenizerModel model = null;
		try {
			model = new TokenizerModel(is);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(inputStr);

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tokens;
	}

	/**
	 * Use below to create and train a fresh openNLP model
	 */
	public void trainModel() {

		String destDir = "resources/customModels";

		String taggedSent = "Hey <START:Name> Deen Deenanath Chauhan <END> made his acting debut on television in 1978. "
				+ "<START:Name> Amitab Bachan <END> was known as <START:Name>  Deen Deenanath Chauhan <END> in those days.";

		ObjectStream<NameSample> nss = new NameSampleDataStream(
				(ObjectStream<String>) new PlainTextByLineStream(
						new StringReader(taggedSent)));
		TokenNameFinderModel model = null;
		try {
			model = NameFinderME.train("en", "default", nss,
					(AdaptiveFeatureGenerator) null,
					Collections.<String, Object> emptyMap(), 70, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(destDir, "nameModel.bin");
		FileOutputStream outFileStream = null;
		try {
			outFileStream = new FileOutputStream(outFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			model.serialize(outFileStream);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param nameSpans
	 *            entity extracted name span array
	 * @param sentence
	 *            token array of the input String
	 */
	private static void displayEntity(Span[] nameSpans, String[] sentence) {
		for (int si = 0; si < nameSpans.length; si++) {
			StringBuilder cb = new StringBuilder();
			for (int ti = nameSpans[si].getStart(); ti < nameSpans[si].getEnd(); ti++) {
				cb.append(sentence[ti]).append(" ");
			}
			System.out.println(cb.substring(0, cb.length() - 1) + " : "
					+ nameSpans[si].getType());
		}
	}

	// Optional main body
	public static void main(String[] args) {
		OpenNLPEntityExtractor openNLPEntityExtractor = new OpenNLPEntityExtractor();

		// At a time either perform a training or entity extraction for better
		// understanding of this code

		// first create an train a model
		openNLPEntityExtractor.trainModel();

		// method to extract entity using trained model
		openNLPEntityExtractor
				.getEntities("In Aganipath Amitab Bachan palyed role of Deen Deenanath Cahuhan's son.");

	}
}