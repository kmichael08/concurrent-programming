#include <iostream>
#include <fstream>
#include <locale>
#include <string>
#include <list>
#include <codecvt>
#include <thread>
#include <future>
#include <vector>

const int N_THREADS = 5;

/**
 * Counts a word in the file.
 */
int grep(std::string filename, std::wstring word) {
    std::locale loc("pl_PL.UTF-8");
    std::wfstream file(filename);
    file.imbue(loc);
    std::wstring line;
	unsigned int count = 0;
    while (getline(file, line)) {
        for (auto pos = line.find(word,0);
             pos != std::string::npos;
             pos = line.find(word, pos+1))
            count++;
    }
    return count;
}

/**
 * Counts a word in the file_list.
 */
void grep_list(std::list<std::string>& filenames, std::wstring word, std::promise<unsigned int>& count) {
	unsigned int counter = 0;
	
	
	for (auto filename : filenames) {    
        counter += grep(filename, word);
    }	
    
    
    count.set_value(counter);
}


int main() {
    std::ios::sync_with_stdio(false);
    std::locale loc("pl_PL.UTF-8");
    std::wcout.imbue(loc);
    std::wcin.imbue(loc);

    std::wstring word;
    std::getline(std::wcin, word);

    std::wstring s_file_count;
    std::getline(std::wcin, s_file_count);
    int file_count = std::stoi(s_file_count);
       
    std::wstring_convert<std::codecvt_utf8<wchar_t>, wchar_t> converter;
    
    // list of files to search for each thread
    std::vector<std::list<std::string> > file_list(N_THREADS);
    std::promise<unsigned int> freq_promise[N_THREADS];
    std::future<unsigned int> freq_future[N_THREADS];
    std::vector<std::thread> threads(N_THREADS);
    	
    // distribute files through the threads
    for (int file_num = 0; file_num < file_count; file_num++) {
        std::wstring w_filename;
        std::getline(std::wcin, w_filename);
        std::string s_filename = converter.to_bytes(w_filename);
        file_list[file_num % N_THREADS].push_back(s_filename);
    }
    
    
    for (int i = 0; i < N_THREADS; i++) {
		threads.at(i) = std::thread{[&file_list, i, word, &freq_promise] { grep_list(file_list[i], word, freq_promise[i]); } };
	}
	
    for (int i = 0; i < N_THREADS; i++)
		freq_future[i] = freq_promise[i].get_future();

	
    unsigned int word_count = 0;
    
	for (int i = 0; i < N_THREADS; i++)
		word_count += freq_future[i].get();
		
	
	for (int i = 0; i < N_THREADS; i++)
		threads.at(i).join();
    
    std::wcout << word_count << std::endl;

	return 0;
}
