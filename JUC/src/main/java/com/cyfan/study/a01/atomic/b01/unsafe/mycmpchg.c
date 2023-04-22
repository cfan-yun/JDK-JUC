#include<stdio.h>

//hospot -- atomic_linux_x86.inline.hpp 文件
int  cmpxchg(int exchange_value, volatile int* dest, int compare_value) {
  __asm__ __volatile__ ("lock cmpxchgq %1,(%3)"   // 如果是多核加lock前缀， 加了lock之后，该指令具备了原子性
                        : "=a" (exchange_value)             // %0 exchange_value; 表示返回值，（返回逻辑：将rax寄存器中的值放入 exchange_value, 然后将其返回）
                        : "r" (exchange_value), "a" (compare_value), "r" (dest) //%1 exchange_value; %2 compare_value; %3 dest; %4 mp;
                        : "cc", "memory");
  return exchange_value;
}

int main(void)
{

	int exchange_value = 4;
	int dest =  3;
	int compare_value =  3;
	int ret = cmpxchg(exchange_value, &dest, compare_value);
	printf("dest = %ld, ret = %ld\n", dest,ret);

	dest =  2;
	ret = cmpxchg(exchange_value, &dest, compare_value);
	printf("dest = %ld, ret = %ld\n", dest,ret);

}