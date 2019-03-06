#include <cstdint>
#include <iostream>
#include <gtk/gtk.h>
class a
{
 public:
  virtual void tick(uint64_t n) = 0;
  virtual uint64_t getvalue() = 0;
  virtual void foo()=0;
};


class DynamicInterface :public a
{
 public:
  virtual void tick(uint64_t n) = 0;
  virtual uint64_t getvalue() = 0;
  virtual void foo(){std::cout<<"11";};
};

class DynamicImplementation : public DynamicInterface
{
  uint64_t counter;

 public:
  DynamicImplementation()
      : counter(0) { }

  virtual void tick(uint64_t n)
  {
    counter += n;
  }

  virtual uint64_t getvalue()
  {
    return counter;
  }
  virtual void foo(){std::cout<<"?";}
};

const unsigned N = 40000;

void run_dynamic(a* obj)
{
  for (unsigned i = 0; i < N; ++i)
  {
    for (unsigned j = 0; j < i; ++j)
    {
      obj->tick(j);
    }
  }
}

int main()
{
  DynamicImplementation e;
  run_dynamic(&e);
  std::cout<<e.getvalue();  e.foo();
}

