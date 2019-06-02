function [v] = compute_v(X,K,L)
% [n,m]=size(X);
v = 1;
ratings1=transformation(X, K, L, v);
startSkew = skewness(ratings1);
skewness2=inf;
lamda = 0.001;
round = 0;
while 1
    round = round + 1;
    skewness1=skewness2;
    %update v
    mu=mean(ratings1);
    sigma=std(ratings1);
    a=(K - L)./(ratings1 - L);
    pxpv=-a.^v.*log(a)./(a.^v-1);
    pepv=(ratings1-mu).^2.*pxpv;
    dv = mean(pepv)*3/sigma^3;% derivative of skewness to v;
    v = v-lamda*dv;
    ratings1 = transformation(X, K, L, v);
    skewness2=skewness(ratings1);
    if(skewness2<=0)
        if(abs(skewness2)>abs(skewness1))
            v=v+lamda*dv;
            %b=b+alpha*db
            break;
        else v;
            break;
        end      
    end
    if mod(round,20) ==0
        skewness2;
        drop=skewness1-skewness2;
    end
end
skewness2;
end