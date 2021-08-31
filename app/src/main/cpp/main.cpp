//
// Created by 203 on 2021/8/31.
//
#include "iostream"

int count;
extern void write_extern();
int main(){
    count = 5;
    write_extern();
}