#ifndef FUNCTIONS
#define FUNCTIONS

#include <vector>

typedef long long ll;

// --x
struct dec 
{
    static ll eval(ll x) 
    {
        return x - 1;
    }
};

// !x
struct neg 
{
    static ll eval(ll x) 
    {
        return !x;
    }
};

// x + y
struct sum 
{
    static ll eval(ll x, ll y) 
    {
        return x + y;
    }
};

// x - y
struct substract 
{
    static ll eval(ll x, ll y) 
    {
        return x - y;
    }
};

// x / y
struct divide 
{
    static ll eval(ll x, ll y) 
    {
        return x / y;
    }
};

// x % y
struct mod 
{
    static ll eval(ll x, ll y) 
    {
        return x % y;
    }
};

// x * y
struct mul 
{
    static ll eval(ll x, ll y) 
    {
        return x * y;
    }
};

// x ^ y
struct pow 
{
    static ll eval(ll x, ll y) 
    {
        ll result = 1;
        while (y > 0) 
        {
        	result *= x;
        	y--;
        }
        return result;
    }
};

// partial log
struct plog 
{
    static ll eval(ll x, ll y) 
    {
        ll result = 0;
        while ((y % pow::eval(x, result + 1)) == 0) 
        {
            result++;
        }
        return result;
    }
};

struct nth_prime 
{
    static std::vector<ll> primes;
    static ll eval(ll n) {
        for (ll x = primes.back() + 1; primes.size() <= n; x++) 
        {
            bool is_prime = true;
            for (ll prime : primes) 
            {
                if (!(is_prime = x % prime)) 
                {
                    break;
                }
            }
            if (is_prime) 
            {
                primes.push_back(x);
            }
        }
        return primes[n];
    }
};
// init nth_prime with first prime number
std::vector<ll> nth_prime::primes { 2 };



// Primitive recursive functions
struct Z 
{
    template <typename... Func>
    static ll eval(Func... xs) 
    {
        return 0;
    }
};

struct N 
{
    static ll eval(ll x) 
    {
        return x + 1;
    }
};

template <int n>
struct U 
{
    template<typename... Func>
    static ll eval(ll a, Func... f) 
    {
        return U<n-1>::eval(f...);
    }
};

template<>
struct U<0> 
{
    template <typename... Func>
    static ll eval(ll a, Func... f) 
    {
        return a;
    }
};

template <typename f, typename... g>
struct S 
{
    template <typename... Func>
    static ll eval(Func... func) 
    {
        return f::eval(g::eval(func...)...);
    }
};

template <typename f, typename g>
struct R 
{
    template <typename... Func>
    static ll eval(ll y, Func... xs) 
    {
        ll result = f::eval(xs...);
        for (ll x = 0; x < y; ++x) 
        {
            result = g::eval(x, xs..., result);
        }
        return result;
    }
};

template <typename f>
struct M 
{
    template <typename... Func>
    static ll eval(Func... func) 
    {
        int i = 0;
        while (true) 
        {
            if (f::eval(func..., i) == 0) 
            {
                return i;
            }
            i++;
        }
    }
};


// numbers
typedef S <Z, U<0>> zero;
typedef S <N, zero> one;
typedef S <N, one> two;

// statements
typedef S< neg, dec > is_empty;
typedef M<S<neg, S<mod, U<0>, S<nth_prime, U<1>>>>> min_prime_number_id;
typedef S< neg, S<dec, min_prime_number_id> > size_is_one;

// stack manipulations. TODO: remove a lot of garbage methods
typedef S<mul, S<pow, S<nth_prime, S<min_prime_number_id, U<0>>>, S<N, U<1>>>, U<0>> stack_push;
typedef S<divide, U<0>, S<pow, U<1>, S<plog, U<1>, U<0>>>> _stack_pop;
typedef S< _stack_pop, U<0>, S< nth_prime, S< dec, min_prime_number_id > > > stack_pop;
typedef S< dec, S< plog, S< nth_prime, S< dec, min_prime_number_id > >, U<0> > > stack_peek;
typedef S< R< stack_pop, S< stack_peek, U<2> > >, one, U<0> > stack_peek_second;
typedef S< stack_push, S< stack_push, one, U<0> >, U<1> > stack_init;
typedef S< R< stack_pop, S< stack_pop, U<2> > >, one, U<0> > stack_pop2;
typedef S< R< S< stack_pop2, U<0> >, S< stack_push, U<3>, U<2> > >, one, U<0>, U<1> > stack_pop2_stack_push;
typedef S< R< S< stack_pop2_stack_push, U<0>, U<1> >, S< stack_push, U<4>, U<3> > >, one, U<0>, U<1>, U<2> > stack_pop2_stack_push2;
typedef S< R< S< stack_pop2_stack_push2, U<0>, U<1>, U<2> >, S< stack_push, U<5>, U<4> > >, one, U<0>, U<1>, U<2>, U<3> > stack_pop2_stack_push3;

// kind of pattern matching
typedef S< stack_pop2_stack_push, U<0>, S< N, stack_peek > > ackermann_0_n;
typedef S< stack_pop2_stack_push2, U<0>, S< dec, stack_peek_second >, one > ackermann_m_0;
typedef S< stack_pop2_stack_push3, U<0>, S< dec, stack_peek_second >, stack_peek_second, S< dec, stack_peek > > ackermann_m_m;

typedef S<R<S<R<ackermann_m_m, S<ackermann_m_0, U<1>>>, S<neg, stack_peek>, U<0>>, 
	S<ackermann_0_n, U<1>>>, S< neg, stack_peek_second>, U<0>> ackermann_reverse;
typedef S<R<U<0>, S<ackermann_reverse, U<2>>>, U<1>, U<0>> ackermann_reverse_eval;
typedef M<S<neg, S<size_is_one, ackermann_reverse_eval>>> ackermann_start;

// main function
typedef S<S<stack_peek, S<ackermann_reverse_eval, U<0>, ackermann_start>>, stack_init> ackermann;

#endif
