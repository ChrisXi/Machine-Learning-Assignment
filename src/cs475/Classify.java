package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	static public LinkedList<Option> options = new LinkedList<Option>();
	/*----vision------*/
	//-mode train -algorithm majority -model_file data/vision.majority.model -data data/vision/vision.train
	//-mode test -model_file data/vision.majority.model -data data/vision/vision.dev -predictions_file data/vision.dev.predictions
	
	//-mode train -algorithm even_odd -model_file data/vision.even_odd.model -data data/vision/vision.train
	//-mode test -model_file data/vision.even_odd.model -data data/vision/vision.dev -predictions_file data/vision.dev.predictions
	
	/*----nlp------*/
	//-mode train -algorithm majority -model_file data/nlp.majority.model -data data/nlp/nlp.train
	//-mode test -model_file data/nlp.majority.model -data data/nlp/nlp.dev -predictions_file data/nlp.dev.predictions
	
	//-mode train -algorithm even_odd -model_file data/nlp.even_odd.model -data data/nlp/nlp.train
	//-mode test -model_file data/nlp.even_odd.model -data data/nlp/nlp.dev -predictions_file data/nlp.dev.predictions
	
	/*----speech------*/
	//-mode train -algorithm majority -model_file data/speech.majority.model -data data/speech/speech.train
	//-mode test -model_file data/speech.majority.model -data data/speech/speech.dev -predictions_file data/speech.dev.predictions
	
	//-mode train -algorithm even_odd -model_file data/speech.even_odd.model -data data/speech/speech.train
	//-mode test -model_file data/speech.even_odd.model -data data/speech/speech.dev -predictions_file data/speech.dev.predictions

	/*----logistic regression ----*/
	//-mode train -algorithm logistic_regression -model_file data/speech.logistic_regression.model -data data/speech/speech.train
	//-mode test -model_file data/speech.logistic_regression.model -data data/speech/speech.dev -predictions_file data/speech.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/vision.logistic_regression.model -data data/vision/vision.train
	//-mode test -model_file data/vision.logistic_regression.model -data data/vision/vision.dev -predictions_file data/vision.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/nlp.logistic_regression.model -data data/nlp/nlp.train
	//-mode test -model_file data/nlp.logistic_regression.model -data data/nlp/nlp.dev -predictions_file data/nlp.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/finance.logistic_regression.model -data data/finance/finance.train
	//-mode test -model_file data/finance.logistic_regression.model -data data/finance/finance.dev -predictions_file data/finance.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/bio.logistic_regression.model -data data/bio/bio.train
	//-mode test -model_file data/bio.logistic_regression.model -data data/bio/bio.dev -predictions_file data/bio.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/easy.logistic_regression.model -data data/synthetic/easy.train
	//-mode test -model_file data/easy.logistic_regression.model -data data/synthetic/easy.dev -predictions_file data/easy.dev.predictions
	
	//-mode train -algorithm logistic_regression -model_file data/hard.logistic_regression.model -data data/synthetic/hard.train
	//-mode test -model_file data/hard.logistic_regression.model -data data/synthetic/hard.dev -predictions_file data/hard.dev.predictions
	
	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);
	
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");
		
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Train the model.
			Predictor predictor = train(instances, algorithm);
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	

	private static Predictor train(List<Instance> instances, String algorithm) {
		// TODO Train the model using "algorithm" on "data"
		// TODO Evaluate the model
		
		Predictor classifier;
		if(algorithm.equals("majority")) {
			classifier = new MajorityClassifier();
			classifier.train(instances);
			
			int correct = 0;
			int sum = 0;
			for (Instance instance : instances) {
				Label label = classifier.predict(instance);
				Label originalLabel = instance._label;
				if(originalLabel != null && (label.toString()).equals(originalLabel.toString())) {
					correct ++;
				}
				sum ++;
			}
			float precent = (float)correct/(float)sum;
			System.out.println("Evaluate the model: "+precent);
			
			return classifier;
		} else if(algorithm.equals("even_odd")) {
			classifier = new EvenOddClassifier();
			classifier.train(instances);
			
			int correct = 0;
			int sum = 0;
			for (Instance instance : instances) {
				Label label = classifier.predict(instance);
				Label originalLabel = instance._label;
				if(originalLabel != null && (label.toString()).equals(originalLabel.toString())) {
					correct ++;
				}
				sum ++;
			}

			double precent = (double)correct/(double)sum;
			System.out.println("Evaluate the model: "+precent);
			return classifier;
		} else if(algorithm.equals("logistic_regression")) {
			//TODO: confirm the size of 'featureNum'
			//regression parameter
			int sgd_iterations = 20;
			if(CommandLineUtilities.hasArg("sgd_iterations")) 
				sgd_iterations = CommandLineUtilities.getOptionValueAsInt("sgd_iterations");
			
			double sgd_eta0 = 0.01;
			if(CommandLineUtilities.hasArg("sgd_eta0")) 
				sgd_eta0 = CommandLineUtilities.getOptionValueAsInt("sgd_eta0");
			
			classifier = new LogisticRegressionClassifier(instances, sgd_iterations, sgd_eta0);
			classifier.train(instances);
			
			int correct = 0;
			int sum = 0;
			int index = 1;
			for (Instance instance : instances) {
				
				Label label = classifier.predict(instance);
				Label originalLabel = instance._label;
				
				if(originalLabel != null && (label.toString()).equals(originalLabel.toString())) {
					correct ++;
				}
				sum ++;
				index ++;
			}
//			System.out.println("sum:"+sum+" correct:"+correct);
			float precent = (float)correct/(float)sum;
			if(correct != 0)
				System.out.println("train: Evaluate the model: "+precent);
			
			return classifier;
		}

		return null;
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available. 
		
		int correct = 0;
		int sum = 0;
		int index = 1;
		for (Instance instance : instances) {
			
			Label label = predictor.predict(instance);
			Label originalLabel = instance._label;
			
			if(originalLabel != null && (label.toString()).equals(originalLabel.toString())) {
				correct ++;
			} 
			sum ++;
			writer.writePrediction(label);
			index ++;
		}
//		System.out.println("sum:"+sum+" correct:"+correct);
		float precent = (float)correct/(float)sum;
		
		if(correct != 0)
			System.out.println("test: Evaluate the model: "+precent);
		
		writer.close();
		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Classify.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		
		// Other options will be added here.
		registerOption("sgd_eta0", "double", true, "The constant scalar for learning rate in AdaGrad.");
		registerOption("sgd_iterations", "int", true, "The number of SGD iterations");
	}
}
