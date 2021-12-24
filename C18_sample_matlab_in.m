T = {1};
symbols = (1:5);
prob = [1 1 1 1 2]/6;
dict = huffmandict(symbols,prob, 2, 'min');
T = [T ; {length(dict)} ; dict(:,2)];
T = cell2table(T)
writetable(T,'C18_sample_matlab_out.csv')
