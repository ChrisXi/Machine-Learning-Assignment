package hw2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LogisticRegressionClassifier extends Predictor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6777601125541027708L;

	ClassificationLabel label;
	
	int featureNum;
	double weight[];
	double weightPowSum[];
	
	double sgd_eta0;
	int sgd_iterations;
	
	
	
	LogisticRegressionClassifier(List<Instance> instances, int sgd_iterations, double sgd_eta0) {
		
		this.sgd_eta0 = sgd_eta0;
		this.sgd_iterations = sgd_iterations;
		this.featureNum = getFeaturesNum(instances);
//		System.out.println(this.featureNum);
		
		weight = new double[this.featureNum];
		weightPowSum = new double[this.featureNum];
		
		for(int i=0; i<this.featureNum; i++) {
			weight[i] = 0.0;
			weightPowSum[i] = 0.0;
		}
			
	}
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		for (int iterationCount = 0;  iterationCount < sgd_iterations; iterationCount ++) {
			
			for (Instance instance : instances) {
				
				double logisticFun = 0.0;
				int negOrPos = 0;
				
				String label = ((ClassificationLabel)instance._label).toString();
				if(label.equals("1")) {
					logisticFun = logFun(instance,-1);
					negOrPos = 1;
					
				}else if(label.equals("0")) {
					logisticFun = logFun(instance,1);
					negOrPos = -1;
				}
				
				Iterator iterator = instance._feature_vector.features.entrySet().iterator();        
		        while (iterator.hasNext()) {    
		        	Map.Entry entry = (Map.Entry) iterator.next();
		        	Object key = entry.getKey();    
		        	Object val = entry.getValue();
		        	int index = Integer.parseInt(String.valueOf(key))-1;
		        	double value = Double.parseDouble(val.toString());
		        	
		        	double partialDeriv = logisticFun * value * negOrPos;
		        	weightPowSum[index] += partialDeriv*partialDeriv;
		        	
		        	double sgd_eta = sgd_eta0/Math.sqrt(1+weightPowSum[index]);
		        	weight[index] = weight[index] + sgd_eta*partialDeriv;
		        	
//		        	System.out.println(index+" value: "+value+" log: "+logisticFun+" partialDeriv: "+partialDeriv+
//		        			" sgd_eta: "+sgd_eta+" w: "+weight[index]);
				}
			}
		}
		
		for(int i=0; i<featureNum; i++) {
			//System.out.println(i+": "+weight[i]);
			//System.out.println(weightPowSum[i]);
		}
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		
		double logisticFun = logFun(instance,1);
		
		if(logisticFun >= 0.5) {
			ClassificationLabel label = new ClassificationLabel(1);
			this.label = label;
		} else {
			ClassificationLabel label = new ClassificationLabel(0);
			this.label = label;
		}
		
		//System.out.println(label+" "+logisticFun);
		return label;
	}

	int getFeaturesNum(List<Instance> instances) {
		
		int maxFeatureIndex = 0;
		
			
		for (Instance instance : instances) {
		
			
			Iterator iterator = instance._feature_vector.features.entrySet().iterator();        
	        while (iterator.hasNext()) {    
	        	Map.Entry entry = (Map.Entry) iterator.next();
	        	Object key = entry.getKey();    
	        	int index = Integer.parseInt(String.valueOf(key));
	        	
	        	if(index > maxFeatureIndex) {
	        		maxFeatureIndex = index;
	        	}
	        
			}
		}
		
		return maxFeatureIndex;
		
	}
	
	double logFun(Instance instance, int negOrPos) {
		
		double sum = 0.0;
		Iterator iterator = instance._feature_vector.features.entrySet().iterator();        
//		int index = 0;
        while (iterator.hasNext()) {    
        	Map.Entry entry = (Map.Entry) iterator.next();
        	Object key = entry.getKey();    
        	Object val = entry.getValue();
        	int index = Integer.parseInt(String.valueOf(key))-1;
        	double value = Double.parseDouble(val.toString());
        	
        	//System.out.println(index);
        	if(index < featureNum)
        		sum += weight[index] * value;
        	
		}
//        System.out.println("sum: "+sum+" "+negOrPos+" e: "+Math.exp(sum*negOrPos));
        
		return 1/(1+Math.exp(negOrPos*sum*-1));	
	}
}
