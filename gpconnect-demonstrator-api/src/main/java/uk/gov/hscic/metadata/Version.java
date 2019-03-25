/*
 Copyright 2016  Simon Farrow <simon.farrow1@hscic.gov.uk>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.gov.hscic.metadata;

/**
 * Software version class Enables various comparisons on three dot seaparated
 * integers
 *
 * @author simonfarrow
 */
public class Version implements Comparable {

    private final int major;
    private final int minor;
    private final int bugfix;

    /**
     * expects string of the form [0-9]+.[0-9]+.[0-9]+
     *
     * @param value
     */
    public Version(String value) {
        major = Integer.parseInt(value.replaceFirst("^([0-9]+).*$", "$1"));
        minor = Integer.parseInt(value.replaceFirst("^[0-9]+\\.([0-9]+).*$", "$1"));
        bugfix = Integer.parseInt(value.replaceFirst("^[0-9]+\\.[0-9]+\\.([0-9]+)$", "$1"));
    }

    @Override
    public int compareTo(Object o) {
        Version cValue = (Version) o;
        if (major < cValue.major) {
            return -1;
        } else if (major > cValue.major) {
            return +1;
        } else if (minor < cValue.minor) {
            return -1;
        } else if (minor > cValue.major) {
            return +1;
        } else if (bugfix < cValue.bugfix) {
            return -1;
        } else if (bugfix > cValue.bugfix) {
            return +1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        return compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.major;
        hash = 97 * hash + this.minor;
        hash = 97 * hash + this.bugfix;
        return hash;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + bugfix;
    }

    /**
     * @return the major
     */
    public int getMajor() {
        return major;
    }

    /**
     * @return the minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * @return the bugfix
     */
    public int getBugfix() {
        return bugfix;
    }

}
