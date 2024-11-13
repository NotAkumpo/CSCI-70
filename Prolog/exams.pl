test(quiz1).
test(quiz2).
test(quiz3).
test(midterm).

student(mary).
student(john).

studied(mary, quiz1).
studied(mary, quiz2).
studied(mary, quiz3).
studied(mary, midterm).

studied(john, quiz2).

easy(quiz1).
easy(quiz3).

willPass(S, T) :- easy(T).
willPass(S, T) :- \+ easy(T), studied(S, T).