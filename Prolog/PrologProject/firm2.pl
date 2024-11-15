% employee(mike).
% employee(mary).
% employee(martha).
% employee(john).
% employee(josh).
% employee(julia).
% employee(jack).

% department(production).
% department(sales).
% department(marketing).
% department(accounting).

% project(proj1).
% project(proj2).
% project(proj3).

% works(mike, production).
% works(mary, production).
% works(john, sales).
% works(josh, sales).
% works(julia, marketing).
% works(jack, marketing).

% assigned(mary, proj1).
% assigned(martha, proj1).
% assigned(martha, proj2).
% assigned(josh, proj2).
% assigned(julia, proj2).
% assigned(jack, proj3).

% departmentMates(X, Y) :- works(X, D), works(Y, D), X \= Y.
% projectMates(X, Y) :- assigned(X, P), assigned(Y, P), X \= Y.
% hasWorkers(D) :- works(_, D).

status(proj1, completed).
status(proj2, completed).
status(proj3, ongoing).

head(marketing, mike).

isBusy(D) :- forall(works(X, D), assigned(X, _)).

isEfficient(E) :- forall(assigned(E, P), status(P, completed)).