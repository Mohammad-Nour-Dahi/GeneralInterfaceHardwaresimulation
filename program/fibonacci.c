#include <stdio.h>


/*
 * Calculates the nth Fibonacci number.
 *
 * @param n The index of the Fibonacci number to calculate.
 * @return The nth Fibonacci number.
 */
long long fibonacci(int n) {
    if (n <= 1)
        return n;

    long long a = 0, b = 1, result;
    //int i;
    for (int i = 2; i <= n; i++) {
        result = a + b;
        a = b;
        b = result;
    }

    return result;
}

int main() {
    int n = 30; // Calculate the first 30 Fibonacci numbers

    printf("The first %d Fibonacci numbers are:\n", n);
    int i;
    for (i= 0; i < n; i++) {
        printf("%lld ", fibonacci(i));
    }

    return 0;
}
