hasWings(tweety).
hasWings(bartok).
hasFeathers(tweety).
hasHair(willy).
hasHair(simba).
hasFur(goofy).
hasFur(bartok).
fish(nemo).
fish(X) :- swims(X), \+ hasHair(X), \+ hasFur(X).
swims(willy).

bird(X) :- hasFeathers(X).
mammal(X) :- hasHair(X); hasFur(X).
canFly(X) :- hasWings(X).