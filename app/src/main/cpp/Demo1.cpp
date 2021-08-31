//
// Created by 203 on 2021/8/31.
//

#include <iostream>
#include "Demo1.h"
using namespace std;

double Demo1::get() const {
    return length * breadth * height;
}

void Demo1::set(double len, double bre, double hei) {
    length = len;
    breadth = bre;
    height = hei;
}

// 成员函数定义，包括构造函数
Demo1::Demo1()
{
    cout << "Object is being created" << endl;
}

int main(){

    Demo1 demo1;

    demo1.breadth = 0.34;
}


class Shape
{
public:
    void setWidth(int w)
    {
        width = w;
    }
    void setHeight(int h)
    {
        height = h;
    }
protected:
    int width;
    int height;
};

// 派生类
class Rectangle: public Shape
{
public:
    int getArea()
    {
        return (width * height);
    }
};

class PrintData{
public:
    void print(){
        cout<<"dd"<<endl;
    }
};