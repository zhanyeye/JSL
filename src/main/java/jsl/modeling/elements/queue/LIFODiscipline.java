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
package jsl.modeling.elements.queue;

import java.util.List;
import jsl.modeling.ModelElement;

/** The LIFODiscipline provides a mechanism to the Queue object
 * for ordering the elements according to the first in, first out
 * rule.
 */
@Deprecated
public class LIFODiscipline extends QueueDiscipline {

    public LIFODiscipline(ModelElement parent) {
        this(parent, null);
    }

    public LIFODiscipline(ModelElement parent, String name) {
        super(parent, name);
    }

    /** Adds the specified element to the proper location in the
     * supplied list.
     *
     * LIFO discipline ensures that each new element is added to the end of the list
     *
     * @param list The list to which the object is being added
     * @param qObject The element to be added to the supplied list
     */
    protected void add(List<QObject> list, QObject qObject) {
        list.add(qObject);
    }

    /** Returns a reference to the next QObjectIfc to be removed
     * from the queue according to the LIFO discipline.  The item is
     * not removed from the list.
     * @param list The list to be peeked into
     * @return The QObjectIfc that is next, or null if the list is empty
     */
    protected QObject peekNext(List<QObject> list) {

        if (list.isEmpty()) {
            return (null);
        }

        return ((QObject) list.get(list.size() - 1));// in LIFO the last element added is removed first
    }

    /** Removes the next item from the supplied list according to
     * the LIFO discipline
     * @param list The list for which the next item will be removed
     * @return A reference to the QObjectIfc item that was removed or null if the list is empty
     */
    protected QObject removeNext(List<QObject> list) {

        if (list.isEmpty()) {
            return (null);
        }

        return ((QObject) list.remove(list.size() - 1)); // in LIFO the last element added is removed first
    }

    /** Provides a "hook" method to be called when switching from one discpline to another
     *  The implementor should use this method to ensure that the underlying queue is in a state
     *  that allows it to be managed by this queue discipline.
     *
     *  Since lifo can be applied from any queue state this method performs no operation.
     *
     * @param list The list for which the next item will be removed
     * @param currentDiscipline The queueing discpline that is currently managing the queue
     */
    protected void switchFrom(List<QObject> list, QueueDiscipline currentDiscipline) {
    }

}
