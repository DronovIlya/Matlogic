#include <cstdio>
#include <iostream>
#include <time.h>
#include <vector>
#include "primitives.h"

#define MAX_TEST_COUNT_PER_FUNCTION 5

template<typename f, typename... g>
int run(g... args)
{
	std::vector<int> result {args...};
	return f::eval(result);
}

void testAdd()
{
	printf("### ADD tests start processing... ###\n");
	int success_count = 0;
	for (int i = 0; i < MAX_TEST_COUNT_PER_FUNCTION; i++) {
		std::vector<int> args = {rand() % 200, rand() % 200};
		
		int actual = run<add>(args[0], args[1]);
		int expected = args[0] + args[1];

		printf("a = %d, b = %d, actual = %d, expected = %d\n", args[0], args[1],
			actual, expected);
		success_count += (actual == expected ? 1 : 0);
	}
	printf("result = %d/%d\ncorrectness = %s\n", success_count, MAX_TEST_COUNT_PER_FUNCTION, 
		success_count == MAX_TEST_COUNT_PER_FUNCTION ? "OK" : "FAIL");
	printf("### ADD tests end processing... ###\n");
}

void testSubstract()
{
	printf("### SUBSTRACT tests start processing... ###\n");
	int success_count = 0;
	for (int i = 0; i < MAX_TEST_COUNT_PER_FUNCTION; i++) {
		std::vector<int> args = {rand() % 200, rand() % 200};
		
		int actual = run<substract>(args[0], args[1]);
		int expected = args[0] - args[1];
		expected = expected > 0 ? expected : 0;

		printf("a = %d, b = %d, actual = %d, expected = %d\n", args[0], args[1],
			actual, expected);
		success_count += (actual == expected ? 1 : 0);
	}
	printf("result = %d/%d\ncorrectness = %s\n", success_count, MAX_TEST_COUNT_PER_FUNCTION, 
		success_count == MAX_TEST_COUNT_PER_FUNCTION ? "OK" : "FAIL");
	printf("### SUBSTRACT tests end processing... ###\n");
}

void testMul()
{
	printf("### MUL tests start processing... ###\n");
	int success_count = 0;
	for (int i = 0; i < MAX_TEST_COUNT_PER_FUNCTION; i++) {
		std::vector<int> args = {rand() % 200, rand() % 200};
		
		int actual = run<mul>(args[0], args[1]);
		int expected = args[0] * args[1];

		printf("a = %d, b = %d, actual = %d, expected = %d\n", args[0], args[1],
			actual, expected);
		success_count += (actual == expected ? 1 : 0);
	}
	printf("result = %d/%d\ncorrectness = %s\n", success_count, MAX_TEST_COUNT_PER_FUNCTION, 
		success_count == MAX_TEST_COUNT_PER_FUNCTION ? "OK" : "FAIL");
	printf("### MUL tests end processing... ###\n");
}

void testIsPrime() {
	printf("### IS_PRIME tests start processing... ###\n");
	int success_count = 0;
	for (int i = 0; i < MAX_TEST_COUNT_PER_FUNCTION; i++) {
		std::vector<int> args = {rand() % 200};
		
		int actual = run<is_prime>(args[0]);

		bool ok = true;
		for (int i = 2; i < args[0]; i++)
			if (args[0] % i == 0) {
				ok = false;
			}
		int expected = ok ? 1 : 0;

		printf("a = %d, b = %d, actual = %d, expected = %d\n", args[0], args[1],
			actual, expected);
		success_count += (actual == expected ? 1 : 0);
	}
	printf("result = %d/%d\ncorrectness = %s\n", success_count, MAX_TEST_COUNT_PER_FUNCTION, 
		success_count == MAX_TEST_COUNT_PER_FUNCTION ? "OK" : "FAIL");
	printf("### IS_PRIME tests end processing... ###\n");

}

void testNthPrime() {
	printf("### IS_PRIME tests start processing... ###\n");
	int success_count = 0;
	for (int i = 0; i < 10; i++) {
		printf("%d-th prime number is = %d\n", (i + 1), run<nth_prime>(i));
	}
	printf("### IS_PRIME tests end processing... ###\n");

}


int main()
{
	printf("--- tests start processing... ---\n");
	testAdd();
	testSubstract();
	testMul();
	testIsPrime();
	testNthPrime();
	printf("--- tests end processing... ---\n");
	return 0;
}