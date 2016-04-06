#include <cstdio>
#include <iostream>
#include <vector>

typedef std::vector<int> vec;

struct Z {
	static const int size = 1;
	static int eval(std::vector<int> &args)
	{
		// TODO: check vector size
		return 0;
	}
};

struct N
{
	static const int size = 1;
	static int eval(std::vector<int> &args)
	{
		// TODO: check vector size
		return args[0] + 1;
	}	
};

template<int n, int i>
struct U
{
	static const int size = n;
	static int eval(std::vector<int> &args)
	{
		// TODO: check vector size
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

// template<typename f, typename g>
// struct R
// {
// 	static const int size = f::size + 1;
// 	static int eval(std::vector<int> &args)
// 	{
// 		for (int i = 0; i < args.size(); i++) {
// 			printf("%d ", args[i]);
// 		}
// 		printf("\n");
// 		std::vector<int> p (args.begin(), args.end());
// 		p.pop_back();

// 		printf("dasdadasds\n");
// 		for (int i = 0; i < p.size(); i++) {
// 			printf("%d ", p[i]);
// 		}
// 		printf("\n");
		

// 		int last = args.back();
// 		if (last == 0)
// 		{
// 			f::eval(p);
// 		}
// 		else
// 		{
// 			p.push_back(last - 1);
// 			int res = R<f, g>::eval(p);
// 			p.push_back(res);
// 			return g::eval(p);
// 		}
// 	}	
// };

template<typename Func, typename Func1>
struct R
{
    static const int size = Func::size+1;


    static int eval(vec const & args)
    {
        vec v (args.begin(), --args.end());

		for (int i = 0; i < args.size(); i++) {
			printf("%d ", args[i]);
		}
		printf("\n");

        if(args.back() == 0)
        {
            return Func::eval(v);
        }
        else
        {
            v.push_back(args.back()-1);
            int res = R<Func, Func1>::eval(v);
            v.push_back(res);
            return Func1::eval(v);
        }

    }
};

template<typename Func>
struct M {
    static const int size = Func::size - 1;
    static int eval(vec const & args)
    {
        int y=0;
        vec v2 = args;
        v2.push_back(y);
        while(Func::eval(v2) != 0) {
            v2[size] = ++y;
        }
        return y;
    }
};

typedef S<N, Z> one;
typedef R< U<1,1>, S<N, U<3,3> > > sum;
typedef S< R<Z, U<3,2> >, U<1,1>, U<1,1> > minus1;//minus1
typedef R< U<1,1>, S<minus1, U<3,3> > > sub;
typedef R< Z, S<sum, U<3,1>, U<3,3> > > mul;
typedef R<one, S<mul, U<3,1>, U<3,3> > > pow;
typedef S< minus1, S< M< S<sub, U<3,1>, S<mul, U<3, 2>, U<3, 3> > > >, S<N, U<2, 1> >, U<2, 2> > > divide;
typedef S<sub, U<2,1>, S<mul, U<2,2>, S< divide, U<2,1>, U<2,2> > > > mod;
typedef S< R< N, S<Z, U<3, 3> > >, U<1,1>, U<1,1> > _not;
typedef S<_not, mod> divideable;
typedef S< minus1, M<S< divideable, U<3,2>, S<pow, U<3,1>, U<3,3> > > > > plog;
typedef _not is_zero;
typedef S<_not, is_zero> is_not_zero;
typedef S<is_not_zero, mul> _and;
typedef S<N, N> plus2;
typedef S< is_not_zero, sub> _greater;
typedef S<N, one> two;
typedef S<
    R<
        S<_greater, U<1,1>, one>,
        S<
            S< _and,
                S< mod, U<3,1>, U<3,2> >,
                U<3,3>
            >,
            U<3,1>,
            S<plus2, U<3,2> >,
            U<3,3>
        >
    >,
    U<1,1>,
    S<
        minus1,
        S<
            divide,
            U<1,1>,
            two
        >
    >
> is_prime;


typedef S<
    R<
        Z,
        S<
            sum,
            S<is_prime, U<3,2> >,
            U<3,3>
        >
    >,
    U<1,1>,
    S<
        N,
        U<1,1>
    >
> prev_prime;


typedef S <
    M<
        S<
            _greater,
            U<2,1>,
            S<prev_prime, U<2,2> >
        >
    >,
    N
> nth_prime;


template<typename f, typename... g>
void run(g... args)
{
	std::vector<int> result {args...};
	std::cout << f::eval(result) << std::endl;
}

typedef Z zero;
typedef R<U<1,1>,S<N, U<3,3>>> add;


int main()
{
	run<one>();
    run<sum>(120,100);
    run<minus1>(120);
    run<sub>(120,100);
    run<mul>(120,100);
    run<pow>(2,5);
    run<divide>(8,3);
    run<mod>(8,3);
    run<_not>(1);
    run<divideable>(6,3);//нацело
    run<plog>(3,12);
    run<is_zero>(0);
    run<_and>(1,4);//isnotzero(mul)
    run<_greater>(5,1);//first > second
    run<two>(1000);//return 2
    run<is_prime>(23);
	run<nth_prime>(8);
	return 0;
}