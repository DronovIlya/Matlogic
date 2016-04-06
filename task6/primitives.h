#ifndef PRIMITIVES
#define PRIMITIVES

struct Z {

    static const int size = 1;
	static int eval(std::vector<int> &args)
	{
		return 0;
	}
};

struct N
{
    static const int size = 1;
	static int eval(std::vector<int> &args)
	{
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
    static int eval(std::vector<int> const & args)
    {
        std::vector<int> p (args.begin(), --args.end());

		// for (int i = 0; i < args.size(); i++) {
		// 	printf("%d ", args[i]);
		// }
		// printf("\n");

        if (args.back() == 0)
        {
            return f::eval(p);
        }
        else
        {
            p.push_back(args.back()-1);
            int res = R<f, g>::eval(p);
            p.push_back(res);
            return g::eval(p);
        }

    }
};

template<typename f>
struct M {
    static const int size = f::size - 1;
    static int eval(std::vector<int> const & args)
    {
        int y=0;
        std::vector<int> p = args;
        p.push_back(y);
        while(f::eval(p) != 0) {
            p[size] = ++y;
        }
        return y;
    }
};

#endif PRIMITIVES