data = csvread('jesterfinal151cols.txt');
K = 10;
L = -10;
[m,n] = size(data);
data = data(:);
data = data(data <=K & data >= L);
data(data == K) = K - 0.01;
data(data == L) = L + 0.01;
v = compute_v(data,K,L)  % MASE estimate parameter v
transformed_data = transformation(data,K,L,v);
[beta,mu,alpha] = compute_paras_for_GGD(transformed_data)