;PART 1

;1. Given the numbers a, b, and c, return the sum of a and b subtracted by c.
(defun number1(a b c)
    (- (+ a b) c)
)

;2. Given the number a, return the result of (a - 1)(a - 3).
(defun number2(a)
    (let ( (b (- a 1)) (c (- a 3)) )
        (* b c)
    )
)

;3. Return the second to the last element of a given list.
(defun number3(l)
    (cond 
        ( (null l) nil )
        ( (= 2 (length l) ) (first l) )
        ( t ( number3 (rest l) ))
    )
)

;4. Given a number n and a list, return the nth element of the list.
(defun number4(n l)
    (cond 
        ( (null l) nil )
        ( (> n (length l)) '(N is out of bounds!) )
        ( (= n 1) (first l) )
        ( t ( number4 (- n 1) (rest l) ))
    )
)

;5. Given a list n, return a list that contains all but the last element of the list n.
(defun number5(n)
    (cond
        ( (null n) nil)
        ( (= 1 (length n)) nil)
        ( t  (number6(rest (number6 n))) )
    )
)

;6. Given a list, return the reverse of the list.
(defun number6(l)
    (cond
        ( (null l) nil )
        ( t 
            (append 
                (rvs (rest l))
                (list (first l))
            ) 
        )
    )
)

;7. Given a list, remove the first element if it is of even length, and add the number 1 to the front of it if it is of odd length.
(defun number7(l)
    (cond
        ( (null l) nil)
        ( (= 0 (mod (length l) 2)) (rest l) )
        ( t (cons '1 l) )
    )
)

;8. Return the last two elements of a list
(defun number8(l)
    (cond 
        ( (null l) nil )
        ( (= 2 (length l) ) l )
        ( t ( number8 (rest l) ))
    )
)


;PART 2

;(defun filterless(n l)
;    (cond
;        ( (null l) nil)
;        ( (> n (first l)) (cons (first l) (filterless n (rest l))) ) 
;        ( t (filterless n (rest l)) )
;    )
;)

;(defun filtermore(n l)
;    (cond
;        ( (null l) nil)
;        ( (< n (first l)) (cons (first l) (filtermore n (rest l))) ) 
;        ( t (filtermore n (rest l)) )
;    )
;)

(defun filter(op n l)
    (cond
        ( (null l) nil)
        ( (funcall op n (first l)) (cons (first l) (filter op n (rest l))) ) 
        ( t (filter op n (rest l)) )
    )
)

(defun qsort(l)
    (let* ( (p (first l)) (l-rest (rest l)) (a (filter #'> p l-rest)) (b (filter #'< p l-rest)) )
        (cond
            ( (>= 1 (length l)) l )
            ( t ( append (qsort a) (list p) (qsort b) ) )
        )
    )
)