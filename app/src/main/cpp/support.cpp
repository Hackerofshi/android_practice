//
// Created by 203 on 2021/8/31.
//
#include "iostream"

extern int count;

void write_extern(void) {
    std::cout << "count is" << count << std::endl;
}