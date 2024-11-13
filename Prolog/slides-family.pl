parents(william,diana,charles).
parents(henry,diana,charles).
parents(charles,elizabeth, philip).
parents(diana,frances,edward).
parents(anne,elizabeth,philip).
parents(andrew,elizabeth, philip).
parents(edwardW,elizabeth,philip).
married(diana,charles).
married(elizabeth,philip).
married(frances,edward).
married(anne,mark).

parent(C,M) :- parents(C,M,D).
parent(C,D) :- parents(C,M,D).

sibling(X,Y) :- parents(X,M,D),
parents(Y,M,D), X \= Y.

aORuDirect(C, A) :- parent(C,P),
sibling(P,A).
aORuMarr(C, A) :- aORuDirect(C,X),
married(X,A).
aORuMarr(C, A) :- aORuDirect(C,X),
married(A,X).

aORu(C,A) :- aORuDirect(C,A).
aORu(C,A) :- aORuMarr(C,A).