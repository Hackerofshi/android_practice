package com.shixin.bean.entity;

import com.shixin.bean.Animal;

public class Duck {
    public void test() {
        Animal animal = new Animal();
        //子包中只能访问public
        animal.testPublic();
    }
}
