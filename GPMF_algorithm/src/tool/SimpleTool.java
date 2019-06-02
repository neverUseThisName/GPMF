package tool;

import datamodel.Triple;

public class SimpleTool {
	/**
	 * ���ֲ��ң��ҵ���ֵ�������е��±꣬����Ϊ-1
	 */
	public static int binarySerach(int[] array, int key) {
		int left = 0;
		int right = array.length - 1;

		// ��������� <=
		while (left <= right) {
			int mid = (left + right) / 2;
			if (array[mid] == key) {
				return mid;
			} else if (array[mid] < key) {
				left = mid + 1;
			} else {
				right = mid - 1;
			} // of if
		} // of while

		return -1;
	}// of binarySerach

	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(int[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is an int matrix: ");
		} // Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			if (paraMatrix[i] == null) { // modified by zhr 2014/3/30
				continue;
			} // of if
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + ",");
			} // Of for j
			System.out.println();
		} // Of for i
	}// Of printMatrix

	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(double[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is a matrix: ");
		} // Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + "\t");
			} // Of for j
			System.out.println();
		} // Of for i
	}// Of printMatrix

	/**
	 *************************** 
	 * Print an int matrix, simply for test.
	 * 
	 * @param paraMatrix
	 *            The given matrix. Different rows may contain different number
	 *            of values.
	 *************************** 
	 */
	public static void printMatrix(boolean[][] paraMatrix) {
		if (paraMatrix.length == 0) {
			System.out.println("This is an empty matrix.");
			return;
		} else {
			System.out.println("This is an boolean matrix: ");
		} // Of if

		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print("" + paraMatrix[i][j] + "\t");
			} // Of for j
			System.out.println();
		} // Of for i
	}// Of printMatrix

	/**
	 *************************** 
	 * Print an int array, simply for test.
	 * 
	 * @param paraIntArray
	 *            The given int array.
	 *************************** 
	 */
	public static void printIntArray(int[] paraIntArray) {
		if (paraIntArray == null || paraIntArray.length == 0) {
			System.out.println("This is an empty int array.");
			return;
		} else {
			System.out.print("This is an int array: ");
		}
		for (int i = 0; i < paraIntArray.length; i++) {
			System.out.print("" + paraIntArray[i] + "\t");
		} // Of for i
		System.out.println();
	}// Of paraIntArray

	/**
	 *************************** 
	 * Print a long array, simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArray(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			System.out.print("" + paraLongArray[i] + "\t");
		} // Of for i
		System.out.println();
	}// Of printLongArray

	/**
	 ************************* 
	 * Print a double array.
	 ************************* 
	 */
	public static void printDoubleArray(double[] paraDoubleArray) {
		for (int i = 0; i < paraDoubleArray.length; i++) {
			System.out.print(paraDoubleArray[i] + " ");
		} // Of for i
		System.out.println();
	}// Of printDoubleArray

	/**
	 *************************** 
	 * Print a long array, zero is not printed. Simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArrayNoZero(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			if (paraLongArray[i] == 0)
				continue;
			System.out.print("" + paraLongArray[i] + "\t");
		} // Of for i
	}// Of printLongArrayNoZero

	/**
	 *************************** 
	 * Print an triple, simply for test.
	 * 
	 * @param paraTriple
	 *            The given triple object. of values.
	 *************************** 
	 */
	public static void printTriple(Triple paraTriple) {
		System.out.println("user: " + paraTriple.i + " item: " + paraTriple.j + " rate: " + paraTriple.rate);
	}// Of printTriple

	/**
	 *************************** 
	 * Print an triple, simply for test.
	 * 
	 * @param paraTriple
	 *            The given triple object. of values.
	 *************************** 
	 */
	public static void printTriple(Triple[] paraTriple) {
		for (int i = 0; i < paraTriple.length; i++) {
			System.out
					.println("user: " + paraTriple[i].i + " item: " + paraTriple[i].j + " rate: " + paraTriple[i].rate);
		} // Of for i

	}// Of printTriple

	/**
	 * 
	 * @param x_n
	 * @param Mu_k
	 * @param Sigma_k
	 * @return
	 */
	public static double GaussianFunction(double x_n, double Mu_k, double Sigma_k) { // return
		// N(x_n|...)

		double Prob = 0;
		Prob = Math.pow(2 * 3.14159265 * Sigma_k, -0.5) * Math.exp(-(Math.pow(x_n - Mu_k, 2)) / (2 * Sigma_k));
		return Prob;
	}//of GaussianFunction
	
	/**
	 * 
	 * @param num
	 * @return
	 */
	public static double log2(double num) {
		if (num == 0)
			return 0;
		else
			return (Math.log(num) / Math.log(2));
	}//of log2
	
	public static int random() {
		int temp = 0;
		temp = (int) (Math.random() * 1000) + 1;
		return temp;
	}

	public static int random_Num_between(int a, int b) {

		int num;
		while (true) {
			num = a + random() / 100;
			if (num >= a && num <= b) {
				break;
			}
		}
		return num;
	}
	
	 public static void bubbleSortForTriple(Triple[] numbers)
	    {
	        int temp = 0;
	        double temp2 = 0;
	        int size = numbers.length;
	        for(int i = 0 ; i < size-1; i ++)
	        {
	        for(int j = 0 ;j < size-1-i ; j++)
	        {
	            if(numbers[j].rate > numbers[j+1].rate)  
	            {
	            temp = numbers[j].i;
	            numbers[j].i = numbers[j+1].i;
	            numbers[j+1].i = temp;
	            
	            temp = numbers[j].j;
	            numbers[j].j = numbers[j+1].j;
	            numbers[j+1].j = temp;
	            
	            temp2 = numbers[j].rate;
	            numbers[j].rate = numbers[j+1].rate;
	            numbers[j+1].rate = temp2;
	            }
	        }
	        }
	    }
}// Of class SimpleTool
