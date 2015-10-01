package hw1;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EvenOddClassifier extends Predictor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5768346445753287105L;
	
	ClassificationLabel label;
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub

		

	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		
		double sumOdd=0;
		double sumEven=0;
		
		Iterator iterator = instance._feature_vector.features.entrySet().iterator();                
        while (iterator.hasNext()) {    
        	Map.Entry entry = (Map.Entry) iterator.next();
        	Object key = entry.getKey();    
        	Object val = entry.getValue();
        	int index = Integer.parseInt(String.valueOf(key));
        	double value = Double.parseDouble(val.toString());
        	
        	if(index%2 == 1) {
        		sumOdd += value;
       		} else if(index%2 == 0) {
       			sumEven += value;
       		}
		}
//        System.out.println("odd:"+sumOdd);
//        System.out.println("even:"+sumEven);
		if(sumOdd <= sumEven) {
			ClassificationLabel label = new ClassificationLabel(1);
			this.label = label;
		} else {
			ClassificationLabel label = new ClassificationLabel(0);
			this.label = label;
		}
		
		return this.label;
	}

}
