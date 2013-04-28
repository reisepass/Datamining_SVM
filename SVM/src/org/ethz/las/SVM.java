package org.ethz.las;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import java.util.*;

public class SVM {

  // Hyperplane weights.
  RealVector weights;

  public SVM(RealVector weights) {
    this.weights = weights;
  }

  /**
   * Instantiates an SVM from a list of training instances, for a given
   * learning rate 'eta' and regularization parameter 'lambda'.
   */
  
  public int subGrai(int ywx){
	  if(ywx>1)
		  return 0;
	  else if (ywx<1)
		  return ywx;   //TODO check this formula. I think the slide 19 dm-05-sup*.pdf  is wrong it just says -y_t*x_t  but that doens't make much sense does it ?
	  else 
		  return 0;
  }
  
  public SVM(List<TrainingInstance> trainingSet, double lambda, double eta) {
    // TODO: Implement me!
	  int dim= trainingSet.get(0).getFeatureCount();
	  double[] curW = new double[dim];
	  Random nran = new Random();
	  
	  for (int i =0; i<curW.length; i++){
		  curW[i]=nran.nextDouble();    //TODO make sure that this is inside S
	  }
	  RealVector w = new RealVector(curW);
	  SVM lastSVM = new  SVM(w);
	  
  	for( TrainingInstance datum: trainingSet){
  		int predClass=lastSVM.classify(datum);
  		
  		if(datum.getLabel()*predClass>=1)
  			w=w; 
  		else{
  			
  			RealVector  Yx = datum.getFeatures().scale(datum.getLabel());
  			w.add(Yx.scaleThis(eta));
  			w.scaleThis(Math.min((double)1,(1/(w.getNorm()*lambda)))); //back projection as per slide 20 dm-05-sun*.pdf 
  		}
  		lastSVM = new SVM(w);
  	
  	}
	  	
	this.weights=w;
  }

  /**
   * Instantiates SVM from weights given as a string.
   */
  public SVM(String w) {
    List<Double> ll = new LinkedList<Double>();
    Scanner sc = new Scanner(w);
    while(sc.hasNext()) {
      double coef = sc.nextDouble();
      ll.add(coef);
    }

    double[] weights = new double[ll.size()];
    int cnt = 0;
    for (Double coef : ll)
      weights[cnt++] = coef;

    this.weights = new RealVector(weights);
  }

  /**
   * Instantiates the SVM model as the average model of the input SVMs.
   */
  public SVM(List<SVM> svmList) {
    int dim = svmList.get(0).getWeights().getDimension();
    RealVector weights = new RealVector(dim);
    for (SVM svm : svmList)
      weights.add(svm.getWeights());

    this.weights = weights.scaleThis(1.0/svmList.size());
  }

  /**
   * Given a training instance it returns the result of sign(weights'instanceFeatures).
   */
  public int classify(TrainingInstance ti) {
    RealVector features = ti.getFeatures();
    double result = ti.getFeatures().dotProduct(this.weights);
    if (result >= 0) return 1;
    else return -1;
  }

  public RealVector getWeights() {
    return this.weights;
  }

  @Override
  public String toString() {
    return weights.toString();
  }
}
