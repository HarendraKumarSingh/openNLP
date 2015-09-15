# openNLP
This is a very simple and easy java-based NLP guide for quick start to NLP model creation and use for entity extraction.

#NLP Model creation and use:</br>
Below steps are for openNLP model creation, training the model with simple training set and then using the same trained model from entity extraction.</br>
<ul><li>openNLP custom model creation using annotated training set.</br>
 Example for annotated training text :</br> " The highest temperature recorded in <code>&lt;START:location&gt; Delhi &lt;END&gt;</code> before this was 47.4 degree Celsius at Palam on June 16, 1995. " </br>
      Here location is the key and the Delhi is the annotated sample value for it.So next time when an input document finds Delhi then using the above trained model you can extracts entity as Delhi :location ,means Delhi is a location.</li>
<li>Recieves input for entity extraction.
<li>Tokenize the input text fragment using pre tained tokenizer model provided by openNLP.</li>
<li>Pass the token array and newly created model for entity extraction.
<li>Return the extracted key-value pair.</li>
</ul></br>
Go through the code and you will understand every thing.


This code can be used to create any kind of simple trained model,but the precision depends on the more you train the more the entity extraction becomes accurate.

#Using the code </br>
In order to use OpenNLP in your project, you must define below maven dependency of opennlp.tools : 
	 <pre><code>&lt;dependency&gt;
    &lt;groupId&gt;org.apache.opennlp&lt;/groupId&gt;
    &lt;artifactId&gt;opennlp-tools&lt;/artifactId&gt;
    &lt;version&gt;1.5.3&lt;/version&gt;
&lt;/dependency&gt;
</code></pre>

	
	
