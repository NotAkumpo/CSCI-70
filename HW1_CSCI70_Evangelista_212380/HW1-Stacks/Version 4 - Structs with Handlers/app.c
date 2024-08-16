#include <stdio.h>
#include "stack.h"

void application(){
    push(0, 'x');
    push(1, 'y');
    push(1, 'z');
    push(0, 'a');
    push(2, 'b');
    push(3, 'c');
    push(4, 'd');
    push(5, 'e');
    push(6, 'f');
    push(7, 'g');
    push(8, 'h');
    push(9, 'i');
    push(10, 'j');
    char ans = pop(1);
    printf("Popped from stack 0: %c\n", ans);
}

int main(){
    application();
    return 0;
}