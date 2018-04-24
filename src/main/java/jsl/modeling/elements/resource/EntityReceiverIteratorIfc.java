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
package jsl.modeling.elements.resource;


/**
 *
 */
public interface EntityReceiverIteratorIfc {

    /** Returns the next receiver or null if none
     * @return Returns the next EntityReceiver.
     */
    public EntityReceiverAbstract nextEntityReceiver();

    public EntityReceiverAbstract previousEntityReceiver();

    public boolean hasNextEntityReceiver();

    public boolean hasPreviousEntityReceiver();

    public int nextEntityReceiverIndex();

    public int previousEntityReceiverIndex();
    
}
