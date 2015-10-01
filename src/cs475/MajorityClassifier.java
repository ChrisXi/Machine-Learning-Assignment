package cs475;

import java.util.List;

public class MajorityClassifier extends Predictor{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1205226045284296144L;
	
	ClassificationLabel label;
	
	@Override
	public void train(List<Instance> instances) {
		// TODO Auto-generated method stub
		int countFirst=0;
		int countSecond=0;;
		for (Instance instance : instances) {
			String label = ((ClassificationLabel)instance._label).toString();
			if(label.equals("1")) {
				countFirst++;
			}else if(label.equals("0")) {
				countSecond++;
			}
		}
		if(countFirst>=countSecond) {
			ClassificationLabel label = new ClassificationLabel(1);
			this.label = label;
		} else if(countFirst<countSecond) {
			ClassificationLabel label = new ClassificationLabel(0);
			this.label = label;
		} 
		
	}

	@Override
	public Label predict(Instance instance) {
		// TODO Auto-generated method stub
		return this.label;
	}

}
