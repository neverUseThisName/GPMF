package tool;

public class Gamma {  	
	
	static double GAMMA = 0.577215664901532860606512090082;
	static double GAMMA_MINX = 1.e-12;
	static double DIGAMMA_MINNEGX = -1250;
	static double C_LIMIT = 49;
	static double S_LIMIT = 1e-5;
	
	
	public static double logGamma(double x) {  
	double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);  
	double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)  
		         + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)  
		         +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);  
	 return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));  
	}  
	
	public static double gamma(double x) { 
		return Math.exp(logGamma(x)); 
	}  
	
	public static void main(String[] args) {   
		 double x = 0.8;  
		 System.out.println("Gamma(" + x + ") = " + gamma(x));   
		 System.out.println("Digamma(" + x + ") = " + digamma(x));  
		 System.out.println(gamma(1/x)/x);
	}  
		  
  

public static double digamma(double x) {

    double value = 0;

    while (true){

        if (x >= 0 && x < GAMMA_MINX) {
            x = GAMMA_MINX;
        }
        if (x < DIGAMMA_MINNEGX) {
            x = DIGAMMA_MINNEGX + GAMMA_MINX;
            continue;
        }
        if (x > 0 && x <= S_LIMIT) {
            return value + -GAMMA - 1 / x;
        }

        if (x >= C_LIMIT) {
            double inv = 1 / (x * x);
            return value + Math.log(x) - 0.5 / x - inv
                    * ((1.0 / 12) + inv * (1.0 / 120 - inv / 252));
        }

        value -= 1 / x;
        x = x + 1;
    }

}
}