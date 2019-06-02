# GPMF
This is the GPMF implementation code. I use Matlab to estimate the parameters and Java to solve the optimazation.
Link to GPMF paper: https://ieeexplore.ieee.org/abstract/document/8588818
## Parameter estimation:
Run estimateParameters.m to get parameters needed for GPMF.
You can also use the parameters to fit rating data distribution using GLG-N.
![BX users' average ratings and the corresponding GLG-N ﬁt.](https://github.com/neverUseThisName/GPMF/blob/master/BX_userAvg_8more_GLG-N_fit.png)
![MovieLens users' average ratings and the corresponding GLG-N ﬁt.](https://github.com/neverUseThisName/GPMF/blob/master/ML_latest_userAvg_GLG-N_fit.png)
![Jester2+ distribution and its fit](https://github.com/neverUseThisName/GPMF/blob/master/jester2plus.png)
## GPMF algorithm
Build project in 'GPMF_algorithm' folder and run GL.java to run optimization.
## Results
![](https://github.com/neverUseThisName/GPMF/blob/master/PRE_REC_F1_th3_jester1_1.png)
![](https://github.com/neverUseThisName/GPMF/blob/master/PRE_REC_F1_th3_jester1_2.png)
![](https://github.com/neverUseThisName/GPMF/blob/master/PRE_REC_F1_th3_jester2+.png)
![](https://github.com/neverUseThisName/GPMF/blob/master/ROC-curve-th3_jester1_1.png)
![](https://github.com/neverUseThisName/GPMF/blob/master/ROC-curve-th3_jester1_2.png)
![ROC](https://github.com/neverUseThisName/GPMF/blob/master/ROC-curve-th3_jester2+.png)
![](nfeat_vs_Metrics.png)
