package algorithm;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import datamodel.DataInfo;
import tool.SimpleTool;

/**
 * 
 * @author Henry
 * Generalized logistic distribution
 *
 */
public class MF {
	public static String dataPath = new String("data/jester-data-1/jester-data-1.txt");
	static String split_Sign = new String("	");

	/**
	 * 给两个矩阵填上随机值
	 */
	static void initFeature() {
		Random rand = new Random();

		for (int i = 0; i < DataInfo.userNumber; i++) {
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				DataInfo.uFeature[i][j] = 1 * rand.nextDouble() - 0.5;
			} // of for j
		} // Of for i

		// SimpleTool.printMatrix(DataInfo.userFeature);
		for (int i = 0; i < DataInfo.itemNumber; i++) {
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				DataInfo.iFeature[i][j] = 1 * rand.nextDouble() - 0.5;
			} // Of for j
		} // Of for i
	}// of initFeature

	/**
	 * R_{i,j} = \sigma_{l \in [0, k]} U_{i,l} * V_{l, j}
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public static double predict(int userId, int itemId) {
		double pre = 0;
		for (int i = 0; i < DataInfo.featureNumber; i++) {
			// User的行向量和Item列向量的乘积
			// 这种存储方式非常精妙
			pre += DataInfo.uFeature[userId][i] * DataInfo.iFeature[itemId][i];
		} // of for i
		return pre;
	}// Of predict
	
	/**
	 * 所有基于矩阵分解的方法核心变化就在update_one上
	 */
	public static void update_one() {
		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(i % 10 == DataInfo.teIndxRem){
				continue;
			}//of if
			
			int tempUserId = (Integer) DataInfo.data[i].i;
			int tempItemId = (Integer) DataInfo.data[i].j;
			double tempRate = (Double) DataInfo.data[i].rate;

			double tempVary = predict(tempUserId, tempItemId) - tempRate;//残差

			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = tempVary * DataInfo.iFeature[tempItemId][j] + DataInfo.lambda * DataInfo.uFeature[tempUserId][j];
				DataInfo.uFeature[tempUserId][j] = DataInfo.uFeature[tempUserId][j] - DataInfo.alpha * tmp;
			}//of for j

			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = tempVary * DataInfo.uFeature[tempUserId][j] + DataInfo.lambda * DataInfo.iFeature[tempItemId][j];
				DataInfo.iFeature[tempItemId][j] = DataInfo.iFeature[tempItemId][j] - DataInfo.alpha * tmp;
			}//of for j

		}//of for i
	}//Of update_one

	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public double rmse() {
		double rmse = 0;
		int tempTestCount = 0;

		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(i % 10 != DataInfo.teIndxRem){
				continue;
			}//of if
			
			int tempUserIndex = DataInfo.data[i].i;
			int tempItemIndex = DataInfo.data[i].j;
			double tempRate = DataInfo.data[i].rate;

			double prediction = predict(tempUserIndex, tempItemIndex) ;//+ DataInfo.mean_rating;

			
			if (prediction < -10) {
				prediction = -10;
			} // Of if
			if (prediction > 10) {
				prediction = 10;
			} // of if

			prediction = tempRate - prediction;
			rmse += prediction * prediction;
			tempTestCount ++;
		} // Of for i

		return Math.sqrt(rmse / tempTestCount);
	}// Of eval
	
	/**
	 * Compute the MAE
	 * 
	 * @return
	 */
	public double mae() {
		double mae = 0;
		int tempTestCount = 0;

		for (int i = 0; i < DataInfo.rateNumber; i++) {
			if(i % 10 != DataInfo.teIndxRem){
				continue;
			}//of if
			
			int tempUserIndex = DataInfo.data[i].i;
			int tempItemIndex = DataInfo.data[i].j;
			double tempRate = DataInfo.data[i].rate;

			double prediction = predict(tempUserIndex, tempItemIndex) ;//+ DataInfo.mean_rating;

			
			if (prediction < -10) {
				prediction = -10;
			} // Of if
			if (prediction > 10) {
				prediction = 10;
			} // of if

			prediction = tempRate - prediction;
			mae += Math.abs(prediction);
			tempTestCount ++;
		} // Of for i

		return (mae / tempTestCount);
	}// Of eval

	/**
	 * 
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String args[]) {
		try {
			// Step 1. read the training and testing data
			DataInfo tempData = new DataInfo(dataPath);
			//SimpleTool.printTriple(tempData.data);
			MF tempGL = new MF();
			// Step 2. Initialize the feature matrices U and V
			initFeature();
			// Step 3. update and predict
			System.out.println("Begin Training ! ! !");

			for (int i = 0; i < DataInfo.round; i++) {
				System.out.println("round:  " + (i + 1));
				update_one();
				double tempMAE = tempGL.mae();
				System.out.println("MAE: " + tempMAE);
			}//of for i
			
		} catch (Exception e) {
			e.printStackTrace();
		} // of try
	}// of main

}// of class GL
