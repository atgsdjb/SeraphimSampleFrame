/*
 * Copyright 2011 David Simmons
 * http://cafbit.com/entry/testing_multicast_support_on_android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seraphim.td.remote.airplay.mdns;

public abstract class DNSComponent {
    
    public enum DNSType {
        A(1),
        NS(2),
        CNAME(5),
        PTR(12),
        MX(15),
        TXT(16),
        AAAA(28),
        ANY(255),
        SRV(0X0021),
        OTHER(0);
        public int qtype;
        DNSType(int qtype) {
            this.qtype = qtype;
        }
        public static DNSType getType(int qtype) {
            for (DNSType type : DNSType.values()) {
                if (type.qtype == qtype) {
                    return type;
                }
            }
            return OTHER;
        }
    }
    public abstract int length();
    public abstract void serialize(DNSBuffer buffer);

}
