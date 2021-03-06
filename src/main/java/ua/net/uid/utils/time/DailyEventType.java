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

public enum DailyEventType {
    NORMAL_DAY("normal"), 
    POLAR_NIGHT("night"), 
    POLAR_DAY("day");
    
    private final String title;

    DailyEventType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
