#include <stdio.h>

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
    int n = 30; // Calculate the first 20 Fibonacci numbers

    printf("The first %d Fibonacci numbers are:\n", n);
    int i;
    for (i= 0; i < n; i++) {
        printf("%lld ", fibonacci(i));
    }

    return 0;
}
