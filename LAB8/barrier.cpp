#include <thread>
#include <mutex>
#include <iostream>
#include <chrono>
#include "log.h"
#include <ctime>

const int RESIST = 4;

int getRandom(int size) {
	return std::rand() % size;
}

class Barrier {
private:
	int resistance;
	std::mutex mut;
public:
	Barrier(int resistance) : resistance(resistance) { mut.lock(); }
	void reach() {
		if (resistance > 1) {
			resistance--;
			std::this_thread::sleep_for(std::chrono::milliseconds(getRandom(100)));
			mut.lock();
		}
		else if(resistance == 1) {
			resistance--;
			log("Barrier broken!");
		}
			
		mut.unlock();
	}
					
};

void f(const std::string& name, Barrier &barrier) {
	log(name, " starts ");
	barrier.reach();
	log(name, " after reach");
	std::this_thread::sleep_for(std::chrono::milliseconds(getRandom(1000)));
	log(name, " ends ");
}

int main() {
    std::cout << "main() starts" << std::endl;
    Barrier barrier(RESIST);
    srand(time(NULL));
        
    std::thread t1{[&barrier]{f("t1", barrier);} };
    std::thread t2{[&barrier]{f("t2", barrier);} };
    std::thread t3{[&barrier]{f("t3", barrier);} };
    std::thread t4{[&barrier]{f("t4", barrier);} };
    std::thread t5{[&barrier]{f("t5", barrier);} };
    
    t1.join();
    t2.join();
    t3.join();
    t4.join();
    t5.join();
    
    std::cout << "main() completes" << std::endl;
    
    return 0;
}

