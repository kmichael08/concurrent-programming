#include <thread>
#include <future>
#include <vector>
#include <iostream>
#include <algorithm>
#include "log.h"
#include <vector>
#include <ctime>
using namespace std;

void par_sort(int beg, int end, vector<int> &v, vector<int>& help) {
	if (end - beg <= 1)
		return;
				
	int med = (beg + end) / 2;
	
	auto left = std::async(std::launch::async, [beg, med, &v, &help] { par_sort(beg, med, v, help);});
	auto right = std::async(std::launch::async, [med, end, &v, &help] { par_sort(med, end, v, help); });
		
	left.wait();
	right.wait();
	
	vector<int>::iterator it = v.begin();
	
	// uses additional memory
	merge(it + beg, it + med, it + med, it + end, help.begin() + beg);
	
	for (int i = beg; i < end; i++)
		v[i] = help[i];
}

void merge_sort(vector<int> &v) {
	int len = v.size();
	vector<int> help(len, 0);
	par_sort(0, len, v, help);
}

int main() 
{
  vector<int> v = {6, 1, 11, 4, 10};
  
  merge_sort(v);
    
  for (auto item : v) {
	log(item);  
  }
  
  
  vector<int> a = {1, 0};
  
  merge_sort(a);
  
  for (auto item : a) {
	log(item);  
  }
  
  srand(time(NULL));
  
  vector<int> b;
  
  for (int i = 1; i <= 40; i++)
	b.push_back(rand() % 5);
  
  merge_sort(b);
  
  for (auto item : b) {
	log(item);  
  }
  
}

