#include <cstdio>
#include <iostream>
#include <vector>
#include "primitives.h"

typedef S<N, Z> one;
typedef R< U<1,1>, S<N, U<3,3> > > sum;
typedef S< R<Z, U<3,2> >, U<1,1>, U<1,1> > minus_1;
typedef R< U<1,1>, S<minus_1, U<3,3> > > sub;
typedef R< Z, S<sum, U<3,1>, U<3,3> > > mul;
typedef R<one, S<mul, U<3,1>, U<3,3> > > pow;
typedef S< minus_1, S< M< S<sub, U<3,1>, S<mul, U<3, 2>, U<3, 3> > > >, S<N, U<2, 1> >, U<2, 2> > > divide;
typedef S<sub, U<2,1>, S<mul, U<2,2>, S< divide, U<2,1>, U<2,2> > > > mod;
typedef S< R< N, S<Z, U<3, 3> > >, U<1,1>, U<1,1> > _not;
typedef S<_not, mod> divideable;
typedef S< minus_1, M<S< divideable, U<3,2>, S<pow, U<3,1>, U<3,3> > > > > plog;
typedef _not is_zero;
typedef S<_not, is_zero> is_not_zero;
typedef S<is_not_zero, mul> _and;
typedef S<N, N> plus2;
typedef S< is_not_zero, sub> greater;
typedef S<N, one> two;
typedef S<
    R<
        S<greater, U<1,1>, one>,
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
        minus_1,
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
            greater,
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
    run<minus_1>(120);
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
    run<greater>(5,1);//first > second
    run<two>(1000);//return 2
    run<is_prime>(23);
	run<nth_prime>(8);
	return 0;
}