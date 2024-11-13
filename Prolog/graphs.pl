vertice(a).
vertice(b).
vertice(c).
vertice(d).

edge(a, b).
edge(a, c).
edge(b, c).
edge(d, c).
edge(c, d).

path(X, Y) :- X = Y.
path(X, Y) :- edge(X, Y).
path(X, Y) :- edge(X, Z), path(Z, Y).