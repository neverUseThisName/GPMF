function [beta,mu,alpha] = compute_paras_for_GGD(samples)
mu = mean(samples);
m1 = mean(abs(samples - mu));
m2 = mean((samples - mu).^2);
syms b
beta = solve(gamma(2/b)/(gamma(1/b) * gamma(3/b))^0.5 - m1/m2^0.5, b);
beta = subs(beta);
alpha = std(samples) * (gamma(1/beta) / gamma(3/beta))^0.5;