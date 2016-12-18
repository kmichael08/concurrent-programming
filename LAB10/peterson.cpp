#include <iostream>
#include <atomic>
#include "peterson.h"

static std::atomic<bool> flag[2];
static std::atomic<int> turn;

void peterson_entry(int nr) {
	flag[nr] = true;
	turn = 1 - nr;
	while (flag[1 - nr] && turn == 1 - nr)
	{}
}

void peterson_exit(int nr) {
	flag[nr] = false;
}
