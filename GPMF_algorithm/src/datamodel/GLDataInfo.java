package datamodel;

import java.util.Arrays;

import algorithm.GL;
import tool.Gamma;

public class GLDataInfo {
	public static Triple[] GLdata = new Triple[DataInfo.rateNumber];
	public static double magnification = 1;
	public static double mean_rating = 0;
	public static double sigma_U=4;//4
	public static double sigma_V=4;
	public static double alpha = 0.001;  //0.003
	public static double alphaForBeta=0.000002;
	public static double lambda = 0.5;
	public static int nFeatures=20;
	public static int userNumber = 50692;
	public static int itemNumber = 140;
	public static int numFolds=10;
	public static double[] item_mean;
	public static double[] item_alpha;
	public static double[] item_stdv;
	public static int[] nRate;
	public static int[] tag_test;
	public static double classTreshhold=3;
	public static int TP,TN,FP,FN;
	public static double global_alpha;
	/**
	 * 
	 * @param paraX
	 * @return
	 */
	
	public static void GenerateTestSets_equal_per_user(){
		int[] ntest_per_user=new int[userNumber] ;
		tag_test = new int[GLdata.length];
		Arrays.fill(ntest_per_user, 0);
		Arrays.fill(tag_test, 0);
		for (int i = 0; i < GLdata.length; i++) {
			if(ntest_per_user[GLdata[i].i]<3){
				tag_test[i]=1;
				ntest_per_user[GLdata[i].i] ++;
			}
		}
	}
	public static void GenerateTestSets_every_tenth(){
		tag_test = new int[GLdata.length];
		Arrays.fill(tag_test, 0);
		for (int i = 0; i < tag_test.length; i++) {
			if(i % 10 == DataInfo.teIndxRem){
				tag_test[i]=1;
			}
		}
	}
	
	public static void computeTP(){
		TP=0;
		TN=0;
		FP=0;
		FN=0;
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i]==1){
				double tempPred=GLDataInfo.GLretransfer(GL.predict(GLdata[i].i, GLdata[i].j) * GLDataInfo.global_alpha/ GLDataInfo.magnification + GLDataInfo.mean_rating);
				if(tempPred>classTreshhold & DataInfo.data[i].rate>classTreshhold){
					TP ++;
				}
				if(tempPred>classTreshhold & DataInfo.data[i].rate<=classTreshhold){
					FP ++;
				}
				if(tempPred<=classTreshhold & DataInfo.data[i].rate<classTreshhold){
					TN ++;
				}
				if(tempPred<=classTreshhold & DataInfo.data[i].rate>=classTreshhold){
					FN ++;
				}
			}
		}
		System.out.println("TP,FP,TN,FN="+TP+","+FP+","+TN+","+FN);
	}
	
	public static double[][] computeROC(){
		int tempFN=0;
		int tempFP=0;
		int tempTN=0;
		int tempTP=0;
		
		Triple[] tempTriple = new Triple[181046];
		int tempTestsCount=0;
		for (int i = 0; i < DataInfo.data.length; i++) {
			if(tag_test[i]==1){
				tempTestsCount ++;
				tempTriple[tempTestsCount-1]=DataInfo.data[i];
			}
		}
		System.out.println("test num = "+tempTestsCount);
		int tempNumPoints = tempTestsCount/1600 +20;
		double[] pred_out = new double[tempTestsCount];
		double[][] tempROC = new double[tempNumPoints][2];
		for (int i = 0; i < tempTestsCount; i++) {	
			double tempPred=GLDataInfo.GLretransfer(GL.predict(tempTriple[i].i, tempTriple[i].j) / GLDataInfo.magnification + GLDataInfo.mean_rating);
			pred_out[i] = tempPred;			
		}
		tempTriple=bubbleSort(pred_out, tempTriple);
//		System.out.println(Arrays.toString(pred_out));
		int tempPointCount=0;
		for (int i = 0; i < tempTriple.length; i=i+1600) {
			tempFN=0;
			tempFP=0;
			tempTN=0;
			tempTP=0;
			tempPointCount ++;
			System.out.println("i = "+i+"  count= "+tempPointCount);
			for (int j = 0; j < i; j++) {
				if(tempTriple[j].rate > classTreshhold){
					tempFN ++;
				}
				if(tempTriple[j].rate <= classTreshhold){
					tempTN ++;
				}
			}
			for (int j = i; j < tempTriple.length; j++) {
				if(tempTriple[j].rate > classTreshhold){
					tempTP ++;
				}
				if(tempTriple[j].rate <= classTreshhold){
					tempFP ++;
				}
			}
			tempROC[tempPointCount-1][0]=(double)tempFP/(tempFP+tempTN);
			tempROC[tempPointCount-1][1]=(double)tempTP/(tempTP+tempFN);
		}
//		for (int i = 172810; i < tempTriple.length; i=i+5) {
//			tempPointCount ++;
//			System.out.println("i = "+i+"  count= "+tempPointCount);
//			for (int j = 0; j < i; j++) {
//				if(tempTriple[j].rate > classTreshhold){
//					tempFN ++;
//				}
//				if(tempTriple[j].rate <= classTreshhold){
//					tempTN ++;
//				}
//			}
//			for (int j = i; j < tempTriple.length; j++) {
//				if(tempTriple[j].rate > classTreshhold){
//					tempTP ++;
//				}
//				if(tempTriple[j].rate <= classTreshhold){
//					tempFP ++;
//				}
//			}
//			tempROC[tempPointCount-1][0]=(double)tempFP/(tempFP+tempTN);
//			tempROC[tempPointCount-1][1]=(double)tempTP/(tempTP+tempFN);
//		}
		return tempROC;
	}
	
	public static Triple[] bubbleSort(double[] numbers, Triple[] paraTriple)
    {
		Triple[] tempTriple=paraTriple;
        double temp = 0;
        int intTemp;
        int size = numbers.length;
        for(int i = 0 ; i < size-1; i ++)
        {
	        for(int j = 0 ;j < size-1-i ; j++)
	        {
	            if(numbers[j] > numbers[j+1])  //交换两数位置
	            {
	            temp = numbers[j];
	            numbers[j] = numbers[j+1];
	            numbers[j+1] = temp;
	            
	            intTemp=tempTriple[j].i;
	            tempTriple[j].i=tempTriple[j+1].i;
	            tempTriple[j+1].i = intTemp;
	            
	            intTemp=tempTriple[j].j;
	            tempTriple[j].j=tempTriple[j+1].j;
	            tempTriple[j+1].j = intTemp;
	            
	            temp=tempTriple[j].rate;
	            tempTriple[j].rate=tempTriple[j+1].rate;
	            tempTriple[j+1].rate = temp;
	            }
	        }
        }
        return tempTriple;
    }

	public static double GLtransfer(double paraX){
		double tempY = 0;
		double b = 1;
		double v = 1.04466;//0.3028;3026;1.04466
		
		tempY = (- 1 / b) * (Math.log(Math.pow(20.0 / (paraX + 10), v) - 1));
		//tempY = (- v / b) * (Math.log(20.0 / (paraX + 10))) + 1 / b;
		return tempY;
	}//of GLtransfer 
	
	/**
	 * 
	 * @param paraX
	 * @return
	 */
	public static double GLretransfer(double paraY){
		double tempX = 0;
		double A = -10;
		double K = 10;
		double C = 1;
		double Q = 1;
		double B = 1;
		double v = 1.04466;
		
		tempX = A + (K - A)/(Math.pow((C + Q * Math.exp(-B * paraY)), 1/v));
		
		return tempX;
	}//of GLtransfer
	
	/**
	 * 
	 */
	public static void dataToGLData(){
		
		for(int i = 0; i < DataInfo.data.length; i ++){
//			if(DataInfo.data.length-i<24000){
//				System.out.print(DataInfo.data[i].rate+" ");
//			}
			GLdata[i] = new Triple();
			GLdata[i].i = DataInfo.data[i].i;
			GLdata[i].j = DataInfo.data[i].j;
			
			if(DataInfo.data[i].rate == 10){
				DataInfo.data[i].rate = 9.99;
			}//of if
			
			if(DataInfo.data[i].rate == -10){
				DataInfo.data[i].rate = -9.99;
			}//of if			
			double tempRate = GLtransfer(DataInfo.data[i].rate);	
			GLdata[i].rate = tempRate;
		}//Of for i 
			 //			
			//System.out.println("oldRate: " + DataInfo.data[i].rate +
			//		" newRate: " + GLdata[i].rate);
		
	}//of dataToGLData
	public static void removeMean(){
		computeGlobleMean();
		System.out.println(mean_rating);
		for (int i = 0; i < GLdata.length; i++) {
			GLdata[i].rate = magnification*(GLdata[i].rate-mean_rating);
		}
	}
	public static void computeGlobleMean(){
		mean_rating=0;
		int numTrain=0;
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i] == 0){
				numTrain++;
				mean_rating += GLdata[i].rate;
			}
		}
		mean_rating /= numTrain;
	}
	public static void computeItemMean(){
		item_mean=new double[DataInfo.itemNumber];
		nRate=new int[DataInfo.itemNumber];
		Arrays.fill(nRate, 0);
		Arrays.fill(item_mean, 0);
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i] == 0){
				nRate[GLdata[i].j]++;
				item_mean[GLdata[i].j]+=GLdata[i].rate;
			}
		}
		for (int i = 0; i < item_mean.length; i++) {
			item_mean[i]=item_mean[i]/nRate[i];
		}
	}
	public static void computeStd(){
		item_stdv=new double[DataInfo.itemNumber];
		Arrays.fill(item_stdv, 0);
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i] == 0){
				item_stdv[GLdata[i].j]+=Math.pow(Math.abs(GLdata[i].rate-item_mean[GLdata[i].j]), 2);
			}
		}
		for (int i = 0; i < item_stdv.length; i++) {			
			item_stdv[i]=Math.sqrt(item_stdv[i]/nRate[i]);
		}
	}
	
	public static double computeGlobalStd(){
		double globalStd = 0;
		int nRate = 0;
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i] == 0){
				globalStd += Math.pow(GLdata[i].rate-mean_rating, 2);
				nRate ++;
			}			
		}
		globalStd = Math.sqrt(globalStd / (nRate - 1));
		return globalStd;
	}
	
	public static double[] computeAlphaByML(double paraBeta){
		double[] temp_item_alpha=new double[DataInfo.itemNumber];
		Arrays.fill(temp_item_alpha, 0);
		for (int i = 0; i < GLdata.length; i++) {
			if(tag_test[i] == 0){
				temp_item_alpha[GLdata[i].j]+=Math.pow(Math.abs(GLdata[i].rate-item_mean[GLdata[i].j]), paraBeta);
			}
		}
		for (int i = 0; i < temp_item_alpha.length; i++) {			
			temp_item_alpha[i]=Math.pow(temp_item_alpha[i]/(nRate[i])*paraBeta, 1.0/paraBeta);
//			System.out.println("stdv="+item_stdv[i]);
		}
		return temp_item_alpha;
	}
	public static double[] computeAlphaByMM(double paraBeta){

		double[] temp_item_alpha=new double[DataInfo.itemNumber];
		Arrays.fill(temp_item_alpha, 0);
		for (int i = 0; i < item_stdv.length; i++) {
			temp_item_alpha[i] = item_stdv[i]*Math.sqrt(Gamma.gamma(1.0/paraBeta)/Gamma.gamma(3.0/paraBeta));
		}
		return temp_item_alpha;
	}
	
	public static void computeGlobleAlphaByMM(double paraBeta, double paraStd){

		global_alpha=0;
		global_alpha = paraStd * Math.sqrt(Gamma.gamma(1.0/paraBeta)/Gamma.gamma(3.0/paraBeta));
	}
	
	public static double computeMatrixMean(double[][] paraMatrix){
		double tempMean=0 ;
		int tempN=0;
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				tempMean += paraMatrix[i][j];
				tempN ++;
			}
		}
		tempMean /= tempN;
		return tempMean;
	}
	
	public static double computeMatrixStd(double[][] paraMatrix){
		double tempMean = computeMatrixMean(paraMatrix);
		double tempStd = 0;
		int tempN = 0;
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				tempStd += (paraMatrix[i][j] - tempMean) * (paraMatrix[i][j] - tempMean);
				tempN ++;
			}
		}
		tempStd /= tempN;
		return tempStd;
	}
	public static void normalizeData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < GLdata.length; i++) {
			GLdata[i].rate = (GLdata[i].rate - mean_rating) / global_alpha;
		}
	}
	public static void GenerateDevSets_every_tenth() {
		// TODO Auto-generated method stub
		for (int i = 0; i < tag_test.length; i++) {
			if(i % 10 == DataInfo.teIndxRem && tag_test[i+1] != 1){
				tag_test[i+1]=2;
			}
		}
	}
}
