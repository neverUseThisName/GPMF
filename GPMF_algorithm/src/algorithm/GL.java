package algorithm;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import datamodel.DataInfo;
import datamodel.GLDataInfo;
import datamodel.Triple;
import tool.Gamma;
import tool.Shuffle;
import tool.SimpleTool;

/**
 * 
 * @author Henry
 * Used and extended by Fu.
 * 
 */
public class GL {
	public static String dataPath = new String("data/jester-data-1/jesterfinal141cols.txt");//Dataset path
	static String split_Sign = new String(",");//How data is split in your file
	public static double beta=0.9879;//Shape parameter in GGD;Jester2+:0.9879;Jester1_1:1.6696;
	/**
	 *Randomly initialize parameters. 
	 */
	static void initFeature() {
		Random rand = new Random();

		for (int i = 0; i < DataInfo.userNumber; i++) {
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				DataInfo.uFeature[i][j] = 0.1 * rand.nextDouble();
			} // of for j
		} // Of for i

		// SimpleTool.printMatrix(DataInfo.userFeature);
		for (int i = 0; i < DataInfo.itemNumber; i++) {
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				DataInfo.iFeature[i][j] = 0.1* rand.nextDouble();
			} // Of for j
		} // Of for i
	}// of initFeature

	/**
	 *
	 * Predict rating using inner product
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public static double predict(int userId, int itemId) {
		double pre = 0;
		for (int i = 0; i < DataInfo.featureNumber; i++) {
			pre += DataInfo.uFeature[userId][i] * DataInfo.iFeature[itemId][i];//Prediction 
		} // of for i
		return pre;
	}// Of predict
	
	/**
	 *
	 * Optimize with mini-Batch SGD
	 */
	public static void update_miniBatchSGD(int paraRound, int miniBatchSize) {
		if(paraRound<100000){
			double tempSumUserFactorGradPerRate = 0;
			double avgUserFactorGradPerRound = 0.2;//Magnitude of gradient to be checked per round 
			int numTrains = 0;
			int nSampled = 0;
			while(nSampled < miniBatchSize) {
				Random rand = new Random();
				int tempRateID = rand.nextInt(DataInfo.rateNumber);
				if(GLDataInfo.tag_test[tempRateID] == 1 || GLDataInfo.tag_test[tempRateID] == 2){
					continue;
				}//of if
				numTrains ++;
				int tempUserId = (Integer) GLDataInfo.GLdata[tempRateID].i;
				int tempItemId = (Integer) GLDataInfo.GLdata[tempRateID].j;
				double tempRate = (Double) GLDataInfo.GLdata[tempRateID].rate;
				double userFactorGradPerRate = 0;
				double tempVary = predict(tempUserId, tempItemId) - tempRate;//�в�			
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1)*sign(tempVary) * DataInfo.iFeature[tempItemId][j] + Math.pow(1, beta)/(GLDataInfo.sigma_U*GLDataInfo.sigma_U) * DataInfo.uFeature[tempUserId][j];
					DataInfo.uFeature[tempUserId][j] = DataInfo.uFeature[tempUserId][j] - GLDataInfo.alpha * tmp / avgUserFactorGradPerRound;
					userFactorGradPerRate += tmp * tmp;
				}//of for j
				userFactorGradPerRate = Math.pow(userFactorGradPerRate / DataInfo.featureNumber , 0.5);
				tempSumUserFactorGradPerRate += userFactorGradPerRate;
				//System.out.println("per rate user factor gradient: " + userFactorGradPerRate);
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1) * sign(tempVary) * DataInfo.uFeature[tempUserId][j] + Math.pow(1, beta)/(GLDataInfo.sigma_V*GLDataInfo.sigma_V) * DataInfo.iFeature[tempItemId][j];
					DataInfo.iFeature[tempItemId][j] = DataInfo.iFeature[tempItemId][j] - GLDataInfo.alpha * tmp / avgUserFactorGradPerRound;
				}//of for j		
				nSampled ++;
			}//of for i
			//check gradient norm per round
			avgUserFactorGradPerRound = tempSumUserFactorGradPerRate / numTrains;
			
			System.out.println("avg User Factor Grad Per Round: " + avgUserFactorGradPerRound);
			//shrink step length
			GLDataInfo.alpha *= Math.exp(-paraRound / 40);			
		}
	}
	
	/**
	 * ���л��ھ���ֽ�ķ������ı仯����update_one��
	 */
	public static double update_one(int paraRound) {
			double tempSumUserFactorGradPerRate = 0;//sum of magnitude of user gradient per round 
			double tempSumItemFactorGradPerRate = 0;//Sum of magnitude of item gradient per round 
			double avgUserFactorGradPerRound = 0.2;//Avg of magnitude of user gradient per round 
			double avgItemFactorGradPerRound = 0.2;//Avg of magnitude of user gradient per round 
			int numTrains = 0;
			for (int i = 0; i < DataInfo.rateNumber; i++) {
				if(GLDataInfo.tag_test[i] == 1 || GLDataInfo.tag_test[i] == 2){//1 for 
					continue;
				}//of if
				numTrains ++;
				int tempUserId = (Integer) GLDataInfo.GLdata[i].i;
				int tempItemId = (Integer) GLDataInfo.GLdata[i].j;
				double tempRate = (Double) GLDataInfo.GLdata[i].rate;
				double userFactorGradPerRate = 0;
				double itemFactorGradPerRate = 0;
				double tempVary = predict(tempUserId, tempItemId) - tempRate;//�в�			
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1)*sign(tempVary) * DataInfo.iFeature[tempItemId][j] + Math.pow(1, beta)/(GLDataInfo.sigma_U*GLDataInfo.sigma_U) * DataInfo.uFeature[tempUserId][j];
					DataInfo.uFeature[tempUserId][j] = DataInfo.uFeature[tempUserId][j] - GLDataInfo.alpha * tmp / avgUserFactorGradPerRound;
					userFactorGradPerRate += tmp * tmp;
				}//of for j
				userFactorGradPerRate = Math.pow(userFactorGradPerRate / DataInfo.featureNumber , 0.5);
				tempSumUserFactorGradPerRate += userFactorGradPerRate;
				//System.out.println("per rate user factor gradient: " + userFactorGradPerRate);
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1) * sign(tempVary) * DataInfo.uFeature[tempUserId][j] + Math.pow(1, beta)/(GLDataInfo.sigma_V*GLDataInfo.sigma_V) * DataInfo.iFeature[tempItemId][j];
					DataInfo.iFeature[tempItemId][j] = DataInfo.iFeature[tempItemId][j] - GLDataInfo.alpha * tmp / avgItemFactorGradPerRound;
					itemFactorGradPerRate += tmp * tmp;
				}//of for j		
				itemFactorGradPerRate = Math.pow(itemFactorGradPerRate / DataInfo.featureNumber , 0.5);
				tempSumItemFactorGradPerRate += itemFactorGradPerRate;
			}//of for i
			//check gradient norm per round
			avgUserFactorGradPerRound = tempSumUserFactorGradPerRate / numTrains;
			avgItemFactorGradPerRound = tempSumItemFactorGradPerRate / numTrains;
			//System.out.println("avg User Factor Grad Per Round: " + avgUserFactorGradPerRound);
			//System.out.println("avg Item Factor Grad Per Round: " + avgItemFactorGradPerRound);
			return avgUserFactorGradPerRound;
			//shrink step length
			//GLDataInfo.alpha *= Math.exp(-paraRound / 100);
//		System.out.println(Arrays.deepToString(DataInfo.uFeature));
//		System.out.println(Arrays.deepToString(DataInfo.iFeature));
		//update sigma for U and V
//		double tempVar=0;
//		for (int i = 0; i < GLDataInfo.userNumber; i++) {
//			for (int j = 0; j < DataInfo.featureNumber; j++) {
//				tempVar += Math.pow(DataInfo.uFeature[i][j],2);
//			}
//		}
//		GLDataInfo.sigma_U = Math.sqrt(tempVar/(GLDataInfo.userNumber*DataInfo.featureNumber-1));
//		System.out.println("sigma_U="+GLDataInfo.sigma_U);
//		tempVar=0;
//		for (int i = 0; i < GLDataInfo.itemNumber; i++) {
//			for (int j = 0; j < DataInfo.featureNumber; j++) {
//				tempVar += Math.pow(DataInfo.iFeature[i][j],2);
//			}
//		}
//		GLDataInfo.sigma_V = Math.sqrt(tempVar/(GLDataInfo.itemNumber*DataInfo.featureNumber-1));
//		System.out.println("sigma_V="+GLDataInfo.sigma_V);
		
	}//Of update_one
	
	public static void update_two(int paraRound, boolean paraUpdateBEta) {
		if(paraRound<100000){
		for (int i = 0; i < DataInfo.rateNumber; i++) {
				if(GLDataInfo.tag_test[i] == 1){
					continue;
				}//of if
				
				int tempUserId = (Integer) GLDataInfo.GLdata[i].i;
				int tempItemId = (Integer) GLDataInfo.GLdata[i].j;
				double tempRate = (Double) GLDataInfo.GLdata[i].rate;
	
				double tempVary;//�в�			
	
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					tempVary = predict(tempUserId, tempItemId) - tempRate;
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1)*sign(tempVary) * DataInfo.iFeature[tempItemId][j] + Math.pow(GLDataInfo.item_alpha[tempItemId], beta)/(GLDataInfo.sigma_U*GLDataInfo.sigma_U) * DataInfo.uFeature[tempUserId][j];
					DataInfo.uFeature[tempUserId][j] = DataInfo.uFeature[tempUserId][j] - GLDataInfo.alpha * tmp;
				}//of for j
	
				for (int j = 0; j < DataInfo.featureNumber; j++) {
					tempVary = predict(tempUserId, tempItemId) - tempRate;
					double tmp = beta*Math.pow(Math.abs(tempVary),beta-1) * sign(tempVary) * DataInfo.uFeature[tempUserId][j] + Math.pow(GLDataInfo.item_alpha[tempItemId], beta)/(GLDataInfo.sigma_V*GLDataInfo.sigma_V) * DataInfo.iFeature[tempItemId][j];
					DataInfo.iFeature[tempItemId][j] = DataInfo.iFeature[tempItemId][j] - GLDataInfo.alpha * tmp;
				}//of for j			
			}//of for i
		}
//		System.out.println(Arrays.deepToString(DataInfo.uFeature));
//		System.out.println(Arrays.deepToString(DataInfo.iFeature));
		//update sigma for U and V
//		double tempVar=0;
//		for (int i = 0; i < GLDataInfo.userNumber; i++) {
//			for (int j = 0; j < DataInfo.featureNumber; j++) {
//				tempVar += Math.pow(DataInfo.uFeature[i][j],2);
//			}
//		}
//		GLDataInfo.sigma_U = Math.sqrt(tempVar/(GLDataInfo.userNumber*DataInfo.featureNumber-1));
//		System.out.println("sigma_U="+GLDataInfo.sigma_U);
//		tempVar=0;
//		for (int i = 0; i < GLDataInfo.itemNumber; i++) {
//			for (int j = 0; j < DataInfo.featureNumber; j++) {
//				tempVar += Math.pow(DataInfo.iFeature[i][j],2);
//			}
//		}
//		GLDataInfo.sigma_V = Math.sqrt(tempVar/(GLDataInfo.itemNumber*DataInfo.featureNumber-1));
//		System.out.println("sigma_V="+GLDataInfo.sigma_V);
		
		if(paraRound>60 & paraUpdateBEta==true){
			System.out.println("before update beta, E = "+computeObjective(beta, GLDataInfo.item_alpha));
			double dbeta=0;
			//********approximate gradient*************
			double[] tempItemAlpha=GLDataInfo.computeAlphaByMM(beta-0.01);
	//		System.out.println(Arrays.toString(GLDataInfo.item_alpha));
	//		System.out.println(Arrays.toString(tempItemAlpha));               //not the same
			double y1=computeObjective(beta-0.01, tempItemAlpha);
			System.out.println("y1 = "+y1);
			tempItemAlpha=GLDataInfo.computeAlphaByMM(GL.beta+0.01);
			double y2=computeObjective(beta+0.01, tempItemAlpha);
			System.out.println("y2 = "+y2);
			dbeta=sign(y2-y1)*0.01;
		//******************************************
		
		
//		double tempValue=Math.pow(Gamma.gamma(1/beta)/Gamma.gamma(3/beta), -0.5) * (3*Gamma.digamma(3/beta) * Gamma.gamma(3/beta) * Gamma.gamma(1/beta)-Gamma.digamma(1/beta) * Gamma.gamma(1/beta) * Gamma.gamma(3/beta))/(2*beta*beta*Gamma.gamma(3/beta)*Gamma.gamma(3/beta));
//		//System.out.println("tempValue= "+tempValue);
//		for (int i = 0; i < DataInfo.rateNumber; i++) {
//			if(i % GLDataInfo.numFolds == DataInfo.teIndxRem){
//				continue;
//			}//of if
//			int tempUserId = (Integer) GLDataInfo.GLdata[i].i;
//			int tempItemId = (Integer) GLDataInfo.GLdata[i].j;
//			double tempRate = (Double) GLDataInfo.GLdata[i].rate;
//			double tempVary = (predict(tempUserId, tempItemId) - tempRate)/GLDataInfo.item_alpha[tempItemId];
//			
//			dbeta+=Math.pow(Math.abs(tempVary), beta)*Math.log(Math.abs(tempVary))-( Gamma.digamma(1/beta)/(beta*beta) + 1/beta) + GLDataInfo.item_stdv[tempItemId]  * tempValue /GLDataInfo.item_alpha[tempItemId];
//		}
//		//dbeta += -DataInfo.rateNumber*(Gamma.digamma(1/beta)/(beta*beta)+1/beta);
			beta -= dbeta;
			System.out.println("dbeta="+GLDataInfo.alphaForBeta*dbeta+"beta="+beta);
		}
	}//Of update_one

	/**
	 * Compute the MAE
	 * 
	 * @return
	 */
	public static double computeObjective(double paraBeta, double[] paraItemAlpha) {
		double tempObjective = 0;
		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(i % GLDataInfo.numFolds == DataInfo.teIndxRem){
				continue;
			}//of if
			int tempUserId = (Integer) GLDataInfo.GLdata[i].i;
			int tempItemId = (Integer) GLDataInfo.GLdata[i].j;
			double tempRate = (Double) GLDataInfo.GLdata[i].rate;
			double tempVary = (predict(tempUserId, tempItemId) - tempRate)/paraItemAlpha[tempItemId];
			tempObjective += Math.pow(Math.abs(tempVary), paraBeta);//+Math.log(2*paraItemAlpha[tempItemId]*Gamma.gamma(1/paraBeta)/paraBeta);
		}
		//tempObjective += DataInfo.rateNumber*(Math.log(Gamma.gamma(1/beta)/beta));
		double U_norm_Fro=0;
		double V_norm_Fro=0;
		for (int i = 0; i < GLDataInfo.nFeatures; i++) {
			for (int j = 0; j < GLDataInfo.userNumber; j++) {
				U_norm_Fro += DataInfo.uFeature[j][i] * DataInfo.uFeature[j][i];
			}
			for (int j = 0; j < GLDataInfo.itemNumber; j++) {
				V_norm_Fro += DataInfo.iFeature[j][i] * DataInfo.iFeature[j][i];
			}
		}
		U_norm_Fro /= GLDataInfo.sigma_U*GLDataInfo.sigma_U;
		V_norm_Fro /= GLDataInfo.sigma_V*GLDataInfo.sigma_V;
		tempObjective += U_norm_Fro+V_norm_Fro;
		return tempObjective;
	}
	public double mae(String paraSetName) {
		int setIndicator = 1; // 0: train; 1: test; 2: dev.
		if(paraSetName.toLowerCase() == "train") {
			setIndicator = 0;
		}
		else if(paraSetName.toLowerCase() == "test") {
			setIndicator = 1;
		}
		else if(paraSetName.toLowerCase() == "dev") {
			setIndicator = 2;
		}
		double mae = 0;
		int tempTestCount = 0;

		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(GLDataInfo.tag_test[i] == setIndicator){						
				int tempUserIndex = GLDataInfo.GLdata[i].i;
				int tempItemIndex = GLDataInfo.GLdata[i].j;
				double tempRate = DataInfo.data[i].rate;	
				double prediction = predict(tempUserIndex, tempItemIndex);
				double newPrediction = GLDataInfo.GLretransfer(prediction * GLDataInfo.global_alpha/ GLDataInfo.magnification + GLDataInfo.mean_rating);
				newPrediction = tempRate - newPrediction;
				mae += Math.abs(newPrediction);
				tempTestCount ++;
			}
		} // Of for i
//		System.out.println("test num="+tempTestCount);
		return (mae / tempTestCount);
	}// Of eval
	
	public double RMSE() {
		double tempRMSE = 0;
		int tempTestCount = 0;

		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(i % GLDataInfo.numFolds != DataInfo.teIndxRem){
				continue;
			}//of if
			
			int tempUserIndex = GLDataInfo.GLdata[i].i;
			int tempItemIndex = GLDataInfo.GLdata[i].j;
			double tempRate = DataInfo.data[i].rate;

			double prediction = predict(tempUserIndex, tempItemIndex);
			double newPrediction = GLDataInfo.GLretransfer(prediction / GLDataInfo.magnification + GLDataInfo.mean_rating);
			
			//System.out.println("OldPrediction: " + 
			//		prediction + " newPrediction: " + newPrediction);
			
			if (newPrediction < -10) {
				newPrediction = -10;
			} // Of if
			if (newPrediction > 10) {
				newPrediction = 10;
			} // of if

			newPrediction = tempRate - newPrediction;
			tempRMSE += Math.pow(newPrediction,4);
			tempTestCount ++;
		} // Of for i
//		System.out.println("test num="+tempTestCount);
		tempRMSE = Math.pow(tempRMSE/tempTestCount,0.25);
		return tempRMSE;
	}// Of eval
	public static int sign(double paraNum){
		if(paraNum>0){
			return 1;
		}
		if(paraNum<0)	
			return -1;
		return 0;
	}
	

	public double computeMaeOnDevSet() {
			// TODO Auto-generated method stub
			double mae = 0;
			int tempTestCount = 0;
	
			for (int i = 0; i < DataInfo.rateNumber; i++) {
				if(GLDataInfo.tag_test[i] == 0 || GLDataInfo.tag_test[i] == 1){
					continue;
				}//of if
				
				int tempUserIndex = GLDataInfo.GLdata[i].i;
				int tempItemIndex = GLDataInfo.GLdata[i].j;
				double tempRate = DataInfo.data[i].rate;
	
				double prediction = predict(tempUserIndex, tempItemIndex);
				double newPrediction = GLDataInfo.GLretransfer(prediction * GLDataInfo.global_alpha/ GLDataInfo.magnification + GLDataInfo.mean_rating);
				
				//System.out.println("OldPrediction: " + 
				//		prediction + " newPrediction: " + newPrediction);
				
				if (newPrediction < -10) {
					newPrediction = -10;
				} // Of if
				if (newPrediction > 10) {
					newPrediction = 10;
				} // of if
	
				newPrediction = tempRate - newPrediction;
				mae += Math.abs(newPrediction);
				tempTestCount ++;
			} // Of for i
	//		System.out.println("test num="+tempTestCount);
			return (mae / tempTestCount);
		}

	/**
	 * 
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String args[]) {
		try {
			int n_exps = 1;//number of repeated experiments	
			double[] MAE = new double[n_exps];
			double[] precisions = new double[n_exps];
			double[] recalls = new double[n_exps];
			double[] F1s = new double[n_exps];
			double[] accs = new double[n_exps];
			for(int j = 0;j < n_exps;j++) {	//Repeated experiments			
				// Step 1. read the training and testing data
				DataInfo tempData = new DataInfo(dataPath);//Read as raw data
				//SimpleTool.printTriple(tempData.data);
				GL tempGL = new GL();
				DataInfo.data=Shuffle.arrayShuffle(DataInfo.data);//Random shuffling
				GLDataInfo.dataToGLData();//Logistic transformation to GL data
				GLDataInfo.GenerateTestSets_every_tenth();//Create test set
				GLDataInfo.GenerateDevSets_every_tenth();//Creat validation set
				GLDataInfo.computeGlobleMean();//Compute mean of transformed data
				GLDataInfo.computeGlobleAlphaByMM(beta, GLDataInfo.computeGlobalStd());//Compute generalized variance of transformed data
				GLDataInfo.normalizeData();//Normalization
	//			for (int j = 0; j < GLDataInfo.GLdata.length; j++) {
	//				System.out.print(GLDataInfo.GLdata[j].rate+" ");
	//			}			
				for (int i = 0; i < 1; i++) {
//					System.out.println("************fold "+i+" ***************");
					DataInfo.teIndxRem=i;	//i for n-fold validation. 						
					// Step 2. Initialize the feature matrices U and V
					initFeature();
					// Step 3. update and predict
//					System.out.println("Begin Training ! ! !");
					int round = 0;
					double tempObjective2 = Double.MAX_VALUE;//Loss before update
					double tempObjective1;//Loss after update
					double maeOnDevSetLastRnd = Double.MAX_VALUE;//Validation error before update
					double maeOnDevSetThisRnd;//Validation error after update
					while(true){
						tempObjective1=tempObjective2;
						round++;				
						System.out.println("round:  " +round);
						double tempGrad = update_one(round);//Gradient descent optimization						
	//					System.out.println("MAE on train set: " + tempGL.mae("train"));
	//					System.out.println("MAE on test set: " + tempGL.mae("test"));
						maeOnDevSetThisRnd = tempGL.mae("dev");//Compute MAE on validation set
						System.out.println("MAE on dev set: " + maeOnDevSetThisRnd);
						if(maeOnDevSetThisRnd >= maeOnDevSetLastRnd) {
							break;
						}
						maeOnDevSetLastRnd = maeOnDevSetThisRnd;								
					}//of for while			
					
					//The following evaluates metrics
					GLDataInfo.computeTP();//Compute TP,FP,TN,FN
					double precision=(double)GLDataInfo.TP/(GLDataInfo.TP+GLDataInfo.FP);
					double recall=(double)GLDataInfo.TP/(GLDataInfo.TP+GLDataInfo.FN);
					double F1=2/(1/precision+1/recall);
					double accuracy=(double)(GLDataInfo.TP+GLDataInfo.TN)/(GLDataInfo.TP+GLDataInfo.TN+GLDataInfo.FP+GLDataInfo.FN);
					MAE[j] = tempGL.mae("test");				
					precisions[j] = precision;
					recalls[j] = recall;
					F1s[j] = F1;
					accs[j] = accuracy;
				
					System.out.println("MAE = "+MAE[j]+" precision = "+precision+"  recall = "+recall+" F1= "+F1+" accuracy = "+accuracy);
				    
					//****Computes ROC when needed***
//					double[][] tempROC = GLDataInfo.computeROC();
//					System.out.println("ROC = ");
//					for (int j1 = 0; j1 < tempROC.length; j1++) {
//						System.out.println(tempROC[j1][0]+"\t"+tempROC[j1][1]);
//					}								
				}
			}
			for(int i = 0;i < MAE.length;i++) {
				System.out.print(MAE[i]+"\t");
			}
			System.out.print("\n");
			for(int i = 0;i < precisions.length;i++) {
				System.out.print(precisions[i]+"\t");
			}
			System.out.print("\n");
			for(int i = 0;i < recalls.length;i++) {
				System.out.print(recalls[i]+"\t");				
			}
			System.out.print("\n");
			for(int i = 0;i < F1s.length;i++) {
				System.out.print(F1s[i]+"\t");
			}
			System.out.print("\n");
			for(int i = 0;i < accs.length;i++) {
				System.out.print(accs[i]+"\t");
			}
			System.out.print("\n");
		} catch (Exception e) {
			e.printStackTrace();
		} // of try
	}// of main

}// of class GL
