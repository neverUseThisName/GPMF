package datamodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import tool.SimpleTool;

public class DataInfo {
	public static int userNumber = 50692;//24983;23500，50692// 49290 //9439;//88238;//4696;
										// //1049511;//6040; 49290

	public static int itemNumber = 140;// 139738 // 139738; //66726;//3952;
										// 139738
	public static int rateNumber = 1728847;//1810455,1708993，1728847 //532274;// 4851475;
	public static int trNumber = 0;
	public static int teNumber = 0; //132550;// 93100
	public static int teIndxRem = 2; //0--9
	
	/**********************Feature Matrix***********************************/
	public static short featureNumber = 20;
	public static double[][] uFeature = new double[userNumber][featureNumber];
	public static double[][] iFeature = new double[itemNumber][featureNumber];

	/******************** Training set *******************************/
	public static Triple[] data = new Triple[rateNumber];
	
	/****
	 */
	public static int round = 1000; 
	public static double mean_rating = 0;
	
	public static double alpha = 0.0001;
	
	public static double lambda = 0.005;

	//public static int score_record = 0;
	
	public static String dataPath = new String("data/jester-data-1/jester-data-1.txt");
	//public static String testPath = new String("data/ml-100k/u1.test");
	static String split_Sign = new String(",");

	static int[] userCount;
	static int[] userPot;
	
	/**
	 * 
	 * @param paraFile
	 * @throws Exception
	 */
	public DataInfo(String paraDataPath) throws IOException{
		readData(paraDataPath);
		//readTestData(paraTestPath);
		setPot();
	}//of the first constructor
	
	/**
	 * 
	 * @param paraDataPath
	 * @throws IOException
	 */
	static void readData(String paraDataPath) throws IOException {
		File file = new File(paraDataPath);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		double sum = 0;
		int userIndex = 0;
		int index = 0;
		for (int i = 0; i < DataInfo.rateNumber; i++) {
			data[i] = new Triple();
		} // Of for i
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int user = userIndex;// user id
			for(int i = 1; i <= itemNumber; i ++){
				int item = i - 1;// item id
//				System.out.print(user+", "+item+" ");
				double rating = Double.parseDouble(parts[i]);// rating
//				if(userIndex==0){
//					System.out.print(parts[i]+" ");
//				}
				if(rating != 99){
					data[index].i = user;
					data[index].j = item;
					data[index].rate = rating;
//					if(index % GLDataInfo.numFolds != teIndxRem){
//						sum += rating;// total rating
//						trNumber ++;
//					}//Of if
					index++;
				}//Of if
			}//Of for i
			userIndex ++;
		} // Of while
		//System.out.print(data.length);
//		teNumber = rateNumber - trNumber;
//		System.out.println("index:" + index);
//		mean_rating = sum / trNumber;// average rating
//		for (int i = 0; i < DataInfo.rateNumber; i++) {
//			double tmp = (Double) data[i].rate;// - mean_rating;
//			data[i].rate = tmp;// ԭʼ����-ƽ����
//		} // of for i
		buffRead.close();
	}
	
	/**
	 * 
	 */
	static void setPot() {
		// ����һ����Ϊ�˱��ں��洦��
		userCount = new int[DataInfo.userNumber + 1];// ���һ������Ϊ0
		userPot = new int[DataInfo.userNumber + 1];

		for (int i = 0; i < DataInfo.rateNumber; i++) {
			userCount[data[i].i]++;
		} // Of for i

		for (int i = 1; i <= DataInfo.userNumber; i++) {
			userPot[i] = userPot[i - 1] + userCount[i - 1];
		} // Of for i
	}// Of setPot
	
	
	/**
	 * 
	 * @param paraUser
	 * @param paraItem
	 * @return
	 */
	static Triple getDataInfo(int paraUser, int paraItem){
		int left = userPot[paraUser];
		int right = userPot[paraUser + 1] - 1;
		
		while (left <= right) {
			int mid = (left + right) / 2;
			if (data[mid].j > paraItem) {
				right = mid - 1;
			} else if (data[mid].j < paraItem) {
				left = mid + 1;
			} else {
				return data[mid];
			} // of if
		} // of while
		return null;
	}//Of getDataInfo
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			//Step 1. Initialize the train and test data based on group information
			DataInfo tempData = new DataInfo(dataPath);
		
			//Step3. Test
			SimpleTool.printTriple(tempData.data);
			//Triple tempElement = getDataInfo(9, 6);
			//SimpleTool.printTriple(tempElement);
			
		}catch(Exception e){
			e.printStackTrace();
		}//of try
	}//of main
}//Of class DataInfo


