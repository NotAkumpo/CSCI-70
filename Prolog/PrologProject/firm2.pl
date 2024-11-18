status(proj1, completed).
status(proj2, completed).
status(proj3, ongoing).

head(marketing, mike).

isBusy(D) :- forall(works(X, D), assigned(X, _)).

isEfficient(E) :- forall(assigned(E, P), status(P, completed)).