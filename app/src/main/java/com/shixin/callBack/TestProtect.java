package com.shixin.callBack;

import com.shixin.bean.Animal;

public class TestProtect extends Animal {

    public void test() {
        testPublic();
        testProtect();
    }
}


class Test1 {
    public void test(){
        Animal animal = new Animal();
        animal.testPublic();
    }
}