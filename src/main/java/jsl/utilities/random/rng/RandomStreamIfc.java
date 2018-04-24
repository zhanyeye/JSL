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
package jsl.utilities.random.rng;

/**
 * Controls the movement through a pseudo-random number stream
 *
 */
public interface RandomStreamIfc {

    /**
     * The resetStartStream method will position the RNG at the beginning of its
     * stream. This is the same location in the stream as assigned when the RNG
     * was created and initialized.
     */
    public void resetStartStream();

    /**
     * Resets the position of the RNG at the start of the current substream
     */
    public void resetStartSubstream();

    /**
     * Positions the RNG at the beginning of its next substream
     */
    public void advanceToNextSubstream();

    /**
     * Tells the stream to start producing antithetic variates
     *
     * @param flag true means that it produces antithetic variates.
     */
    public void setAntitheticOption(boolean flag);

    /**
     *
     * @return true means on
     */
    public boolean getAntitheticOption();
}
