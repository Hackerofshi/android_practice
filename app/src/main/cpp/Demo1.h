//
// Created by 203 on 2021/8/31.
//

#ifndef ANDROID_PRACTICE_DEMO1_H
#define ANDROID_PRACTICE_DEMO1_H


class Demo1 {
public:
    double length;   // 盒子的长度
    double breadth;  // 盒子的宽度
    double height;   // 盒子的高度
    double get(void) const;
    void set(double len, double bre, double hei);
    Demo1();
};


#endif //ANDROID_PRACTICE_DEMO1_H
