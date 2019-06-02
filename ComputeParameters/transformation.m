function [ X_trans ] = transformation(X, K, L, v)
X_trans = -log(((K - L)./(X-L)).^v-1);
