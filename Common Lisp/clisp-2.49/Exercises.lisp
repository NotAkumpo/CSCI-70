;1. Returns a list that contains the two roots of a quadratic formula, given a, b, and c

(defun quad (a b c)
    (list
        (/
            (+
                (- b)
                (sqrt
                    (-
                        (expt b 2)
                        (* 4 a c)
                    )
                )
            )
            (* a 2)
        )
        (/
            (-
                (- b)
                (sqrt
                    (-
                        (expt b 2)
                        (* 4 a c)
                    )
                )
            )
            (* a 2)
        )
    )
)

;You can test this with (print (quad 1 -7 10))

;2. Returns the last element of a list (without using last)

;To get to the last element, we use rest until there is only one element
;left. Once there is only one element left, we can return that element.
;There are three cases to consider for this getlast function:
    ;The list is empty                          return nil
    ;The length of the list is more than 1      call getlast on the rest of the list
    ;The length of the list is 1                return the first of the list
     
(defun getlast (l)
    (cond
        ( (null l) nil )
        ( (> (length l) 1) (getlast (rest l) ) )
        ( t (first l))
    )
)

;Note that using t on cond makes it the catch-all. If none of the conditions are met,
;this expression is run instead.

;You can also just use reverse and return the first of the reversed list. Which is fine. I guess.

;3. Given a number N and a list, remove all the numbers in the list that are lower than N

;For this we compare the first item of the list to N, and keep it if it's not lower.
;For this filter function we have three cases to consider:

    ;The list is empty                              return nil
    ;First item is less than N                      call filter on the rest of the list
    ;First item is greater than or equal to N       attach the first item to calling filter on the rest of list

(defun filter (N l)
  (cond
    ( (null l) nil )
    ( (> N (first l) ) (filter N (rest l) ) )
    ( t (cons (first l) (filter N (rest l) ) ) )
  )
)

;4. Given two lists of ordered integers (lowest to highest), return a list of all their unique integers

;First thing we'll check for is if any or both of the lists (A and B) are null
;If A is null, whatever is in B must be unique, and thus we return B, and vice versa.
;If both are null, we return nil.
;If none of them are the case, we can now check for uniqueness
;Since they're arranged in ascending order, there are a few cases to consider:
;If both first elements are equal, then they are not unique. Thus we remove them.
;If they are not equal, we then have to think about what could be happening.
;If, for example, we have 1 as the first element of A, and 2 as the first element of B,
;It's possible that A still has a 2 that we haven't seen yet. However, it's not possible
;for B to have a 1 that we haven't seen yet, since if it did have one, we would be seeing 
;it now, considering that it's in ascending order. Thus, if they're not equal, we keep the
;lowest value. Here are the cases for the function:

    ;A and B are empty                              return nil
    ;Only A is empty                                return B 
    ;Only B is empty                                return A
        ;first A and first B are equal              call func with rest A and rest B 
            ;first A is smaller than rest B         attach first A to calling func on rest B
            ;first B is smaller than rest A         attach first B to calling func on rest A

(defun func (a b)
    (cond
        ( ( and ( null a ) ( null b ) ) nil)
        ( ( null a ) b )
        ( ( null b ) a )
        ( t 
            (cond
                ( (eq ( first a ) ( first b ) )
                    (func ( rest a ) ( rest b ) )
                )
                ( (< ( first a ) ( first b ))
                    (cons ( first a ) ( func ( rest a ) b ) )
                )
                ( t
                    (cons ( first b )  (func a ( rest b ) ) )
                )
            )
        )
    )
)