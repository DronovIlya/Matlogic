#ifndef PRIMITIVES
#define PRIMITIVES

#include <vector>

struct Z {
    static const int size = 1;
	static int eval(std::vector<int> &args)
	{
        if (args.size() != 1) {
            std::cout << "Z function must have 1 argument" << std::endl;
        }
		return 0;
	}
};

struct N
{
    static const int size = 1;
	static int eval(std::vector<int> &args)
	{
        if (args.size() != 1) {
            std::cout << "N function must have 1 argument" << std::endl;
        }
		return args[0] + 1;
	}	
};

template<int n, int i>
struct U
{
	static const int size = n;
	static int eval(std::vector<int> &args)
	{
		return args[i - 1];
	}
};

template<typename f, typename g, typename ... x>
struct S
{
    static const int size = g::size;
	static int eval(std::vector<int> & args)
	{
		std::vector<int> result = {g::eval(args), x::eval(args)...};
		return f::eval(result);
	}
};

template<typename f, typename g>
struct R
{
    static const int size = f::size;
    static int eval(std::vector<int> & args)
    {
        int y = 0;
        int count = args[args.size() - 1];
        args.pop_back();
        int res = f::eval(args);
        args.push_back(0);
        args.push_back(res);
        for (int i = 0; i < count; i++)
        {
            res = g::eval(args);
            args[args.size() - 2]++;
            args[args.size() - 1] = res;
        }
        args.pop_back();
        args.pop_back();
        args.push_back(count);
        return res;
    }
};

template<typename f>
struct M {
    static const int size = f::size - 1;
    static int eval(std::vector<int> & args)
    {
        int i = 0;
        args.push_back(0);
        while (true)
        {
            if (f::eval(args) == 0) {
                args.pop_back();
                return i;
            }
            i++;
            args[args.size() - 1]++;
        }
        return i;
    }
};

// --a
typedef S<R<Z, U<3,2>>, U<1,1>, U<1,1>> dec; 
// a + b
typedef R<U<1, 1>, S<N, U<3, 3>>> add;
// a - b
typedef R<U<1,1>, S<dec, U<3, 3>>> substract;
// a * b
typedef R<Z, S<add, U<3,1>, U<3,3>>> mul;
// a / b
typedef S<dec, S<M<S<substract, U<3, 1>, S<mul, U<3, 2>, U<3, 3>>>>, S<N, U<2, 1>>, U<2, 2>>> divide;
// a % b
typedef S<substract, U<2,1>, S<mul, U<2,2>, divide>> mod;
// a ^ b
typedef R<S<N,Z>, S<mul, U<3,1>, U<3,3>>> pow;

// statements
typedef S<R<S<N, Z>,S<Z, U<3, 2>>>, U<1, 1>, U<1, 1>> is_zero;
typedef S<substract, S<N,Z>, is_zero> is_not_zero;

typedef S<is_zero, substract> less_eq;
typedef S<R<U<2, 1>, U<4, 2>>, U<2,1>, U<2,2>, S<is_zero, U<2,1>>> non_zero;

// minimum
typedef S<R<U<2,2>, U<4,1>>, U<2,1>, U<2,2>, less_eq> min;
// maximum
typedef S<substract, add, min> max;

// primitive numbers
typedef S<N, Z> one;
typedef S<N, S<N, Z>> two;

// workaround with prime numbers
typedef S<R<Z, S<non_zero, U<3, 3>, S<is_zero, S<mod, U<3, 1>, S<add, U<3, 2>, 
    S<two, U<3, 1>>>>>>>, U<1, 1>, S<substract, U<1,1>, two>> _is_prime;

typedef S<R<S<substract, S<N, Z>, _is_prime>, S<Z, U<3, 1>>>, U<1, 1>, 
    S<less_eq, U<1, 1>, S<N, Z>>> is_prime;

typedef S<R<Z, S<add, U<3, 3>, S<is_prime, U<3, 2>>>>, U<1, 1>, S<N, U<1, 1>>> count_primes; 
// calculate n-th prime number
typedef M<S<substract, S<N, U<2,1>>, S<count_primes, U<2,2>>>> nth_prime;

// partial log
typedef S<R<S<Z,U<2,1>>, S<max, U<4,4>, S<R<S<Z, U<1,1>>, U<3,1>>, U<4,3>, S<is_zero, S<mod, U<4,2>, 
    S<pow, U<4,1>, U<4,3>>>>>>>, U<2,1>, U<2,2>, U<2,2>> plog;

#endif