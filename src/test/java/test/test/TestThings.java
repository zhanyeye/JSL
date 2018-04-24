/*
 * Copyright (c) 2018. Manuel D. Rossetti, manuelrossetti@gmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test;

/**
 *
 * @author rossetti
 */
public class TestThings {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thing<String> thing = new Thing<>();
        thing.setThing("stuff");
        String thing1 = thing.getThing();
        Thing2<Integer> thing2 = new Thing2<>();
        thing2.setThing(0);
        Integer thing3 = thing2.getThing();
        
        Something s = new Something();
        Thing something = s.getSomething();
    }
    
}
