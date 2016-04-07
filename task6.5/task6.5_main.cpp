#include <cstdio>
#include <iostream>
#include <time.h>
#include <vector>
#include "functions.h"

#define MAX_TEST_COUNT_PER_FUNCTION 5

template<typename f, typename... g>
ll run(g... args)
{
	std::vector<ll> result {args...};
	return f::eval(result);
}

int main()
{
	printf("--- tests start processing... ---\n");
	printf("n = %d, m = %d, result = %lld\n", 1, 1, ackermann::eval(1, 1));
	printf("n = %d, m = %d, result = %lld\n", 0, 1, ackermann::eval(0, 1));
	printf("n = %d, m = %d, result = %lld\n", 1, 0, ackermann::eval(1, 0));
	printf("n = %d, m = %d, result = %lld\n", 1, 1, ackermann::eval(1, 1));
	printf("n = %d, m = %d, result = %lld\n", 1, 2, ackermann::eval(1, 2));
	printf("n = %d, m = %d, result = %lld\n", 2, 1, ackermann::eval(2, 1));
	printf("n = %d, m = %d, result = %lld\n", 3, 0, ackermann::eval(3, 0));
	printf("n = %d, m = %d, result = %lld\n", 2, 5, ackermann::eval(2, 5));
	printf("--- tests end processing... ---\n");
	return 0;
}