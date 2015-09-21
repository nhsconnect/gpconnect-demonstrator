package org.rippleosi.common.repo;

/**
 */
public interface Repository {

    /**
     * @return Used to identify a specific implementation of a Repository
     */
    String getSource();

    /**
     * @return Used to determine order of preference for a Repository's data
     */
    int getPriority();
}
