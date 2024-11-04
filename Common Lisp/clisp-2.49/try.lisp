;Checks to see if load worked
(print (+ 4 5 6))
(print "Hello, Lisp!")

;Square function
(defun sqr(a) 
    (* a a)
)

;Absolute value function
(defun absval(b)
    (cond
        ( (> b 0) b )
        ( t (- b) )
    )
)

;Factorial function
(defun fct(c)
    (cond
        ( (= c 0) 1 )
        ( t 
            (* c 
                (fct 
                    (- c 1)
                )
            )
        )
    )
)

;List reverse function
(defun rvs(l)
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

;Flatten list function
(defun flatten(m)
    (let ( (f (first m)) (r (rest m)) ) 
        (cond
            ( (null m) nil )
            ( (atom f) 
                (cons f (flatten r)) 
            )
            ( t (append (flatten f) (flatten r) ) 
            )

        )
    )
)