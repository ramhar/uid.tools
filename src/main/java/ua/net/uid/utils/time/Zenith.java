/*
 * Copyright 2019 nightfall.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.net.uid.utils.time;

public enum Zenith {
    ASTRONOMICAL(108.), // Astronomical sunrise/set is when the sun is 18 degrees below the horizon.
    NAUTICAL(102.),     // Nautical sunrise/set is when the sun is 12 degrees below the horizon.
    CIVIL(96.),         // Civil sunrise/set (dawn/dusk) is when the sun is 6 degrees below the horizon.
    OFFICIAL(90.8333);  // Official sunrise/set is when the sun is 50' below the horizon.
    
    private final double degrees;

    Zenith(double degrees) {
        this.degrees = degrees;
    }

    public double getDegrees() {
        return degrees;
    }
}
